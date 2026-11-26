package com.finalProject.Ui;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ListAssignmentPane extends BorderPane {
    private final Consumer<Gui.AssignmentRow> onShowEditView;
    private final Runnable onShowCreateView;
    private final Consumer<Gui.AssignmentRow> onShowEvalView;
    private final Consumer<Gui.AssignmentRow> onDelete;
    private final List<Gui.AssignmentRow> assignmentList;
    
    public ListAssignmentPane(List<Gui.AssignmentRow> assignmentList, Consumer<Gui.AssignmentRow> onShowEditView, Runnable onShowCreateView, Consumer<Gui.AssignmentRow> onShowEvalView, Consumer<Gui.AssignmentRow> onDelete) {
        this.onShowEditView = onShowEditView;
        this.onShowCreateView = onShowCreateView;
        this.onShowEvalView = onShowEvalView;
        this.onDelete = onDelete;
        this.assignmentList = assignmentList;

        setCenter(buildPane());
    }

    private GridPane buildPane() {
        GridPane grid = new GridPane(10, 10);

        Button addButton = new Button("New");
        addButton.setOnAction(e -> onShowCreateView.run());
        grid.add(addButton, 0, 0);

        assignmentList.sort((a, b) -> a.entry().name().compareTo(b.entry().name()));
        int rowIndex = 1;
        for (var row : assignmentList) {
            Label nameLabel = new Label(row.entry().name());
            Button evalButton = new Button("Eval");
            evalButton.setTooltip(new Tooltip("Evaluate assignment"));
            evalButton.setAccessibleText("Evaluate assignment");
            evalButton.setOnAction(e -> {
                onShowEvalView.accept(row);
            });
            Button deleteButton = new Button("Delete"); // 🗑
            deleteButton.setTooltip(new Tooltip("Delete assignment"));
            deleteButton.setAccessibleText("Delete assignment");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setOnAction(e -> {
                if(new Alert(
                    AlertType.CONFIRMATION, 
                    "Forget this assignment? No data will be deleted.", 
                    ButtonType.YES, 
                    ButtonType.NO
                ).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                    onDelete.accept(row);
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
                    onShowEditView.accept(row);
                }
            });
            
            grid.add(rowNode, 0, rowIndex++, 2, 1);
        }

        return grid;
    }
}
