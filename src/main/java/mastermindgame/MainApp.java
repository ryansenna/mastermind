package mastermindgame;

import fxcontrollers.GamePageController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private GamePageController gpc;
    private Stage stage;

    public MainApp() {
        super();
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        Scene scene2 = createGamePage();

        this.stage.setScene(scene2);

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

    private Scene createGamePage() throws Exception {
        // Instantiate the FXMLLoader
        FXMLLoader loader = new FXMLLoader();

        // Set the location of the fxml file in the FXMLLoader
        loader.setLocation(this.getClass().getResource("/fxml/GamePage.fxml"));

        // Parent is the base class for all nodes that have children in the
        // scene graph such as AnchorPane and most other containers
        Parent root = (TabPane) loader.load();

        gpc = loader.getController();
        Scene scene = new Scene(root);
        return scene;
    }


}
