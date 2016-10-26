/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author 1333612
 */
public class ServerInputController {


    @FXML
    private TextField serverNum;

    private Scene scene;
    private Stage stage;
    private GamePageController gpc;
    
    public ServerInputController(){
        super();
    }

    //public void setSceneStageController()
    /**
     * Initializes the controller class.
     */
    @FXML
    public void goToGamePage(ActionEvent event) {
        // Change the scene on the stage
        stage.setScene(scene);
    }

    public void setSceneStageController(Scene scene2, Stage stage, GamePageController gpc) {
        this.scene = scene2;
        this.stage = stage;
        this.gpc = gpc;
    }

}
