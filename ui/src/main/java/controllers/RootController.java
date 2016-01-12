package controllers;

import core.Gap;
import folderTreeView.FilePathTreeCell;
import folderTreeView.FilePathTreeItem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Client;
import model.ClientType;
import service.ClientService;
import service.DatabaseService;
import service.impl.DefaultClientService;
import service.impl.DefaultDatabaseService;
import textInputControl.GapTextArea;
import util.FileType;
import util.FilesUtilities;

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
    private DatabaseService databaseService;
    private ClientService clientService;

    public void initialize(URL location, ResourceBundle resources) {
        initializeRoot();
        initializeOptionsMenuBar();
        initializeFolderStructureTreeView();
        initializeFilesTabPane();
        gapTextList = new ArrayList<>();
        normalTextButtonType = new ButtonType("Normal text");
        richTextButtonType = new ButtonType("Rich text");
        cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        databaseService = new DefaultDatabaseService();
        clientService = new DefaultClientService(databaseService);
    }

    public void handleNewFileAction(ActionEvent actionEvent) {
        Optional<ButtonType> result = createChooseDialog("Choose text type", "Please select your desired text type",
                "Given options are:", normalTextButtonType, richTextButtonType, cancelButtonType);
        if (result.get() == normalTextButtonType) {
            TextArea text = new TextArea();
            Tab tab = new Tab("Normal text", text);
            GapTextArea gapTextArea = new GapTextArea(text, tab);
            gapTextList.add(gapTextArea);
            filesTabPane.getTabs().add(tab);
        } else if (result.get() == richTextButtonType) {
            filesTabPane.getTabs().add(new Tab("Rich text", new HTMLEditor()));
        }
    }

    public void handleOpenFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
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
        Optional<Pair<String, String>> result = createLoginDialog();

        result.ifPresent(usernamePassword -> {
            if(usernamePassword.getKey().equals("admin") && usernamePassword.getValue().equals("admin")) {
                databaseService.connectToDatabase(ClientType.ADMIN);
            }
            else {
                databaseService.connectToDatabase(ClientType.NORMAL);
            }
        });
    }

    public void handleDisconnectFromDatabase(ActionEvent actionEvent) {
        databaseService.disconnectFromDatabase();
    }

    public void handleInsertClient(ActionEvent actionEvent) {
        Optional<Client> result = createInsertClientDialog();

        if(result.isPresent()) {
            clientService.insertClient(result.get());
        }
    }

    public void handleDeleteClient(ActionEvent actionEvent) {
        Optional<Client> result = createDeleteClientDialog();

        if(result.isPresent()) {
            clientService.deleteClient(result.get());
        }
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

    private Optional<ButtonType> createChooseDialog(String title, String header, String content, ButtonType... buttonTypes) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getButtonTypes().setAll(buttonTypes);
        return alert.showAndWait();
    }

    private Optional<Pair<String, String>> createLoginDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Login to database");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(username::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private Optional<Client> createInsertClientDialog() {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Insert client");
        dialog.setHeaderText("Please enter the client's information");

        dialog.setResizable(true);

        Label firstNameLabel = new Label("FirstName: ");
        Label surnameLabel = new Label("Surname: ");
        Label addressLabel = new Label("Address: ");
        Label emailLabel = new Label("Email: ");
        Label phoneLabel = new Label("Phone: ");
        Label sexLabel = new Label("Sex: ");
        Label ageLabel = new Label("Age: ");

        TextField firstNameTextfield = new TextField();
        TextField surnameTextfield = new TextField();
        TextField addressTextfield = new TextField();
        TextField emailTextfield = new TextField();
        TextField phoneTextfield = new TextField();
        TextField sexTextfield = new TextField();
        TextField ageTextfield = new TextField();

        GridPane grid = new GridPane();
        grid.add(firstNameLabel, 1, 1);
        grid.add(surnameLabel, 1, 2);
        grid.add(addressLabel, 1, 3);
        grid.add(emailLabel, 1, 4);
        grid.add(phoneLabel, 1, 5);
        grid.add(sexLabel, 1, 6);
        grid.add(ageLabel, 1, 7);

        grid.add(firstNameTextfield, 2, 1);
        grid.add(surnameTextfield, 2, 2);
        grid.add(addressTextfield, 2, 3);
        grid.add(emailTextfield, 2, 4);
        grid.add(phoneTextfield, 2, 5);
        grid.add(sexTextfield, 2, 6);
        grid.add(ageTextfield, 2, 7);

        dialog.getDialogPane().setContent(grid);

        ButtonType insertClient = new ButtonType("Insert client", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(insertClient, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == insertClient) {
                return new Client(firstNameTextfield.getText(), surnameTextfield.getText(), addressTextfield.getText(),
                        emailTextfield.getText(), phoneTextfield.getText(), sexTextfield.getText(),
                        Integer.parseInt(ageTextfield.getText()));
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private Optional<Client> createDeleteClientDialog() {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Delete client");
        dialog.setHeaderText("Please enter the client's information");

        dialog.setResizable(true);

        Label firstNameLabel = new Label("FirstName: ");
        Label surnameLabel = new Label("Surname: ");

        TextField firstNameTextfield = new TextField();
        TextField surnameTextfield = new TextField();

        GridPane grid = new GridPane();
        grid.add(firstNameLabel, 1, 1);
        grid.add(surnameLabel, 1, 2);

        grid.add(firstNameTextfield, 2, 1);
        grid.add(surnameTextfield, 2, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType insertClient = new ButtonType("Delete client", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(insertClient, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == insertClient) {
                return new Client(firstNameTextfield.getText(), surnameTextfield.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private FileType determineFileType(String text) {
        if (text.contains("html")) {
            return FileType.RICH;
        } else {
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
