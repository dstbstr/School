package com.finalProject.CodeQuality;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.Evaluator;
import com.finalProject.Settings.Settings;
import com.finalProject.Utils.BedrockResponseUtils;
import com.finalProject.Utils.SubmissionBuilder;

import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

public class RemoteScore implements AutoCloseable {
    public record RemoteScoreResponse(int submission, double totalScore, double correctness, double completeness, double clarity, String comments) {}

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Settings.Model modelSettings;
    private final Settings.Assignment assignmentSettings;

    private final BedrockRuntimeAsyncClient client;

    public RemoteScore(Settings.Assignment assignmentSettings, Settings.Model modelSettings) {
        this.modelSettings = modelSettings;
        this.assignmentSettings = assignmentSettings;
        this.client = BedrockRuntimeAsyncClient.builder()
            .region(Region.of(modelSettings.region()))
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .build();
    }

    @Override
    public void close() {
        if(client != null) {
            client.close();
        }
    }

    public CompletableFuture<Evaluator.RemoteResult<HashMap<String, RemoteScoreResponse>>> RemoteGrade(String submissionDir) {
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
    
            HashMap<String, RemoteScoreResponse> result = new HashMap<>();
            for(var item : responses) {
                var student = sources.get(item.submission());
                result.put(student.name(), item);
            }
    
            return new Evaluator.RemoteResult<>(result, new Evaluator.TokenUsage(response.usage().inputTokens(), response.usage().outputTokens()));
        });
    }


    private String buildPrompt(HashMap<Integer, SubmissionBuilder.NumberedSource> sources) {
        var sb = new StringBuilder(assignmentSettings.gradingPrompt());
        if(assignmentSettings.baseCode() != null && !assignmentSettings.baseCode().isBlank()) {
            Path baseCodePath = Path.of(assignmentSettings.baseCode());
            if(Files.exists(baseCodePath)) {
                sb.append("P) ").append(SubmissionBuilder.buildSubmission(baseCodePath)).append("\n");
            }
        }

        var allEntries = sources.values().stream().sorted((lhs, rhs) -> Integer.compare(lhs.id(), rhs.id())).toList();
        for(var submission : allEntries) {
            sb.append(submission.id()).append(") ").append(submission.code()).append("\n");
        }
        return sb.toString();
    }

    List<RemoteScoreResponse> parseResponse(String response) {
        return BedrockResponseUtils.parseJsonArray(objectMapper, response, RemoteScoreResponse[].class);
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
            "totalScore": 0.00,
            "correctness": 0.00,
            "completeness": 0.00,
            "clarity": 0.00,
            "comments": "Optional comments helpful to a human reviewer"
        }]
 */
}
