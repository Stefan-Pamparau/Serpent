import java.io.*;
import java.net.Socket;

/**
 * Created by bogdy on 12/21/15.
 */

import core.Gap;

public class ClientOutput implements Runnable
{

    private int cPos = 0;

    //for testing purposes
    public void run()
    {
        String hostName = "127.0.0.1";
        int port = 4444;
        try
        {
            Socket commSocket = new Socket(hostName, port);
            OutputStream os = commSocket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            BufferedReader stdIn =
                    new BufferedReader(
                            new InputStreamReader(System.in));
            String userInput;
            while((userInput = stdIn.readLine())!=null)
            {
                Command sendCommand;
                Character c = userInput.charAt(0);
                if(cPos>2)
                {
                    sendCommand = new Insert(cPos-2, userInput.charAt(0));
                    cPos++;
                }
                else
                    sendCommand = new Insert(cPos++, userInput.charAt(0));
                oos.writeObject(sendCommand);
            }
        }
        catch(IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }

    }
}
