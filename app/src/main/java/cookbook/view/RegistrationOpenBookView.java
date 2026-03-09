package cookbook.view;

import cookbook.controller.RegistrationController;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * This class is for the Registration Open Book UI.
 */
public class RegistrationOpenBookView {

  // Class fields.
  private BookAnimation animation;  // Book animation.
  private RegistrationController registrationController;  // RegistrationController object.
  private RegistrationView registrationView;  // RegistrationView object.
  private StackPane root;
  private ChatBox chatBox;

  /**
   * Constructor for the RegistrationOpenBookView class.
   *
   * @param root the StackPane root.
   */
  public RegistrationOpenBookView(StackPane root) {
    this.root = root;
    this.registrationView = new RegistrationView(root);
    this.animation = new BookAnimation(root);
    this.registrationController = new RegistrationController();
  }

  /**
   * This method creates UI with registration content.
   */
  public void createRegisterScreen() {

    // Load the background image (open book).
    Image openBookImage = new Image(RegistrationOpenBookView.class.getResourceAsStream(
        "/assets/mainMenu/centeredOpenBook.png"
    ));

    // ImageView for background.
    ImageView openBookView = createImageView(openBookImage, 422);

    // Add background to StackPane.
    root.getChildren().add(openBookView);

    // VBox that will contain registration content.
    VBox registerBox = new VBox(0);

    // Center VBox children.
    registerBox.setAlignment(Pos.CENTER);
    // Add padding so content is on right page of open book.
    registerBox.setPadding(new Insets(0, 0, 0, 244));

    // Load the registration button image.
    Image registerButtonImage = new Image(RegistrationOpenBookView.class.getResourceAsStream(
        "/assets/buttons/registerButton.png"
    ));
    // Load the active registration button image (button on hover).
    Image registerButtonHoverImage = new Image(RegistrationOpenBookView.class.getResourceAsStream(
        "/assets/buttons/registerButtonActive.png"
    ));
    // Load the registration button image.
    Image exitButtonImage = new Image(RegistrationOpenBookView.class.getResourceAsStream(
        "/assets/buttons/exitButton.png"
    ));
    // Load the active registration button image (button on hover).
    Image exitButtonHoverImage = new Image(RegistrationOpenBookView.class.getResourceAsStream(
        "/assets/buttons/exitButtonActive.png"
    ));

    // ImageView for register button.
    ImageView registerButtonView = createImageView(registerButtonImage, 25);
    // ImageView for register hover button.
    ImageView registerButtonHoverView = createImageView(registerButtonHoverImage, 25);

    // Register button.
    Button registerButton = createButton(registerButtonView, registerButtonHoverView);
    // Register button padding.
    registerButton.setPadding(new Insets(15, 0, 5, 0));

    // Imageview for exit button.
    ImageView exitButtonView = createImageView(exitButtonImage, 25);
    // ImageView for exit hover button.
    ImageView exitButtonHoverView = createImageView(exitButtonHoverImage, 25);

    // Exit button.
    Button exitButton = createButton(exitButtonView, exitButtonHoverView);

    // Load the textfield image.
    Image textfieldImage = new Image(RegistrationOpenBookView.class.getResourceAsStream(
        "/assets/mainMenu/centeredTextfield.png"
    ));

    // Create text fields.
    TextField fnameTextField = createTextField(25, 100);
    TextField lnameTextField = createTextField(25, 100);
    TextField usernameTextField = createTextField(25, 100);
    PasswordField passwordTextField = createPasswordField(25, 100);

    // Logic for pressing register button.
    registerButton.setOnAction(event -> {
      String fname = fnameTextField.getText();
      String lname = lnameTextField.getText();
      String username = usernameTextField.getText();
      String password = passwordTextField.getText();
      // Try to register new user with registration controller.
      registrationController.registerNewUser(fname, lname, username, password);
      // Clear text fields.
      fnameTextField.clear();
      lnameTextField.clear();
      usernameTextField.clear();
      passwordTextField.clear();
    });

    // Logic for pressing exit button.
    exitButton.setOnAction(event -> {
      // Close book animation.
      root.getChildren().clear();
      animation.closeAnimationBook("/assets/mainMenu/registrationAnimationBook");
      PauseTransition pause = new PauseTransition(Duration.seconds(3.5));
      // Go to closed registration book after animation is finished.
      pause.setOnFinished(e -> {
        root.getChildren().clear();
        registrationView.showRegistrationScreen();
      });
      pause.play();
    });

    // GridPane for first name with imageview, textfield and label.
    GridPane fnameGridPane = createTextFieldGridPane(textfieldImage,
                                                     fnameTextField,
                                                     "First name");
    // GridPane for last name with imageview, textfield and label.
    GridPane lnameGridPane = createTextFieldGridPane(textfieldImage,
                                                     lnameTextField,
                                                     "Last name");
    // GridPane for username with imageview, textfield and label.
    GridPane usernameGridPane = createTextFieldGridPane(textfieldImage,
                                                        usernameTextField,
                                                        "Username");
    // GridPane for password with imageview, textfield and label.
    GridPane passwordGridPane = createTextFieldGridPane(textfieldImage,
                                                        passwordTextField,
                                                        "Password");

    // Add content to VBox.
    registerBox.getChildren().addAll(
        fnameGridPane,
        lnameGridPane,
        usernameGridPane,
        passwordGridPane,
        registerButton,
        exitButton
    );

    // Finally add VBox to StackPane.
    root.getChildren().add(registerBox);
    Buttons MainButtons = new Buttons(root);

    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));

    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));
  }

  /**
   * This method is used for setting up a textfield GridPane.
   *
   * @param textfieldImage the textfield Image.
   * @param labelText the label text.
   * @return a textfield GridPane.
   */
  private GridPane createTextFieldGridPane(Image textfieldImage,
                                           TextField textField,
                                           String labelText) {

    // Create GridPane.
    GridPane gridPane = new GridPane();

    // Center GridPane.
    gridPane.setAlignment(Pos.CENTER);

    // Create Label.
    Label label = createLabel(labelText);

    // Create ImageView.
    ImageView imageView = createImageView(textfieldImage, 40);

    // Set alignment for content within grid cells.
    GridPane.setHalignment(label, HPos.CENTER);
    GridPane.setHalignment(imageView, HPos.CENTER);
    GridPane.setHalignment(textField, HPos.CENTER);

    // Add the content to grid.
    gridPane.add(label, 0, 0);
    gridPane.add(imageView, 0, 1);
    gridPane.add(textField, 0, 1);

    return gridPane;
  }

  /**
   * This method creates a TextField with presets.
   *
   * @param prefHeight the preferred height to set.
   * @param maxWidth the max width to set.
   * @return a TextField with presets.
   */
  private TextField createTextField(double prefHeight, double maxWidth) {
    TextField textField = new TextField();
    textField.setPrefHeight(prefHeight);
    textField.setMaxWidth(maxWidth);
    // CSS styling.
    textField.getStyleClass().add("registrationTextField");
    // Font.
    Font font = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), 12);
    textField.setFont(font);
    return textField;
  }

  /**
   * This method creates a PasswordField with presets.
   *
   * @param prefHeight the preferred height to set.
   * @param maxWidth the max width to set.
   * @return a TextField with presets.
   */
  private PasswordField createPasswordField(double prefHeight, double maxWidth) {
    PasswordField passwordField = new PasswordField();
    passwordField.setPrefHeight(prefHeight);
    passwordField.setMaxWidth(maxWidth);
    // CSS styling.
    passwordField.getStyleClass().add("registrationTextField");
    passwordField.setStyle("-fx-font-size: 12px;");
    return passwordField;
  }

  /**
   * This method creates a label with presets.
   *
   * @param labelText the label text.
   * @return a label with presets.
   */
  private Label createLabel(String labelText) {
    Label label = new Label(labelText);
    // Font.
    Font font = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), 14);
    label.setFont(font);
    return label;
  }

  /**
   * This method creates an ImageView.
   *
   * @param image the image to use.
   * @return an ImageView with presets.
   */
  private ImageView createImageView(Image image, double height) {
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(height);
    imageView.setPreserveRatio(true);
    return imageView;
  }

  /**
   * This method creates a Button with hover property.
   *
   * @param buttonView the buttonView.
   * @param hoverButtonView the hover buttonView.
   * @return a Button with hover property.
   */
  private Button createButton(ImageView buttonView, ImageView hoverButtonView) {
    // Create button.
    Button button = new Button();
    button.setGraphic(buttonView);
    button.getStyleClass().add("clearButton");

    // Logic for hovering button.
    button.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        button.setGraphic(hoverButtonView);
      } else {
        button.setGraphic(buttonView);
      }
    });

    return button;
  }

  private void handleSettingButtonInLoginScene(ActionEvent event) {
    System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("RegistrationOpenBook");
    root.getChildren().add(chatBox);
  }

}
