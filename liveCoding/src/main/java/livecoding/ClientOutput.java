package livecoding;

import java.io.*;
import java.net.Socket;

/**
 * Created by bogdy on 12/21/15.
 */

public class ClientOutput
{

    private int cPos = 0;
    private Socket commSocket;


    //for testing purposes
    public void sendCommand(Command command)
    {
        String hostName = "127.0.0.1";
        int port = 4444;

        try
        {
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

    public void setCommSocket(Socket commSocket) {
        this.commSocket = commSocket;
    }
}
