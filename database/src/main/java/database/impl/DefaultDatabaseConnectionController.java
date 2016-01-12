package database.impl;

import database.DatabaseConnectionController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.ClientType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public class DefaultDatabaseConnectionController implements DatabaseConnectionController {
    private Connection connection;

    public void connectToDatabase(ClientType clientType) {
        try {
            if(clientType == ClientType.ADMIN) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/serpentDB?user=admin&password=&noAccessToProcedureBodies=true");
                if(connection != null) {
                    displayInformationDialog("Connection established", "Connection established", "Connection established");
                }
            }
            else {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/serpentDB?user=client&password=&noAccessToProcedureBodies=true");
                if(connection != null) {
                    displayInformationDialog("Connection established", "Connection established", "Connection established");
                }
            }
        }
        catch (Exception exception) {
            displayInformationDialog("Error", "Establishing database connection failed", "Establishing database connection failed");
        }
    }

    public void disconnectFromDatabase() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                displayInformationDialog("Disconnected from database", "Disconnected from database", "Disconnected from database");
            }
            catch (SQLException e) {
                displayInformationDialog("Error", "Closing database connection failed", "Closing database connection failed");
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private void displayInformationDialog(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
