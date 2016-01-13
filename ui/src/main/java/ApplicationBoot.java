import java.util.Locale;
import java.util.ResourceBundle;

import controllers.RootController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ApplicationBoot extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/main.fxml"), ResourceBundle.getBundle("localization/locale", new Locale("en", "EN")));
        GridPane root = fxmlLoader.load();
        Scene scene = new Scene(root, 1000, 1000);
        initializeController(primaryStage, fxmlLoader);
        bindRootToScene(scene, root);
        addApplicationClosedListener(primaryStage, fxmlLoader);
        scene.getStylesheets().add("stylesheets/skin.css");
        primaryStage.setTitle("Serpent");
        primaryStage.setScene(scene);
        primaryStage.show();

        root.setGridLinesVisible(true);
    }

    private void initializeController(Stage primaryStage, FXMLLoader fxmlLoader) {
        RootController rootController = fxmlLoader.getController();
        rootController.setPrimaryStage(primaryStage);
    }

    private void bindRootToScene(Scene scene, GridPane root) {
        root.prefHeightProperty().bind(scene.heightProperty());
        root.prefWidthProperty().bind(scene.widthProperty());
    }

    private void addApplicationClosedListener(Stage primaryStage, FXMLLoader fxmlLoader) {
        primaryStage.setOnCloseRequest(event -> {
            RootController rootController = fxmlLoader.getController();
            rootController.gracefulShutdown();
        });
    }
}
