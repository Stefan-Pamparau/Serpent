package service;

import database.DatabaseConnectionController;
import model.ClientType;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public interface DatabaseService {
    void connectToDatabase(ClientType clientType);

    void disconnectFromDatabase();

    DatabaseConnectionController getConnectionController();

    boolean isConnected();
}
