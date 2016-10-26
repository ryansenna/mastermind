/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcontrollers;

import client.ConfigBean;
import client.MMClient;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * This class is the controller for the GamePage.fxml It will deal with simple
 * game logic.
 *
 * @author Railanderson Sena
 */
public class GamePageController {

    //FXML fixed colors on the right side of the Controller
    @FXML
    private Circle rose;
    @FXML
    private Circle brown;
    @FXML
    private Circle orange;
    @FXML
    private Circle green;
    @FXML
    private Circle purple;
    @FXML
    private Circle yellow;
    @FXML
    private Circle red;
    @FXML
    private Circle blue;

    // The GridPane of the page.
    @FXML
    private GridPane gp;
    @FXML
    private GridPane gpConnect;
    @FXML
    private TabPane tabs;
    @FXML
    private TextField serverNum;
    @FXML
    private TextField displayMessage;

    private HBox currentRow, currentHintRow;// to keep track of each row
    private Circle currentChoice;// to keep track of the color choice.
    private MMClient client = new MMClient();
    private int rowNum = 11;// the starting row number.

    /**
     * This method handles the current click of the user. If the user clicks on
     * one of the right side colors, it will record the color of the circle to
     * put in the code answer set.
     *
     * @param event
     */
    @FXML
    public void handleColorClicked(MouseEvent event) {

        if (event.getSource() == rose) {
            currentChoice = rose;
        } else if (event.getSource() == brown) {
            currentChoice = brown;
        } else if (event.getSource() == orange) {
            currentChoice = orange;
        } else if (event.getSource() == green) {
            currentChoice = green;
        } else if (event.getSource() == purple) {
            currentChoice = purple;
        } else if (event.getSource() == yellow) {
            currentChoice = yellow;
        } else if (event.getSource() == red) {
            currentChoice = red;
        } else if (event.getSource() == blue) {
            currentChoice = blue;
        }
    }

    /**
     * This method takes care of assigning the current choice of the user to the
     * code answer set.
     *
     * @param event
     */
    @FXML
    public void setColorPicked(MouseEvent event) {
        Circle c = (Circle) event.getSource();
        c.setFill(currentChoice.getFill());
    }

    /**
     * This method will be responsible for receiving a hint from the server and
     * displaying to the user.
     */
    @FXML
    public void receiveAnswers() {
        //todo
        try {
            client.receiveHintFromServer();
        } catch (Exception ex) {
            //Logger.getLogger(GamePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method will set the Send button click. It will find the current row
     * in which is dealing with, transcript the colors guess from the user to a
     * string then send to the server.
     */
    @FXML
    public void onSendBtnClick() {
        currentRow = getCurrentRow(rowNum, 1);
        Circle[] currentRowCircles = getCurrentRowCircles(currentRow);
        if (isAFullAnswer(currentRowCircles)) {
            int[] code = getIntValuesFromCurrentRow(currentRowCircles);

            String guess = "";

            for (int i = 0; i < code.length; i++) {
                guess += code[i];
            }
            int nextRow = rowNum - 1;
            currentRow.setDisable(true);// disables the current working row
            getCurrentRow(nextRow, 1).setDisable(false);// enable the next row.

            if (client.sendGuess(guess)) {
                setHints();
                rowNum--;
            }
        }
    }

    /**
     * This method will end the game showing the answer to the user and
     * disabling all the buttons but new game.
     */
    @FXML
    public void onGiveUpClicked() {
        //make answer visible
        getCurrentRow(1, 1).setVisible(true);
        //disable all rows.
        for (int i = 1; i <= 11; i++) {
            getCurrentRow(i, 1).setDisable(true);
        }
        //disable Give Up Btn
        getCurrentRow(12, 1).setDisable(true);
        //disable Send Btn.
        getCurrentRow(12, 2).setDisable(true);
    }

    /**
     * This method will restart the game
     */
    @FXML
    public void onNewGameClicked() {

        //hide the answer
        getCurrentRow(1, 1).setVisible(false);
        //enable give up btn
        getCurrentRow(12, 1).setDisable(false);
        //enable send btn
        getCurrentRow(12, 2).setDisable(false);
        // set row to be initial
        rowNum = 11;
        // set all rows to be color Dodger blue
        for (int i = 2; i <= 11; i++) {
            //repaint everything
            Circle[] currentRowCircles
                    = getCurrentRowCircles(getCurrentRow(i, 1));
            for (int j = 0; j < 4; j++) {
                currentRowCircles[j].setFill(Color.DODGERBLUE);
            }
        }
        //renable first row
        getCurrentRow(11, 1).setDisable(false);
        client.sendGuess("9999");
        //set hidden answer
        //setHiddenAnswer();
    }

    @FXML
    public void onEnterClicked() {
        if (isIpNum() && connectToServer()) {
            displayMessage.setText("Welcome to MasterMind Game. I dare you to beat what is on my mind. Go!");
            tabs.getTabs().get(1).setDisable(false);
        } else {
            displayMessage.setText("Something went wrong. Try again :(");
        }
    }

    /**
     * This method transcripts the color values to int values.
     *
     * @param currentRowCircles
     * @return
     */
    private int[] getIntValuesFromCurrentRow(Circle[] currentRowCircles) {
        int[] array = new int[4];

        for (int i = 0; i < 4; i++) {
            array[i] = switchToInt(currentRowCircles[i].getFill());
        }

        return array;
    }

    /**
     * This is the witch table for ints and colors.
     *
     * @param circleColor
     * @return
     */
    private int switchToInt(Paint circleColor) {

        if (circleColor == Color.BLUE) {
            return 1;
        }
        if (circleColor == Color.RED) {
            return 2;
        }
        if (circleColor == Color.YELLOW) {
            return 3;
        }
        if (circleColor == Color.PURPLE) {
            return 4;
        }
        if (circleColor == Color.GREEN) {
            return 5;
        }
        if (circleColor == Color.ORANGE) {
            return 6;
        }
        if (circleColor == Color.BROWN) {
            return 7;
        }
        if (circleColor == Color.PINK) {
            return 8;
        }
        return 0;
    }

    private Color switchToColor(int n) {
        if (n == 1) {
            return Color.BLUE;
        }
        if (n == 2) {
            return Color.RED;
        }
        if (n == 3) {
            return Color.YELLOW;
        }
        if (n == 4) {
            return Color.PURPLE;
        }
        if (n == 5) {
            return Color.GREEN;
        }
        if (n == 6) {
            return Color.ORANGE;
        }
        if (n == 7) {
            return Color.BROWN;
        }
        if (n == 8) {
            return Color.PINK;
        }

        return Color.DODGERBLUE;
    }
    
    private Color switchToColorForHint(int n)
    {
        if(n == 1)
            return Color.BLACK;
        if(n == 0)
            return Color.WHITE;
        return Color.DODGERBLUE;
    }

    /**
     * This method will get the current working row( row in which the user input
     * their guesses) from the GridPane.
     *
     * @param row the row number to look for
     * @return the row found.
     */
    private HBox getCurrentRow(int row, int col) {
        Node result = new HBox();
        Integer rowIndex, cIndex;
        for (Node n : gp.getChildren()) {
            rowIndex = GridPane.getRowIndex(n);
            cIndex = GridPane.getColumnIndex(n);
            if (rowIndex == row && cIndex == col) {
                result = n;
                break;
            }
        }

        return (HBox) result;
    }

    /**
     * This method gets the current elements inside the row. It will get all the
     * circles as an array of Circles.
     *
     * @param row
     * @return a circle array
     */
    private Circle[] getCurrentRowCircles(HBox row) {
        Circle[] currentRowCircles = new Circle[4];

        for (int i = 0; i < row.getChildren().size(); i++) {
            currentRowCircles[i] = (Circle) row.getChildren().get(i);
        }
        return currentRowCircles;
    }

    /**
     * This method checks if the circle row is a full answer meaning that if the
     * user left the color row being the default, his guess will not get into
     * account.
     *
     * @param c
     * @return
     */
    private boolean isAFullAnswer(Circle[] c) {
        for (int i = 0; i < c.length; i++) {
            if (c[i].getFill() == Color.DODGERBLUE) {
                return false;
            }
        }
        return true;
    }

    private boolean isIpNum() {

        String ipNum = serverNum.getText();
        if (ipNum == null) {
            return false;
        }
        if (ipNum.isEmpty()) {
            return false;
        }
        int numOfDots = 0;
        for (int i = 0; i < ipNum.length(); i++) {
            if (ipNum.charAt(i) == '.') {
                numOfDots++;
            }
            if (!Character.isDigit(ipNum.charAt(i)) && ipNum.charAt(i) != '.') {
                return false;
            }
        }

        if (numOfDots > 3) {
            return false;
        }
        return true;
    }

    private String setSecretCode() {

        HBox thisRow = getCurrentRow(11, 1);
        Circle[] thisRowCircles = getCurrentRowCircles(currentRow);

        int[] code = getIntValuesFromCurrentRow(thisRowCircles);
        String guess = "";

        for (int i = 0; i < code.length; i++) {
            guess += code[i];
        }
        int guessInt = Integer.parseInt(guess);

        if (guessInt == 1585) // << DEVELOPER CODE. TESTING FINAL ANSWER.
        {
            //set the message to be sent to the server here.
        }

        return guess;

    }

    private boolean connectToServer() {
        return client.getConnection();
    }

    private void setHints() {
        // get the first HBox
        currentHintRow = getCurrentRow(rowNum, 2);
        //enable it
        currentHintRow.setDisable(false);
        currentHintRow.setVisible(true);
        
        // get the content of it
        Circle[] currentRowCircles = getCurrentRowCircles(currentHintRow);

        //get hint code from server
        String a = "";
        try {
            a = client.receiveHintFromServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int c;

        for (int i = 0; i < 4; i++) {
            c = (int) a.charAt(i);
            currentRowCircles[i].setFill(switchToColorForHint(c));// assign a color to each.
        }

    }

}
