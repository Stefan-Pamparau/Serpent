package database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.ClientDAO;
import database.DatabaseConnectionController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Client;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public class DefaultClientDAO implements ClientDAO {
    private DatabaseConnectionController databaseConnectionController;

    public DefaultClientDAO(DatabaseConnectionController databaseConnectionController) {
        this.databaseConnectionController = databaseConnectionController;
    }

    public Client getClient(String email, String password) {
        String query = "select * from user where email = ? and password = ?;";
        Client client = null;
        if (!email.equals("") && !password.equals("")) {
            try {
                Connection connection = databaseConnectionController.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    client = new Client(result.getString(2), result.getString(3), result.getString(4), result.getString(5),
                            result.getString(6), result.getString(7), result.getString(8), result.getInt(9));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return client;
    }

    public void insertClient(Client client) {
        String query = "insert into user(firstname, surname, address, email, password, phone, sex, age)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = databaseConnectionController.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getSurname());
            preparedStatement.setString(3, client.getAddress());
            preparedStatement.setString(4, client.getEmail());
            preparedStatement.setString(5, client.getPassword());
            preparedStatement.setString(6, client.getPhone());
            preparedStatement.setString(7, client.getSex());
            preparedStatement.setInt(8, client.getAge());

            preparedStatement.execute();
        } catch (SQLException e) {
            displayInformationDialog(e.getMessage(), e.getLocalizedMessage(), e.getLocalizedMessage());
        }
    }

    public void deleteClient(Client client) {
        String query = "delete from user where email = ?";
        try {
            Connection connection = databaseConnectionController.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, client.getEmail());

            preparedStatement.execute();
        } catch (SQLException e) {
            displayInformationDialog(e.getMessage(), e.getLocalizedMessage(), e.getLocalizedMessage());
        }
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
