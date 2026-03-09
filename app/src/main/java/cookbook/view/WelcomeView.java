package cookbook.view;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * This class is for the welcome scene UI.
*/
public class WelcomeView {

  // Attributes.
  private StackPane root;
  private Buttons MainButtons;
  private ChatBox chatBox;

  /**
   * Constructor.
   */
  public  WelcomeView() {
    this.root = new StackPane();  // The StackPane root.
    this.MainButtons = new Buttons(root);
  }

  /**
   * This method will creat the welcome scene box.
   */
  public Parent createWelcomeScreen() {

    //Button
    Image buttonImage = new Image(WelcomeView
        .class.getResourceAsStream("/assets/logo.png"));
    ImageView buttonImageView = new ImageView(buttonImage);

    //Active Button (button on hover)
    Image buttonHoverImage = new Image(WelcomeView
            .class.getResourceAsStream("/assets/logoActive.png"));
    ImageView buttonHoverImageView = new ImageView(buttonHoverImage);

    //Regular Button Style
    buttonImageView.setFitHeight(350);
    buttonImageView.setPreserveRatio(true);

    //Button on hover Style
    buttonHoverImageView.setFitHeight(355);
    buttonHoverImageView.setPreserveRatio(true);


    //Combine together
    Button enterButton = new Button();
    enterButton.setGraphic(buttonImageView);
    enterButton.getStyleClass().add("clearButton");

    StackPane.setMargin(enterButton, new Insets(0, 0, 0, 0));

    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInScene1(event));

    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInScene1(event));

    //Logic behind it (NON)
    // Switch scenes to login.
    LoginView loginView = new LoginView(root);
    enterButton.setOnAction(event -> {
      root.getChildren().clear();
      loginView.showLoginScene();
    });

    //Hover logic
    enterButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        enterButton.setGraphic(buttonHoverImageView);
      } else {
        enterButton.setGraphic(buttonImageView);
      }
    });

    root.getChildren().addAll(enterButton);
    return root;

  }
  private void handleSettingButtonInScene1(ActionEvent event) {
    System.out.println("Setting");
  }

  private void handleInfoButtonInScene1(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("firstDialogue");
    root.getChildren().add(chatBox);
  }

}
