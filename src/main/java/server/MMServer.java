
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.LoggerFactory;

/**
 *  Responsible for accepting user connection and  opens a game session
 * @author Hau Gilles Che, Denys Melyukhov, Realanderson Sena
 */
public class MMServer {
    private final int portNumber;
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    
    public MMServer(int portNumber) throws IOException{
        this.portNumber=portNumber;
        createServerSocket();
    }
    
    /**
     * accepts user connections and create the socket used for the game session
     * @throws IOException 
     */
    private void createServerSocket() throws IOException{
        ServerSocket sSocket=new ServerSocket(portNumber);
        log.error(sSocket.getInetAddress().getLocalHost().getHostAddress());
        
        //System.out.println(sSocket.getInetAddress().
         //       getLocalHost().getHostAddress());
        
        while(true){
            Socket cSocket=sSocket.accept();
            
            MMServerSession session = new MMServerSession(cSocket);
            
        }
    }
}
