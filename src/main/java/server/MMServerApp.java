/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;

/**
 * Responsible to start the game server
 * @author Hau Gilles Che, Denys Melyukhov, Realanderson Sena
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
