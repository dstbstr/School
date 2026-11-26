package com.finalProject.Ui;

import java.nio.file.Path;
import java.util.Optional;
import java.io.File;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

import com.finalProject.Settings.Settings;
import com.finalProject.Ui.Gui.AssignmentRow;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class EditAssignmentPane extends BorderPane {
    public record AssignmentDraft(
        Optional<UUID> existingId,
        String name,
        Path path,
        Settings.Assignment settings
    ) {}

    private final Runnable onBackToList;
    private final Consumer<AssignmentDraft> onSave;

    private Settings settings;
    private boolean dirty;
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

    public EditAssignmentPane(AssignmentRow assignment, Settings settings, Runnable onBackToList, Consumer<AssignmentDraft> onSave) {
        this.onBackToList = onBackToList;
        this.onSave = onSave;
        this.settings = settings;
        
        if(assignment != null) {
            oldId = assignment.id();
            var entry = assignment.entry();

            nameField.setText(entry.name());
            pathField.setText(entry.path().toString());

            testerField.setText(settings.assignment().testerPath());
            baseCodeField.setText(settings.assignment().baseCode());
            aiCodeDirField.setText(settings.assignment().aiCodeDir());
            outDirField.setText(settings.assignment().outDir());
        }

        similarityPromptField.setText(settings.assignment().similarityPrompt());
        aiLikenessPromptField.setText(settings.assignment().aiLikenessPrompt());
        gradingPromptField.setText(settings.assignment().gradingPrompt());
        compileTimeoutField.getValueFactory().setValue((int) settings.assignment().compileTimeout().getSeconds());
        testTimeoutField.getValueFactory().setValue((int) settings.assignment().testTimeout().getSeconds());

        gradingPromptField.setWrapText(true);
        similarityPromptField.setWrapText(true);
        aiLikenessPromptField.setWrapText(true);
        compileTimeoutField.setEditable(true);
        testTimeoutField.setEditable(true);

        GridPane.setHgrow(gradingPromptField, Priority.ALWAYS);
        GridPane.setHgrow(similarityPromptField, Priority.ALWAYS);
        GridPane.setHgrow(aiLikenessPromptField, Priority.ALWAYS);
        
        GridPane.setVgrow(gradingPromptField, Priority.ALWAYS);
        GridPane.setVgrow(similarityPromptField, Priority.ALWAYS);
        GridPane.setVgrow(aiLikenessPromptField, Priority.ALWAYS);

        setCenter(buildGrid());
    }

    private GridPane buildGrid() {
        GridPane grid = new GridPane();
        // root assignment directory
        DirectoryChooser pathChooser = new DirectoryChooser();
        pathChooser.setTitle("Root assignment directory");
        if(settings.user().rootAssignmentDir() != null && !settings.user().rootAssignmentDir().isEmpty()) {
            pathChooser.setInitialDirectory(new File(settings.user().rootAssignmentDir()));
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

        return grid;
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

            var draft = new AssignmentDraft(Optional.ofNullable(oldId), nameField.getText(), Path.of(pathField.getText()), newSettings);
            onSave.accept(draft);
            dirty = false;
            onBackToList.run();
        }
    }

    private void onCancel() {
        if(dirty) {
            if(new Alert(AlertType.CONFIRMATION, "Would you like to discard changes?", ButtonType.YES, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                dirty = false;
            }
        }
        if(!dirty) {
            onBackToList.run();
        }
    }

    private boolean isValid() {
        return !outDirField.getText().isBlank() &&
                compileTimeoutField.getValue() > 0 &&
                testTimeoutField.getValue() > 0;
    }
}
