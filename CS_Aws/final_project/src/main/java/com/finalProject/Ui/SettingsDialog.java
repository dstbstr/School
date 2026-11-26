package com.finalProject.Ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import com.finalProject.Settings;

public class SettingsDialog extends Dialog<SettingsDialog.SettingsDraft> {
    public record SettingsDraft(Settings.User user, Settings.Eval eval, Settings.Model model) {}

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