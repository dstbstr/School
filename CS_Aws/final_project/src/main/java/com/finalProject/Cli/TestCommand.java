package com.finalProject.Cli;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseRequest;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

@Command(name = "test", description = "Simple CLI test")
public class TestCommand implements Runnable{
    @Option(names = {"-p", "--prompt"}, required = true)
    private String prompt;

    @Override
    public void run() {
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
    }
}
