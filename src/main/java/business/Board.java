package business;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Board class representing a Master Mind game board. contains all the methods
 * to interact with the game board.
 *
 * @author Denys Melyukhov, Hau Gilles Che, Realanderson Sena
 */
public class Board {

    private List<Integer> code = new ArrayList<Integer>();
    private int guessCount = 0;
    private int[] rows = new int[10];

    public Board() {
        generateRandomCode();
        //deleteThisMethod();
    }

    /**
     * Checks if the user won a game
     *
     * @return whether or not the user won the game
     */
    public boolean checkWin() {
        int currentRow = rows[guessCount - 1];
        boolean won = true;
        int[] usersGuess = this.splitRow(currentRow);
        for (int i = 0; i < usersGuess.length; i++) {
            if (code.get(i) != usersGuess[i]) {
                won = false;
            }
        }
        return won;
    }

    /**
     * This Method will generate a random code at the board creation.
     */
    public void generateRandomCode() {
        Random rn = new Random();
        int n;
        for (int i = 0; i < 4; i++) {
            n = rn.nextInt(8) + 1;
            code.add(n);
        }
    }

    /**
     *
     * @return the game board containing the user's guesses
     */
    public Board getBoard() {
        return this;
    }

    /**
     * This Method will get the master mind secret code in order to display.
     *
     * @return
     */
    public List<Integer> getCode() {
        return this.code;
    }

    /**
     * Generates hints based on the user's guess
     *
     * @return the generated hints to give to the user
     */
    public List<Integer> getHints() {
        List<Integer> remainingCode = new ArrayList<Integer>(this.code);
        int currentRow = rows[guessCount - 1];
        int rightCounter = 0;
        List<Integer> hints = new ArrayList<Integer>();
        int[] rowsDigits = this.splitRow(currentRow);

        for (int i = 0; i < code.size(); i++) {
            if (rowsDigits[i] == code.get(i)) {
                remainingCode.remove((Integer) rowsDigits[i]);
                rowsDigits[i] = 0;
                rightCounter++;
            }

        }
        for (int i = 0; i < code.size(); i++) {
            if (remainingCode.contains(rowsDigits[i])) {
                hints.add(2);
            }

        }

        for (int j = 0; j < rightCounter; j++) {
            hints.add(1);
        }

        return hints;
    }

    /**
     * Checks if the game is over
     *
     * @return whether or not the game is over
     */
    public boolean isGameOver() {
        if (guessCount == 10) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param code digits that will be the secret answer of the game
     */
    public void setCode(int code) {
        int[] digits = this.splitRow(code);
        for (int i = 0; i < digits.length; i++) {
            this.code.add(digits[i]);
        }
    }

    /**
     *
     * @param row user's guess to insert into the game board
     */
    public void setRow(int row) {
        this.rows[guessCount] = row;
        guessCount++;
    }

    /**
     *
     * @param row a row which represents a particular user's guess
     * @return an array containing each individual digit of the guess
     */
    public int[] splitRow(int row) {
        int[] digits = new int[4];
        digits[0] = row / 1000;
        digits[1] = row / 100 % 10;
        digits[2] = row % 100 / 10;
        digits[3] = row % 10;
        return digits;
    }

    /**
     * This method sets a fixed code for testing purposes.
     */
    private void deleteThisMethod() {
        code.add(1);
        code.add(2);
        code.add(3);
        code.add(4);
    }

}
