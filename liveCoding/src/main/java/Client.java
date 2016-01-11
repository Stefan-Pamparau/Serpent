import core.Gap;

import java.net.*;
import java.io.*;

/**
 * Created by bogdy on 12/20/15.
 */

public class Client
{

    private static Gap g;
    //this is for testing purposes
    public static void main(String args[])
    {
        g = new Gap(128);
        String hostName = "127.0.0.1";
        int port = 4444;
        Socket commSocket = null;
        InputStream is = null;
        ObjectInputStream ois = null;
        ClientOutput co = new ClientOutput();
        new Thread(co).start();
        try
        {
            Command receivedCommand;
            while(true)
            {
                commSocket = new Socket(hostName, port);
                is = commSocket.getInputStream();
                ois = new ObjectInputStream(is);
                receivedCommand = (Command) ois.readObject();
                switch (receivedCommand.getType())
                {
                    case INSERT:
                        Insert ins = (Insert)receivedCommand;

                        g.moveKeyLeft();
                        g.moveKeyLeft();

                        g.insert(receivedCommand.getC());

                        g.moveKeyRight();
                        g.moveKeyRight();

                        System.out.println(ins.getPosition());
                        System.out.println(g);
                        break;
                    case DELETE:
                        break;
                }
            }
        }
        catch(IOException e) {
            try
            {
                is.close();
                ois.close();
                commSocket.close();
            }
            catch(IOException ex)
            {
                System.out.println("Error at disconnecting the client!");
            }
            catch(NullPointerException ex2)
            {
                System.out.println("Null pointer exception in client disconnection!");
            }
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            //System.exit(1);
        }
        catch(ClassNotFoundException e)
        {
            System.out.print("Class command not found! Did you forgot to add it?");
        }
    }
}
