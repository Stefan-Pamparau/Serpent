import org.easymock.EasyMock;
import org.easymock.internal.EasyMockStatement;
import org.junit.Test;

import java.sql.Connection;

import database.DatabaseConnectionController;
import model.ClientType;

/**
 * Created by FanFan on 1/13/2016.
 */
public class DatabaseConnectionControllerUnitTests {
    @Test
    public void testConnectToDatabase() {
        DatabaseConnectionController databaseConnectionController = EasyMock.createMock(DatabaseConnectionController.class);

        databaseConnectionController.connectToDatabase(ClientType.ADMIN);
        EasyMock.expectLastCall();
        EasyMock.replay(databaseConnectionController);

        databaseConnectionController.connectToDatabase(ClientType.ADMIN);
        EasyMock.verify(databaseConnectionController);
    }

    @Test
    public void testDisconnectFromDatabase() {
        DatabaseConnectionController databaseConnectionController = EasyMock.createMock(DatabaseConnectionController.class);

        databaseConnectionController.disconnectFromDatabase();
        EasyMock.expectLastCall();
        EasyMock.replay(databaseConnectionController);

        databaseConnectionController.disconnectFromDatabase();
        EasyMock.verify(databaseConnectionController);
    }
}
