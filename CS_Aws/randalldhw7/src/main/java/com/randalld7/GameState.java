package com.randalld7;

public record GameState(String gameId, String p1, String p2, boolean p1Turn, int[] board, int lastMove) {
    public static final int BOARD_WIDTH = 16;
    public static final int BOARD_SIZE = BOARD_WIDTH * BOARD_WIDTH;

    public GameState p2(String val) {
        return new GameState(gameId, p1, val, p1Turn, board, lastMove);
    }

    public GameState turn(boolean isP1, int move) {
        return new GameState(gameId, p1, p2, isP1, board, move);
    }

    public GameState board(int[] val) {
        return new GameState(gameId, p1, p2, p1Turn, val, lastMove);
    }

    public record RowCol(int row, int col) {
        public RowCol add(RowCol other) {
            return new RowCol(this.row + other.row, this.col + other.col);
        }
    }

    public static int toIndex(int row, int col) {
        return row * BOARD_WIDTH + col;
    }

    public static int toIndex(RowCol rc) {
        return toIndex(rc.row(), rc.col());
    }

    public static RowCol toRowCol(int index) {
        int row = index / BOARD_WIDTH;
        int col = index % BOARD_WIDTH;
        return new RowCol(row, col);
    }
}
