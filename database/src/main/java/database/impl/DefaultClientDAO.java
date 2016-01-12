package database.impl;

import database.DatabaseConnectionController;
import database.ClientDAO;
import javafx.scene.control.Alert;
import model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public class DefaultClientDAO implements ClientDAO {
    private DatabaseConnectionController databaseConnectionController;

    public DefaultClientDAO(DatabaseConnectionController databaseConnectionController) {
        this.databaseConnectionController = databaseConnectionController;
    }

    public Client getClient(String email) {
        String query = "select from user where email = ?;";
        Client client = null;
        try {
            Connection connection = databaseConnectionController.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet result = preparedStatement.executeQuery(query);
            client = new Client(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
                    result.getString(4), result.getString(5), result.getInt(6));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return client;
    }

    public void insertClient(Client client) {
        String query = " insert into user (firstname, surname, address, email, phone, sex, age)"
                + " values (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = databaseConnectionController.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, client.getFirstname());
            preparedStatement.setString(2, client.getSurname());
            preparedStatement.setString(3, client.getAddress());
            preparedStatement.setString(4, client.getEmail());
            preparedStatement.setString(5, client.getPhone());
            preparedStatement.setString(6, client.getSex());
            preparedStatement.setInt(7, client.getAge());

            preparedStatement.execute(query);
        } catch (SQLException e) {
            System.out.printf(e.getLocalizedMessage());
        }
    }

    public void deleteClient(Client client) {
        String query = "delete from user where email = ?";
        try {
            Connection connection = databaseConnectionController.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, client.getEmail());

            preparedStatement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
