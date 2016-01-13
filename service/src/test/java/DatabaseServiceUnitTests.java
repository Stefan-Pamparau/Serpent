import org.easymock.EasyMock;
import org.junit.Test;

import database.DatabaseConnectionController;
import model.ClientType;
import service.DatabaseService;

/**
 * Created by FanFan on 1/13/2016.
 */
public class DatabaseServiceUnitTests {
    @Test
    public void testConnectToDatabase() {
        DatabaseService databaseService = EasyMock.createMock(DatabaseService.class);

        databaseService.connectToDatabase(ClientType.ADMIN);
        EasyMock.expectLastCall();
        EasyMock.replay(databaseService);

        databaseService.connectToDatabase(ClientType.ADMIN);
        EasyMock.verify(databaseService);
    }

    @Test
    public void testDisconnectFromDatabase() {
        DatabaseService databaseService = EasyMock.createMock(DatabaseService.class);

        databaseService.disconnectFromDatabase();
        EasyMock.expectLastCall();
        EasyMock.replay(databaseService);

        databaseService.disconnectFromDatabase();
        EasyMock.verify(databaseService);
    }
}
