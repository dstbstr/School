package com.randalld7;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

public class AwsTransport implements Transport {
    private final DynamoDbAsyncClient ddb;
    private static final String TABLE_NAME = "games";

    public AwsTransport() {
        this.ddb = DynamoDbAsyncClient.builder()
            .region(Region.US_EAST_1)
            .build();
    }

    @Override
    public CompletableFuture<UUID> findOrStartGame(String playerId) {
        return ensureTableExists().thenCompose(ignored -> {
            // logic goes here
            return CompletableFuture.completedFuture(UUID.randomUUID());
        });
    }

    @Override
    public CompletableFuture<MoveResult> submitMove(UUID gameId, String playerId, int squareIndex) {
        
    }

    @Override
    public CompletableFuture<GameState> waitForTurn(UUID gameId, String playerId) {
        
    }

    @Override
    public CompletableFuture<Void> finishGame(UUID gameId) {
        
    }

    @Override
    public void close() {
        if (ddb != null) {
            ddb.close();
        }
    }

    private CompletableFuture<Optional<GameState>> tryGetPendingGame(String playerId) {
        ScanRequest scanRequest = ScanRequest.builder()
            .tableName(TABLE_NAME)
            .filterExpression("attribute_not_exists(p2) AND p1 <> :playerId")
            .expressionAttributeValues(Map.of(":playerId", AttributeValue.builder().s(playerId).build()))
            .build();

        return ddb.scan(scanRequest)
            .thenApply(scanResponse -> scanResponse.items().stream()
                .findFirst()
                .map(this::toGameState));
    }

    private GameState toGameState(Map<String, AttributeValue> item) {
        String gameId = item.get("gameId").s();
        String p1 = item.get("p1").s();
        String p2 = item.get("p2") != null ? item.get("p2").s() : null;
        boolean p1Turn = item.get("p1Turn") != null ? Boolean.parseBoolean(item.get("p1Turn").s()) : true;
        int[] board = item.get("board") != null ? Arrays.stream(item.get("board").ss().toArray(new String[0])).mapToInt(Integer::parseInt).toArray() : new int[GameState.BOARD_SIZE];
        int lastMove = item.get("lastMove") != null ? Integer.parseInt(item.get("lastMove").s()) : -1;
        
        return new GameState(gameId, p1, p2, p1Turn, board, lastMove);
    }
    /*
        public static int queryTable(DynamoDbClient ddb, String tableName, String partitionKeyName, String partitionKeyVal,
            String partitionAlias) {
        // Set up an alias for the partition key name in case it's a reserved word.
        HashMap<String, String> attrNameAlias = new HashMap<String, String>();
        attrNameAlias.put(partitionAlias, partitionKeyName);

        // Set up mapping of the partition name with the value.
        HashMap<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":" + partitionKeyName, AttributeValue.builder()
                .s(partitionKeyVal)
                .build());

        QueryRequest queryReq = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression(partitionAlias + " = :" + partitionKeyName)
                .expressionAttributeNames(attrNameAlias)
                .expressionAttributeValues(attrValues)
                .build();

        try {
            QueryResponse response = ddb.query(queryReq);
            return response.count();

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return -1;
    }

    */
    
    private CompletableFuture<Void> ensureTableExists() {
        DescribeTableRequest request = DescribeTableRequest.builder().tableName(TABLE_NAME).build();

        return ddb.describeTable(request)
            .thenCompose(ignored -> waitUntilTableExists(request))
            .exceptionallyCompose(err -> {
                Throwable cause = unwrap(err);
                if(!(cause instanceof ResourceNotFoundException)) {
                    return CompletableFuture.failedFuture(cause);
                }

                return createTable()
                    .thenCompose(ignored -> waitUntilTableExists(request))
                    .exceptionallyCompose(err2 -> {
                        Throwable cause2 = unwrap(err2);
                        if(cause2 instanceof ResourceInUseException) {
                            return waitUntilTableExists(request);
                        }
                        return CompletableFuture.failedFuture(cause2);
                    });
                });
    }

    private CompletableFuture<Void> createTable() {
        return ddb.createTable(CreateTableRequest.builder()
            .tableName(TABLE_NAME)
            .attributeDefinitions(AttributeDefinition.builder()
                .attributeName("gameId").attributeType(ScalarAttributeType.S).build())
            .keySchema(KeySchemaElement.builder()
                .attributeName("gameId").keyType(KeyType.HASH).build())
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .build())
            .thenApply(createResponse -> null);
    }

    private CompletableFuture<Void> waitUntilTableExists(DescribeTableRequest request) {
        return ddb.waiter().waitUntilTableExists(request)
            .thenApply(ignored -> (Void) null);
    }

    private static Throwable unwrap(Throwable err) {
        while (err instanceof CompletionException && err.getCause() != null) {
            err = err.getCause();
        }
        return err;
    }
}
