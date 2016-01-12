package service.impl;

import database.DatabaseConnectionController;
import database.impl.DefaultDatabaseConnectionController;
import model.ClientType;
import service.DatabaseService;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public class DefaultDatabaseService implements DatabaseService {

    private DatabaseConnectionController databaseConnectionController;

    public DefaultDatabaseService() {
        databaseConnectionController = new DefaultDatabaseConnectionController();
    }

    public void connectToDatabase(ClientType clientType) {
        databaseConnectionController.connectToDatabase(clientType);
    }

    public void disconnectFromDatabase() {
        databaseConnectionController.disconnectFromDatabase();
    }

    public DatabaseConnectionController getConnectionController() {
        return databaseConnectionController;
    }

    public boolean isConnected() {
        return databaseConnectionController.getConnection() != null;
    }
}
