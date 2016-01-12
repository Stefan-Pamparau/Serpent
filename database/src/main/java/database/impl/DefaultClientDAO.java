package database.impl;

import database.DatabaseConnectionController;
import database.ClientDAO;
import model.Client;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public class DefaultClientDAO implements ClientDAO {
    private DatabaseConnectionController databaseConnectionController;

    public DefaultClientDAO(DatabaseConnectionController databaseConnectionController) {
        this.databaseConnectionController = databaseConnectionController;
    }

    public void insertClient(Client client) {

    }

    public void deleteClient(Client client) {

    }
}
