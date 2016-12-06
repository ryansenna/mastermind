
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is responsible for accepting new clients
 * and pushing them to the background.
 * @author Ryan Sena
 */
public class MMServer {
    private final int portNumber;
    
    public MMServer(int portNumber) throws IOException{
        this.portNumber=portNumber;
        createServerSocket();
    }
    
    /**
     * This method will accept and push them to the background
     * in order to get ready to accept a new client.
     * @throws IOException 
     */
    private void createServerSocket() throws IOException{
        ServerSocket sSocket=new ServerSocket(portNumber);
        System.out.println(sSocket.getInetAddress().
                getLocalHost().getHostAddress());
        
        while(true){
            Socket cSocket=sSocket.accept();// accept client
            MMServerSession session = 
                    new MMServerSession(cSocket);// start the session.
            new Thread(session).start();// push it to the background.
        }
    }
}
