package folderTreeView;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FilePathTreeCell extends TreeCell<String> {
    private TextField fileName;
    private ContextMenu contextMenu;

    public FilePathTreeCell() {
        contextMenu = new ContextMenu();
        MenuItem addNewFileMenuItem = new MenuItem("New file");
        MenuItem addNewFolderMenuItem = new MenuItem("New folder");
        contextMenu.getItems().addAll(addNewFileMenuItem, addNewFolderMenuItem);

        addNewFileEventHandler(addNewFileMenuItem);
        addNewFolderEventHandler(addNewFolderMenuItem);
    }

    private void addNewFileEventHandler(MenuItem addNewFileMenuItem) {
        addNewFileMenuItem.setOnAction((ActionEvent event) -> {
            FilePathTreeItem selectedDirectory = (FilePathTreeItem) getTreeItem();
            if (selectedDirectory.isDirectory()) {
                Path path = Paths.get(selectedDirectory.getFullPath());
                FilePathTreeItem newFile = new FilePathTreeItem(path);
                newFile.setDirectory(false);
                selectedDirectory.getChildren().add(newFile);
            }
        });
    }

    private void addNewFolderEventHandler(MenuItem addNewFolderMenuItem) {
        addNewFolderMenuItem.setOnAction((ActionEvent event) -> {
            FilePathTreeItem selectedDirectory = (FilePathTreeItem) getTreeItem();
            if (selectedDirectory.isDirectory()) {
                Path path = Paths.get(selectedDirectory.getFullPath());
                FilePathTreeItem newFolder = new FilePathTreeItem(path);
                selectedDirectory.getChildren().add(newFolder);
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
                if (!getTreeItem().isLeaf() && getTreeItem().getParent() != null) {
                    setContextMenu(contextMenu);
                }
            }
        }
    }

    private void initializeFileName() {
        fileName = new TextField(getFileName());
        fileName.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(fileName.getText());
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

    }

    private String getFileName() {
        return getItem() == null ? "" : getItem().toString();
    }
}
