package com.randalld.Commands;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.randalld.ProgressReport;
import com.randalld.Store.FileStore;
import com.randalld.Store.LocalFileStore;
import com.randalld.Store.S3FileStore;
import com.randalld.Store.FileStore.FileNode;

public class GuiCommands extends Application {
    private FileStore store;

    private final TreeView<FileNode> treeView = new TreeView<>();
    private Label statusLabel;
    private TreeItem<FileNode> rootNode;
    private ProgressBar progress = new ProgressBar();

    private final Set<TreeItem<FileNode>> loaded = new HashSet<>();
    private final TreeItem<FileNode> emptyNode = new TreeItem<>(new FileNode("loading", "loading", true));

    @Override
    public void start(Stage primaryStage) {
        List<String> args = getParameters().getRaw();
        String backend = args.isEmpty() ? "s3" : args.getFirst();
        store = backend.equalsIgnoreCase("s3") ? new S3FileStore() : new LocalFileStore();

        rootNode = new TreeItem<>(new FileNode("/", "", true));
        rootNode.getChildren().add(emptyNode);

        rootNode.expandedProperty().addListener((obs, oldVal, expanded) -> {
            if (expanded && !loaded.contains(rootNode)) {
                rootNode.getChildren().clear();
                store.listBuckets().whenCompleteAsync((result, error) -> {
                    if(error != null) {
                        showAlert("Error: " + error.getMessage());
                        return;
                    }
                    result.forEach(bucket -> rootNode.getChildren().add(createBucketNode(bucket)));
                    loaded.add(rootNode);
                }, Platform::runLater);
            }
        });

        treeView.setRoot(rootNode);
        treeView.setShowRoot(true);
        treeView.setCellFactory(tv -> {
            TreeCell<FileNode> cell = new TreeCell<>() {
                private final Label icon = new Label();

                @Override
                protected void updateItem(FileNode item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                        icon.setText(item.isFolder() ? "📁" : "📄");
                        setGraphic(icon);
                    }
                    setText(empty ? null : item.toString());
                }
            };
            cell.setOnDragOver(event -> {
                FileNode item = cell.getItem();
                if(item == null || !item.isFolder()) {
                    return;
                }
                if (event.getGestureSource() != cell && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            });
            cell.setOnDragEntered(event -> {
                if(cell.getItem() != null && cell.getItem().isFolder()) {
                    cell.setStyle("-fx-background-color: lightblue;");
                }
            });

            cell.setOnDragExited(event -> {
                cell.setStyle("");
            });
            cell.setOnDragDropped(event -> {
                FileNode target = cell.getItem();
                if(target != null && target.isFolder()) {
                    Dragboard drag = event.getDragboard();
                    if(drag.hasFiles()) {
                        Path path = drag.getFiles().getFirst().toPath();
                        onUpload(path, target, cell.getTreeItem());
                    }
                }
                event.setDropCompleted(true);
                event.consume();
            });

            return cell;
        });

        treeView.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.DELETE) {
                onDelete();
            }
        });

        Button refreshButton = new Button("🔄");
        refreshButton.setOnAction(e -> refreshTree());
        refreshButton.setTooltip(new Tooltip("Refresh"));
        refreshButton.setAccessibleText("Refresh");

        statusLabel = new Label("Status Bar...");

        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(this::onDownload);
        Button createBucketButton = new Button("Create Bucket");
        createBucketButton.setOnAction(this::onCreateBucket);
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> onDelete());
        


        progress.setProgress(0);
        progress.setVisible(false);
        // Layout
        BorderPane root = new BorderPane();
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane statusBar = new StackPane(statusLabel, refreshButton);
        StackPane.setAlignment(statusLabel, Pos.CENTER);
        StackPane.setAlignment(refreshButton, Pos.CENTER_RIGHT);
        statusBar.setPadding(new Insets(5));

        VBox topBox = new VBox(statusBar, progress);
        topBox.setAlignment(Pos.CENTER);

        HBox bottomBox = new HBox(10, downloadButton, createBucketButton, deleteButton);
        bottomBox.setAlignment(Pos.CENTER);

        root.setTop(topBox);
        root.setCenter(treeView);
        root.setBottom(new VBox(new Separator(), bottomBox));

        BorderPane.setMargin(treeView, new Insets(10));
        BorderPane.setMargin(statusBar, new Insets(5));

        Scene scene = new Scene(root, 450, 500);
        primaryStage.setTitle("Drag Drop File Transfer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshTree() {
        boolean wasExpanded = rootNode.isExpanded();
        loaded.clear();
        rootNode.getChildren().setAll(List.of(emptyNode));
        if(wasExpanded) {
            rootNode.setExpanded(false);
            rootNode.setExpanded(true);
        }
    }
    private void onDownload(ActionEvent e) {
        TreeItem<FileNode> selected = treeView.getSelectionModel().getSelectedItem();
        if(selected == null) {
            showAlert("Select something to download");
            return;
        }
        FileNode node = selected.getValue();
        if (node.isFolder()) {
            showAlert("Select a file");
            return;
        }
        Path dest = Path.of("./", node.key());
        ProgressReport report = new ProgressReport(prog -> Platform.runLater(() -> progress.setProgress(prog)));

        statusLabel.setText("Downloading to " + dest.toString());
        progress.setVisible(true);

        store.download(selected.getValue(), dest, report).whenCompleteAsync((result, error) -> {
            if(error != null) {
                showAlert("Error: " + error.getMessage());
                return;
            }

            statusLabel.setText("Downloaded " + dest.toString());
            progress.setVisible(false);
        }, Platform::runLater);
    }

    private void onCreateBucket(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Bucket");
        dialog.setHeaderText("Create a new bucket");
        dialog.setContentText("Bucket name:");
        Optional<String> input = dialog.showAndWait();
        if (input.isEmpty()) {
            return;
        }

        String name = input.get().trim();
        if (name.isEmpty()) {
            showAlert("Bucket name cannot be empty");
            return;
        }

        statusLabel.setText("Creating bucket " + name + "...");
        store.createBucket(name).whenCompleteAsync((bucket, error) -> {
            if(error != null) {
                statusLabel.setText("Could not create bucket " + name);
                showAlert("Could not create bucket " + name);
                return;
            }
            if(loaded.contains(rootNode)) {
                rootNode.getChildren().add(createBucketNode(bucket));
            }
            statusLabel.setText("Bucket created: " + name);
        }, Platform::runLater);
    }

    private void onUpload(Path path, FileNode target, TreeItem<FileNode> parent) {
        ProgressReport report = new ProgressReport(prog -> Platform.runLater(() -> progress.setProgress(prog)));
        FileNode dest = new FileNode(target.bucket(), target.key() + path.getFileName(), false);

        statusLabel.setText("Uploading " + path.getFileName() + " to " + target);
        progress.setVisible(true);
        store.upload(path, dest, report).whenCompleteAsync((result, error) -> {
            progress.setVisible(false);
            if(error != null) {
                showAlert("Upload failed: " + error.getMessage());
                return;
            }
            statusLabel.setText("Upload success!");
            loaded.remove(parent);
            reloadChildren(parent);
        }, Platform::runLater);
    }

    private void onDelete() {
        TreeItem<FileNode> item = treeView.getSelectionModel().getSelectedItem();
        if(item == null || item == rootNode) return;
        FileNode target = item.getValue();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + target + "?");
        if(alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        statusLabel.setText("Deleting " + target + "...");

        CompletableFuture<Boolean> future = target.key().isEmpty() ? store.deleteBucket(target.bucket()) : store.delete(target);
        future.whenCompleteAsync((result, error) -> {
            if(error != null) {
                showAlert("Error: " + error.getMessage());
                return;
            }
            item.getParent().getChildren().remove(item);
            statusLabel.setText("Delete success!");
        }, Platform::runLater);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private TreeItem<FileNode> createBucketNode(FileNode parent) {
        TreeItem<FileNode> node = new TreeItem<>(parent);
        node.getChildren().add(emptyNode);

        node.expandedProperty().addListener((obs, old, expanded) -> {
            if(expanded && !loaded.contains(node)) {
                node.getChildren().clear();
                store.listChildren(parent).whenCompleteAsync((result, error) -> {
                    if(error != null) {
                        showAlert("Could not list children: " + error.getMessage());
                        return;
                    }
                    result.forEach(child -> node.getChildren().add(createChildNode(child)));
                    loaded.add(node);
                }, Platform::runLater);
            }
        });

        return node;
    }

    private TreeItem<FileNode> createChildNode(FileNode file) {
        TreeItem<FileNode> node = new TreeItem<>(file);

        if (file.isFolder()) {
            node.getChildren().add(emptyNode);

            node.expandedProperty().addListener((obs, oldVal, expanded) -> {
                if (expanded && !loaded.contains(node)) {
                    node.getChildren().clear();

                    store.listChildren(file).whenCompleteAsync((result, error) -> {
                        if(error != null) {
                            showAlert("Could not list children: " + error.getMessage());
                            return;
                        }
                        result.forEach(child -> node.getChildren().add(createChildNode(child)));
                        loaded.add(node);
                    }, Platform::runLater);
                }
            });
        }

        return node;
    }

    private void reloadChildren(TreeItem<FileNode> node) {
        node.getChildren().clear();
        store.listChildren(node.getValue()).whenCompleteAsync((result, error) -> {
            if(error != null) {
                showAlert("Could not list children: " + error.getMessage());
                return;
            }
            result.forEach(child -> node.getChildren().add(createChildNode(child)));
            loaded.add(node);
        }, Platform::runLater);
    }
}
