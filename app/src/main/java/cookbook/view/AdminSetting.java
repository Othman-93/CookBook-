package cookbook.view;

import cookbook.model.AdminDataHandler;
import cookbook.model.UserDataHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/** Ui for when the book opens. */
public class AdminSetting {

  // Attributes.
  private StackPane root;
  private Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 15);
  private AdminDataHandler adminDataHandler;
  private String adminUsername;


  /**
   * The Constructor.
   *
   * @param root the StackPane root.
   */
  public AdminSetting(StackPane root, String adminUsername, AdminDataHandler adminDataHandler) {
    this.root = root;
    this.adminDataHandler = adminDataHandler;
    this.adminUsername = adminUsername;
  }


  // create a ui for scene after login book opens.


  /** Create a ui for scene in the admin user settings. */
  public void showAdminSetting() {

    String firstName = adminDataHandler.getAdminFirstName(adminUsername);
    String lastName = adminDataHandler.getAdminLastName(adminUsername);


    Image adminSignFirstName = new Image(AdminUserSetting.class.getResourceAsStream(
        "/assets/mainMenu/adminPanel/adminSettingFirstName.png"
    ));
    ImageView adminSignFirstNameImage = new ImageView(adminSignFirstName);
    adminSignFirstNameImage.setFitHeight(60);
    adminSignFirstNameImage.setPreserveRatio(true);
    StackPane.setMargin(adminSignFirstNameImage, new Insets(-220, -260, 0, 0));

    //TextField for First name
    //it's a example change it from fetched from database
    TextField adminTextFieldFirstName = new TextField(firstName);
    adminTextFieldFirstName.setMaxWidth(170);
    adminTextFieldFirstName.getStyleClass().add("fieldBox");
    StackPane.setMargin(adminTextFieldFirstName, new Insets(-190, -260, 0, 0));

    Image adminSignLastName = new Image(AdminUserSetting.class.getResourceAsStream(
          "/assets/mainMenu/adminPanel/adminSettingLastName.png"));

    ImageView adminSignLastNameImage = new ImageView(adminSignLastName);
    adminSignLastNameImage.setFitHeight(60);
    adminSignLastNameImage.setPreserveRatio(true);
    StackPane.setMargin(adminSignLastNameImage, new Insets(-80, -260, 0, 0));

    //Textfield for Last Name
    //Again as a example change it from fetched from database
    TextField adminTextFieldLastName = new TextField(lastName);
    adminTextFieldLastName.setMaxWidth(170);
    adminTextFieldLastName.getStyleClass().add("fieldBox");
    StackPane.setMargin(adminTextFieldLastName, new Insets(-50, -260, 0, 0));

    Image adminSignPassword = new Image(AdminUserSetting.class.getResourceAsStream(
        "/assets/mainMenu/adminPanel/adminSettingPassword.png"
        ));

    ImageView adminSignPasswordImage = new ImageView(adminSignPassword);
    adminSignPasswordImage.setFitHeight(60);
    adminSignPasswordImage.setPreserveRatio(true);

    StackPane.setMargin(adminSignPasswordImage, new Insets(60, -260, 0, 0));

    //TextField for Password
    PasswordField adminTextFieldPassword = new PasswordField();
    adminTextFieldPassword.setMaxWidth(170);
    adminTextFieldPassword.getStyleClass().add("fieldBox");
    StackPane.setMargin(adminTextFieldPassword, new Insets(90, -260, 0, 0));

    //Images
    root.getChildren().addAll(adminSignFirstNameImage,
                            adminSignLastNameImage,
                            adminSignPasswordImage,
                            adminTextFieldFirstName,
                            adminTextFieldLastName,
                            adminTextFieldPassword);
    applyButton(
              adminTextFieldFirstName,
              adminTextFieldLastName,
              adminTextFieldPassword);
  }


  private void applyButton(
                         TextField adminTextFieldFirstName,
                         TextField adminTextFieldLastName,
                         PasswordField adminTextFieldPassword) {

    // Load button image for continue, settings and exit
    Image applyButtonImage = new Image(AdminUserSetting.class.getResourceAsStream(
        "/assets/buttons/admin/adminApplyButton.png"));
    ImageView applyButtonImageView = new ImageView(applyButtonImage);
    applyButtonImageView.setFitHeight(30);
    applyButtonImageView.setPreserveRatio(true);

    // Active continue Button (button on hover)
    Image applyHoverImage = new Image(AdminUserSetting.class.getResourceAsStream(
        "/assets/buttons/admin/adminApplyButtonActive.png"));
    ImageView applyHoverImageView = new ImageView(applyHoverImage);
    applyHoverImageView.setFitHeight(29);
    applyHoverImageView.setPreserveRatio(true);

    Button applyButton = new Button();
    applyButton.setGraphic(applyButtonImageView);
    applyButton.getStyleClass().add("clearButton");

    //Margins
    StackPane.setMargin(applyButton, new Insets(250, -260, 0, 0));

    //Hover logic
    applyButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        applyButton.setGraphic(applyHoverImageView);
      } else {
        applyButton.setGraphic(applyButtonImageView);
      }
    });

    // event handlers
    applyButton.setOnAction(event -> {


      String newFirstName = adminTextFieldFirstName.getText();
      String newLastName = adminTextFieldLastName.getText();
      String newPassword = adminTextFieldPassword.getText();

      boolean firstNameUpdated = adminDataHandler.updateAdminFirstName(adminUsername, newFirstName);
      boolean lastNameUpdated = adminDataHandler.updateAdminLastName(adminUsername, newLastName);
      boolean passwordUpdated = adminDataHandler.updateAdminPassword(adminUsername, newPassword);

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

      root.getChildren().clear();
      AdminOpenBookView adminOpenBook = new AdminOpenBookView(root, adminUsername);
      adminOpenBook.showOpenBookScene();
    });

    //add to the scene
    root.getChildren().add(applyButton);
  }
}
