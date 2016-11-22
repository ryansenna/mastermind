/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcontrollers;

import client.MMClient;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * This class is the controller for the GamePage.fxml It will deal with simple
 * game logic.
 *
 * @author Railanderson Sena
 */
public class GamePageController {

    @FXML
    private Circle blue;
    @FXML
    private Circle brown;
    @FXML
    private TextField displayMessage;
    @FXML
    private GridPane gp;
    @FXML
    private Circle green;
    @FXML
    private Circle orange;
    @FXML
    private Circle purple;
    @FXML
    private Circle red;
    @FXML
    private Circle rose;
    @FXML
    private TextField serverNum;
    @FXML
    private TabPane tabs;
    @FXML
    private Circle yellow;

    private MMClient client = new MMClient();
    private Circle currentChoice;// to keep track of the color choice.
    private HBox currentHintRow;
    private HBox currentRow;
    private int rowNum = 11;// the starting row number.

    private Stage stage;
    private Scene scene;
    private FrontPageController fpc;

    /**
     * This method handles the current click of the user. If the user clicks on
     * one of the right side colors, it will record the color of the circle to
     * put in the code answer set.
     *
     * @param event
     */
    @FXML
    public void handleColorClicked(MouseEvent event) {
        //disable give up during the pick
        getCurrentRow(12, 1).setDisable(true);
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
     * This Method welcomes and tries to connect to the server when the user
     * clicks
     */
    @FXML
    public void onEnterClicked() {
        if (isIpNum() && connectToServer()) {
            displayMessage.setText("Welcome to MasterMind Game!");
            tabs.getTabs().get(1).setDisable(false);
            tabs.getTabs().get(0).setDisable(true);

        } else {
            displayMessage.setText("Something went wrong. Try again :(");
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
        client.sendGuess(new int[]{9, 1, 1, 1});
        getCurrentRow(12, 3).setDisable(false);

    }

    /**
     * This method will restart the game
     */
    @FXML
    public void onNewGameClicked() {
        //hide the answer
        getCurrentRow(1, 1).setVisible(false);
        //enable send btn
        getCurrentRow(12, 2).setDisable(false);
        // set row to be initial
        rowNum = 11;
        // set all rows to be color Dodger blue
        for (int i = 2; i <= 11; i++) {
            HBox thisRow = getCurrentRow(i, 1);
            //repaint everything
            Circle[] currentRowCircles
                    = getCurrentRowCircles(thisRow);
            for (int j = 0; j < 4; j++) {
                currentRowCircles[j].setFill(Color.DODGERBLUE);
            }
            thisRow.setDisable(true);
        }
        // set all hints to be color Dodger blue
        for (int i = 2; i <= 11; i++) {
            HBox thisRow = getCurrentRow(i, 2);
            //repaint everything
            Circle[] currentRowCircles
                    = getCurrentRowCircles(thisRow);
            for (int j = 0; j < 4; j++) {
                currentRowCircles[j].setFill(Color.DODGERBLUE);
            }
            thisRow.setVisible(false);
        }

        //renable first row
        getCurrentRow(11, 1).setDisable(false);

        client.sendGuess(new int[]{9, 9, 9, 9});
        setSecretCode();
        getCurrentRow(12, 3).setDisable(true);
        //set hidden answer
        //setHiddenAnswer();
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
            int[] guess = getIntValuesFromCurrentRow(currentRowCircles);

            int nextRow = rowNum - 1;
            currentRow.setDisable(true);// disables the current working row
            getCurrentRow(nextRow, 1).setDisable(false);// enable the next row.

            if (client.sendGuess(guess)) {
                //enable give up btn after sending a guess.
                getCurrentRow(12, 1).setDisable(false);
                setHints();
                rowNum--;

            }
        }
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
            ex.printStackTrace();
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
     * This method is responsible for connecting the client to the server.
     *
     * @return
     */
    private boolean connectToServer() {
        return client.getConnection();
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

    /**
     * This method does a short data validation for entering an ip number.
     *
     * @return
     */
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

    /**
     * This helper method will paint the hints into the view.
     */
    private void setHints() {
        // get the first HBox
        currentHintRow = getCurrentRow(rowNum, 2);
        //enable it
        currentHintRow.setDisable(false);
        //make it visible
        currentHintRow.setVisible(true);

        // get the content of it
        Circle[] currentRowCircles = getCurrentRowCircles(currentHintRow);

        //get hint code from server
        int[] ansFromServer = new int[4];
        try {
            ansFromServer = client.receiveHintFromServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int winCodeCount = 0;
        int loseCodeCount = 0;
        for (int i = 0; i < ansFromServer.length; i++) {
            if (ansFromServer[i] == 7) {
                winCodeCount++;
            }
            if (ansFromServer[i] == 8) {
                loseCodeCount++;
            }
            currentRowCircles[i].
                    setFill(switchToColorForHint(ansFromServer[i]));
        }

        if (winCodeCount == 4 || loseCodeCount == 4) {
            getCurrentRow(1, 1).setVisible(true);// make the secret code visible
            getCurrentRow(12, 1).setDisable(true);// disable give up
            getCurrentRow(12, 2).setDisable(true);// disable send
            getCurrentRow(12, 3).setDisable(false);//enable new game
        }

    }

    /**
     * This helper method sets the secret code from the server hidden in the
     * view.
     */
    private void setSecretCode() {
        int[] secretCode = new int[4];
        try {
            secretCode = client.receiveHintFromServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        HBox thisRow = getCurrentRow(1, 1);
        Circle[] thisRowCircles = getCurrentRowCircles(thisRow);

        setSecretCodeInView(thisRowCircles, secretCode);
    }

    /**
     * This Method will paint the secret code in hte view.
     *
     * @param thisRowCircles
     * @param digits
     */
    private void setSecretCodeInView(Circle[] thisRowCircles, int[] digits) {
        int j = 0;
        for (int i = 3; i >= 0; i--) {
            thisRowCircles[i].setFill(switchToColor(digits[j]));
            j++;
        }
    }

    /**
     * This helper method switches int values to color values.
     *
     * @param n
     * @return
     */
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

    /**
     * This helper method switches the int numbers from the hints to colors.
     *
     * @param n
     * @return
     */
    private Color switchToColorForHint(int n) {
        if (n == 1) {
            return Color.BLACK;
        }
        if (n == 2) {
            return Color.AQUA;
        }
        return Color.DODGERBLUE;
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
}
