/**
 * Created by bogdy on 12/20/15.
 */

import java.lang.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/*
This class serves the request and mantains lists for Commands and connected clients
 */

public class ServiceProcessor implements Runnable
{
    private LinkedList<ServiceProcessing> connections = null;//the list of clients
    private LinkedBlockingQueue<Command> commands = null;//the blocking queue for commands

    public ServiceProcessor()
    //constructor
    {
        connections = new LinkedList<ServiceProcessing>();
        commands = new LinkedBlockingQueue<Command>();
    }


    public void AddCommand(Command incomingCommand)
            //this method will insert a command in to the queue
    {
        try
        {
            commands.put(incomingCommand);
        }
        catch(InterruptedException e)
        {
            System.out.println("ServiceProcessor was interrupted while adding in the Queue!");
        }
    }

    public void RemoveConnection(ServiceProcessing incomingConnection)
            //this method will remove a client
    {
        connections.remove(incomingConnection);
    }

    public void AddConnection(ServiceProcessing incomingConnection)
            //this method will add a new client
    {
        connections.add(incomingConnection);
    }

    public void run()
            //main method. it takes a command from the list and sends it to the clients
    {
        while(true)
        {
            try
            {
                //this part sends the current command to all the clients
                Command current = commands.take();
                for(ServiceProcessing sp : connections)
                    sp.addCommand(current);
            }
            catch(InterruptedException e)
            {
                 System.out.println("ServiceProcessor was interrupted while taking from the Queue!");
            }
        }
    }
}
