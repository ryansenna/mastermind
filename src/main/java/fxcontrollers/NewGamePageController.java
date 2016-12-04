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
 *
 * @author Railanderson Sena
 */
public class NewGamePageController {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @FXML
    private Label win_lose_result;
    @FXML
    private GridPane grid_pane_choices;
    @FXML
    private GridPane hint_grid;
    @FXML
    private MenuItem new_game_option;
    @FXML
    private MenuItem test_dev_option;

    private MMClient client;
    private HBox[] allRowsInPane;
    private HBox[] allHintsInPane;
    private Button clickedBtn;
    private Button[] currentRowOfBtns = new Button[4];
    private int currentRow;

    public NewGamePageController() {
    }

    public void loadClient(MMClient client) {
        this.client = client;
        log.error("CLIENT LOADED: " + this.client);
    }

    @FXML
    private void initialize() {
        allRowsInPane = getAllRowsFromGrid(grid_pane_choices);
        allHintsInPane = getAllRowsFromGrid(hint_grid);
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
        allRowsInPane[currentRow].setDisable(false);
        allRowsInPane[currentRow].setVisible(true);
        // send the guess to the server.
        try {
            client.sendGuess(guess);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log.error(ioe.getMessage());
            alertMistake("your guess wasnt sent :(");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            alertMistake("Something went wrong!");
        }
        // Receive and display answer from the server.
        receiveAnswerFromServer();
    }

    private void receiveAnswerFromServer() {
        try {
            client.receiveMsgFromServer();
            if (client.isGameWon()) {
                //display nice msg.
                alertSuccess("You Won!");
            } else if (client.isGameOver()) {
                alertSuccess("You lost!");
                //display nice msg.
            } else if(client.devMsgReceived()){
                alertSuccess("Dev Guess Setted!");
            }
            else {
                int[] hints = client.getHints();
                displayHints(hints);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log.error(ioe.getMessage());
            alertMistake("Server Error, Restart the game.");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            alertMistake("Server Error, Reload the game");
        }
    }

    private void displayHints(int[] hints) {
        Label l = getCurrentHintBox();
        String display = "";
        for(int i : hints)
            display += i;
        l.setText(display);
    }

    private List<Integer> parseBtnTextToGuess() {
        List<Integer> guess = new ArrayList<>();
        for (Button button : currentRowOfBtns) {
            guess.add(Integer.parseInt(button.getText()));
        }
        return guess;
    }

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

    @FXML
    private void onTestDevClicked(ActionEvent event) {

        try {
            client.startDevGame();
            allRowsInPane[0].setDisable(false);// enable first row.
            currentRow = 0;
            new_game_option.setDisable(true);
            alertSuccess("Development Game Started!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log.error(ioe.getMessage());
            alertMistake("Can't Connect!");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            alertMistake("Something went wrong!");
        }
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        try {
            client.closeConnection();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            Platform.exit();
        }
    }

    @FXML
    private void onNewGameClicked(ActionEvent event) {
        try {
            client.startNewGame();
            allRowsInPane[0].setDisable(false);// enable first row.
            test_dev_option.setDisable(true);
            alertSuccess("New Game Started!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log.error(ioe.getMessage());
            alertMistake("Can't Connect!");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            alertMistake("Something went wrong!");
        }
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
}
