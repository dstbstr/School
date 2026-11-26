package com.finalProject.AiDetect;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.Evaluator;
import com.finalProject.Settings;
import com.finalProject.Utils.BedrockResponseUtils;
import com.finalProject.Utils.SubmissionBuilder;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

public class RemoteAiDetect implements AutoCloseable {
    public record RemoteAiResponse(int submission, double aiLikenessScore, String comments) {}

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Settings.Model modelSettings;
    private final Settings.Assignment assignmentSettings;

    private final BedrockRuntimeAsyncClient client;

    public RemoteAiDetect(Settings.Assignment assignmentSettings, Settings.Model modelSettings) {
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

    /*
        INPUT
        P) <provided code or assignment specifications>
        1) <submission 1>
        2) <submission 2>
        ...
        N) <submission N>

        OUTPUT
        Return only valid JSON, with one entry per submission:
        [{
            "submission": 1,
            "aiLikenessScore": 0.00,
            "comments": "Optional comments helpful to a human reviewer"
        }]
 */
    public CompletableFuture<Evaluator.RemoteResult<Map<String, RemoteAiResponse>>> DetectAi(String submissionDir) {
        var sources = SubmissionBuilder.buildNumberedSources(Path.of(submissionDir));

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
        );

        return task.thenApply(response -> {
            var responseText = response.output().message().content().getFirst().text();
            BedrockResponseUtils.logResponseDetails(response, modelSettings.modelId(), responseText);
            var responses = parseResponse(responseText);

            Map<String, RemoteAiResponse> highestScores = new HashMap<>();
            for(var item : responses) {
                var student = sources.get(item.submission());
                highestScores.put(student.name(), item);
            }

            return new Evaluator.RemoteResult<>(highestScores, new Evaluator.TokenUsage(response.usage().inputTokens(), response.usage().outputTokens()));
        });
    }

    private String buildPrompt(HashMap<Integer, SubmissionBuilder.NumberedSource> sources) {
        StringBuilder sb = new StringBuilder();
        sb.append(assignmentSettings.aiLikenessPrompt());
        sb.append("\n");

        Path baseCodePath = Path.of(assignmentSettings.baseCode());
        if(baseCodePath != null && Files.exists(baseCodePath)) {
            sb.append("P) ").append(SubmissionBuilder.buildSubmission(baseCodePath)).append("\n");
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
