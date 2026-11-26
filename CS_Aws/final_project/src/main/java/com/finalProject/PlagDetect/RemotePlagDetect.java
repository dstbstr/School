package com.finalProject.PlagDetect;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.Evaluator;
import com.finalProject.PlagDetect.LocalPlagDetect.SimilarityReport;
import com.finalProject.Settings.Settings;
import com.finalProject.Utils.BedrockResponseUtils;
import com.finalProject.Utils.SubmissionBuilder;
import com.finalProject.Utils.SubmissionGrouper;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

public class RemotePlagDetect implements AutoCloseable {
    public record RemoteAiResponse(int submission, double score, int mostSimilar, String evidence) {}

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Settings.Model modelSettings;
    private final Settings.Assignment assignmentSettings;

    private final BedrockRuntimeAsyncClient client;

    public RemotePlagDetect(Settings.Assignment assignmentSettings, Settings.Model modelSettings) {
        this.modelSettings = modelSettings;
        this.assignmentSettings = assignmentSettings;
        this.client = BedrockRuntimeAsyncClient.builder()
            .region(Region.of(modelSettings.region()))
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .build();
    }

    @Override 
    public void close() {
        if(this.client != null) {
            this.client.close();
        }
    }

    public CompletableFuture<Evaluator.RemoteResult<Map<String, RemoteAiResponse>>> DetectPlag(SimilarityReport localResults, String submissionDir) {
        var namedSources = SubmissionBuilder.buildNamedSources(Path.of(submissionDir));

        // group by local results
        var groups = SubmissionGrouper.groupSubmissions(localResults.highestScores(), modelSettings.plagThreshold());

        var tasks = new ArrayList<CompletableFuture<Evaluator.RemoteResult<HashMap<String, RemoteAiResponse>>>>();
        for(var group : groups) {
            var sources = new HashMap<Integer, SubmissionBuilder.NumberedSource>();
            for(var entry : group.stream().sorted().toList()) {
                int id = sources.size() + 1;
                sources.put(id, new SubmissionBuilder.NumberedSource(id, entry, namedSources.get(entry)));
            }

            String prompt = buildPrompt(sources);
            Message message = Message.builder()
                .role(ConversationRole.USER)
                .content(ContentBlock.fromText(prompt))
                .build();

            var task = client.converse(ConverseRequest.builder()
                .modelId(modelSettings.modelId())
                .messages(message)
                .inferenceConfig(InferenceConfiguration.builder()
                    .maxTokens(modelSettings.maxTokens())
                    .temperature(modelSettings.temperature())
                    .build()
                )
                .build()
            ).thenApply(response -> {
                var responseText = response.output().message().content().getFirst().text();
                BedrockResponseUtils.logResponseDetails(response, modelSettings.modelId(), responseText);
                var items = parseResponse(responseText);
                var result = new HashMap<String, RemoteAiResponse>(); 
                for(var item : items) {
                    var student = sources.get(item.submission());
                    var match = sources.get(item.mostSimilar());
                    if(student == null || match == null) {
                        throw new RuntimeException("Failed to find student or match in sources: " + item.submission() + " or " + item.mostSimilar());
                    }
                    result.put(student.name(), item);
                }
                return new Evaluator.RemoteResult<>(result, new Evaluator.TokenUsage(response.usage().inputTokens(), response.usage().outputTokens()));
            });

            tasks.add(task);
        }

        return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                var combined = new HashMap<String, RemoteAiResponse>();
                var tokenUsage = Evaluator.TokenUsage.ZERO;

                for(var task : tasks) {
                    var result = task.join();
                    combined.putAll(result.value());
                    tokenUsage = tokenUsage.plus(result.tokenUsage());
                }
                return new Evaluator.RemoteResult<>(combined, tokenUsage);
        });
    }

    private String buildPrompt(HashMap<Integer, SubmissionBuilder.NumberedSource> sources) {
        StringBuilder sb = new StringBuilder();
        sb.append(assignmentSettings.similarityPrompt());
        sb.append("\n");

        var baseCode = assignmentSettings.baseCode();
        if(baseCode != null && !baseCode.isBlank()) {
            var path = Path.of(baseCode);
            if(Files.exists(path)) {
                sb.append("P) ").append(SubmissionBuilder.buildSubmission(path)).append("\n");
            }
        }

        List<SubmissionBuilder.NumberedSource> allEntries = sources.values().stream().sorted((lhs, rhs) -> Integer.compare(lhs.id(), rhs.id())).toList();
        for(var submission : allEntries) {
            sb.append(submission.id()).append(") ").append(submission.code()).append("\n");
        }
        return sb.toString();
    }

    List<RemoteAiResponse> parseResponse(String response) {
        return BedrockResponseUtils.parseJsonArray(objectMapper, response, RemoteAiResponse[].class);
    }
}
