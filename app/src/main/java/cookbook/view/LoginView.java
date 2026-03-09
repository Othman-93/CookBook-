package cookbook.view;

import cookbook.controller.LoginController;
import cookbook.model.DatabaseUtilProviderImpl;
import cookbook.model.UserDataHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


/**
 * this class is for the login scene UI.
 *
 */
public class LoginView {

  // Attributes.
  private StackPane root;
  private LoginController loginController;
  private ChatBox chatBox;
  private Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 15);

  /**
   * Constructor.
   *
   * @param root the StackPane root.
   */
  public LoginView(StackPane root) {
    DatabaseUtilProviderImpl databaseUtilProvider = new DatabaseUtilProviderImpl();
    UserDataHandler userDataHandler = new UserDataHandler();
    this.loginController = new LoginController(userDataHandler, databaseUtilProvider);
    this.root = root;
  }

  /**
   * create ui.
   */
  public void showLoginScene() {

    root.setAlignment(Pos.CENTER);

    // Load the image
    Image loginImage = new Image("/assets/mainMenu/loginBook.png");
    ImageView loginImageView = new ImageView(loginImage);
    loginImageView.setFitHeight(350);
    loginImageView.setPreserveRatio(true);

    VBox loginBox = new VBox(5);
    loginBox.setAlignment(Pos.CENTER);
    loginBox.setPadding(new Insets(2, 0, 0, 30));


    // Create labels for the prompts
    Label usernamePrompt = new Label("Username");
    Label passwordPrompt = new Label("Password");

    // Create CSS style for bold prompts
    usernamePrompt.getStyleClass().add("boldPromptStyle");
    passwordPrompt.getStyleClass().add("boldPromptStyle");

    // Create text fields
    TextField usernameField = new TextField();
    VBox.setMargin(usernameField, new Insets(16, 0, 0, 0));
    usernameField.setFont(pixelFont);

    PasswordField passwordField = new PasswordField();
    VBox.setMargin(passwordField, new Insets(16, 0, 0, 0));
    usernameField.getStyleClass().add("fieldBox");
    passwordField.getStyleClass().add("fieldBox");
    usernameField.setMaxWidth(88);
    passwordField.setMaxWidth(90);

    //String to notife if your password or username correct
    Label notification = new Label();
    notification.setStyle("-fx-text-fill: #ffdd56; -fx-font-weight: bold;");
    VBox.setMargin(notification, new Insets(0, 0, 20, 0));

    // Set alignment of labels and fields in StackPanes
    StackPane.setAlignment(usernamePrompt, Pos.TOP_LEFT);
    StackPane.setAlignment(passwordPrompt, Pos.TOP_LEFT);
    StackPane.setAlignment(notification, Pos.TOP_LEFT);
    StackPane.setAlignment(usernameField, Pos.BOTTOM_LEFT);
    StackPane.setAlignment(passwordField, Pos.BOTTOM_LEFT);
    StackPane.setAlignment(notification, Pos.BOTTOM_LEFT);

    // Add the username and password fields to the login box
    loginBox.getChildren().addAll(usernameField, passwordField, notification);

    // Load the login button image
    Image buttonImage = new Image(LoginView.class.getResourceAsStream(
        "/assets/buttons/buttonToLogin.png"));
    ImageView buttonImageView = new ImageView(buttonImage);
    buttonImageView.setFitHeight(25);
    buttonImageView.setPreserveRatio(true);

    //Active Button (button on hover)
    Image buttonHoverImage = new Image(LoginView.class.getResourceAsStream(
        "/assets/buttons/buttonToLoginActive.png"));
    ImageView buttonHoverImageView = new ImageView(buttonHoverImage);
    buttonHoverImageView.setFitHeight(24);
    buttonHoverImageView.setPreserveRatio(true);

    Button loginButton = new Button();
    loginButton.setGraphic(buttonImageView);
    loginButton.getStyleClass().add("clearButton");


    loginButton.setOnAction(event -> {
      String username = usernameField.getText();
      String password = passwordField.getText();
      loginController.manageLogin(username, password, root, notification);
    });
    //Hover logic
    loginButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        loginButton.setGraphic(buttonHoverImageView);
      } else {
        loginButton.setGraphic(buttonImageView);
      }
    });

    //event listiner, press Enter to enter
    passwordField.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        loginController.manageLogin(username, password, root, notification);
      }
    });

    // Add the login button to the root
    StackPane.setMargin(loginButton, new Insets(150, 0, 0, 24));

    // Add Arrow
    Image arrowImage = new Image("/assets/buttons/arrowRight.png");
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
    StackPane.setMargin(arrowButton, new Insets(0, 0, 0, 350));

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
    Tooltip.Tooltip(arrowButton, "tooltipRegistration.png", Side.RIGHT, 100); //(Button, a picture, and what side to put tooltip, Tooltip size)

    // Arrow button logic.
    // Go to registration scene.
    RegistrationView registrationView = new RegistrationView(root);
    arrowButton.setOnAction(event -> {
      root.getChildren().clear();
      registrationView.showRegistrationScreen();
    });

    // Set position of arrow button
    StackPane.setMargin(arrowButton, new Insets(0, 0, 0, 350));    

    // Add the image and login box to the login layout
    root.getChildren().addAll(loginImageView, loginBox, loginButton, arrowButton);

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
    chatBox.setupChatBox("Login");
    root.getChildren().add(chatBox);
  }
}
