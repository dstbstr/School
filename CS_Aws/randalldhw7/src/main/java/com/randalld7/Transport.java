package com.randalld7;

import java.util.concurrent.CompletableFuture;
import java.util.UUID;

public interface Transport {
    public record MoveResult(Boolean accepted, GameState gameState) {}

    CompletableFuture<UUID> findOrStartGame(String playerId);
    CompletableFuture<MoveResult> submitMove(UUID gameId, String playerId, int squareIndex);
    CompletableFuture<GameState> waitForTurn(UUID gameId, String playerId);

    CompletableFuture<Void> finishGame(UUID gameId);

    void close();
}
