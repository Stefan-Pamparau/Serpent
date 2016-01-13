package livecoding;

import java.io.*;
import java.net.Socket;

/**
 * Created by bogdy on 12/21/15.
 */

public class ClientOutput
{

    private int cPos = 0;

    //for testing purposes
    public void sendCommand(Command command)
    {
        String hostName = "127.0.0.1";
        int port = 4444;

        try
        {
            Socket commSocket = new Socket(hostName, port);
            OutputStream os = commSocket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            oos.writeObject(command);
        }
        catch(IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }

    }
}
