package database;

import model.ClientType;

import java.sql.Connection;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public interface DatabaseConnectionController {
    void connectToDatabase(ClientType clientType);

    void disconnectFromDatabase();

    Connection getConnection();
}
