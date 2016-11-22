/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcontrollers;

import client.MMClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    void onOptionAssigned(ActionEvent event) {

    }

    @FXML
    void onOptionClicked(ActionEvent event) {

    }

    @FXML
    void onSendClicked(ActionEvent event) {

    }

}
