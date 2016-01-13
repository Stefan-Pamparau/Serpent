import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import database.ClientDAO;
import model.Client;
import service.ClientService;

/**
 * Created by FanFan on 1/13/2016.
 */
public class ClientServiceUnitTests {
    @Test
    public void getClientTest() {
        Client client = new Client("test@gmail.com");
        ClientService clientService = EasyMock.createMock(ClientService.class);

        EasyMock.expect(clientService.getClient("test@gmail.com", "12345")).andReturn(client);
        EasyMock.replay(clientService);

        Client result = clientService.getClient("test@gmail.com", "12345");

        EasyMock.verify(clientService);
        Assert.assertEquals(result, client);
    }
    @Test
    public void insertClientTest() {
        Client client = new Client("test@gmail.com");
        ClientService clientService = EasyMock.createMock(ClientService.class);

        clientService.insertClient(client);
        EasyMock.expectLastCall();
        EasyMock.expect(clientService.getClient("test@gmail.com", "12345")).andReturn(client);
        EasyMock.replay(clientService);

        clientService.insertClient(client);
        Client result = clientService.getClient("test@gmail.com", "12345");

        EasyMock.verify(clientService);
        Assert.assertEquals(result, client);
    }

    @Test
    public void deleteClientTest() {
        Client client = new Client("test@gmail.com");
        ClientService clientService = EasyMock.createMock(ClientService.class);

        clientService.insertClient(client);
        EasyMock.expectLastCall();
        clientService.deleteClient(client);
        EasyMock.expectLastCall();
        EasyMock.expect(clientService.getClient("test@gmail.com", "12345")).andReturn(null);
        EasyMock.replay(clientService);

        clientService.insertClient(client);
        clientService.deleteClient(client);
        Client result = clientService.getClient("test@gmail.com", "12345");

        EasyMock.verify(clientService);
        Assert.assertEquals(result, null);
    }
}
