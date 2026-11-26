package com.finalProject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.awt.Desktop;

import com.finalProject.Reporting.CommentWriter;
import com.finalProject.Reporting.CsvWriter;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Gui extends Application {
    private final SettingsStore settingsStore = new SettingsStore();
    private final AssignmentRegistry assignmentRegistry = new AssignmentRegistry();
    private Settings.User userSettings;
    private Settings.Eval evalSettings;
    private Settings.Model modelSettings;
    private Map<UUID, AssignmentRegistry.Entry> assignmentMap;
    private BorderPane root = new BorderPane();
    private EditAssignmentPane editAssignmentPane;

    public Gui() {
        userSettings = settingsStore.getUserSettings();
        evalSettings = settingsStore.getEvalSettings();
        modelSettings = settingsStore.getModelSettings();
        assignmentMap = assignmentRegistry.load();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        MenuBar menuBar = createMenu();
        root.setTop(menuBar);

        showListView();
        
        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("/gui.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Auto Grader");
        primaryStage.show();
    }

    private void showListView() {
        root.setCenter(getListAssignmentsPane());
    }
    private void showEditView(AssignmentRow assignment) {
        editAssignmentPane = new EditAssignmentPane(assignment);
        root.setCenter(editAssignmentPane);
    }
    private void showCreateView() {
        editAssignmentPane = new EditAssignmentPane(null);
        root.setCenter(editAssignmentPane);
    }
    private void showEvalView(AssignmentRow assignment) {
        EvalAssignmentPane evalAssignmentPane = new EvalAssignmentPane(assignment);
        root.setCenter(evalAssignmentPane);
    }

    private void showSettings() {
        // Open the settings dialog and update the settings if changed
        SettingsDialog dialog = new SettingsDialog(userSettings, evalSettings, modelSettings);
        dialog.showAndWait().ifPresent(result -> {
            userSettings = result.user();
            evalSettings = result.eval();
            modelSettings = result.model();
            settingsStore.saveUserSettings(userSettings);
            settingsStore.saveEvalSettings(evalSettings);
            settingsStore.saveModelSettings(modelSettings);
        });
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Auto Grader");
        alert.setContentText("Version 1.0\nDeveloped by Dustin Randall 2026");
        alert.showAndWait();
    }

    private MenuBar createMenu() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem settingsItem = new MenuItem("Settings");
        settingsItem.setOnAction(e -> showSettings());
        fileMenu.getItems().addAll(settingsItem);
       
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAbout());
        helpMenu.getItems().add(aboutItem);
    
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    private record AssignmentRow(UUID id, AssignmentRegistry.Entry entry) {}
    private Pane getListAssignmentsPane() {
        GridPane grid = new GridPane(10, 10);

        Button addButton = new Button("New");
        addButton.setOnAction(e -> showCreateView());
        grid.add(addButton, 0, 0);

        List<AssignmentRow> rows = new ArrayList<>();
        for (Map.Entry<UUID, AssignmentRegistry.Entry> entry : assignmentMap.entrySet()) {
            AssignmentRow row = new AssignmentRow(entry.getKey(), entry.getValue());
            rows.add(row);
        }
        rows.sort((a, b) -> a.entry().name().compareTo(b.entry().name()));
        int rowIndex = 1;
        for (AssignmentRow row : rows) {
            Label nameLabel = new Label(row.entry().name());
            Button evalButton = new Button("Eval");
            evalButton.setTooltip(new Tooltip("Evaluate assignment"));
            evalButton.setAccessibleText("Evaluate assignment");
            evalButton.setOnAction(e -> {
                showEvalView(row);
            });
            Button deleteButton = new Button("Delete"); // 🗑
            deleteButton.setTooltip(new Tooltip("Delete assignment"));
            deleteButton.setAccessibleText("Delete assignment");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setOnAction(e -> {
                if(new Alert(AlertType.CONFIRMATION, "Would you like to forget this assignment? No data will be deleted.", ButtonType.YES, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                    assignmentRegistry.deleteEntry(row.id());
                    assignmentRegistry.save();
                    grid.getChildren().removeAll(nameLabel, deleteButton);                    
                }
            });
            deleteButton.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> e.consume());

            HBox rowNode = new HBox(10, nameLabel, evalButton, deleteButton);
            rowNode.setPickOnBounds(true);
            HBox.setHgrow(nameLabel, Priority.ALWAYS);
            rowNode.getStyleClass().add("assignment-row");

            nameLabel.getStyleClass().add("highlight");
            nameLabel.setOnMouseClicked(e -> {
                if(e.getButton() == MouseButton.PRIMARY) {
                    // open assignment(Row);
                    showEditView(row);
                }
            });
            
            grid.add(rowNode, 0, rowIndex++, 2, 1);
        }

        return grid;
    }

    private class EditAssignmentPane extends BorderPane {
        private Settings.Assignment settings;
        private boolean dirty;
        private GridPane grid;
        private UUID oldId;

        private TextField pathField = new TextField();
        private TextField nameField = new TextField();
        private TextField testerField = new TextField();
        private TextField baseCodeField = new TextField();
        private TextField aiCodeDirField = new TextField();
        private TextArea gradingPromptField = new TextArea();
        private TextArea similarityPromptField = new TextArea();
        private TextArea aiLikenessPromptField = new TextArea();
        private TextField outDirField = new TextField();
        private Spinner<Integer> compileTimeoutField = new Spinner<>(1, 3600, 20);
        private Spinner<Integer> testTimeoutField = new Spinner<>(1, 3600, 40);

        public EditAssignmentPane(AssignmentRow assignment) {
            
            if(assignment != null) {
                oldId = assignment.id();
                var entry = assignment.entry();
                settings = settingsStore.getAssignmentSettings(entry.path());

                nameField.setText(entry.name());
                pathField.setText(entry.path().toString());

                testerField.setText(settings.testerPath());
                baseCodeField.setText(settings.baseCode());
                aiCodeDirField.setText(settings.aiCodeDir());
                similarityPromptField.setText(settings.similarityPrompt());
                aiLikenessPromptField.setText(settings.aiLikenessPrompt());
                outDirField.setText(settings.outDir());
                gradingPromptField.setText(settings.gradingPrompt());
                compileTimeoutField.getValueFactory().setValue((int) settings.compileTimeout().getSeconds());
                testTimeoutField.getValueFactory().setValue((int) settings.testTimeout().getSeconds());
            } else {
                var defaults = settingsStore.defaultAssignmentSettings(Path.of(userSettings.rootAssignmentDir()));
                similarityPromptField.setText(defaults.similarityPrompt());
                aiLikenessPromptField.setText(defaults.aiLikenessPrompt());
                gradingPromptField.setText(defaults.gradingPrompt());
                compileTimeoutField.getValueFactory().setValue((int) defaults.compileTimeout().getSeconds());
                testTimeoutField.getValueFactory().setValue((int) defaults.testTimeout().getSeconds());
            }

            gradingPromptField.setWrapText(true);
            similarityPromptField.setWrapText(true);
            aiLikenessPromptField.setWrapText(true);
            compileTimeoutField.setEditable(true);
            testTimeoutField.setEditable(true);

            GridPane.setHgrow(gradingPromptField, Priority.ALWAYS);
            GridPane.setHgrow(similarityPromptField, Priority.ALWAYS);
            GridPane.setVgrow(gradingPromptField, Priority.ALWAYS);
            GridPane.setVgrow(similarityPromptField, Priority.ALWAYS);

            buildGrid();
            setCenter(grid);
        }

        private void buildGrid() {
            // root assignment directory
            DirectoryChooser pathChooser = new DirectoryChooser();
            pathChooser.setTitle("Root assignment directory");
            if(userSettings.rootAssignmentDir() != null && !userSettings.rootAssignmentDir().isEmpty()) {
                pathChooser.setInitialDirectory(new File(userSettings.rootAssignmentDir()));
            }
            Button pathBrowseButton = new Button("Browse...");

            // Tester
            FileChooser testerChooser = new FileChooser();
            testerChooser.setTitle("Select tester java program");
            testerChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));
            if(pathField.getText() != null && !pathField.getText().isEmpty()) {
                testerChooser.setInitialDirectory(new File(pathField.getText()));
            }
            Button testerBrowseButton = new Button("Browse...");
            testerBrowseButton.setOnAction(e -> {
                File selectedFile = testerChooser.showOpenDialog(getScene().getWindow());
                if(selectedFile != null) {
                    testerField.setText(selectedFile.getAbsolutePath());
                }
            });

            // Base Code
            FileChooser baseCodeChooser = new FileChooser();
            baseCodeChooser.setTitle("Select base code java program");
            baseCodeChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));
            if(pathField.getText() != null && !pathField.getText().isEmpty()) {
                baseCodeChooser.setInitialDirectory(new File(pathField.getText()));
            }

            Button baseCodeBrowseButton = new Button("Browse...");
            baseCodeBrowseButton.setOnAction(e -> {
                File selectedFile = baseCodeChooser.showOpenDialog(getScene().getWindow());
                if(selectedFile != null) {
                    baseCodeField.setText(selectedFile.getAbsolutePath());
                }
            });

            // AI Code Directory
            DirectoryChooser aiCodeDirChooser = new DirectoryChooser();
            aiCodeDirChooser.setTitle("Select AI code directory");
            if(pathField.getText() != null && !pathField.getText().isEmpty()) {
                aiCodeDirChooser.setInitialDirectory(new File(pathField.getText()));
            }
            Button aiCodeDirBrowseButton = new Button("Browse...");
            aiCodeDirBrowseButton.setOnAction(e -> {
                File selectedDirectory = aiCodeDirChooser.showDialog(getScene().getWindow());
                if(selectedDirectory != null) {
                    aiCodeDirField.setText(selectedDirectory.getAbsolutePath());
                }
            });

            // Out Dir
            DirectoryChooser outDirChooser = new DirectoryChooser();
            outDirChooser.setTitle("Select output directory");
            if(pathField.getText() != null && !pathField.getText().isEmpty()) {
                outDirChooser.setInitialDirectory(new File(pathField.getText()));
            }
            Button outDirBrowseButton = new Button("Browse...");
            outDirBrowseButton.setOnAction(e -> {
                File selectedDirectory = outDirChooser.showDialog(getScene().getWindow());
                if(selectedDirectory != null) {
                    outDirField.setText(selectedDirectory.toString());
                }
            });
            
            pathBrowseButton.setOnAction(e -> {
                File selectedDirectory = pathChooser.showDialog(getScene().getWindow());
                if(selectedDirectory != null) {
                    pathField.setText(selectedDirectory.getAbsolutePath());
                    nameField.setText(selectedDirectory.toPath().getFileName().toString());
                    var testerPath = selectedDirectory.toPath().resolve("Tester.java");
                    testerField.setText(testerPath.toFile().exists() ? testerPath.toString() : "");

                    var baseCodePath = selectedDirectory.toPath().resolve("Provided.java");
                    baseCodeField.setText(baseCodePath.toFile().exists() ? baseCodePath.toString() : "");

                    var aiCodeDirPath = selectedDirectory.toPath().resolve("AiSubmissions");
                    aiCodeDirField.setText(aiCodeDirPath.toFile().exists() ? aiCodeDirPath.toString() : "");

                    var oldDirPath = outDirField.getText().isEmpty() ? null : new File(outDirField.getText()).toPath().getFileName();
                    var defaultDirPath = selectedDirectory.toPath().resolve("Results");
                    var newDirPath = selectedDirectory.toPath().resolve(oldDirPath == null ? defaultDirPath.getFileName() : oldDirPath);

                    outDirField.setText(newDirPath.toString());

                    testerChooser.setInitialDirectory(selectedDirectory);
                    baseCodeChooser.setInitialDirectory(selectedDirectory);
                    aiCodeDirChooser.setInitialDirectory(selectedDirectory);
                    outDirChooser.setInitialDirectory(selectedDirectory);                    
                }
            });

            pathField.textProperty().addListener((_, __, ___) -> dirty = true);
            nameField.textProperty().addListener((_, __, ___) -> dirty = true);
            testerField.textProperty().addListener((_, __, ___) -> dirty = true);
            baseCodeField.textProperty().addListener((_, __, ___) -> dirty = true);
            aiCodeDirField.textProperty().addListener((_, __, ___) -> dirty = true);
            outDirField.textProperty().addListener((_, __, ___) -> dirty = true);
            compileTimeoutField.valueProperty().addListener((_, __, ___) -> dirty = true);
            testTimeoutField.valueProperty().addListener((_, __, ___) -> dirty = true);
            similarityPromptField.textProperty().addListener((_, __, ___) -> dirty = true);
            aiLikenessPromptField.textProperty().addListener((_, __, ___) -> dirty = true);
            gradingPromptField.textProperty().addListener((_, __, ___) -> dirty = true);

            Button saveButton = new Button("Save");
            saveButton.setOnAction(e -> onSave());
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(e -> onCancel());

            
            grid = new GridPane(10, 10);
            grid.addRow(0, new Label("Assignment Path"), pathField, pathBrowseButton);
            grid.addRow(1, new Label("Assignment Name"), nameField);
            grid.addRow(2, new Label("Tester Path"), testerField, testerBrowseButton);
            grid.addRow(3, new Label("Base Code"), baseCodeField, baseCodeBrowseButton);
            grid.addRow(4, new Label("AI Code Dir"), aiCodeDirField, aiCodeDirBrowseButton);
            grid.addRow(5, new Label("Out Dir"), outDirField, outDirBrowseButton);
            grid.addRow(6, new Label("Compile Timeout"), compileTimeoutField);
            grid.addRow(7, new Label("Test Timeout"), testTimeoutField);
            grid.addRow(8, new Label("Similarity Prompt"), similarityPromptField);
            grid.addRow(9, new Label("AI Likeness Prompt"), aiLikenessPromptField);
            grid.addRow(10, new Label("AI Grading Prompt"), gradingPromptField);
            grid.addRow(11, saveButton, cancelButton);
        }

        private void onSave() {
            if(dirty) {
                if(!isValid()) {
                    new Alert(AlertType.ERROR, "Please fill in all fields correctly.").showAndWait();
                    return;
                }

                var newSettings = new Settings.Assignment(
                    testerField.getText(), 
                    baseCodeField.getText(), 
                    aiCodeDirField.getText(), 
                    gradingPromptField.getText(),
                    similarityPromptField.getText(),
                    aiLikenessPromptField.getText(),
                    outDirField.getText(), 
                    Duration.ofSeconds(compileTimeoutField.getValue()), 
                    Duration.ofSeconds(testTimeoutField.getValue()));

                settingsStore.saveAssignmentSettings(newSettings, Path.of(pathField.getText()));
                if(oldId == null) {
                    assignmentRegistry.addEntry(nameField.getText(), Path.of(pathField.getText()));
                } else {
                    assignmentRegistry.updateEntry(oldId, nameField.getText(), Path.of(pathField.getText()));
                }
                assignmentRegistry.save();
                assignmentMap = assignmentRegistry.get();
                dirty = false;
                showListView();
            }
        }

        private void onCancel() {
            if(dirty) {
                if(new Alert(AlertType.CONFIRMATION, "Would you like to discard changes?", ButtonType.YES, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                    dirty = false;
                }
            }
            if(!dirty) {
                showListView();
            }
        }

        private boolean isValid() {
            return !outDirField.getText().isBlank() &&
                   compileTimeoutField.getValue() > 0 &&
                   testTimeoutField.getValue() > 0;
        }
    }

    private class EvalAssignmentPane extends BorderPane {
        private final AssignmentRow assignment;
        private final Settings.Assignment settings;
        
        private final TextField submissionDirField = new TextField();
        private final Button submitButton = new Button("Submit");
        private final Button backButton = new Button("Back");
        private final ProgressIndicator progress = new ProgressIndicator();

        private final Label errorLabel = new Label();
        private String errorMessage;

        public EvalAssignmentPane(AssignmentRow assignment) {
            this.assignment = assignment;
            this.settings = settingsStore.getAssignmentSettings(assignment.entry().path());
            errorLabel.setWrapText(true);
            errorLabel.getStyleClass().add("error-banner");

            if(errorMessage != null && !errorMessage.isBlank()) {
                errorLabel.setText("Error: " + errorMessage);
                setTop(errorLabel);
            }

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
                showListView();
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
            if(evalSettings.testRun() && (settings.testerPath() == null || settings.testerPath().isEmpty() || !Path.of(settings.testerPath()).toFile().exists())) {
                return Optional.of("Test Run requires Tester path");
            }
            if((evalSettings.remotePlagDetect()) && (settings.similarityPrompt() == null || settings.similarityPrompt().isEmpty())) {
                return Optional.of("Remote AI Detect requires Similarity Prompt");
            }
            if(evalSettings.remoteAiScore() && (settings.gradingPrompt() == null || settings.gradingPrompt().isEmpty())) {
                return Optional.of("Remote AI Score requires Grading Prompt");
            }
            if(evalSettings.remoteAiDetect() && (settings.aiLikenessPrompt() == null || settings.aiLikenessPrompt().isEmpty())) {
                return Optional.of("Remote AI Detect requires AI Likeness Prompt");
            }
            if((evalSettings.localAiDetect() || evalSettings.remoteAiDetect()) && (settings.aiCodeDir() == null || settings.aiCodeDir().isEmpty() || !Path.of(settings.aiCodeDir()).toFile().exists())) {
                return Optional.of("AI Detection requires AI Code Directory");
            }
            return Optional.empty();
        }

        private void runEvaluation() {
            Task<Evaluator.EvalResult> task = new Task<>() {
                @Override
                protected Evaluator.EvalResult call() {
                    try(Evaluator evaluator = new Evaluator(new Settings(userSettings, settings, evalSettings, modelSettings))) {
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
            CommentWriter commentWriter = new CommentWriter(settings);
            commentWriter.writeComments(results);
            
            var usage = results.tokenUsage();
            var tokenLabel = new Label(String.format(Locale.ROOT, "Token Usage: %,d input + %,d output = %,d total", usage.input(), usage.output(), usage.input() + usage.output()));
            Button exportCsvButton = new Button("Export Csv");
            Button backButton = new Button("Back");

            exportCsvButton.setOnAction(e -> {
                try(var csv = new CsvWriter(settings, results.runId())) {
                    for(var entry : results.results().entrySet()) {
                        csv.writeResult(entry.getKey(), entry.getValue());
                    }
                } catch(Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            backButton.setOnAction(e -> {
                showEvalView(assignment);
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

    private record SettingsDraft(Settings.User user, Settings.Eval eval, Settings.Model model) {}

    private class SettingsDialog extends Dialog<SettingsDraft> {
        private final TextField junitPathField;
        private final TextField javaDirField;
        private final TextField rootAssignmentDirField;
        private final Spinner<Integer> maxThreadsSpinner;
        private final CheckBox localAiDetect;
        private final CheckBox remoteAiDetect;
        private final CheckBox localPlagDetect;
        private final CheckBox remotePlagDetect;
        private final CheckBox testRun; //boolean localAiDetect, boolean remoteAiDetect, boolean localPlagDetect, boolean remotePlagDetect, boolean testRun
        private final CheckBox remoteAiScore;
        private final TextField modelNameField;
        private final TextField regionField;
        private final Spinner<Integer> maxTokensSpinner;
        private final Spinner<Double> temperatureSpinner;
        private final Spinner<Double> plagThresholdSpinner;

        public SettingsDialog(Settings.User userSettings, Settings.Eval evalSettings, Settings.Model modelSettings) {
            this.junitPathField = new TextField(userSettings.junitJar());
            this.javaDirField = new TextField(userSettings.javaDir());
            this.rootAssignmentDirField = new TextField(userSettings.rootAssignmentDir());
            this.maxThreadsSpinner = new Spinner<>(0, 32, userSettings.maxThreads());
            this.maxThreadsSpinner.setEditable(true);

            this.localAiDetect = new CheckBox();
            this.localAiDetect.setSelected(evalSettings.localAiDetect());
            this.remoteAiDetect = new CheckBox();
            this.remoteAiDetect.setSelected(evalSettings.remoteAiDetect());
            this.localPlagDetect = new CheckBox();
            this.localPlagDetect.setSelected(evalSettings.localPlagDetect());
            this.remotePlagDetect = new CheckBox();
            this.remotePlagDetect.setSelected(evalSettings.remotePlagDetect());
            this.testRun = new CheckBox();
            this.testRun.setSelected(evalSettings.testRun());
            this.remoteAiScore = new CheckBox();
            this.remoteAiScore.setSelected(evalSettings.remoteAiScore());
            this.modelNameField = new TextField(modelSettings.modelId());
            this.regionField = new TextField(modelSettings.region());
            this.maxTokensSpinner = new Spinner<>(0, 32768, modelSettings.maxTokens());
            this.maxTokensSpinner.setEditable(true);
            this.temperatureSpinner = new Spinner<>(0.0, 1.0, modelSettings.temperature());
            this.temperatureSpinner.setEditable(true);
            this.plagThresholdSpinner = new Spinner<>(0.0, 1.0, modelSettings.plagThreshold());
            this.plagThresholdSpinner.setEditable(true);

            setResizable(true);
            setTitle("Settings");
            var pane = new DialogPane();
            pane.setContent(new TabPane(getUserTab(), getEvalTab(), getModelTab()));
            pane.setMinWidth(800);
            setDialogPane(pane);
            
            ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(save, ButtonType.CANCEL);
            setResultConverter(button -> button == save 
                ? new SettingsDraft(
                    new Settings.User(javaDirField.getText(), junitPathField.getText(), rootAssignmentDirField.getText(), maxThreadsSpinner.getValue()), 
                    new Settings.Eval(localAiDetect.isSelected(), remoteAiDetect.isSelected(), localPlagDetect.isSelected(), remotePlagDetect.isSelected(), testRun.isSelected(), remoteAiScore.isSelected()),
                    new Settings.Model(modelNameField.getText(), regionField.getText(), maxTokensSpinner.getValue(), temperatureSpinner.getValue().floatValue(), plagThresholdSpinner.getValue())
                ) 
                : null);
        }

        private Tab getUserTab() {
            var tab = new Tab("User");
            tab.setClosable(false);

            // junit
            FileChooser junitChooser = new FileChooser();
            junitChooser.setTitle("Select JUnit Console JAR");
            junitChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR Files", "*.jar"));
            if (Files.exists(Path.of(junitPathField.getText()))) {
                junitChooser.setInitialDirectory(Path.of(junitPathField.getText()).getParent().toFile());
            }

            Button browseButton = new Button("Browse...");
            browseButton.setOnAction(e -> {
                File selectedFile = junitChooser.showOpenDialog(getDialogPane().getScene().getWindow());
                if (selectedFile != null) {
                    junitPathField.setText(selectedFile.getAbsolutePath());
                }
            });

            // java
            DirectoryChooser javaChooser = new DirectoryChooser();
            javaChooser.setTitle("Select Java bin Directory");
            if (Files.exists(Path.of(javaDirField.getText()))) {
                javaChooser.setInitialDirectory(Path.of(javaDirField.getText()).toFile());
            }

            Button javaBrowseButton = new Button("Browse...");
            javaBrowseButton.setOnAction(e -> {
                File selectedFile = javaChooser.showDialog(getDialogPane().getScene().getWindow());
                if (selectedFile != null) {
                    javaDirField.setText(selectedFile.getAbsolutePath());
                }
            });

            // root assignment dir
            DirectoryChooser rootAssignmentChooser = new DirectoryChooser();
            rootAssignmentChooser.setTitle("Select Root Assignment Directory");
            if (Files.exists(Path.of(rootAssignmentDirField.getText()))) {
                rootAssignmentChooser.setInitialDirectory(Path.of(rootAssignmentDirField.getText()).toFile());
            }

            Button rootAssignmentBrowseButton = new Button("Browse...");
            rootAssignmentBrowseButton.setOnAction(e -> {
                File selectedFile = rootAssignmentChooser.showDialog(getDialogPane().getScene().getWindow());
                if (selectedFile != null) {
                    rootAssignmentDirField.setText(selectedFile.getAbsolutePath());
                }
            });

            // layout
            var grid = getGrid();

            grid.addRow(0, new Label("JUnit Console Jar"), junitPathField, browseButton);
            grid.addRow(1, new Label("Java Directory"), javaDirField, javaBrowseButton);
            grid.addRow(2, new Label("Root Assignment Directory"), rootAssignmentDirField, rootAssignmentBrowseButton);
            grid.addRow(3, new Label("Max Threads"), maxThreadsSpinner);
            GridPane.setHgrow(junitPathField, Priority.ALWAYS);
            GridPane.setHgrow(javaDirField, Priority.ALWAYS);
            GridPane.setHgrow(rootAssignmentDirField, Priority.ALWAYS);
            GridPane.setHgrow(maxThreadsSpinner, Priority.ALWAYS);
            tab.setContent(grid);

            return tab;
        }
        
        private Tab getEvalTab() {
            var tab = new Tab("Evaluation");
            tab.setClosable(false);
            
            
            var grid = getGrid();
            grid.addRow(0, new Label("Local AI Detection"), localAiDetect);
            grid.addRow(1, new Label("Remote AI Detection"), remoteAiDetect);
            grid.addRow(2, new Label("Local Plagiarism Detection"), localPlagDetect);
            grid.addRow(3, new Label("Remote Plagiarism Detection"), remotePlagDetect);
            grid.addRow(4, new Label("Test Run"), testRun);
            grid.addRow(5, new Label("Remote AI Scoring"), remoteAiScore);
            tab.setContent(grid);
            return tab;
        }

        private Tab getModelTab() {
            var tab = new Tab("Model");
            tab.setClosable(false);

            var grid = getGrid();
            grid.addRow(0, new Label("Model Name"), modelNameField);
            grid.addRow(1, new Label("Region"), regionField);
            grid.addRow(2, new Label("Max Tokens"), maxTokensSpinner);
            grid.addRow(3, new Label("Temperature"), temperatureSpinner);
            grid.addRow(4, new Label("Plagiarism Threshold"), plagThresholdSpinner);
            tab.setContent(grid);
            return tab;
        }

        private GridPane getGrid() {
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            ColumnConstraints labelColumn = new ColumnConstraints();
            labelColumn.setPrefWidth(140);

            ColumnConstraints inputColumn = new ColumnConstraints();
            inputColumn.setHgrow(Priority.ALWAYS);

            ColumnConstraints browseColumn = new ColumnConstraints();
            browseColumn.setPrefWidth(90);

            grid.getColumnConstraints().addAll(labelColumn, inputColumn, browseColumn);
            return grid;
        }

    }
}
