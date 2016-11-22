/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcontrollers;

import client.MMClient;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Railanderson Sena
 */
public class NewGamePageController {
    
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    
    @FXML
    private Label win_lose_result;
    
    private MMClient client;
    
    public NewGamePageController(){
    }
    
    public void loadClient(MMClient client){
        this.client = client;
        log.error("CLIENT LOADED: " + this.client);
    }

    @FXML
    private void initialize() {

    }
    
    
    @FXML
    private void onOptionAssigned(ActionEvent event) {

    }

    @FXML
    private void onOptionClicked(ActionEvent event) {

    }

    @FXML
    private void onSendClicked(ActionEvent event) {

    }
    
    @FXML
    private void onNewGameClicked(ActionEvent event){
        try{
            client.startNewGame();
            alertSuccess("New Game Started!");
        }catch(IOException ioe){
            ioe.printStackTrace();
            log.error(ioe.getMessage());
            alertMistake("Can't Connect!");
        }catch(Exception ex){
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

}
