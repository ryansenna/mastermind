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
    private int totalBytesRcvd, bytesRcvd;
    private byte[] serverAnswer;

    public MMClient() {
        configForServer = new ConfigBean();
        guessBuffer = null;
        socket = null;
        totalBytesRcvd = 0;
        bytesRcvd = 0;
        serverAnswer = new byte[4];
    }

    public MMClient(ConfigBean configurationsForServer) {
        this.configForServer = configurationsForServer;
    }

    /**
     * This method will try to get a connection to the server.
     *
     * @return
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
        serverAnswer = new byte[4];
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

    public boolean isGameWon() throws IOException {
        int message = MMPacket.readBytes(serverAnswer);
        if (message == 7777) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGameOver() throws IOException {
        int message = MMPacket.readBytes(serverAnswer);
        if (message == 8888) {
            return true;
        } else {
            return false;
        }
    }

    public boolean devMsgReceived() throws IOException {
        int message = MMPacket.readBytes(serverAnswer);
        if (message == 6666) {
            return true;
        } else {
            return false;
        }
    }

    public int[] getHints() throws IOException {
        int message = MMPacket.readBytes(serverAnswer);
        int[] digits = new int[4];
        int count = 0;
        while (message > 0) {
            digits[count] = message % 10;
            message = message / 10;
            count++;
        }
        return digits;
    }

    public void closeConnection() throws IOException {
        this.socket.close();
    }

}
