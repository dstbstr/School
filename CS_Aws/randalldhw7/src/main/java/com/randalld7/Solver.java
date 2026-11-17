package com.randalld7;
import java.util.Set;
import java.util.HashSet;
public class Solver {
    public enum GameOverState { NONE, P1_WINS, P2_WINS, DRAW }
    public record SolveResult(GameOverState gameOverState, Set<Integer> winPositions) {}

    private final int winLength;
    public Solver(int winLength) {
        this.winLength = winLength;
    }

    public SolveResult solve(GameState gameState) {
        int[] board = gameState.board();
        Set<Integer> winPositions = new HashSet<>();

        int symbol = board[gameState.lastMove()];
        if(symbol == 0) throw new RuntimeException("Provided square is empty");

        GameState.RowCol rc = GameState.toRowCol(gameState.lastMove());
        HashSet<Integer> horizontal = getWinPositions(board, symbol, rc, new GameState.RowCol(0, 1));
        horizontal.addAll(getWinPositions(board, symbol, rc, new GameState.RowCol(0, -1)));
        HashSet<Integer> vertical = getWinPositions(board, symbol, rc, new GameState.RowCol(1, 0));
        vertical.addAll(getWinPositions(board, symbol, rc, new GameState.RowCol(-1, 0)));
        HashSet<Integer> diagonal1 = getWinPositions(board, symbol, rc, new GameState.RowCol(1, 1));
        diagonal1.addAll(getWinPositions(board, symbol, rc, new GameState.RowCol(-1, -1)));
        HashSet<Integer> diagonal2 = getWinPositions(board, symbol, rc, new GameState.RowCol(1, -1));
        diagonal2.addAll(getWinPositions(board, symbol, rc, new GameState.RowCol(-1, 1)));

        if(horizontal.size() >= winLength) {
            winPositions.addAll(horizontal);
        }
        if(vertical.size() >= winLength) {
            winPositions.addAll(vertical);
        }
        if(diagonal1.size() >= winLength) {
            winPositions.addAll(diagonal1);
        }
        if(diagonal2.size() >= winLength) {
            winPositions.addAll(diagonal2);
        }

        if(winPositions.size() > 0) {
            return new SolveResult(symbol == 1 ? GameOverState.P1_WINS : GameOverState.P2_WINS, winPositions);
        }

        if(isDraw(board)) {
            return new SolveResult(GameOverState.DRAW, winPositions);
        }
        
        return null;
    }

    private static boolean isInRange(GameState.RowCol rc) {
        return rc.row() >= 0 && rc.row() < GameState.BOARD_WIDTH && rc.col() >= 0 && rc.col() < GameState.BOARD_WIDTH;
    }

    private HashSet<Integer> getWinPositions(int[] board, int symbol, GameState.RowCol rc, GameState.RowCol delta) {
        HashSet<Integer> winPositions = new HashSet<>();

        GameState.RowCol current = rc;
        while(isInRange(current)) {
            int index = GameState.toIndex(current);
            if(board[index] != symbol) break;
            winPositions.add(index);
            current = current.add(delta);
        }
        return winPositions;
    }

    private boolean isDraw(int[] board) {
        for(int cell : board) {
            if(cell == 0) return false;
        }
        return true;
    }
}
