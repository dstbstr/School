package com.randalld7;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

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
            return findOrStartReady(playerId);
        });
    }

    @Override
    public CompletableFuture<MoveResult> submitMove(UUID gameId, String playerId, int squareIndex) {
        return getGameState(gameId)
            .thenCompose(gameState -> {
                boolean isP1 = playerId.equals(gameState.p1());
                if(gameState.p1Turn() == isP1) {
                    if(gameState.board()[squareIndex] == 0) {
                        gameState.board()[squareIndex] = isP1 ? 1 : 2;
                        gameState = gameState.turn(!gameState.p1Turn(), squareIndex);

                        return save(gameId, gameState)
                            .thenApply(newState -> new MoveResult(true, newState));
                    }
                }

                return CompletableFuture.completedFuture(new MoveResult(false, gameState));
            });
    }

    @Override
    public CompletableFuture<GameState> waitForTurn(UUID gameId, String playerId) {
        return tryGetActiveGame(gameId).thenCompose(result -> {
            if(result.isPresent()) {
                GameState state = result.get();
                boolean isP1 = playerId.equals(state.p1());

                if(state.p1Turn() == isP1) {
                    return CompletableFuture.completedFuture(state);
                }
            }

            Executor afterDelay = CompletableFuture.delayedExecutor(250, TimeUnit.MILLISECONDS);
            return CompletableFuture.runAsync(() -> {}, afterDelay)
                .thenCompose(ignored -> waitForTurn(gameId, playerId));
        });
    }

    @Override
    public void close() {
        if (ddb != null) {
            ddb.close();
        }
    }

    private CompletableFuture<Optional<GameState>> tryGetActiveGame(UUID gameId) {
        return getGameState(gameId)
            .thenApply(Optional::of)
            .exceptionally(ignored -> Optional.empty());
    }

    private CompletableFuture<GameState> getGameState(UUID gameId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
            .tableName(TABLE_NAME)
            .key(Map.of("gameId", AttributeValue.builder().s(gameId.toString()).build()))
            .build();

        return ddb.getItem(getItemRequest)
            .thenApply(getItemResponse -> toGameState(getItemResponse.item()));
    }
    
    private CompletableFuture<UUID> findOrStartReady(String playerId) {
        return tryGetPendingGame(playerId)
        .thenCompose(candidate -> {
            if(candidate.isEmpty()) {
                return startNewGame(playerId);
            }

            GameState game = candidate.get();
            UUID gameId = UUID.fromString(game.gameId());

            return tryClaimPendingGame(gameId, playerId).thenCompose(claimed -> {
                return claimed 
                    ? CompletableFuture.completedFuture(gameId)
                    : startNewGame(playerId);
            });
        });
    }

    private CompletableFuture<Optional<GameState>> tryGetPendingGame(String playerId) {
        ScanRequest scanRequest = ScanRequest.builder()
            .tableName(TABLE_NAME)
            .consistentRead(true)
            .filterExpression("attribute_not_exists(p2) AND p1 <> :playerId")
            .expressionAttributeValues(Map.of(":playerId", AttributeValue.builder().s(playerId).build()))
            .build();

        return ddb.scan(scanRequest)
            .thenApply(scanResponse -> scanResponse.items().stream()
                .findFirst()
                .map(this::toGameState));
    }

    private CompletableFuture<UUID> startNewGame(String playerId) {
        UUID gameId = UUID.randomUUID();
        GameState newGame = new GameState(gameId.toString(), playerId, null, true, new int[GameState.BOARD_SIZE], -1);

        return save(gameId, newGame)
            .thenApply(ignored -> gameId);
    }

    private CompletableFuture<GameState> save(UUID gameId, GameState gameState) {
        Map<String, AttributeValue> item = toAttributeMap(gameState);
        item.put("expiresAt", AttributeValue.builder()
            .n(Long.toString(Instant.now().plus(Duration.ofDays(1)).getEpochSecond()))
            .build());
            
        PutItemRequest putItemRequest = PutItemRequest.builder()
            .tableName(TABLE_NAME)
            .item(item)
            .build();

        return ddb.putItem(putItemRequest).thenApply(ignored -> gameState);
    }

    private CompletableFuture<Boolean> tryClaimPendingGame(UUID gameId, String playerId) {
        var request = UpdateItemRequest.builder()
            .tableName(TABLE_NAME)
            .key(Map.of("gameId", AttributeValue.builder().s(gameId.toString()).build()))
            .updateExpression("SET p2 = :playerId")
            .conditionExpression("attribute_not_exists(p2) AND p1 <> :playerId")
            .expressionAttributeValues(Map.of(
                ":playerId", AttributeValue.builder().s(playerId).build()
            ))
            .build();

        return ddb.updateItem(request)
            .thenApply(updateItemResponse -> true)
            .exceptionally(err -> {
                Throwable cause = unwrap(err);
                if(cause instanceof ConditionalCheckFailedException) {
                    return false;
                }
                throw new CompletionException(cause);
            });
    }

    private GameState toGameState(Map<String, AttributeValue> item) {
        String gameId = item.get("gameId").s();
        String p1 = item.get("p1").s();
        String p2 = item.get("p2") != null ? item.get("p2").s() : null;
        boolean p1Turn = item.get("p1Turn") != null ? Boolean.parseBoolean(item.get("p1Turn").s()) : true;
        int[] board = item.get("board").l().stream()
            .mapToInt(v -> Integer.parseInt(v.n())).toArray();
        int lastMove = item.get("lastMove") != null ? Integer.parseInt(item.get("lastMove").s()) : -1;
        
        return new GameState(gameId, p1, p2, p1Turn, board, lastMove);
    }

    private Map<String, AttributeValue> toAttributeMap(GameState state) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("gameId", AttributeValue.builder().s(state.gameId()).build());
        item.put("p1", AttributeValue.builder().s(state.p1()).build());
        if(state.p2() != null) {
            item.put("p2", AttributeValue.builder().s(state.p2()).build());
        }
        item.put("p1Turn", AttributeValue.builder().s(Boolean.toString(state.p1Turn())).build());
        item.put("board", AttributeValue.builder()
            .l(Arrays.stream(state.board())
                .mapToObj(i -> AttributeValue.builder()
                    .n(Integer.toString(i)).build())
                .toList())
            .build());
        item.put("lastMove", AttributeValue.builder().s(Integer.toString(state.lastMove())).build());
        return item;
    }
    
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
