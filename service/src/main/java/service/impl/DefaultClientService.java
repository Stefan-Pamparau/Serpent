package service.impl;

import database.ClientDAO;
import database.impl.DefaultClientDAO;
import model.Client;
import service.ClientService;
import service.DatabaseService;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public class DefaultClientService implements ClientService {
    private ClientDAO clientDAO;

    public DefaultClientService(DatabaseService databaseService) {
        this.clientDAO = new DefaultClientDAO(databaseService.getConnectionController());
    }

    public Client getClient(String email, String password) {
        return clientDAO.getClient(email, password);
    }

    public void insertClient(Client client) {
        clientDAO.insertClient(client);
    }

    public void deleteClient(Client client) {
        clientDAO.deleteClient(client);
    }
}
