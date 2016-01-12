package service;

import model.Client;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public interface ClientService {
    void insertClient(Client client);

    void deleteClient(Client client);
}
