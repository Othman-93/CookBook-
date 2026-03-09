package cookbook.view;

import cookbook.model.UserDataHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * user settings menu.
 */
public class ProfileSettingView {

  // Attributes.
  private HBox gridContainer;
  private String username;
  private UserDataHandler userDataHandler;

  /**
   * The constructor.
   *
   * @param gridContainer gridContainer that we add settings grid to.
   */
  public ProfileSettingView(HBox gridContainer, String username, UserDataHandler userDataHandler) {
    this.gridContainer = gridContainer;
    this.username = username;
    this.userDataHandler = userDataHandler;
  }

  /**
   * Create profile ui.
   */
  public void showProfileSettingScene() {

    // GridPane for all the settings.
    GridPane settingsGrid = new GridPane();

    // Center GridPane.
    settingsGrid.setAlignment(Pos.CENTER);

    // Load image for text field.
    Image textFieldImage = new Image(ProfileSettingView.class.getResourceAsStream(
          "/assets/mainMenu/centeredTextfield.png"
          ));

    // Pixel font.
    Font font = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), 12);

    // Create label and text field for first name.
    Label firstNamePrompt = new Label("First Name");
    firstNamePrompt.setFont(font);
    TextField firstNameField = createTextField();
    ImageView firstNameImageView = createImageView(textFieldImage);

    // Add first name text field objects to GridPane.
    settingsGrid.add(firstNamePrompt, 0, 0);
    settingsGrid.add(firstNameImageView, 0, 1);
    settingsGrid.add(firstNameField, 0, 1);

    // Create label and text field for last name.
    Label lastNamePrompt = new Label("Last Name");
    lastNamePrompt.setFont(font);
    TextField lastNameField = createTextField();
    ImageView lastNameImageView = createImageView(textFieldImage);

    // Add last name text field objects to GridPane.
    settingsGrid.add(lastNamePrompt, 0, 2);
    settingsGrid.add(lastNameImageView, 0, 3);
    settingsGrid.add(lastNameField, 0, 3);


    // create label and field for password
    Label passwordPrompt = new Label("Password");
    passwordPrompt.setFont(font);
    PasswordField passwordField = createPasswordField();
    ImageView passwordImageView = createImageView(textFieldImage);

    // add password field object to gridpane
    settingsGrid.add(passwordPrompt, 0, 4);
    settingsGrid.add(passwordImageView, 0, 5);
    settingsGrid.add(passwordField, 0, 5);


    // Load the login button image
    Image saveButtonImage = new Image(ProfileSettingView.class.getResourceAsStream(
          "/assets/buttons/saveButton.png"
          ));
    ImageView saveButtonImageView = new ImageView(saveButtonImage);
    saveButtonImageView.setFitHeight(25);
    saveButtonImageView.setPreserveRatio(true);

    // Active Button (button on hover)
    Image buttonHoverImage = new Image(ProfileSettingView.class.getResourceAsStream(
        "/assets/buttons/saveButtonActive.png"));
    ImageView buttonHoverImageView = new ImageView(buttonHoverImage);
    buttonHoverImageView.setFitHeight(25);
    buttonHoverImageView.setPreserveRatio(true);

    // create the save button
    Button saveButton = new Button();
    saveButton.setGraphic(saveButtonImageView);
    saveButton.getStyleClass().add("clearButton");

    // Add save button to GridPane.
    settingsGrid.add(saveButton, 0, 6);
    // Save button padding.
    saveButton.setPadding(new Insets(30, 0, 0, 0));

    // Set horizontal alignment of all GridPane children to CENTER.
    for (javafx.scene.Node child : settingsGrid.getChildren()) {
      GridPane.setHalignment(child, HPos.CENTER);
    }

    // save button logic on click.
    saveButton.setOnAction(event -> {


      String newFirstName = firstNameField.getText();
      String newLastName = lastNameField.getText();
      String newPassword = passwordField.getText();

      boolean firstNameUpdated = userDataHandler.updateFirstName(username, newFirstName);
      boolean lastNameUpdated = userDataHandler.updateLastName(username, newLastName);
      boolean passwordUpdated = userDataHandler.updatePassword(username, newPassword);

      if (firstNameUpdated && lastNameUpdated && passwordUpdated) {
        // Show success alert
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Changes saved successfully!");
        successAlert.showAndWait();
      } else {
        // Show error alert
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText("Failed to save changes.");
        errorAlert.showAndWait();
      }
    });

    // Logic for hovering save button.
    saveButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        saveButton.setGraphic(buttonHoverImageView);
      } else {
        saveButton.setGraphic(saveButtonImageView);
      }
    });

    // Add spacing between HBox children.
    gridContainer.setSpacing(45);

    // Add settings GridPane to gridContainer.
    gridContainer.getChildren().add(settingsGrid);
  }


  /**
   * Creates text field with presets.
   *
   * @return text field.
   */
  private TextField createTextField() {
    TextField textField = new TextField();
    textField.setPrefHeight(25);
    textField.setMaxWidth(100);
    Font font = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), 12);
    textField.setFont(font);
    textField.getStyleClass().add("registrationTextField");
    return textField;
  }

  /**
   * Creates image view with presets.
   *
   * @param image image to use.
   * @return image view.
   */
  private ImageView createImageView(Image image) {
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(40);
    imageView.setPreserveRatio(true);
    return imageView;
  }

  /**
   * creates fields for passwords.
   */
  
  private PasswordField createPasswordField() {
    PasswordField passwordField = new PasswordField();
    passwordField.setPrefHeight(25);
    passwordField.setMaxWidth(100);
    passwordField.getStyleClass().add("registrationTextField");
    return passwordField;
  }

}