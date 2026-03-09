package cookbook.view;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


/**
 * This class is for the registration screen UI.
 *
 */
public class RegistrationView {

  // Attributes.
  private StackPane root;
  private ChatBox chatBox;

  /**
   * Constructor.
   *
   * @param root the StackPane root.
   */
  public RegistrationView(StackPane root) {
    this.root = root;
  }

  /**
  * create UI method.
  */
  public void showRegistrationScreen() {

    // Load the image
    Image registrationImage = new Image("/assets/mainMenu/registrationBook.png");
    ImageView registrationImageView = new ImageView(registrationImage);
    registrationImageView.setFitHeight(350);
    registrationImageView.setPreserveRatio(true);

    // Load the registration button image
    Image buttonImage = new Image(RegistrationView.class.getResourceAsStream(
        "/assets/buttons/buttonToRegister.png"));
    ImageView buttonImageView = new ImageView(buttonImage);
    buttonImageView.setFitHeight(25);
    buttonImageView.setPreserveRatio(true);

    //Active Button (button on hover)
    Image buttonHoverImage = new Image(WelcomeView.class.getResourceAsStream(
                                  "/assets/buttons/buttonToRegisterActive.png"
                                      ));
    ImageView buttonHoverImageView = new ImageView(buttonHoverImage);
    buttonHoverImageView.setFitHeight(26);
    buttonHoverImageView.setPreserveRatio(true);

    Button registrationButton = new Button();
    registrationButton.setGraphic(buttonImageView);
    registrationButton.getStyleClass().add("clearButton");


    // Add the registation button to the root
    StackPane.setMargin(registrationButton, new Insets(200, 0, 0, 0));

    //Button Logic
    BookAnimation registrationAnimation = new BookAnimation(root);
    registrationButton.setOnAction(event -> {
      root.getChildren().clear();
      registrationAnimation.openAnimationBook("/assets/mainMenu/registrationAnimationBook", true, 17, ".png", 0, 422);
      // Wait for animation to finish then add RegistrationOpenBookView VBox.
      PauseTransition pause = new PauseTransition(Duration.seconds(3.7));
      RegistrationOpenBookView registrationOpenBookView = new RegistrationOpenBookView(root);
      pause.setOnFinished(e -> {
        root.getChildren().clear();
        registrationOpenBookView.createRegisterScreen();
      });
      pause.play();
    });

    //Logic for hovering
    registrationButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        registrationButton.setGraphic(buttonHoverImageView);
      } else {
        registrationButton.setGraphic(buttonImageView);
      }
    });

    // Add ArrowButton
    Image arrowImage = new Image("/assets/buttons/arrowLeft.png");
    ImageView arrowImageView = new ImageView(arrowImage);
    arrowImageView.setFitHeight(50);
    arrowImageView.setPreserveRatio(true);

    ImageView arrowImageViewHovered = new ImageView(arrowImage);
    arrowImageViewHovered.setFitHeight(55);
    arrowImageViewHovered.setPreserveRatio(true);

    // Create button with arrow image
    Button arrowButton = new Button();
    arrowButton.setGraphic(arrowImageView);
    arrowButton.getStyleClass().add("arrowButton");

    // Set position of arrow button
    StackPane.setMargin(arrowButton, new Insets(0, 350, 0, 0));

    //Logic for hovering arrowButton
    arrowButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        arrowButton.setGraphic(arrowImageViewHovered);
      } else {
        arrowButton.setGraphic(arrowImageView);
      }
    });

    //Initiazing Tooltip! 
    SetupTooltip Tooltip = new SetupTooltip();
    Tooltip.Tooltip(arrowButton, "tooltipLogin.png", Side.LEFT, 80); //(Button, a picture, and what side to put tooltip, Size of the tool tip)

    //Tranfer from another book
    LoginView mainMenuView = new LoginView(root);
    arrowButton.setOnAction(event -> {
      root.getChildren().clear();
      mainMenuView.showLoginScene();
    });

    // Add the image and button to the screen
    root.getChildren().addAll(registrationImageView, registrationButton, arrowButton);
    Buttons MainButtons = new Buttons(root);

    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));

    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));

  }
    private void handleSettingButtonInLoginScene(ActionEvent event) {
     System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("Registration");
    root.getChildren().add(chatBox);
  }
}

