package cookbook.view;

import cookbook.controller.ViewController;
import cookbook.model.AdminDataHandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


/**
 * Ui for when the book opens.
 */
public class AdminOpenBookView {

  // Attributes.
  private StackPane root;
  private BookAnimation animation;
  private AdminUserPanel userPanel;
  private AdminSetting adminSetting;
  private LoginView mainMenuView;
  private String adminUsername;
  private ChatBox chatBox;

  /**
   * Constructor.
   *
   * @param root the root StackPane.
   */
  public AdminOpenBookView(StackPane root, String adminUsername) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.userPanel = new AdminUserPanel(root, adminUsername);
    this.adminUsername = adminUsername;
    AdminDataHandler adminDataHandler = new AdminDataHandler();
    this.adminSetting = new AdminSetting(root, adminUsername, adminDataHandler);
    this.mainMenuView = new LoginView(root);
  }

  /**
   * create a ui for scene after login book opens.
   *
   */

  public void showOpenBookScene() {

    // Load the image
    Image openBookImage = new Image("/assets/mainMenu/openBook.png");
    ImageView bookImageView = new ImageView(openBookImage);
    bookImageView.setFitHeight(422);
    bookImageView.setPreserveRatio(true);
    bookImageView.setTranslateY(-18);


    // Load button image for continue, settings and exit
    Image welcomeSign = new Image(LoginView.class.getResourceAsStream(
        "/assets/mainMenu/adminPanel.png"
        ));
    ImageView welcomeSignImage = new ImageView(welcomeSign);
    welcomeSignImage.setFitHeight(60);
    welcomeSignImage.setPreserveRatio(true);

    //Margins
    StackPane.setMargin(welcomeSignImage, new Insets(-200, 228, 0, 0));

    // Add the book image and buttons to the root StackPane
    root.getChildren().addAll(bookImageView, welcomeSignImage);
    continueButton();
    accountButton();
    settingButton();
    exitButton();

    Buttons MainButtons = new Buttons(root);

    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));

    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));

  }

  private void continueButton() {
    // Load button image for continue, settings and exit
    Image continueButtonImage = new Image(LoginView.class.getResourceAsStream(
        "/assets/buttons/admin/adminContinueButton.png"));
    ImageView continueButtonImageView = new ImageView(continueButtonImage);
    continueButtonImageView.setFitHeight(30);
    continueButtonImageView.setPreserveRatio(true);

    // Active continue Button (button on hover)
    Image continueHoverImage = new Image(AdminOpenBookView.class.getResourceAsStream(
        "/assets/buttons/admin/adminContinueButtonActive.png"));
    ImageView continueHoverImageView = new ImageView(continueHoverImage);
    continueHoverImageView.setFitHeight(29);
    continueHoverImageView.setPreserveRatio(true);

    Button continueButton = new Button();
    continueButton.setGraphic(continueButtonImageView);
    continueButton.getStyleClass().add("clearButton");

    //Margins
    StackPane.setMargin(continueButton, new Insets(-90, 228, 0, 0));

    //Hover logic
    continueButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        continueButton.setGraphic(continueHoverImageView);
      } else {
        continueButton.setGraphic(continueButtonImageView);
      }
    });

    // Continue event handler
    continueButton.setOnAction(event -> {
      ViewController control = new ViewController(root);
      try {
        control.StageTwo(adminUsername);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    //add to the scene
    root.getChildren().add(continueButton);
  }

  private void accountButton() {
    // Load button image for continue, settings and exit
    Image accountButtonImage = new Image(LoginView.class.getResourceAsStream(
        "/assets/buttons/admin/adminAccountButton.png"));
    ImageView accountButtonImageView = new ImageView(accountButtonImage);
    accountButtonImageView.setFitHeight(30);
    accountButtonImageView.setPreserveRatio(true);

    // Active continue Button (button on hover)
    Image accountHoverImage = new Image(AdminOpenBookView.class.getResourceAsStream(
        "/assets/buttons/admin/adminAccountButtonActive.png"));
    ImageView accountHoverImageView = new ImageView(accountHoverImage);
    accountHoverImageView.setFitHeight(29);
    accountHoverImageView.setPreserveRatio(true);

    Button accountButton = new Button();
    accountButton.setGraphic(accountButtonImageView);
    accountButton.getStyleClass().add("clearButton");

    //Margins
    StackPane.setMargin(accountButton, new Insets(15, 228, 0, 0));

    //Hover logic
    accountButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        accountButton.setGraphic(accountHoverImageView);
      } else {
        accountButton.setGraphic(accountButtonImageView);
      }
    });

    // event handlers
    accountButton.setOnAction(event -> {
      root.getChildren().clear();
      showOpenBookScene();
      userPanel.showUserPanel();
    });

    //add to the scene
    root.getChildren().add(accountButton);
  }

  private void settingButton() {
    Image settButtonImage = new Image(LoginView.class.getResourceAsStream(
        "/assets/buttons/admin/adminSettingsButton.png"));
    ImageView settButtonImageView = new ImageView(settButtonImage);
    settButtonImageView.setFitHeight(30);
    settButtonImageView.setPreserveRatio(true);

    // Active settings Button (button on hover)
    Image sbuttonHoverImage = new Image(AdminOpenBookView.class.getResourceAsStream(
        "/assets/buttons/admin/adminSettingsButtonActive.png"));
    ImageView sbuttonHoverImageView = new ImageView(sbuttonHoverImage);
    sbuttonHoverImageView.setFitHeight(29);
    sbuttonHoverImageView.setPreserveRatio(true);

    Button settingsButton = new Button();
    settingsButton.setGraphic(settButtonImageView);
    settingsButton.getStyleClass().add("clearButton");

    //Margins
    StackPane.setMargin(settingsButton, new Insets(120, 228, 0, 0));

    //Hover logic
    settingsButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        settingsButton.setGraphic(sbuttonHoverImageView);
      } else {
        settingsButton.setGraphic(settButtonImageView);
      }
    });
    settingsButton.setOnAction(event -> {
      root.getChildren().clear();
      showOpenBookScene();
      adminSetting.showAdminSetting();
    });

    //add to the scene
    root.getChildren().add(settingsButton);
  }

  private void exitButton() {
    Image exitButtonImage = new Image(LoginView.class.getResourceAsStream(
        "/assets/buttons/admin/adminExitButton.png"));
    ImageView exitButtonImageView = new ImageView(exitButtonImage);
    exitButtonImageView.setFitHeight(30);
    exitButtonImageView.setPreserveRatio(true);

    // Active exit Button (button on hover)
    Image ebuttonHoverImage = new Image(AdminOpenBookView.class.getResourceAsStream(
        "/assets/buttons/admin/adminExitButtonActive.png"));
    ImageView ebuttonHoverImageView = new ImageView(ebuttonHoverImage);
    ebuttonHoverImageView.setFitHeight(29);
    ebuttonHoverImageView.setPreserveRatio(true);

    Button exitButton = new Button();
    exitButton.setGraphic(exitButtonImageView);
    exitButton.getStyleClass().add("clearButton");

    //Hover logic
    exitButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        exitButton.setGraphic(ebuttonHoverImageView);
      } else {
        exitButton.setGraphic(exitButtonImageView);
      }
    });

    //Event manager
    exitButton.setOnAction(event -> {

      root.getChildren().clear();
      animation.closeAnimationBook("/assets/mainMenu/loginAnimationBook");

      PauseTransition pause = new PauseTransition(Duration.seconds(3.5));
      pause.setOnFinished(e -> {

        root.getChildren().clear();
        mainMenuView.showLoginScene();

      });

      pause.play();
    });

    // Set margins for buttons
    StackPane.setMargin(exitButton, new Insets(225, 228, 0, 0));

    //Add to the scene
    root.getChildren().add(exitButton);
  }
  private void handleSettingButtonInLoginScene(ActionEvent event) {
    System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("LoginOpenBookAdmin");
    root.getChildren().add(chatBox);
  }
}
