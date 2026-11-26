package com.finalProject.Ui;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.finalProject.Settings.AssignmentRegistry;
import com.finalProject.Settings.Settings;
import com.finalProject.Settings.SettingsStore;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Gui extends Application {
    public record AssignmentRow(UUID id, AssignmentRegistry.Entry entry) {}

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
        List<Gui.AssignmentRow> rows = new ArrayList<>();
        for (Map.Entry<UUID, AssignmentRegistry.Entry> entry : assignmentMap.entrySet()) {
            var row = new Gui.AssignmentRow(entry.getKey(), entry.getValue());
            rows.add(row);
        }
        
        root.setCenter(new ListAssignmentPane(
            rows, 
            this::showEditView, 
            this::showCreateView, 
            this::showEvalView, 
            this::deleteAssignment));
    }

    private void showEditView(Gui.AssignmentRow assignment) {
        var assignmentSettings = settingsStore.getAssignmentSettings(assignment.entry().path());
        editAssignmentPane = new EditAssignmentPane(
            assignment, 
            new Settings(userSettings, assignmentSettings, evalSettings, modelSettings), 
            this::showListView, 
            this::onSaveAssignment);

        root.setCenter(editAssignmentPane);
    }

    private void showCreateView() {
        var assignmentSettings = settingsStore.defaultAssignmentSettings(Path.of(userSettings.rootAssignmentDir()));
        editAssignmentPane = new EditAssignmentPane(
            null, 
            new Settings(userSettings, assignmentSettings, evalSettings, modelSettings), 
            this::showListView, 
            this::onSaveAssignment);

        root.setCenter(editAssignmentPane);
    }

    private void showEvalView(Gui.AssignmentRow assignment) {
        var assignmentSettings = settingsStore.getAssignmentSettings(assignment.entry().path());
        var evalAssignmentPane = new EvalAssignmentPane(
            assignment, 
            new Settings(userSettings, assignmentSettings, evalSettings, modelSettings), 
            this::showListView, 
            this::showEvalView);

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

    private void deleteAssignment(Gui.AssignmentRow assignment) {
        assignmentRegistry.deleteEntry(assignment.id());
        assignmentRegistry.save();
        assignmentMap = assignmentRegistry.get();
        showListView();
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Auto Grader");
        alert.setContentText("Version 1.0\nDeveloped by Dustin Randall 2026");
        alert.showAndWait();
    }

    private void onSaveAssignment(EditAssignmentPane.AssignmentDraft draft) {
        settingsStore.saveAssignmentSettings(draft.settings(), draft.path());
        if(draft.existingId().isEmpty()) {
            assignmentRegistry.addEntry(draft.name(), draft.path());
        } else {
            assignmentRegistry.updateEntry(draft.existingId().get(), draft.name(), draft.path());
        }
        assignmentRegistry.save();
        assignmentMap = assignmentRegistry.get();
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
}
