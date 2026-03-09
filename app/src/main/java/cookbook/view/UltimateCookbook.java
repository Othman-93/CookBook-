package cookbook.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
public class UltimateCookbook {

  // Class fields.
  private BookAnimation animation;  // Book animation.
  private StackPane root;
  private ChatBox chatBox;
  private String username;

  /**
   * Constructor for the UltimateCookbook class.
   *
   * @param root the StackPane root.
   */
  public UltimateCookbook(StackPane root, String username) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.username = username;
  }

  /**
   * This method creates a animation for the book to apear.
   */

  public void startingButton() {
    // Load the background image (open book).
    Image BookImage = new Image(UltimateCookbook.class.getResourceAsStream(
        "/assets/stageTwo/library/cookbookAnimation/frame1.png"
    ));
    //implamenting style to image
    ImageView bookView =  new ImageView(BookImage);
    bookView.setFitHeight(120);
    bookView.setPreserveRatio(true);

    //creating button
    Button cookButton = new Button();
    cookButton.setGraphic(bookView);
    cookButton.getStyleClass().add("clearButton");

    // Initial translation positions based on initial margins
    cookButton.setTranslateX(-27);
    cookButton.setTranslateY(79);

    //adding button to the scene
    root.getChildren().add(cookButton);

    Buttons MainButtons = new Buttons(root);

    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));

    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));

    //Start postion og the book
    Timeline timeline = new Timeline();
    KeyValue kvStartX = new KeyValue(cookButton.translateXProperty(), -27);
    KeyValue kvStartY = new KeyValue(cookButton.translateYProperty(), 79);

    //after animation position of the book
    KeyValue kvEndX = new KeyValue(cookButton.translateXProperty(), 0);
    KeyValue kvEndY = new KeyValue(cookButton.translateYProperty(), -18);

    //the size of the book (the zoom in animtion)
    KeyValue kvStart = new KeyValue(bookView.fitHeightProperty(), 120);
    KeyValue kvEnd = new KeyValue(bookView.fitHeightProperty(), 522);

    //the main keyframe for timing the things above
    KeyFrame kfStart = new KeyFrame(Duration.ZERO, kvStartX, kvStartY, kvStart);
    KeyFrame kfEnd = new KeyFrame(Duration.seconds(1), kvEndX, kvEndY, kvEnd);

    timeline.getKeyFrames().addAll(kfStart, kfEnd);
    Timeline appearAfterOpen = new Timeline();
    appearAfterOpen.getKeyFrames().add(
        new KeyFrame(Duration.seconds(2), event -> {
          //This thing
          BrowseRecipesView browseRecipesView = new BrowseRecipesView(root, username);
          browseRecipesView.createBrowseRecipesScreen();
          // RecipeView recipeView = new RecipeView(root);
          // recipeView.displayRecipeView();
          // CreateRecipeView createRecipeView = new CreateRecipeView(root);
          // createRecipeView.displayCreateRecipeView();
      })
    );
    //execute after zoom-in
    timeline.getKeyFrames().add(
        new KeyFrame(Duration.seconds(1), event -> {

            //Removing button
            Parent parent = cookButton.getParent();
            ((Pane) parent).getChildren().remove(cookButton); // need more work on it!

            //Executing BookAnimation class and method in it.
            animation.openAnimationBook("/assets/stageTwo/library/cookbookAnimation", false, 17, ".png", 0, 522);
            appearAfterOpen.play();
        })
);
    //that's a action of the button
    cookButton.setOnAction(event -> {
      timeline.play(); //executes a zoom-in
    });
}
  private void handleSettingButtonInLoginScene(ActionEvent event) {
     System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("StageTwoLibrary");
    root.getChildren().add(chatBox);
  }

}
