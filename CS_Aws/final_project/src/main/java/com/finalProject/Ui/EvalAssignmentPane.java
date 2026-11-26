package com.finalProject.Ui;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;

import java.awt.Desktop;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import com.finalProject.Evaluator;
import com.finalProject.Reporting.CommentWriter;
import com.finalProject.Reporting.CsvWriter;
import com.finalProject.Settings.Settings;

public class EvalAssignmentPane extends BorderPane {
    private final AssignmentRow assignment;
    private final Settings settings;
    private final Runnable onBackToList;
    private final Consumer<AssignmentRow> onBackToEval;
    
    private final TextField submissionDirField = new TextField();
    private final Button submitButton = new Button("Submit");
    private final Button backButton = new Button("Back");
    private final ProgressIndicator progress = new ProgressIndicator();

    private final Label errorLabel = new Label();
    private String errorMessage;

    public EvalAssignmentPane(AssignmentRow assignment, Settings settings, Runnable onBackToList, Consumer<AssignmentRow> onBackToEval) {
        this.assignment = assignment;
        this.settings = settings;
        this.onBackToList = onBackToList;
        this.onBackToEval = onBackToEval;
        errorLabel.setWrapText(true);
        errorLabel.getStyleClass().add("error-banner");

        setCenter(setupGrid());
    }

    private GridPane setupGrid() {
        GridPane grid = new GridPane();
        DirectoryChooser subChooser = new DirectoryChooser();
        subChooser.setInitialDirectory(assignment.entry().path().toFile());
        subChooser.setTitle("Select Root Submission Directory");

        Button browseSubButton = new Button("Browse...");
        browseSubButton.setOnAction(e -> {
            File selectedDir = subChooser.showDialog(this.getScene().getWindow());
            if (selectedDir != null) {
                submissionDirField.setText(selectedDir.getAbsolutePath());
            }
        });

        submitButton.setOnAction(e -> {
            errorMessage = null;
            setTop(null);

            var errors = getValidationErrors();
            if(errors.isPresent()) {
                errorMessage = errors.get();
                // Alert alert = new Alert(Alert.AlertType.ERROR, errors.get(), ButtonType.OK);
                // alert.showAndWait();
                errorLabel.setText("Error: " + errorMessage);
                setTop(errorLabel);
                return;
            }

            runEvaluation();
        });

        backButton.setOnAction(e -> {
            onBackToList.run();
        });

        progress.setVisible(false);
        progress.setManaged(false);

        HBox actions = new HBox(10, submitButton, progress, backButton);

        grid.addRow(0, new Label("Submission Directory:"), submissionDirField, browseSubButton);
        grid.addRow(1, actions);
        
        GridPane.setHgrow(submissionDirField, Priority.ALWAYS);
        return grid;
    }
        
    private Optional<String> getValidationErrors() {
        if(submissionDirField.getText() == null || submissionDirField.getText().isEmpty() || !Path.of(submissionDirField.getText()).toFile().exists()) {
            return Optional.of("Submission Directory is required and must exist");
        }
        if(settings.eval().testRun() && (settings.assignment().testerPath() == null || settings.assignment().testerPath().isEmpty() || !Path.of(settings.assignment().testerPath()).toFile().exists())) {
            return Optional.of("Test Run requires Tester path");
        }
        if((settings.eval().remotePlagDetect()) && (settings.assignment().similarityPrompt() == null || settings.assignment().similarityPrompt().isEmpty())) {
            return Optional.of("Remote AI Detect requires Similarity Prompt");
        }
        if(settings.eval().remoteAiScore() && (settings.assignment().gradingPrompt() == null || settings.assignment().gradingPrompt().isEmpty())) {
            return Optional.of("Remote AI Score requires Grading Prompt");
        }
        if(settings.eval().remoteAiDetect() && (settings.assignment().aiLikenessPrompt() == null || settings.assignment().aiLikenessPrompt().isEmpty())) {
            return Optional.of("Remote AI Detect requires AI Likeness Prompt");
        }
        if((settings.eval().localAiDetect() || settings.eval().remoteAiDetect()) && (settings.assignment().aiCodeDir() == null || settings.assignment().aiCodeDir().isEmpty() || !Path.of(settings.assignment().aiCodeDir()).toFile().exists())) {
            return Optional.of("AI Detection requires AI Code Directory");
        }
        return Optional.empty();
    }

    private void runEvaluation() {
        Task<Evaluator.EvalResult> task = new Task<>() {
            @Override
            protected Evaluator.EvalResult call() {
                try(Evaluator evaluator = new Evaluator(settings)) {
                    return evaluator.evaluate(submissionDirField.getText()).join();
                }
            }
        };

        submitButton.setDisable(true);
        progress.setVisible(true);
        progress.setManaged(true);
        task.setOnSucceeded(e -> {
            submitButton.setDisable(false);
            progress.setVisible(false);
            progress.setManaged(false);
            showResults(task.getValue());
        });
        task.setOnFailed(event -> {
            submitButton.setDisable(false);
            progress.setVisible(false);
            progress.setManaged(false);
            showError(task.getException());
        });

        Thread worker = new Thread(task, "Evaluation");
        worker.setDaemon(true);
        worker.start();
    }

    private record ResultRow(String submission, Evaluator.EvalRow result) {}

    private void showResults(Evaluator.EvalResult results) {
        CommentWriter commentWriter = new CommentWriter(settings.assignment());
        commentWriter.writeComments(results);
        
        var usage = results.tokenUsage();
        var tokenLabel = new Label(String.format(Locale.ROOT, "Token Usage: %,d input + %,d output = %,d total", usage.input(), usage.output(), usage.input() + usage.output()));
        Button exportCsvButton = new Button("Export Csv");
        Button backButton = new Button("Back");

        exportCsvButton.setOnAction(e -> {
            try(var csv = new CsvWriter(settings.assignment(), results.runId())) {
                for(var entry : results.results().entrySet()) {
                    csv.writeResult(entry.getKey(), entry.getValue());
                }
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        backButton.setOnAction(e -> {
            onBackToEval.accept(assignment);
        });
        TableView<ResultRow> table = new TableView<>();

        TableColumn<ResultRow, String> submission = new TableColumn<>("Submission");
        TableColumn<ResultRow, String> localAiScore = new TableColumn<>("Local AI Score");
        TableColumn<ResultRow, String> remoteAiScore = new TableColumn<>("Remote AI Score");
        TableColumn<ResultRow, String> localPlagScore = new TableColumn<>("Local Plag Score");
        TableColumn<ResultRow, String> remotePlagScore = new TableColumn<>("Remote Plag Score");
        TableColumn<ResultRow, String> testRunScore = new TableColumn<>("Test Run Score");
        TableColumn<ResultRow, String> remoteScore = new TableColumn<>("Remote Score");
        TableColumn<ResultRow, Void> actions = new TableColumn<>("Actions");

        submission.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().submission()));
        localAiScore.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatScore(cell.getValue().result().localAiScore())));
        remoteAiScore.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatScore(cell.getValue().result().remoteAiScore())));
        localPlagScore.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatScore(cell.getValue().result().localPlagScore())));
        remotePlagScore.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatScore(cell.getValue().result().remotePlagScore())));
        testRunScore.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatScore(cell.getValue().result().testRunScore())));
        remoteScore.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatScore(cell.getValue().result().remoteQualityScore())));
        actions.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("View comments");

            {
                viewButton.setOnAction(event -> {
                    ResultRow row = getTableView().getItems().get(getIndex());
                    Path file = commentWriter.getCommentFilePath(results.runId(), row.submission());

                    if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                        try {
                            Desktop.getDesktop().open(file.toFile());
                        } catch(Exception ex) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Failed to open the file: " + file.toAbsolutePath());
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText(null);
                        alert.setContentText("Cannot open the file automatically. Please open it manually: " + file.toAbsolutePath());
                        alert.showAndWait();
                        // consider showing on the GUI instead
                    }
                });
            }

            @Override 
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }                
        });
        
        table.getColumns().addAll(List.of(submission, localAiScore, remoteAiScore, localPlagScore, remotePlagScore, testRunScore, remoteScore, actions));
        ObservableList<ResultRow> data = FXCollections.observableArrayList();
        for (var entry : results.results().entrySet()) {
            var result = entry.getValue();
            data.add(new ResultRow(entry.getKey(),result));
        }
        table.setItems(data);

        HBox actionsBox = new HBox(10, exportCsvButton, backButton, tokenLabel);
        setTop(actionsBox);
        setCenter(table);
    }

    private String formatScore(Optional<Double> score) {
        return score.map(value -> String.format(Locale.ROOT, "%.2f", value)).orElse("");
    }
    
    private void showError(Throwable exception) {
        StringBuilder sb = new StringBuilder();
        sb.append(exception.getMessage());
        while(exception.getCause() != null) {
            exception = exception.getCause();
            sb.append("\nCaused by: ").append(exception.getMessage());
        }
        errorMessage = sb.toString();
        errorLabel.setText("Error: " + sb.toString());
        setTop(errorLabel);
    }
}
