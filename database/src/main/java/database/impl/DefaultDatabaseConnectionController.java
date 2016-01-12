package database.impl;

import database.DatabaseConnectionController;
import model.ClientType;

import java.sql.Connection;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public class DefaultDatabaseConnectionController implements DatabaseConnectionController {
    private Connection connection;

    public void connectToDatabase(ClientType clientType) {
        //connection = DriverManager.getConnection();
    }

    public void disconnectFromDatabase() {

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
