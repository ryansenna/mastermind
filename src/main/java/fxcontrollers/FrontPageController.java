/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcontrollers;

import client.ConfigBean;
import client.MMClient;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Railanderson Sena
 */
public class FrontPageController {

    @FXML
    private TextField server_input;

    @FXML
    private Button enter_btn;

    private Stage stage;
    private Scene scene;
    private NewGamePageController gpc;
    private MMClient client;
    private ConfigBean config;
    private boolean connected;

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public FrontPageController() {
        connected = false;
    }

    public void setSceneStateSecPage(Scene scene, Stage stage, NewGamePageController gpc) {
        this.scene = scene;
        this.stage = stage;
        this.gpc = gpc;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void onEnterClicked(ActionEvent event) {
        config = new ConfigBean(server_input.getText(), 50000);
        client = new MMClient(config);
        try {
            client.getConnection();
            gpc.loadClient(client);// loads the client to the next page.
            stage.setScene(scene);// sets the second page.
            alertSuccess("Connected!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log.error(ioe.getMessage());
            alertMistake("Invalid Connection, try again!");

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            alertMistake("Oops! Something went wrong.");
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
