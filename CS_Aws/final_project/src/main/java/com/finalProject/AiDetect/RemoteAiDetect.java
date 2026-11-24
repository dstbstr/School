package com.finalProject.AiDetect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.Settings;
import com.finalProject.AiDetect.LocalAiDetect.AiMatch;
import com.finalProject.AiDetect.LocalAiDetect.AiReport;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

public class RemoteAiDetect implements AutoCloseable {
    private record RemoteAiResponse(int submission, double score, int mostSimilar, String evidence) {}
    private enum SourceKind { STUDENT, AI };
    private record NumberedSource(int id, SourceKind kind, String name, String code) {}

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Settings.Model modelSettings;
    private final Settings.Assignment assignmentSettings;
    private final String submissionDir;

    private final BedrockRuntimeClient client;
/*
        Message message = Message.builder()
            .role(ConversationRole.USER)
            .content(ContentBlock.fromText(prompt))
            .build();

        try (BedrockRuntimeClient client = BedrockRuntimeClient.builder()
            .region(Region.US_EAST_2)
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .build()) {
            ConverseResponse response = client.converse(ConverseRequest.builder()
                //.modelId("us.openai.gpt-5.4")
                .modelId("us.amazon.nova-micro-v1:0")
                .messages(message)
                .build());

            System.out.println("Response: " + response);
        }
 */

    public RemoteAiDetect(Settings.Assignment assignmentSettings, Settings.Model modelSettings, String submissionDir) {
        this.modelSettings = modelSettings;
        this.assignmentSettings = assignmentSettings;
        this.submissionDir = submissionDir;
        this.client = BedrockRuntimeClient.builder()
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

    public AiReport DetectAi() {
        var sources = buildSourceMap();

        String prompt = buildPrompt(sources);
        Message message = Message.builder()
            .role(ConversationRole.USER)
            .content(ContentBlock.fromText(prompt))
            .build();

        var response = client.converse(ConverseRequest.builder()
            .modelId(modelSettings.modelId())
            .messages(message)
            .build()
        );

        var responses = parseResponse(response.output().message().content().getFirst().text());

        HashMap<String, AiMatch> highestScores = new HashMap<>();
        for(var item : responses) {
            var student = sources.get(item.submission());
            var match = sources.get(item.mostSimilar());
            if(student == null || match == null || student.kind() != SourceKind.STUDENT || match.kind() != SourceKind.AI) continue;

            highestScores.put(student.name(), new AiMatch(item.score(), match.name()));
        }

        return new AiReport(highestScores);
    }

    private HashMap<Integer, NumberedSource> buildSourceMap() {
        HashMap<Integer, NumberedSource> sources = new HashMap<>();

        try(Stream<Path> aiPath = Files.walk(Path.of(assignmentSettings.aiCodeDir()))) {
            aiPath.filter(Files::isRegularFile)
                  .forEach(path -> {
                      try {
                        sources.put(sources.size() + 1, new NumberedSource(sources.size() + 1, SourceKind.AI, path.getParent().getFileName().toString(), Files.readString(path)));
                      } catch (IOException e) {
                          throw new RuntimeException("Failed to read AI code file", e);
                      }
                  });
        } catch(IOException e) {
            throw new RuntimeException("Failed to read AI code directory", e);
        }

        try(Stream<Path> paths = Files.walk(Path.of(submissionDir))) {
            paths.filter(Files::isRegularFile)
                 .forEach(path -> {
                     // Process each file here
                     try {
                        sources.put(sources.size() + 1, new NumberedSource(sources.size() + 1, SourceKind.STUDENT, path.getParent().getFileName().toString(), Files.readString(path)));
                     } catch (IOException e) {
                         throw new RuntimeException("Failed to read submission file", e);
                     }
                 });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sources;
    }

    private String buildPrompt(HashMap<Integer, NumberedSource> sources) {
        StringBuilder sb = new StringBuilder();
        sb.append(assignmentSettings.similarityPrompt());
        sb.append("\n");

        Path baseCodePath = Path.of(assignmentSettings.baseCode());
        if(baseCodePath != null && Files.exists(baseCodePath)) {
            try {
                sb.append("P) ").append(Files.readString(baseCodePath)).append("\n");
            } catch (IOException e) {
                throw new RuntimeException("Failed to read base code file", e);
            }
        }

        List<NumberedSource> allEntries = sources.values().stream().sorted((lhs, rhs) -> Integer.compare(lhs.id(), rhs.id())).toList();
        for(var submission : allEntries) {
            sb.append(submission.id()).append(") ").append(submission.code()).append("\n");
        }
        return sb.toString();
    }

    List<RemoteAiResponse> parseResponse(String response) {
        try {
            return Arrays.asList(objectMapper.readValue(response, RemoteAiResponse[].class));
        } catch(Exception e ) {
            throw new RuntimeException(e);
        }
    }

}
