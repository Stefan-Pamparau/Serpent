package livecoding; /**
 * Created by bogdy on 12/20/15.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;


public class ServiceInputProcessing implements Runnable
{
    private Socket incomingSocket;
    private Command inCommand;
    private ServiceProcessor sp;

    public ServiceInputProcessing(Socket socket, ServiceProcessor incomingSP)
    {
        incomingSocket = socket;
        sp = incomingSP;
    }

    public void run()
    {
        try
        {
            InputStream is = incomingSocket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            while (true)
            {
                inCommand = (Command) ois.readObject();
                if (inCommand != null)
                {
                    sp.AddCommand(inCommand);//if we have a new command, add it to the queue
                    inCommand = null;
                }
            }
        }
        catch (IOException e)
        {
            //closing the connection
            try
            {
                incomingSocket.close();
            }
            catch(IOException e2)
            {
                System.out.println("Error in disconnecting the socket!!!");
                System.exit(-1);
            }
            //System.exit(1);
        }
        catch(ClassNotFoundException e)
        {
            System.out.print("Class livecoding.Command not found! Did you forgot to add it?");
        }
    }
}
