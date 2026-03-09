package cookbook.view;

import java.time.LocalDate;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * This class is for the UltimateCookbook Animation.
 */
public class MarketView {

  // Class fields.
  private BookAnimation animation;  // Book animation.
  private StackPane root;
  private MarketListView marketListView;
  private final String username;
  private ChatBox chatBox;

  /**
   * Constructor for the UltimateCookbook class.
   *
   * @param root the StackPane root.
   */
  public MarketView(StackPane root, String username) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.marketListView = new MarketListView(root, username);
    this.username = username;
  }

  /**
   * This method creates a animation for the book to apear.
   */

  public void startingButton(String username) {
    // Load the background image (open book).
    Image basket = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/market/basket.png"
    ));
    //implamenting style to image
    ImageView basketView =  new ImageView(basket);
    basketView.setFitHeight(100);
    basketView.setPreserveRatio(true);
    
    //creating button
    Button basketButton = new Button();
    basketButton.setGraphic(basketView);
    basketButton.getStyleClass().add("clearButton");

    // Initial translation positions based on initial margins
    basketButton.setTranslateX(160);

    //adding button to the scene
    root.getChildren().add(basketButton);

    //Animation for the button
    TranslateTransition basketTransition = new TranslateTransition(Duration.seconds(1), 
        basketButton);
    basketTransition.setFromY(140);
    basketTransition.setToY(125);
    basketTransition.setInterpolator(Interpolator.EASE_IN);
    basketTransition.setAutoReverse(true);
    basketTransition.setCycleCount(TranslateTransition.INDEFINITE);

    // Start the basket animation
    basketTransition.play();

    //Preperation for the book to be deploide
    Image noteImage = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/market/noteAnimation/frame1.gif"
    ));

    //implamenting style to image
    ImageView noteView =  new ImageView(noteImage);
    noteView.setFitHeight(1);
    noteView.setPreserveRatio(true);
    
    root.getChildren().add(noteView);

    //inisiating a item board from the market
    Image board = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/market/sign.png"
    ));
    //implamenting style to image
    ImageView boardView =  new ImageView(board);
    boardView.setFitHeight(422);
    boardView.setPreserveRatio(true);
    boardView.setOpacity(0);

    //position of the board
    boardView.setTranslateX(-160);
    boardView.setTranslateY(0);

    root.getChildren().add(boardView);

    //MainButtons for setting and Information
    mainButtonInisiolizer();

    //Fading in the item board
    FadeTransition baordFadeTransition = new FadeTransition(Duration.seconds(2), boardView);
    baordFadeTransition.setFromValue(0.0);
    baordFadeTransition.setToValue(1.0);
    baordFadeTransition.setCycleCount(1);

    //Start postion og the book
    Timeline timeline = new Timeline();
    KeyValue kvStartX = new KeyValue(noteView.translateXProperty(), 160);
    KeyValue kvStartY = new KeyValue(noteView.translateYProperty(), 145);
    
    //after animation position of the book
    KeyValue kvEndX = new KeyValue(noteView.translateXProperty(), 0);
    KeyValue kvEndY = new KeyValue(noteView.translateYProperty(), -18);

    //the size of the book (the zoom in animtion)
    KeyValue kvStart = new KeyValue(noteView.fitHeightProperty(), 0);
    KeyValue kvEnd = new KeyValue(noteView.fitHeightProperty(), 422);
    
    //the main keyframe for timing the things above
    KeyFrame kfStart = new KeyFrame(Duration.ZERO, kvStartX, kvStartY, kvStart);
    KeyFrame kfEnd = new KeyFrame(Duration.seconds(1), kvEndX, kvEndY, kvEnd);

    timeline.getKeyFrames().addAll(kfStart, kfEnd);
    //execute after zoom-in
    timeline.getKeyFrames().add(
        new KeyFrame(Duration.seconds(0.99), event -> {

          //Removing button
          Parent parent = noteView.getParent();
          ((Pane) parent).getChildren().remove(noteView);

            //Executing BookAnimation class and method in it.
            animation.openAnimationBook("/assets/stageTwo/market/noteAnimation", false, 24, ".gif", 0, 422);

          // Start the fade-in board transition
          baordFadeTransition.play();

          //stop button levitation animation
          basketTransition.stop();

          // Initialize the shopping list
          // with weekly dinner list ingredients for a specific user and date
          marketListView.initializeWithWeeklyIngredients(username);


          // Show ingredient options after all elements are added
          marketListView.setupMarketScene();

        })
    );
    //that's a action of the button
    basketButton.setOnAction(event -> {
      timeline.play(); //executes a zoom-in
    });
  }

  private void handleSettingButtonInLoginScene(ActionEvent event) {
    System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("StageTwoMarket");
    root.getChildren().add(chatBox);
  }
  private void mainButtonInisiolizer(){
    Buttons MainButtons = new Buttons(root);
    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));
    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));

  }

}
