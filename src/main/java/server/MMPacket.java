package server;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for reading and writing byte arrays used for communication
 * between the client and the server
 *
 * @author Hau Gilles Che, Denys Melyukhov, Realanderson Sena
 */
public class MMPacket {

    /**
     * Reads a message received and converts it into a usable value for the
     * other classes
     *
     * @param message received message to convert
     * @return converted message
     */
    public static int readBytes(byte[] message) {
        byte[] byteBuffer = new byte[4];

        for (int i = 0; i < byteBuffer.length; i++) {
            byteBuffer[i] = (byte) 0xBB;
        }

        String guess = "";
        for (int i = 0; i < byteBuffer.length; i++) {
            //formats the byte array into hex format string
            guess += String.format("%02x", message[i]);
        }
        guess = guess.replace("0", "");
        return (!guess.isEmpty() ? Integer.parseInt(guess) : -2);
    }

    public static int readBytesForList(byte[] byteBuffer) {
        List<Integer> message = new ArrayList<>();
        String guess = "";
        for (int i = 0; i < byteBuffer.length; i++) {
            guess += byteBuffer[i];
        }
        //guess = guess.replace("0", "");
        int decoded = Integer.parseInt(guess);
        return decoded;
    }

    /**
     * writes a message into the proper format to send as a packet
     *
     * @param message message to convert
     * @return converted message ready to be sent
     */
    public static byte[] writeBytes(List<Integer> message) {
        byte[] byteBuffer = new byte[4];

        //for (int i = 0; i < byteBuffer.length; i++) {
        //    byteBuffer[i] = (byte) 0xBB;
        //}
        for (int i = 0; i < message.size(); i++) {
            byteBuffer[i] = Byte.parseByte(Integer.toHexString(
                    message.get(i)), 16);

            //-1 signifies that the user has won the game
            //create a message accordingly
            if (message.get(i) == -1) {
                byteBuffer[i] = (byte) 0xAA;
            } //-3 signifies that the uer has lost the game
            //create a message accordingly
            else if (message.get(i) == -3) {
                byteBuffer[i] = (byte) 0xFF;
            } else if (message.get(i) == 0) {//all 0 means start a new game.
                byteBuffer[i] = (byte) 0x00;
            }else if (message.get(i) == 9) {//all 9 means start a new dev game.
                byteBuffer[i] = (byte) 0x09;
            }
        }
        return byteBuffer;

    }
}
