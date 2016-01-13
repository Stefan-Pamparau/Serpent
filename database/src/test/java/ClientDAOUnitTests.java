import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import database.ClientDAO;
import model.Client;

/**
 * Created by FanFan on 1/13/2016.
 */
public class ClientDAOUnitTests {
    @Test
    public void getClientTest() {
        Client client = new Client("test@gmail.com");
        ClientDAO clientDAO = EasyMock.createMock(ClientDAO.class);

        EasyMock.expect(clientDAO.getClient("test@gmail.com", "12345")).andReturn(client);
        EasyMock.replay(clientDAO);

        Client result = clientDAO.getClient("test@gmail.com", "12345");

        EasyMock.verify(clientDAO);
        Assert.assertEquals(result, client);
    }
    @Test
    public void insertClientTest() {
        Client client = new Client("test@gmail.com");
        ClientDAO clientDAO = EasyMock.createMock(ClientDAO.class);

        clientDAO.insertClient(client);
        EasyMock.expectLastCall();
        EasyMock.expect(clientDAO.getClient("test@gmail.com", "12345")).andReturn(client);
        EasyMock.replay(clientDAO);

        clientDAO.insertClient(client);
        Client result = clientDAO.getClient("test@gmail.com", "12345");

        EasyMock.verify(clientDAO);
        Assert.assertEquals(result, client);
    }

    @Test
    public void deleteClientTest() {
        Client client = new Client("test@gmail.com");
        ClientDAO clientDAO = EasyMock.createMock(ClientDAO.class);

        clientDAO.insertClient(client);
        EasyMock.expectLastCall();
        clientDAO.deleteClient(client);
        EasyMock.expectLastCall();
        EasyMock.expect(clientDAO.getClient("test@gmail.com", "12345")).andReturn(null);
        EasyMock.replay(clientDAO);

        clientDAO.insertClient(client);
        clientDAO.deleteClient(client);
        Client result = clientDAO.getClient("test@gmail.com", "12345");

        EasyMock.verify(clientDAO);
        Assert.assertEquals(result, null);
    }
}
