package cookbook.view;

import cookbook.controller.UserController;
import cookbook.model.AdminDataHandler;
import cookbook.model.User;
import cookbook.model.UserDataHandler;

import java.util.Optional;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/** Ui for when the book opens. */
public class AdminUserSetting {

  // Attributes.
  private StackPane root;
  private AdminOpenBookView adminOpenBook;
  private BookAnimation animation;
  private Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 15);
  private CheckBox adminCheckbox;
  private CheckBox meatEaterCheckbox;
  private CheckBox vegetarianCheckbox;
  private CheckBox veganCheckbox;
  private UserDataHandler userDataHandler;
  private AdminDataHandler adminDataHandler;
  private String adminUsername;
  private ChatBox chatBox;

  /**
   * Constructor.
   *
   * @param root the StackPane root.
   */
  public AdminUserSetting(StackPane root, String adminUsername,
                          UserDataHandler userDataHandler, AdminDataHandler adminDataHandler) {
    this.root = root;
    this.adminUsername = adminUsername;
    this.animation = new BookAnimation(root);
    this.userDataHandler = userDataHandler;
    this.adminDataHandler = adminDataHandler;
  }

  /** Create a ui for scene after user selected in the user settings. */
  public void showUserSettingPanel(String username) {

    String[] fullName = userDataHandler.getUserFullname(username);
    String firstName = fullName[0];
    String lastName = fullName[1];

    // Load openBook the image
    Image openBookImage = new Image("/assets/mainMenu/openBook.png");
    ImageView bookImageView = new ImageView(openBookImage);
    bookImageView.setFitHeight(422);
    bookImageView.setPreserveRatio(true);
    bookImageView.setTranslateY(-18);

    Image welcomeSign = new Image(AdminUserSetting.class.getResourceAsStream(
          "/assets/mainMenu/wellDoneKittyUser.png"
    ));
    ImageView welcomeSignImage = new ImageView(welcomeSign);
    welcomeSignImage.setFitHeight(150);
    welcomeSignImage.setPreserveRatio(true);
    StackPane.setMargin(welcomeSignImage, new Insets(-150, 235, 0, 0));

    Text usernameText = new Text(username);
    usernameText.setFont(pixelFont);
    StackPane.setMargin(usernameText, new Insets(-25, 235, 0, 0));

    Image adminSignFirstName = new Image(AdminUserSetting.class.getResourceAsStream(
          "/assets/mainMenu/adminPanel/adminSettingFirstName.png"
    ));
    ImageView adminSignFirstNameImage = new ImageView(adminSignFirstName);
    adminSignFirstNameImage.setFitHeight(60);
    adminSignFirstNameImage.setPreserveRatio(true);
    StackPane.setMargin(adminSignFirstNameImage, new Insets(-250, -260, 0, 0));

    //TextField for First name
    //it's a example change it from fetched from database
    TextField adminTextFieldFirstName = new TextField(firstName);
    adminTextFieldFirstName.setMaxWidth(170);
    adminTextFieldFirstName.getStyleClass().add("fieldBox");
    StackPane.setMargin(adminTextFieldFirstName, new Insets(-220, -260, 0, 0));

    Image adminSignLastName = new Image(AdminUserSetting.class.getResourceAsStream(
            "/assets/mainMenu/adminPanel/adminSettingLastName.png"));

    ImageView adminSignLastNameImage = new ImageView(adminSignLastName);
    adminSignLastNameImage.setFitHeight(60);
    adminSignLastNameImage.setPreserveRatio(true);
    StackPane.setMargin(adminSignLastNameImage, new Insets(-110, -260, 0, 0));

    //Textfield for Last Name
    //Again as a example change it from fetched from database
    TextField adminTextFieldLastName = new TextField(lastName);
    adminTextFieldLastName.setMaxWidth(170);
    adminTextFieldLastName.getStyleClass().add("fieldBox");
    StackPane.setMargin(adminTextFieldLastName, new Insets(-80, -260, 0, 0));

    Image adminSignPassword = new Image(AdminUserSetting.class.getResourceAsStream(
          "/assets/mainMenu/adminPanel/adminSettingPassword.png"
          ));

    ImageView adminSignPasswordImage = new ImageView(adminSignPassword);
    adminSignPasswordImage.setFitHeight(60);
    adminSignPasswordImage.setPreserveRatio(true);

    StackPane.setMargin(adminSignPasswordImage, new Insets(30, -260, 0, 0));

    //TextField for Password
    PasswordField adminTextFieldPassword = new PasswordField();
    adminTextFieldPassword.setMaxWidth(170);
    adminTextFieldPassword.getStyleClass().add("fieldBox");
    StackPane.setMargin(adminTextFieldPassword, new Insets(60, -260, 0, 0));

    //Images
    root.getChildren().addAll(bookImageView,
                              welcomeSignImage,
                              usernameText,
                              adminSignFirstNameImage,
                              adminSignLastNameImage,
                              adminSignPasswordImage,
                              adminTextFieldFirstName,
                              adminTextFieldLastName,
                              adminTextFieldPassword);
    applyButton(username,
                adminTextFieldFirstName,
                adminTextFieldLastName,
                adminTextFieldPassword);
    userAdminCheckbox();
    userDeleteButton(username);
    checkBoxPreference(70, 248, 0, 0,
                      "/assets/mainMenu/adminPanel/checkBox/adminUserCheckBoxMeatEater",  root);
    checkBoxPreference(160, 248, 0, 0,
                      "/assets/mainMenu/adminPanel/checkBox/adminUserCheckBoxVegetarian",  root);
    checkBoxPreference(250, 248, 0, 0,
                      "/assets/mainMenu/adminPanel/checkBox/adminUserCheckBoxVegan",  root);
    
                      Buttons MainButtons = new Buttons(root);

                      // Event handler for settingButton
                      MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));
                  
                      // Event handler for infoButton
                      MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));

  }

  private void userDeleteButton(String username) {
    // Load the images
    Image deleteButtonImage = new Image(getClass().getResourceAsStream(
            "/assets/buttons/admin/adminUserButton.png"
    ));
    Image hoverDeleteButtonImage = new Image(getClass().getResourceAsStream(
            "/assets/buttons/admin/adminUserButtonActive.png"
    ));

    ImageView deleteButtonImageView = new ImageView(deleteButtonImage);
    ImageView hoverDeleteButtonImageView = new ImageView(hoverDeleteButtonImage);

    deleteButtonImageView.setPreserveRatio(true);
    deleteButtonImageView.setFitHeight(30);
    hoverDeleteButtonImageView.setPreserveRatio(true);
    hoverDeleteButtonImageView.setFitHeight(30);

    // Create the checkbox
    Button userDeleteButton = new Button();
    userDeleteButton.setGraphic(deleteButtonImageView);
    userDeleteButton.getStyleClass().add("clearButton");

    // Hover logic
    userDeleteButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        userDeleteButton.setGraphic(hoverDeleteButtonImageView);
      } else {
        userDeleteButton.setGraphic(deleteButtonImageView);
      }
    });
    //Action
    userDeleteButton.setOnAction(event -> {
      showAlert(Alert.AlertType.CONFIRMATION, "Delete User",
          "Are you sure you would like to delete this user?", username);
    });

    StackPane.setMargin(userDeleteButton, new Insets(155, -365, 0, 0));
    root.getChildren().add(userDeleteButton);
  }

  private void showAlert(Alert.AlertType type, String title, String message, String username) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);

    ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    alert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);

    Optional<ButtonType> result = alert.showAndWait();

    result.ifPresent(buttonType -> {
      if (buttonType == deleteButtonType) {
        adminDataHandler.deleteUser(username);
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("User Deleted");
        successAlert.setHeaderText(null);
        successAlert.setContentText("User successfully deleted.");
        successAlert.showAndWait();
        animation.openPageAnimation("assets/animation/pageFlipAnimation", 422);

        // Switch scenes.
        PauseTransition pause = new PauseTransition(Duration.seconds(1.7));
        pause.setOnFinished(e -> {
          root.getChildren().clear();
          this.adminOpenBook = new AdminOpenBookView(root, adminUsername);
          adminOpenBook.showOpenBookScene();
        });
        pause.play();
      }
    });
  }

  private void userAdminCheckbox() {
    // Load the images
    Image uncheckedImage = new Image(getClass().getResourceAsStream(
          "/assets/mainMenu/adminPanel/checkBox/adminUserCheckBoxAdmin.png"
    ));
    Image checkedImage = new Image(getClass().getResourceAsStream(
          "/assets/mainMenu/adminPanel/checkBox/adminUserCheckBoxAdminActive.png"
    ));

    ImageView uncheckedImageView = new ImageView(uncheckedImage);
    ImageView checkedImageView = new ImageView(checkedImage);

    uncheckedImageView.setPreserveRatio(true);
    uncheckedImageView.setFitHeight(30);
    checkedImageView.setPreserveRatio(true);
    checkedImageView.setFitHeight(30);

    // Create the checkbox
    adminCheckbox = new CheckBox();

    adminCheckbox.setGraphic(uncheckedImageView);
    adminCheckbox.getStyleClass().add("admin-checkbox");

    // Listen for changes in the selection to update the graphic.
    adminCheckbox.selectedProperty().addListener((obs, notSelected, isSelected) -> {
      if (isSelected) {
        adminCheckbox.setGraphic(checkedImageView);
      } else {
        adminCheckbox.setGraphic(uncheckedImageView);
      }
    });
    StackPane.setMargin(adminCheckbox, new Insets(155, -145, 0, 0));
    root.getChildren().add(adminCheckbox);
  }

  private void checkBoxPreference(int top, int right, int bottom,
                                  int left, String path, StackPane root) {
    // Load the images
    Image uncheckedImage = new Image(getClass().getResourceAsStream(path + ".png"));
    Image checkedImage = new Image(getClass().getResourceAsStream(path + "Active.png"));

    ImageView uncheckedImageView = new ImageView(uncheckedImage);
    ImageView checkedImageView = new ImageView(checkedImage);

    uncheckedImageView.setPreserveRatio(true);
    uncheckedImageView.setFitHeight(30);
    checkedImageView.setPreserveRatio(true);
    checkedImageView.setFitHeight(30);

    // Create the checkbox
    CheckBox preferenceCheckbox;
    if (path.contains("MeatEater")) {
      preferenceCheckbox = meatEaterCheckbox = new CheckBox();
    } else if (path.contains("Vegetarian")) {
      preferenceCheckbox = vegetarianCheckbox = new CheckBox();
    } else if (path.contains("Vegan")) {
      preferenceCheckbox = veganCheckbox = new CheckBox();
    } else {
      preferenceCheckbox = new CheckBox();
    }
    preferenceCheckbox.setGraphic(uncheckedImageView);
    preferenceCheckbox.getStyleClass().add("admin-checkbox");

    // Logic listener for changes in the selection to update the image
    preferenceCheckbox.selectedProperty().addListener((obs, notSelected, isSelected) -> {
      if (isSelected) {
        preferenceCheckbox.setGraphic(checkedImageView);
      } else {
        preferenceCheckbox.setGraphic(uncheckedImageView);
      }
    });
    StackPane.setMargin(preferenceCheckbox, new Insets(top, right, bottom, left));
    root.getChildren().add(preferenceCheckbox);
  }

  private void applyButton(String username,
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

      //All of this are example for database
      String firstName = adminTextFieldFirstName.getText();
      String lastName = adminTextFieldLastName.getText();

      if (adminCheckbox != null) {

        boolean isAdmin = adminCheckbox.isSelected();
        adminDataHandler.modifyAdminStatus(new User(username, "",
            firstName, lastName), isAdmin);

        adminDataHandler.modifyFirstName(new User(username, "",
            firstName, lastName), firstName);
        adminDataHandler.modifyLastName(new User(username, "", firstName, lastName), lastName);
        showAlert("User Information Updated", firstName, lastName, isAdmin);
      }
      if (meatEaterCheckbox != null) {
        System.out.println("Is Meat Eater: " + meatEaterCheckbox.isSelected());
      }
      if (vegetarianCheckbox != null) {
        System.out.println("Is Vegetarian: " + vegetarianCheckbox.isSelected());
      }
      if (veganCheckbox != null) {
        System.out.println("Is Vegan: " + veganCheckbox.isSelected());
      }

      root.getChildren().clear();
      animation.openPageAnimation("assets/animation/pageFlipAnimation", 422);

      PauseTransition pause = new PauseTransition(Duration.seconds(1.7));
      pause.setOnFinished(e -> {
        root.getChildren().clear();
        this.adminOpenBook = new AdminOpenBookView(root, adminUsername);
        adminOpenBook.showOpenBookScene();
      });
      pause.play();
    });

    //add to the scene
    root.getChildren().add(applyButton);
  }

  private void showAlert(String title, String firstName, String lastName, boolean isAdmin) {
    String adminStatus = isAdmin ? "Yes" : "No";
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText("New first name is: " + firstName + "\nNew last name is: "
        + lastName + "\nAdmin privileges granted: " + adminStatus);
    alert.showAndWait();
  }
    private void handleSettingButtonInLoginScene(ActionEvent event) {
    System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("LoginOpenBookAdminUserSetting");
    root.getChildren().add(chatBox);
  }
}
