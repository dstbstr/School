package com.randalld7;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

public class Game extends Application {
    private final String playerId = UUID.randomUUID().toString();
    private final Label playerLabel = new Label("Connecting as " + playerId);
    private final TextArea eventLog = new TextArea();
    private final Button newGameButton = new Button("New Game");

    private Transport transport;
    private Solver solver = new Solver(5);
    private final Square[][] board = new Square[GameState.BOARD_WIDTH][GameState.BOARD_WIDTH];
    private String myMark;
    private String otherMark;
    private UUID gameId;
    private boolean myTurn;
    private boolean gameOver;
    private boolean isP1;
    
    @Override
    public void start(Stage primaryStage) {
        List<String> args = getParameters().getRaw();
        if(args.isEmpty() || args.get(0).equals("aws")) {
            transport = new AwsTransport();
        } else {
            transport = new LocalTransport();
        }

        for(int row = 0; row < GameState.BOARD_WIDTH; row++) {
            for(int col = 0; col < GameState.BOARD_WIDTH; col++) {
                board[row][col] = new Square(GameState.toIndex(row, col), s -> onClick(s));
            }
        }

        reportFailure("findOrStartGame",
            transport.findOrStartGame(playerId)
                .thenAcceptAsync(id -> {
                    gameId = id;
                    eventLog.appendText("Joined Game " + gameId + "\n");
                    waitForMyTurn();
                }, Platform::runLater));

        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        for(int row = 0; row < GameState.BOARD_WIDTH; row++) {
            for(int col = 0; col < GameState.BOARD_WIDTH; col++) {
                grid.add(board[row][col], col, row);
            }
        }

        newGameButton.setVisible(false);
        newGameButton.setManaged(false);
        newGameButton.setOnAction(e -> onNewGame());

        HBox header = new HBox(10, playerLabel, newGameButton);
        header.setAlignment(Pos.CENTER_LEFT);

        eventLog.setEditable(false);
        eventLog.setPrefRowCount(4);
        root.setTop(header);
        root.setCenter(grid);
        root.setBottom(eventLog);   
        
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override 
    public void stop() {
        if(transport != null) {
            transport.close();
        }
    }

    private void waitForMyTurn() {
        reportFailure("waitForTurn", 
            transport.waitForTurn(gameId, playerId)
                .thenAcceptAsync(state -> {
                    isP1 = playerId.equals(state.p1());
                    myMark = isP1 ? "X" : "O";
                    otherMark = isP1 ? "O" : "X";

                    playerLabel.setText((isP1 ? "Player 1: " : "Player 2: ") + myMark);
                    eventLog.appendText("Your turn\n");

                    updateBoard(state, otherMark);
                    if(!gameOver) myTurn = true;
                }, Platform::runLater));
    }

    private void updateBoard(GameState state, String mark) {
        if(state.lastMove() < 0) return;

        GameState.RowCol rc = GameState.toRowCol(state.lastMove());
        board[rc.row()][rc.col()].setMark(mark);

        checkGameOver(state);
    }

    private void onClick(Square square) {
        if(!myTurn || gameOver) return;
        myTurn = false;
        int move = square.getSquareLocation();

        reportFailure("submitMove", 
            transport.submitMove(gameId, playerId, move)
                .thenAcceptAsync(moveResult -> {
                    if(moveResult.accepted()) {
                        updateBoard(moveResult.gameState(), myMark);
                        if(gameOver) {
                            eventLog.appendText("Game over\n");
                        } else {
                            eventLog.appendText((isP1 ? "Player 2 " : "Player 1 ") + "turn\n");
                            waitForMyTurn();
                        }
                    } else {
                        eventLog.appendText("Invalid move\n");
                        myTurn = true;
                    }
            }, Platform::runLater));
    }

    private void onNewGame() {
        for(int i = 0; i < GameState.BOARD_SIZE; i++) {
            GameState.RowCol rc = GameState.toRowCol(i);
            board[rc.row()][rc.col()].reset();
        }

        newGameButton.setVisible(false);
        newGameButton.setManaged(false);

        gameOver = false;
        myTurn = false;
        eventLog.appendText("New game started\n");

        reportFailure("findOrStartGame", 
            transport.findOrStartGame(playerId)
                .thenAcceptAsync(id -> {
                    gameId = id;
                    eventLog.appendText("Joined Game " + gameId + "\n");
                    waitForMyTurn();
                }, Platform::runLater));
    }
    
    private void checkGameOver(GameState state) {
        Solver.SolveResult result = solver.solve(state);
        if(result == null) return;

        eventLog.appendText("Game Over\n");
        newGameButton.setVisible(true);
        newGameButton.setManaged(true);

        gameOver = true;
        if(result.gameOverState() == Solver.GameOverState.DRAW) {
            for(int i = 0; i < GameState.BOARD_SIZE; i++) {
                GameState.RowCol rc = GameState.toRowCol(i);
                board[rc.row()][rc.col()].highlight("gray");
            }

            eventLog.appendText("Draw\n");
            return;
        }

        boolean p1Wins = result.gameOverState() == Solver.GameOverState.P1_WINS;
        String color = p1Wins == isP1 
            ? "green" 
            : "red";

        eventLog.appendText((p1Wins ? "Player 1 " : "Player 2 ") + "wins\n");
        for(int index : result.winPositions()) {
            GameState.RowCol rc = GameState.toRowCol(index);
            board[rc.row()][rc.col()].highlight(color);
        }
    }

    private <T> CompletableFuture<T> reportFailure(String operation, CompletableFuture<T> future) {
        return future.whenComplete((value, error) -> {
            if(error == null) return;

            Throwable cause = error;
            while(cause instanceof CompletionException) {
                cause = cause.getCause();
            }

            cause.printStackTrace();
            String message = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getSimpleName();

            Platform.runLater(() -> {
                myTurn = false;
                playerLabel.setText("Error");
                eventLog.appendText(operation + " failed: " + message + "\n");
            }); 
        });
    }

    private static class Square extends StackPane {
        private final int location;
        private final Label markLabel = new Label();

        Square(int location, Consumer<Square> onClick) {
            this.location = location;

            reset();
            setMinSize(30, 30);
            setPrefSize(30, 30);
            setAlignment(Pos.CENTER);

            getChildren().add(markLabel);

            setOnMouseClicked(e -> onClick.accept(this));
        }

        void setMark(String mark) {
            markLabel.setText(mark);
        }

        void reset() {
            setStyle("-fx-border-color: black; -fx-border-width: 0.5;");
            markLabel.setText("");

        }

        int getSquareLocation() {
            return location;
        }

        void highlight(String color) {
            setStyle("-fx-border-color: " + color + "; -fx-border-width: 2; -fx-background-color: " + color + ";");
        }
    }
}
