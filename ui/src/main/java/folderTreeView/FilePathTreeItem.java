package folderTreeView;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FilePathTreeItem extends TreeItem<String> {
    private static Image fileImage = new Image(ClassLoader.getSystemResourceAsStream("images/file/text-generic.png"));
    private static Image folderExpandedImage = new Image(ClassLoader.getSystemResourceAsStream("images/folder/folder-expanded.png"));
    private static Image folderCollapsedImage = new Image(ClassLoader.getSystemResourceAsStream("images/folder/folder-collapsed.png"));

    private String fullPath;
    private Boolean isDirectory;

    public FilePathTreeItem(Path path) {
        super(path.toString());
        this.fullPath = path.toString();

        checkIfPathIsDirectory(path);
        setItemValue(path);
        addExpandedItemEventHandler();
        addCollapsedItemEventHandler();
    }

    private void checkIfPathIsDirectory(Path path) {
        if (Files.isDirectory(path)) {
            this.isDirectory = true;
            this.setGraphic(new ImageView(folderCollapsedImage));
            this.getChildren().add(new TreeItem<>("make it expandable"));
        } else {
            this.isDirectory = false;
            this.setGraphic(new ImageView(fileImage));
        }
    }

    private void setItemValue(Path path) {
        if (!fullPath.endsWith(File.separator)) {
            String value = path.toString();
            int indexOf = value.lastIndexOf(File.separator);
            if (indexOf > 0) {
                this.setValue(value.substring(indexOf + 1));
            } else {
                this.setValue(value);
            }
        }
    }

    private void addExpandedItemEventHandler() {
        this.addEventHandler(TreeItem.<String>branchExpandedEvent(), event -> {
            FilePathTreeItem source = (FilePathTreeItem) event.getSource();
            if (source.isDirectory()) {
                ImageView imageView = (ImageView) source.getGraphic();
                imageView.setImage(folderExpandedImage);
            }

            try {
                if (!source.getChildren().isEmpty()) {
                    source.getChildren().remove(0, source.getChildren().size());
                }
                Path path = Paths.get(source.getFullPath());
                BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
                if (attributes.isDirectory()) {
                    DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
                    for (Path file : directoryStream) {
                        FilePathTreeItem treeNode = new FilePathTreeItem(file);
                        source.getChildren().add(treeNode);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addCollapsedItemEventHandler() {
        this.addEventHandler(TreeItem.<String>branchCollapsedEvent(), event -> {
            FilePathTreeItem source = (FilePathTreeItem) event.getSource();
            if (source.isDirectory) {
                ImageView imageView = (ImageView) source.getGraphic();
                imageView.setImage(folderCollapsedImage);
            }
        });
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}
