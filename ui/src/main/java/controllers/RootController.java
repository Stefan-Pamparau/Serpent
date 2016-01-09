package controllers;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import folderTreeView.FilePathTreeCell;
import folderTreeView.FilePathTreeItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import textInputControl.GapTextArea;

public class RootController implements Initializable {
    private static Image computerImage = new Image(ClassLoader.getSystemResourceAsStream("images/computer/computer.png"));
    private Stage primaryStage;
    @FXML
    private GridPane root;
    @FXML
    private MenuBar optionsMenuBar;
    @FXML
    private TreeView<String> folderStructureTreeView;
    @FXML
    private TabPane filesTabPane;
    private List<GapTextArea> gapTextList;

    public void initialize(URL location, ResourceBundle resources) {
        initializeOptionsMenuBar();
        initializeFolderStructureTreeView();
        initializeFilesTabPane();
        gapTextList = new ArrayList<>();
    }

    private void initializeOptionsMenuBar() {
        optionsMenuBar.prefWidthProperty().bind(root.prefWidthProperty());
    }

    private void initializeFolderStructureTreeView() {
        folderStructureTreeView.prefHeightProperty().bind(root.prefHeightProperty());
        folderStructureTreeView.setEditable(true);
        folderStructureTreeView.setCellFactory((TreeView<String> p) -> new FilePathTreeCell());
        String hostName = "computer";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        TreeItem<String> rootNode = new TreeItem<String>(hostName, new ImageView(computerImage));
        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path rootDirectory : rootDirectories) {
            FilePathTreeItem filePathTreeItem = new FilePathTreeItem(rootDirectory);
            rootNode.getChildren().add(filePathTreeItem);
        }

        folderStructureTreeView.setRoot(rootNode);
        rootNode.setExpanded(true);
    }

    private void initializeFilesTabPane() {
        filesTabPane.prefWidthProperty().bind(root.prefHeightProperty());
    }

    public void handleNewFileAction(ActionEvent actionEvent) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.showSaveDialog(primaryStage);
        TextArea text = new TextArea();
        Tab tab = new Tab("Text", text);
        GapTextArea gapBuffer = new GapTextArea("", text, tab);
        gapTextList.add(gapBuffer);
        filesTabPane.getTabs().add(tab);
    }

    public void handleOpenFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
    }

    public void handleExitFileAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void gracefulShutdown() {
        gapTextList.forEach(GapTextArea::closeCursorThread);
    }
}
