package database;

import model.Client;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public interface ClientDAO {
    Client getClient(String email);

    void insertClient(Client client);

    void deleteClient(Client client);
}
