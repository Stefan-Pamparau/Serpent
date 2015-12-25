package folderTreeView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FilePathTreeCell extends TreeCell<String> {
    private TextField fileName;
    private ContextMenu folderContextMenu;

    public FilePathTreeCell() {
        initializeContextMenu();
    }

    private void initializeContextMenu() {
        folderContextMenu = new ContextMenu();
        MenuItem addNewFileMenuItem = new MenuItem("New file");
        MenuItem addNewFolderMenuItem = new MenuItem("New folder");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        folderContextMenu.getItems().addAll(addNewFileMenuItem, addNewFolderMenuItem, deleteMenuItem);
        addNewFileEventHandler(addNewFileMenuItem);
        addNewFolderEventHandler(addNewFolderMenuItem);
        addDeleteEventHandler(deleteMenuItem);
    }

    private void addNewFileEventHandler(MenuItem addNewFileMenuItem) {
        addNewFileMenuItem.setOnAction((ActionEvent event) -> {
            FilePathTreeItem selectedDirectory = (FilePathTreeItem) getTreeItem();
            if (selectedDirectory.isDirectory()) {
                Optional<String> result = createTextInputDialog("New file", "New file window", "Please enter the name of the new file");
                if (result.isPresent()) {
                    String fileName = result.get();
                    String directoryPath = selectedDirectory.getFullPath();
                    Path path = Paths.get(directoryPath, fileName);
                    if (!Files.exists(path)) {
                        createNewFile(path);
                        FilePathTreeItem newFile = new FilePathTreeItem(path);
                        selectedDirectory.getChildren().add(newFile);
                    } else {
                        displayInformationDialog("File information", null, "File already exists!");
                    }
                }
            } else {
                displayInformationDialog("Wrong file", null, "File chosen is not a directory!");
            }
        });
    }

    private void createNewFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            displayInformationDialog("File creation error", "File creation error", "Error while creating new file " + e.getLocalizedMessage());
        }
    }

    private void addNewFolderEventHandler(MenuItem addNewFolderMenuItem) {
        addNewFolderMenuItem.setOnAction((ActionEvent event) -> {
            FilePathTreeItem selectedDirectory = (FilePathTreeItem) getTreeItem();
            if (selectedDirectory.isDirectory()) {
                Optional<String> result = createTextInputDialog("New folder", "New folder window", "Please enter the name of the new folder");
                if (result.isPresent()) {
                    String fileName = result.get();
                    String directoryPath = selectedDirectory.getFullPath();
                    Path path = Paths.get(directoryPath, fileName);
                    if (!Files.exists(path)) {
                        createNewFolder(path);
                        FilePathTreeItem newFile = new FilePathTreeItem(path);
                        selectedDirectory.getChildren().add(newFile);
                    } else {
                        displayInformationDialog("Folder information", null, "Folder already exists!");
                    }
                }
            } else {
                displayInformationDialog("Wrong file", null, "File chosen is not a directory!");
            }
        });
    }

    private void createNewFolder(Path path) {
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            displayInformationDialog("Folder creation error", "Folder creation error", "Error while creating new folder " + e.getLocalizedMessage());
        }
    }

    private void addDeleteEventHandler(MenuItem deleteMenuItem) {
        deleteMenuItem.setOnAction((ActionEvent event) -> {
            FilePathTreeItem treeItem = (FilePathTreeItem) getTreeItem();
            Path path = Paths.get(treeItem.getFullPath());
            try {
                Files.delete(path);
                treeItem.getParent().getChildren().removeAll(treeItem);
            } catch (IOException e) {
                displayInformationDialog("Delete error", "Error occurred when deleting" + path.toString(), e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (fileName == null) {
            initializeFileName();
        }
        setText(null);
        setGraphic(fileName);
        fileName.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (fileName != null) {
                    fileName.setText(getFileName());
                }
                setText(null);
                setGraphic(fileName);
            } else {
                setText(getFileName());
                setGraphic(getTreeItem().getGraphic());
                setFolderContextMenu();
            }
        }
    }

    private void setFolderContextMenu() {
        if (getTreeItem() instanceof FilePathTreeItem) {
            FilePathTreeItem treeItem = (FilePathTreeItem) getTreeItem();
            if (treeItem.isDirectory()) {
                setContextMenu(folderContextMenu);
            }
        }
    }

    private void initializeFileName() {
        fileName = new TextField(getFileName());
        fileName.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    renameFile();
                    commitEdit(fileName.getText());
                } catch (IOException e) {
                    displayInformationDialog("File rename error", "Could not rename file", e.getLocalizedMessage());
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

    }

    private void renameFile() throws IOException {
        FilePathTreeItem treeItem = (FilePathTreeItem) getTreeItem();
        Path source = Paths.get(treeItem.getFullPath());
        Files.move(source, source.resolveSibling(fileName.getText()));
    }

    private String getFileName() {
        return getItem() == null ? "" : getItem().toString();
    }

    private void displayInformationDialog(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setHeaderText(content);
            alert.showAndWait();
        });
    }

    private Optional<String> createTextInputDialog(String title, String header, String content) {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle(title);
        textInputDialog.setHeaderText(header);
        textInputDialog.setContentText(content);
        return textInputDialog.showAndWait();
    }
}
