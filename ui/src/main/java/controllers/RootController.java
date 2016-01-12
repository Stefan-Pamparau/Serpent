package controllers;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import core.Gap;
import folderTreeView.FilePathTreeCell;
import folderTreeView.FilePathTreeItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import textInputControl.GapTextArea;
import util.FileType;
import util.FilesUtilities;

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
    private ButtonType normalTextButtonType;
    private ButtonType richTextButtonType;
    private ButtonType cancelButtonType;

    public void initialize(URL location, ResourceBundle resources) {
        initializeRoot();
        initializeOptionsMenuBar();
        initializeFolderStructureTreeView();
        initializeFilesTabPane();
        gapTextList = new ArrayList<>();
        normalTextButtonType = new ButtonType("Normal text");
        richTextButtonType = new ButtonType("Rich text");
        cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    }

    public void handleNewFileAction(ActionEvent actionEvent) {
        Optional<ButtonType> result = createChooseDialog();
        if (result.get() == normalTextButtonType) {
            TextArea text = new TextArea();
            Tab tab = new Tab("Normal text", text);
            GapTextArea gapTextArea = new GapTextArea(text, tab);
            gapTextList.add(gapTextArea);
            filesTabPane.getTabs().add(tab);
        } else if(result.get() == richTextButtonType){
            filesTabPane.getTabs().add(new Tab("Rich text", new HTMLEditor()));
        }
    }

    public void handleOpenFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        if(file != null) {
            TextArea text = new TextArea();
            Tab tab = new Tab("Normal text", text);
            String textFromFile = FilesUtilities.readFromFile(file);
            switch (determineFileType(textFromFile)) {
                case NORMAL:
                    Gap gapBuffer = new Gap(128);
                    copyTextToGap(textFromFile, gapBuffer);
                    GapTextArea gapTextArea = new GapTextArea("", text, tab, gapBuffer);
                    gapTextList.add(gapTextArea);
                    filesTabPane.getTabs().add(tab);
                    break;
                case RICH:
                    HTMLEditor textArea = new HTMLEditor();
                    textArea.setHtmlText(textFromFile);
                    filesTabPane.getTabs().add(new Tab("Rich text", textArea));
                    break;
            }
        }
    }

    public void handleSaveFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(primaryStage);
        filesTabPane.getTabs().stream().filter(Tab::isSelected).forEach(tab -> {
            if (tab.getContent() instanceof TextArea) {
                TextArea text = (TextArea) tab.getContent();
                FilesUtilities.writeToFile(text.getText(), file.toPath());
            } else {
                HTMLEditor text = (HTMLEditor) tab.getContent();
                FilesUtilities.writeToFile(text.getHtmlText(), file.toPath());
            }
        });
    }

    public void handleStartServer(ActionEvent actionEvent) {

    }

    public void handleConnectToServer(ActionEvent actionEvent) {

    }

    public void handleConnectToDatabase(ActionEvent actionEvent) {

    }

    public void handleReconnectToDatabase(ActionEvent actionEvent) {

    }

    public void handleInsertClient(ActionEvent actionEvent) {

    }

    public void handleDeleteClient(ActionEvent actionEvent) {

    }

    public void handleExitFileAction(ActionEvent actionEvent) {
        gracefulShutdown();
        System.exit(0);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void gracefulShutdown() {
        gapTextList.forEach(GapTextArea::closeCursorThread);
    }

    private void initializeRoot() {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(75);
        root.getColumnConstraints().addAll(column1, column2);
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

    private Optional<ButtonType> createChooseDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choose text type");
        alert.setHeaderText("Please select your desired text type");
        alert.setContentText("Given options are");

        alert.getButtonTypes().setAll(normalTextButtonType, richTextButtonType, cancelButtonType);
        return alert.showAndWait();
    }

    private FileType determineFileType(String text) {
        if(text.contains("html")) {
            return FileType.RICH;
        }
        else {
            return FileType.NORMAL;
        }
    }

    private void copyTextToGap(String text, Gap gapBuffer) {
        char[] charArray = text.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            gapBuffer.insert(charArray[i]);
        }
    }
}
