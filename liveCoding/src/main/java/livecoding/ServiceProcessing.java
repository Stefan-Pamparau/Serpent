package livecoding; /**
 * Created by bogdy on 12/20/15.
 */

import java.io.OutputStream;
import java.lang.*;
import java.io.*;
import java.net.*;

public class ServiceProcessing implements Runnable
{

    private Socket incomingSocket;
    private volatile Command outCommand;
    private ServiceProcessor sp;
    private ServiceInputProcessing sip;

    public ServiceProcessing(Socket socket, ServiceProcessor incomingSP)
    {
        incomingSocket = socket;
        sp = incomingSP;
        sip = new ServiceInputProcessing(socket, incomingSP);
    }

    public void addCommand(Command receivedCommand)
    {
        outCommand = receivedCommand;
    }


    public void run()
    {
        try
        {
            //we initialize the required objects for communication(input and output)
            OutputStream os = incomingSocket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            new Thread(sip).start();
            while (true)
            {
                //we poll to see if we have an outCommand to output to the specified client
                if (outCommand != null)
                {
                    //write the command
                    oos.writeObject(outCommand);
                    //make it null again
                    outCommand = null;
                }
            }
        }
        catch (IOException e)
        {
            try
            {
                sp.RemoveConnection(this);
                incomingSocket.close();
            }
            catch(IOException e2)
            {
                System.out.println("Error in disconnecting the socket!!!");
                System.exit(-1);
            }
            //System.exit(1);
        }

    }

}
