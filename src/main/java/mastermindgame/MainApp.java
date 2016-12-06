package mastermindgame;

import fxcontrollers.FrontPageController;
import fxcontrollers.NewGamePageController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private NewGamePageController fpc;
    private Stage stage;

    public MainApp() {
        super();
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        Scene scene2 = createGamePage();
        Scene scene1 = createFrontPage(scene2);

        this.stage.setScene(scene1);// set the front page.

        this.stage.setTitle("Ryan's Mastermind Game");

        this.stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Scene createFrontPage(Scene scene2) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(this.getClass().getResource("/fxml/FrontPage.fxml"));

        Parent root = (GridPane) loader.load();

        FrontPageController frontPageContr = loader.getController();
        // set the references to the second page.
        frontPageContr.setSceneStateSecPage(scene2, stage, fpc);

        Scene scene = new Scene(root);
        return scene;
    }

    private Scene createGamePage() throws Exception {
        // Instantiate the FXMLLoader
        FXMLLoader loader = new FXMLLoader();

        // Set the location of the fxml file in the FXMLLoader
        loader.setLocation(this.getClass().
                getResource("/fxml/NewGamePage.fxml"));

        // Parent is the base class for all nodes that have children in the
        // scene graph such as AnchorPane and most other containers
        Parent root = (BorderPane) loader.load();

        fpc = loader.getController();
        Scene scene = new Scene(root);
        return scene;
    }

}
