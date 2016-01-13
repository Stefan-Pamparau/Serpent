package livecoding;

import core.Gap;

import java.net.*;
import java.io.*;

/**
 * Created by bogdy on 12/20/15.
 */

public class Client implements Runnable
{
    private ClientOutput co;
    private Gap gap;

    public Client(Gap gap) {
        co = new ClientOutput();
        this.gap = gap;
    }

    public void sendInsert(int position, Character character) {
        co.sendCommand(new Insert(position, character));
    }

    public void sendDelete(int position) {
        co.sendCommand(new Delete(position, null));
    }

    public void run() {
        String hostName = "127.0.0.1";
        int port = 4444;
        Socket commSocket = null;
        InputStream is = null;
        ObjectInputStream ois = null;
        try {
            Command receivedCommand;
            while (true) {
                commSocket = new Socket(hostName, port);
                is = commSocket.getInputStream();
                ois = new ObjectInputStream(is);
                receivedCommand = (Command) ois.readObject();
                switch (receivedCommand.getType()) {
                    case INSERT:
                        Insert ins = (Insert) receivedCommand;

                        gap.insert(receivedCommand.getC());

                        break;
                    case DELETE:
                        break;
                }
            }
        } catch (IOException e) {
            try {
                is.close();
                ois.close();
                commSocket.close();
            } catch (IOException ex) {
                System.out.println("Error at disconnecting the client!");
            } catch (NullPointerException ex2) {
                System.out.println("Null pointer exception in client disconnection!");
            }
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            //System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.print("Class command not found! Did you forgot to add it?");
        }
    }
}
