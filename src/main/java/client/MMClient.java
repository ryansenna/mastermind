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

    public MMClient() {
        configForServer = new ConfigBean();
        guessBuffer = null;
        socket = null;
    }

    public MMClient(ConfigBean configurationsForServer) {
        this.configForServer = configurationsForServer;
    }

    /**
     * The method will take a guess from the app, convert to bytes and send to
     * the server.
     *
     * @param guess the user's guess.
     * @throws IOException
     */
    public boolean sendGuess(String guess) {

        List<Integer> guessList = new ArrayList<>(4);
        //converts the guess string into a list of integers
        for (int i = 0; i < guess.length(); i++) {
            int digit = Integer.parseInt(guess.substring(i, i + 1));
            guessList.add(digit);
        }

        guessBuffer = MMPacket.writeBytes(guessList);
        try {
            OutputStream out = socket.getOutputStream();
            //send to the server.
            out.write(guessBuffer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The method is responsible to compute an answer from the server and return
     * it to the app/user.
     *
     * @return the server answer.
     * @throws IOException
     */
    public String receiveHintFromServer() throws IOException {
        if (guessBuffer == null || socket == null) {
            throw new NullPointerException(
                    "You must send a guess before"
                    + " trying to receive and answer.");
        }

        int totalBytesRcvd = 0, bytesRcvd;
        // 50 will change when I see the server code.
        byte[] serverAnswer = new byte[4];
        InputStream in = socket.getInputStream();
        while (totalBytesRcvd < guessBuffer.length) {
            System.out.println(totalBytesRcvd);
            bytesRcvd = in.read(serverAnswer, totalBytesRcvd,
                    guessBuffer.length - totalBytesRcvd);
            System.out.println(bytesRcvd);
            if (bytesRcvd == -1) {
                throw new SocketException("Connection close prematurely");
            }

            totalBytesRcvd += bytesRcvd;
        }

        //socket.close();

        //converts the received message into int value
        int message = MMPacket.readBytes(serverAnswer);
        System.out.println(message);
        return message + "";
    }

    /**
     * This method will try to get a connection to the server.
     *
     * @return
     */
    public boolean getConnection() {
        String serverNum = configForServer.getServerNumber();
        int serverPort = 50000;
        try {
            this.socket = new Socket(serverNum, serverPort);
            // send the request start message here !
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
