package cookbook;

import cookbook.controller.ViewController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The application main class.
 */
public class App extends Application {
  private StackPane root = new StackPane();

  /**
   * Stage one, Title screen, Login, Registration.
   *
   * @Override
   *
   */

  public void start(Stage primaryStage) throws Exception {
    // Create the main controller and pass the root
    ViewController viewController = new ViewController(root);

    //String username = "user2";
    //viewController.StageTwo(username);
    


    // Let ViewController handle the initial setup
     viewController.stageOneView(primaryStage);

    // viewController.stageOneView(primaryStage);

    //viewController.StageTwo("user1");
    //viewController.StageTwoLibrary("user1");

    // Set up the scene
    Scene scene = new Scene(root, 800, 600);
    scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setTitle("Cook Book by Fahrenheit 451");
    primaryStage.setResizable(false);

    // Adding icon
    Image icon = new Image("./assets/icon.png");
    primaryStage.getIcons().add(icon);

    primaryStage.show();
  }
}
