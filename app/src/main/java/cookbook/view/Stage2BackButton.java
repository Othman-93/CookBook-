package cookbook.view;

import cookbook.controller.ViewController;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Stage2BackButton {

  // Class fields.
  private StackPane root;
  private ViewController Stage2;

  /**
   * Constructor for the UltimateCookbook class.
   *
   * @param root the StackPane root.
   */
  public Stage2BackButton(StackPane root, ViewController stage2) {
    this.root = root;
    this.Stage2 = stage2;
  }


    public void startingButton(String username) {
      // Load the background image (open book).
      Image arrowDown = new Image(MarketView.class.getResourceAsStream(
          "/assets/stageTwo/backButton.png"
      ));
      //implamenting style to image
      ImageView backButtonView =  new ImageView(arrowDown);
      backButtonView.setFitHeight(30);
      backButtonView.setPreserveRatio(true);
      
      //creating button
      Button backtButton = new Button();
      backtButton.setGraphic(backButtonView);
      backtButton.getStyleClass().add("clearButton");

      // Initial translation positions based on initial margins
      backtButton.setTranslateX(0);

      //adding button to the scene
      root.getChildren().add(backtButton);

      //Animation for the button
      TranslateTransition backtButtonTransition = new TranslateTransition(Duration.seconds(1), backtButton);
      backtButtonTransition.setFromY(250);
      backtButtonTransition.setToY(235);
      backtButtonTransition.setInterpolator(Interpolator.EASE_IN);
      backtButtonTransition.setAutoReverse(true);
      backtButtonTransition.setCycleCount(TranslateTransition.INDEFINITE);

      // Start the basket animation
      backtButtonTransition.play();

      backtButton.setOnAction(event -> {
        //deletes a button 
        Parent parent = backtButton.getParent();
            ((Pane) parent).getChildren().remove(backtButton);

        Stage2.StageTwo(username);
        
      });
      

    }
}
