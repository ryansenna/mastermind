/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcontrollers;

import client.MMClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.slf4j.LoggerFactory;

/**
 * This is the main class for the master mind game.
 * Here you will find how everything works.
 * @author Railanderson Sena
 */
public class NewGamePageController {
    private HBox[] allHintsInPane;
    private HBox[] allRowsInPane;
    private Button clickedBtn;
    private MMClient client;
    private int currentRow;
    private Button[] currentRowOfBtns = new Button[4];
    @FXML
    private MenuItem give_up_option;
    @FXML
    private GridPane grid_pane_choices;
    @FXML
    private GridPane hint_grid;
    private final org.slf4j.Logger log = 
            LoggerFactory.getLogger(this.getClass().getName());
    @FXML
    private MenuItem new_game_option;
    @FXML
    private MenuItem test_dev_option;
    @FXML
    private Label win_lose_result;


    public NewGamePageController() {
    }
    /**
     * This method closes the client and the whole game.
     * @param event 
     */
    @FXML
    public void exitApplication(ActionEvent event) {
        try {
            client.closeConnection();
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
        } finally {
            Platform.exit();
        }
    }
    /**
     * This method serves to get the client from the front page.
     * @param client 
     */
    public void loadClient(MMClient client) {
        this.client = client;
        log.error("CLIENT LOADED: " + this.client);
    }
    /**
     * This creates a alert box informing the user about a mistake.
     *
     * @param message
     */
    private void alertMistake(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ryan's MAstermind Game");
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * This creates an alert box informing the user about a success.
     *
     * @param message
     */
    private void alertSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ryan's Mastermind Game");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method will clear all the fields 
     * from the game page in order to start a new game.
     */
    private void clear() {
        currentRow = 0;// reset the current row to 0.
        log.error("THE LENGTH OF ALLROWS INPANE IS " 
                + this.allRowsInPane.length);
        for (HBox h : allRowsInPane) {
            if (h != null) {
                h.setDisable(true);// disable all the rows.
                h.setVisible(false);
                // set the text of all buttons to be empty again.
                for (Node n : h.getChildren()) {
                    if (n instanceof Button) {
                        Button b = (Button) n;
                        if (!b.getText().equalsIgnoreCase("send")) {
                            b.setText("");
                        }
                    }
                }
            }
        }

    }
    /**
     * This method will take care of displaying the hints into the right
     * grid pane that I setted up.
     * @param hints 
     */
    private void displayHints(int hints) {
        Label l = getCurrentHintBox();
        l.setText(hints + "");
        if (hints == -2) {
            l.setText("");
        }
    }
    /**
     * This method get all HBoxes from a given grid pane
     * in my case I have 2 grid panes that I need to change 
     * their stae every time,the grid pane that holds all the user 
     * guesses and the grid pane to display the hints.
     * @param pane
     * @return 
     */
    private HBox[] getAllRowsFromGrid(GridPane pane) {
        int j = 0;
        HBox[] allRows = new HBox[11];
        for (Node n : pane.getChildren()) {
            log.error("INDEX J is " + j);
            if (!(n instanceof Group)) {
                allRows[j] = (HBox) n;
                j++;
            }
        }
        return allRows;
    }
    /**
     * This method takes care of getting the current hint box that will display 
     * the hint.
     * @return 
     */
    private Label getCurrentHintBox() {
        HBox box = allHintsInPane[currentRow];
        Label l = null;
        for (Node n : box.getChildren()) {
            if (!(n instanceof Group)) {
                l = (Label) n;
            }
        }
        return l;
    }
    /**
     * This method will get all the relevant buttons from the current row
     * in order to compute the user's guesses.
     */
    private void getCurrentRow() {
        HBox box = allRowsInPane[currentRow];
        int i = 0;
        for (Node b : box.getChildren()) {
            if (!(b instanceof Group)) {
                Button a = (Button) b;
                if (!a.getText().equalsIgnoreCase("send")) {
                    currentRowOfBtns[i] = a;
                    i++;
                }
            }
        }
    }
    /**
     * Framework method to initialize my variables.
     */
    @FXML
    private void initialize() {
        allRowsInPane = getAllRowsFromGrid(grid_pane_choices);
        allHintsInPane = getAllRowsFromGrid(hint_grid);
    }
    /**
     * This method will display info when the about is clicked.
     * @param event 
     */
    @FXML
    private void onAboutClicked(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setContentText("This Game is brought to you by Ryan Sena \n"
                + "Master mind is a normal casual game to pass the "
                + "time and have fun. \n"
                + "I hope you have fun playing it.\n"
                + "Regards, Ryan.");
        alert.showAndWait();
        
    }
    /**
     * This method will ask the client to give up 
     * and end the game to start a new one.
     * @param event 
     */
    @FXML
    private void onGiveUpClicked(ActionEvent event) {
        try {
            client.giveUp();
            new_game_option.setDisable(false);
            test_dev_option.setDisable(false);
            give_up_option.setDisable(true);
            int secretAnswer = client.getSecretAnswer();
            alertSuccess("You lost! The Master Mind Code is " + secretAnswer);
            clear();
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
        } catch(Exception ex){
            log.error(ex.getMessage());
        }
    }
    /**
     * This method will display info about how to play the game.
     * @param event 
     */
    @FXML
    private void onHowToClicked(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How To Play Master Mind");
        alert.setContentText("The Computer hides a secret. \n"
                + "I designated you the task to break and discover "
                + "this secret. \n"
                + "You have 10 tries to discover the computer's secret. \n"
                + "On your right, you will see empty spaces,"
                + "the computer will \n"
                + "tell you how close you are by sending you hints.\n"
                + "1 means you got one of the 4 numbers right on "
                + "the right spot.\n"
                + "2 means you got one of the 4 numbers right on "
                + "the wrong spot. \n"
                + "Your mind is your weapon! \n"
                + "Break The Master Mind!");
        alert.showAndWait();
    }
    /**
     * This method will ask the client to start a new game on the server.
     * @param event 
     */
    @FXML
    private void onNewGameClicked(ActionEvent event) {
        try {
            client.startNewGame();
            currentRow = 0;
            allRowsInPane[currentRow].setDisable(false);// enable first row.
            allRowsInPane[currentRow].setVisible(true);// enable first row.
            new_game_option.setDisable(true);
            test_dev_option.setDisable(true);
            give_up_option.setDisable(false);
            alertSuccess("New Game Started!");
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
            alertMistake("Can't Connect!");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            alertMistake("Something went wrong!");
        }
    }
    /**
     * This method will assign the value picked by the user to his own set of
     * choices.
     *
     * @param event
     */
    @FXML
    private void onOptionAssigned(ActionEvent event) {
        Button b = (Button) event.getSource();
        b.setText(clickedBtn.getText());
    }
    /**
     * This method will assign the value picked by the user to a place holder
     * variable.
     *
     * @param event
     */
    @FXML
    private void onOptionClicked(ActionEvent event) {
        clickedBtn = (Button) event.getSource();
    }
    /**
     * This method will ask the client to send the user's guesses to the server
     * and will ask an answer from the server.
     * @param event 
     */
    @FXML
    private void onSendClicked(ActionEvent event) {
        // get current row of buttons.
        getCurrentRow();
        // parse the text to int.
        List<Integer> guess = parseBtnTextToGuess();
        // disable the current row.
        allRowsInPane[currentRow].setDisable(true);
        currentRow++;
        // enable the next row.
        if (currentRow < 10) {
            allRowsInPane[currentRow].setDisable(false);
            allRowsInPane[currentRow].setVisible(true);
        }
        // send the guess to the server.
        try {
            client.sendGuess(guess);
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
            alertMistake("your guess wasnt sent :(");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            alertMistake("Something went wrong!");
        }
        // Receive and display answer from the server.
        receiveAnswerFromServer();
    }
    /**
     * This method will ask the client to ask a 
     * developer session with the server.
     * Meaning that the first guess sent will be 
     * the the master mind secret code.
     * @param event 
     */
    @FXML
    private void onTestDevClicked(ActionEvent event) {
        
        try {
            client.startDevGame();
            currentRow = 0;
            allRowsInPane[currentRow].setDisable(false);// enable first row.
            allRowsInPane[currentRow].setVisible(true);// enable first row.
            new_game_option.setDisable(true);
            test_dev_option.setDisable(true);
            give_up_option.setDisable(false);
            alertSuccess("Development Game Started!");
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
            alertMistake("Can't Connect!");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            alertMistake("Something went wrong!");
        }
    }
    /**
     * This method will parse the button's text to Integers in order to be 
     * sent over the stream.
     * @return 
     */
    private List<Integer> parseBtnTextToGuess() {
        List<Integer> guess = new ArrayList<>();
        for (Button button : currentRowOfBtns) {
            guess.add(Integer.parseInt(button.getText()));
        }
        return guess;
    }
    /**
     * This method will receive and evaluate the server answer.
     * if the use won, lost and how he lost it.
     */
    private void receiveAnswerFromServer() {
        try {
            client.receiveMsgFromServer();
            if (client.isGameWon()) {
                //display nice msg.
                int secretAnswer = client.getSecretAnswer();
                alertSuccess("You Won! The Master Mind Code is " 
                        + secretAnswer);
                new_game_option.setDisable(false);
                test_dev_option.setDisable(false);
                give_up_option.setDisable(true);
                clear();
            } else if (client.isGameOver()) {
                int secretAnswer = client.getSecretAnswer();
                alertSuccess("You lost! The Master Mind Code is " 
                        + secretAnswer);
                //display nice msg.
                new_game_option.setDisable(false);
                test_dev_option.setDisable(false);
                give_up_option.setDisable(true);
                clear();
            } else if (client.devMsgReceived()) {
                alertSuccess("Dev Guess Setted!");
            } else {
                int hints = client.getHints();
                displayHints(hints);
            }
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
            alertMistake("Server Error, Restart the game.");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            alertMistake("Server Error, Reload the game");
        }
    }
}
