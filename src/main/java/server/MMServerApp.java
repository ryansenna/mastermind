package server;

import java.io.IOException;

/**
 * This class is the back end class responsible for starting up the server.
 * @author Ryan Sena
 */
public class MMServerApp {
    public static void main(String[] args){
        startServer();
    }
    
    /**
     * Starts the game server
     */
    private static void startServer(){
        try{
            MMServer server=new MMServer(50000);
        }catch (IOException ioe){
            System.exit(0);
        }
         
    }
}
