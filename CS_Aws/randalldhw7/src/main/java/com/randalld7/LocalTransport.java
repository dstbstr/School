package com.randalld7;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;


public class LocalTransport implements Transport {
    private static final ObjectMapper JSON = new ObjectMapper();

    private static final String RootDir = "Z:/Temp";
    private final Executor IO_EXECUTOR = Executors.newCachedThreadPool();

    @Override
    public CompletableFuture<UUID> findOrStartGame(String playerId) {
        return CompletableFuture.supplyAsync(() -> {
            GameState pendingGame = getPendingGame();
            if(pendingGame != null) {
                UUID gameId = UUID.fromString(pendingGame.gameId());
                promotePendingGame(gameId, playerId);
                return gameId;
            } else {
                UUID gameId = UUID.randomUUID();
                makePendingGame(gameId, playerId);
                return gameId;
            }
        }, IO_EXECUTOR);
    }

    @Override
    public CompletableFuture<MoveResult> submitMove(UUID gameId, String playerId, int squareIndex) {
        GameState gameState = getActiveGame(gameId);
        boolean isP1 = playerId.equals(gameState.p1());
        if(gameState.p1Turn() == isP1) {
            if(gameState.board()[squareIndex] == 0) {
                gameState.board()[squareIndex] = isP1 ? 1 : 2;
                gameState = gameState.turn(!gameState.p1Turn(), squareIndex);

                saveActiveGame(gameId, gameState);
                return CompletableFuture.completedFuture(new MoveResult(true, gameState));
            }
        }

        return CompletableFuture.completedFuture(new MoveResult(false, gameState));
    }

    @Override
    public CompletableFuture<GameState> waitForTurn(UUID gameId, String playerId) {
        return CompletableFuture
            .supplyAsync(() -> tryGetActiveGame(gameId))
            .thenCompose(result -> {
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
    public CompletableFuture<Void> finishGame(UUID gameId) {
        try {
            Files.deleteIfExists(Path.of(RootDir, "active", gameId.toString()));
        } catch(IOException ex) {
            throw new CompletionException("Finish failed", ex);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override 
    public void close() {
        if(IO_EXECUTOR instanceof ExecutorService es) {
            es.shutdown();
        }
    }

    private GameState getPendingGame() {
        Path pendingDir = Path.of(RootDir, "pending");
        try {
            Files.createDirectories(pendingDir);
        } catch (IOException ex) {
            throw new CompletionException("Create pending directory failed", ex);
        }

        try (Stream<Path> paths = Files.list(pendingDir)) {
            return paths
                .filter(Files::isRegularFile)
                .findFirst()
                .map(p -> {
                    try {
                        String json = Files.readString(p);
                        return JSON.readValue(json, GameState.class);
                    } catch(IOException ex) {
                        throw new CompletionException("Read failed", ex);
                    }
                })
                .orElse(null);
        } catch(IOException ex) {
            throw new CompletionException("List failed", ex);
        }
    }

    private void makePendingGame(UUID gameId, String playerId) {
        try {
            GameState initialState = new GameState(gameId.toString(), playerId, null, true, new int[GameState.BOARD_SIZE], -1);
            String fileName = gameId.toString() + ".json";
            Path filePath = Path.of(RootDir, "pending", fileName);
            Files.createDirectories(filePath.getParent());
            String json = JSON.writeValueAsString(initialState);
            Files.writeString(filePath, json);
        } catch(IOException ex) {
            throw new CompletionException("Create failed", ex);
        }
    }

    private void promotePendingGame(UUID gameId, String playerId) {
        try {
            String fileName = gameId.toString() + ".json";
            String json = Files.readString(Path.of(RootDir, "pending", fileName));
            GameState gameState = JSON.readValue(json, GameState.class);
            gameState = gameState.p2(playerId);

            Files.createDirectories(Path.of(RootDir, "active"));
            Files.writeString(Path.of(RootDir, "active", fileName), JSON.writeValueAsString(gameState));
            Files.delete(Path.of(RootDir, "pending", fileName));
        } catch(IOException ex) {
            throw new CompletionException("Promote failed", ex);
        }
    }

    private Optional<GameState> tryGetActiveGame(UUID gameId) {
        try {
            return Optional.of(getActiveGame(gameId));
        } catch(CompletionException ex) {
            if(ex.getCause() instanceof NoSuchFileException) {
                return Optional.empty();
            }
            throw ex;
        }
    }
    
    private GameState getActiveGame(UUID gameId) {
        try {
            String fileName = gameId.toString() + ".json";
            String json = Files.readString(Path.of(RootDir, "active", fileName));
            return JSON.readValue(json, GameState.class);
        } catch(IOException ex) {
            throw new CompletionException("Read failed", ex);
        }
    }

    private void saveActiveGame(UUID gameId, GameState gameState) {
        try {
            String fileName = gameId.toString() + ".json";
            Files.writeString(Path.of(RootDir, "active", fileName), JSON.writeValueAsString(gameState));
        } catch(IOException ex) {
            throw new CompletionException("Save failed", ex);
        }
    }
}
