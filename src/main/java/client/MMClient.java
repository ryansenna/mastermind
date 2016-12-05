/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import server.MMPacket;

/**
 * This class is the Client class, the one that will receive the guesses from
 * the MasterMind app and submit to the Server.
 *
 * @author Railanderson Sena
 */
public class MMClient {

    private ConfigBean configForServer;
    private byte[] guessBuffer;
    private Socket socket;
    private byte[] serverAnswer;
    private int secretAnswer;

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public MMClient() {
        configForServer = new ConfigBean();
        guessBuffer = null;
        socket = null;
        serverAnswer = new byte[4];
    }

    public MMClient(ConfigBean configurationsForServer) {
        this.configForServer = configurationsForServer;
    }

    /**
     * This method will try to Open a connection to the server.
     * 
     *@throws 
     */
    public void getConnection() throws IOException {
        String serverNum = configForServer.getServerNumber();
        int serverPort = configForServer.getPortNum();
        this.socket = new Socket(serverNum, serverPort);
    }

    /**
     * The method is responsible to compute an answer from the server.
     *
     * @throws IOException
     */
    public void receiveMsgFromServer() throws IOException {
        if (guessBuffer == null || socket == null) {
            throw new NullPointerException(
                    "You must send a guess before"
                    + " trying to receive and answer.");
        }
        InputStream in = socket.getInputStream();
        int totalBytesRcvd = 0, bytesRcvd = 0;
        serverAnswer = new byte[4];
        while (totalBytesRcvd < guessBuffer.length) {
            log.error("The Total Bytes Received is : " + totalBytesRcvd);
            bytesRcvd = in.read(serverAnswer, totalBytesRcvd,
                    guessBuffer.length - totalBytesRcvd);
            log.error("The Bytes Received is : " + bytesRcvd);
            if (bytesRcvd == -1) {
                throw new SocketException("Connection close prematurely");
            }

            totalBytesRcvd += bytesRcvd;
        }
    }

    /**
     * The method will take a guess from the app, convert to bytes and send to
     * the server.
     *
     * @param guess the user's guess.
     * @throws IOException
     */
    public void sendGuess(List<Integer> guess) throws IOException {

        guessBuffer = MMPacket.writeBytes(guess);
        OutputStream out = socket.getOutputStream();
        //send to the server.
        out.write(guessBuffer);
    }
    /**
     * This method will send a message to the server telling that this is a 
     * normal new game.
     * @throws IOException 
     */
    public void startNewGame() throws IOException {
        List<Integer> message = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            message.add(0);
        }

        guessBuffer = MMPacket.writeBytes(message);
        OutputStream out = socket.getOutputStream();
        //send to the server.
        out.write(guessBuffer);
    }
    /**
     * This method will send a message to the server telling that this is a 
     * testing game.
     * @throws IOException 
     */
    public void startDevGame() throws IOException {
        List<Integer> message = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            message.add(9);
        }

        guessBuffer = MMPacket.writeBytes(message);
        OutputStream out = socket.getOutputStream();
        //send to the server.
        out.write(guessBuffer);
    }
    /**
     * This method will send a message to the server telling that the user wishes
     * to give up on his current match.
     * @throws IOException 
     */
    public void giveUp() throws IOException {
        secretAnswer = 0;
        List<Integer> message = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            message.add(0xB);
        }

        guessBuffer = MMPacket.writeBytes(message);
        OutputStream out = socket.getOutputStream();
        //send to the server.
        out.write(guessBuffer);
        this.receiveMsgFromServer();// get ready to receive the secretAnswer.
        secretAnswer = MMPacket.readBytes(serverAnswer);// read it.
    }
    /**
     * This method reads an answer from the server and evaluating
     * if the user won the game.
     * @return
     * @throws IOException 
     */
    public boolean isGameWon() throws IOException {
        secretAnswer = 0;
        int message = MMPacket.readBytes(serverAnswer);
        if (message == 7777) {
            this.receiveMsgFromServer();// get ready to receive the secretAnswer.
            secretAnswer = MMPacket.readBytes(serverAnswer);// read it.
            return true;
        } else {
            return false;
        }
    }
    /**
     * This method reads an answer from the server and evaluates if
     * the user lost the game.
     * @return
     * @throws IOException 
     */
    public boolean isGameOver() throws IOException {
        secretAnswer = 0;
        int message = MMPacket.readBytes(serverAnswer);
        if (message == 8888) {
            this.receiveMsgFromServer();// get ready to receive the secretAnswer.
            secretAnswer = MMPacket.readBytes(serverAnswer);// read it.
            return true;
        } else {
            return false;
        }
    }
    /**
     * This method will read the message from the server telling that the
     * developer game was started successfuly.
     * @return
     * @throws IOException 
     */
    public boolean devMsgReceived() throws IOException {
        int message = MMPacket.readBytes(serverAnswer);
        if (message == 6666) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * This method will read the hints from the server.
     * @return
     * @throws IOException 
     */
    public int getHints() throws IOException {
        return MMPacket.readBytes(serverAnswer);
    }
    /**
     * This method will close the connection.
     * @throws IOException 
     */
    public void closeConnection() throws IOException {
        this.socket.close();
    }
    /**
     * This method returns the master mind for displaying purposes.
     * @return 
     */
    public int getSecretAnswer() {
        return secretAnswer;
    }

}
