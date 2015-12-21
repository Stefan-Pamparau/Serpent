import java.net.*;
import java.io.*;

/*
this is the main server. here we will make the processing of the messages based on a queue
 */

public class Server
{
    public static void main(String args[])
    {
        ServerSocket serverSocket = null;
        ServiceProcessor sp = new ServiceProcessor();
        new Thread(sp).start();
        //we initialize the server part.
        try {
            serverSocket = new ServerSocket(4444);//initialize the server socket
            while(true)
            {
                ServiceProcessing service = new ServiceProcessing(serverSocket.accept(),sp);
                sp.AddConnection(service);
                new Thread(service).start();
            }

        }
        catch(IOException e)
        {
            System.err.println("Can't handle I/O communication!");
            try
            {
                serverSocket.close();
            }
            catch(IOException e2)
            {
                System.out.println("Error in disconnecting the server socket!");
            }
            System.exit(1);
        }
    }
}