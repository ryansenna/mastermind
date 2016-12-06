package server;

import business.Board;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * This class will simulate a session for each client.
 * For example, when a client sends the message to connect, a session starts.
 *
 * @author Ryan Sena
 */
public class MMServerSession implements Runnable {

    private final Socket socket;
    private boolean devOptionActivated = false;
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public MMServerSession(Socket socket) throws IOException {
        this.socket = socket;
    }

    /**
     * This is the method responsible to run in the background when start is 
     * invoked.
     */
    @Override
    public void run() {
        try {
            playGames();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * generate a message that will signal to the user that he lost
     *
     * @return loosing message
     */
    private List<Integer> generateLoosingMsg() {
        List<Integer> loseMsg = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            loseMsg.add(8);
        }
        return loseMsg;
    }

    /**
     * generate a message that will signal to the user that he won
     *
     * @return winning message
     */
    private List<Integer> generateWinMsg() {
        List<Integer> winMsg = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            winMsg.add(7);
        }
        return winMsg;
    }
    /**
     * generates a message to the user that it is a developer game.
     * @return 
     */
    private List<Integer> generateDevMsg() {
        List<Integer> devMsg = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            devMsg.add(6);
        }
        return devMsg;
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
            int msgFromClient = MMPacket.readBytesForList(byteBuffer);
            log.error(msgFromClient + "");

            Board gameBoard = new Board();

            //9999 means that the developper option was activated, therefore,
            //we know the secret code in advance.
            if (msgFromClient == 9999) {
                log.error("DEV OPTION ACTIVATED");
                devOptionActivated = true;
            }
            byte[] answer;
            List<Integer> hint = new ArrayList<>(4);
            //inner loop operating 1 game until
            //the game is over or the user gave up.
            while ((recvMsgSize = in.read(byteBuffer)) != -1) {// get the guess from the user.
                 int guess = MMPacket.readBytesForList(byteBuffer);
                log.error("THE GUESS IS " + guess);
                gameBoard.setRow(guess);
                if(guess == 11111111){
                    out.write(MMPacket.writeBytes(gameBoard.getCode()));
                    break;
                }
                if (devOptionActivated) {// if the dev option was activated, we set the code to be the first try of the user.
                    // if the dev option wasnt activated, we leave it random.
                    gameBoard.setCode(guess);
                    devOptionActivated = false; // I do not want to set this again forhe next tries.
                    answer = MMPacket.writeBytes(generateDevMsg());
                    out.write(answer);
                    continue;
                }
                if (gameBoard.checkWin()) {// if the user wins
                    hint = this.generateWinMsg();
                    answer = MMPacket.writeBytes(hint);//write the answer back
                    out.write(answer);
                    out.write(MMPacket.writeBytes(gameBoard.getCode()));
                    break;
                } else if (gameBoard.isGameOver()) {
                    //generate message about game over.
                    hint = this.generateLoosingMsg();
                    answer = MMPacket.writeBytes(hint);
                    out.write(answer);
                    out.write(MMPacket.writeBytes(gameBoard.getCode()));
                    break;
                } else {// if the user didnt win and the game wasnt over, generate a hint.
                    hint = gameBoard.getHints();
                    answer = MMPacket.writeBytes(hint);
                    out.write(answer);
                }
            }
        }

    }

}
