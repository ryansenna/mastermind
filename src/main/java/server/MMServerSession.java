/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import business.Board;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for a session of one or more games with one user
 *
 * @author Hau Gilles Che, Denys Melyukhov, Realanderson Sena
 */
public class MMServerSession {

    private final Socket socket;

    public MMServerSession(Socket socket) throws IOException {
        this.socket = socket;
        playGames();
    }

    /**
     * execute game logic to play one or more games with the user
     *
     * @throws IOException
     */
    private void playGames() throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        byte[] byteBuffer = new byte[4];
        int recvMsgSize;

        //will cycle until client closes the socket
        while ((recvMsgSize = in.read(byteBuffer)) != -1) {
            int guess = MMPacket.readBytes(byteBuffer);
            //int guess = 1234;
            Board gameBoard = new Board();

            //-2 signifies that the user did not enter a code to use as secret 
            //code, if it is not equal to -2, use the received msg as code
            if (guess !=  9999) {
                gameBoard.setCode(guess);
            }
            byte[] answer;
            List<Integer> hint = new ArrayList<>(4);
            recvMsgSize = in.read(byteBuffer);
            //inner loop operating 1 game until the game is over
            while (!gameBoard.isGameOver() && recvMsgSize != -1) {
                guess = MMPacket.readBytes(byteBuffer);
                gameBoard.setRow(guess);

                //generates hints if the user did not guess right
                if (!gameBoard.checkWin()) {
                    hint = gameBoard.getHints();
                    answer = MMPacket.writeBytes(hint);
                    out.write(answer);
                } else {
                    hint = generateWinMsg(hint);
                    answer = MMPacket.writeBytes(hint);
                    out.write(answer);
                }
                recvMsgSize = in.read(byteBuffer);// THIS IS THE TROUBLE LINE.
            }
            if (!gameBoard.checkWin()) {
                hint=generateLoosingMsg(hint);
                answer = MMPacket.writeBytes(hint);
                out.write(answer);
            }

        }

    }
    

    /**
     * generate a message that will signal to the user that he won
     *
     * @param hint message to send
     * @return winning message
     */
    private List<Integer> generateWinMsg(List<Integer> hint) {
        for (int i = 0; i < hint.size(); i++) {
            hint.set(i, -1);
        }
        return hint;
    }

    /**
     * generate a message that will signal to the user that he lost
     *
     * @param hint message to send
     * @return loosing message
     */
    private List<Integer> generateLoosingMsg(List<Integer> hint) {
        for (int i = 0; i < hint.size(); i++) {
            hint.set(i, -3);
        }
        return hint;
    }

}
