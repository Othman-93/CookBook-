package cookbook.view;

import cookbook.controller.ViewController;
import cookbook.model.DatabaseUtilProviderImpl;
import cookbook.model.UserDataHandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/** Ui for when the book opens. */
public class UserOpenBookView {

  // Attributes.
  private StackPane root;
  private BookAnimation animation;
  private LoginView loginView;
  private HBox gridContainer; // This HBox will contain all grids of this open book view.
  private String username;
  private ChatBox chatBox;

  /**
   * Constructor.
   *
   * @param root the StackPane root.
   */
  public UserOpenBookView(StackPane root, String username) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.loginView = new LoginView(root);
    this.gridContainer = new HBox();
    this.username = username;
  }

  /** create a ui for scene after login book opens. */
  public void showOpenBookScene() {

    // Load the image
    Image openBookImage = new Image("/assets/mainMenu/centeredOpenBook.png");
    ImageView bookImageView = new ImageView(openBookImage);
    bookImageView.setFitHeight(422);
    bookImageView.setPreserveRatio(true);

    // Load button image for continue, settings and exit
    Image welcomeSign =
        new Image(LoginView.class.getResourceAsStream("/assets/mainMenu/welcomeScroll.png"));
    ImageView welcomeSignImage = new ImageView(welcomeSign);
    welcomeSignImage.setFitHeight(64);
    welcomeSignImage.setPreserveRatio(true);

    // Username display TODO: Full Name
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 15);
    Text usernameText = new Text(username);
    usernameText.setFont(pixelFont);

    // GridPane for all the user options above.
    GridPane optionsGrid = new GridPane();

    // Center GridPane.
    optionsGrid.setAlignment(Pos.CENTER);

    // Add all user options to GridPane.
    optionsGrid.add(welcomeSignImage, 0, 0);
    optionsGrid.add(usernameText, 0, 0);
    optionsGrid.add(continueButton(), 0, 1);
    optionsGrid.add(settingsButton(), 0, 2);
    optionsGrid.add(exitButton(), 0, 3);

    // Set horizontal alignment of all GridPane children to CENTER.
    for (javafx.scene.Node child : optionsGrid.getChildren()) {
      GridPane.setHalignment(child, HPos.CENTER);
    }

    // Set vertical spacing of GridPane children.
    optionsGrid.setVgap(35);

    // Set padding of HBox.
    gridContainer.setPadding(new Insets(0, 0, 0, 155));

    // Add optionsGrid to gridContainer.
    gridContainer.getChildren().add(optionsGrid);

    // Add open book image and HBox (gridContainer) to root StackPane.
    root.getChildren().addAll(bookImageView, gridContainer);
    Buttons MainButtons = new Buttons(root);

    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));

    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));
  }

  /** Button To Continue. */
  public Button continueButton() {
    // Load button image for continue, settings and exit
    Image contButtonImage =
        new Image(LoginView.class.getResourceAsStream("/assets/buttons/continueButton.png"));
    ImageView contButtonImageView = new ImageView(contButtonImage);
    contButtonImageView.setFitHeight(30);
    contButtonImageView.setPreserveRatio(true);

    // Active continue Button (button on hover)
    Image cbuttonHoverImage =
        new Image(
            UserOpenBookView.class.getResourceAsStream("/assets/buttons/continueButtonActive.png"));
    ImageView cbuttonHoverImageView = new ImageView(cbuttonHoverImage);
    cbuttonHoverImageView.setFitHeight(30);
    cbuttonHoverImageView.setPreserveRatio(true);

    Button continueButton = new Button();
    continueButton.setGraphic(contButtonImageView);
    continueButton.getStyleClass().add("clearButton");
    StackPane.setMargin(continueButton, new Insets(-40, 228, 0, 0));

    // Continue button on hover.
    continueButton
        .hoverProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                continueButton.setGraphic(cbuttonHoverImageView);
              } else {
                continueButton.setGraphic(contButtonImageView);
              }
            });

    // Continue button on click action.
    continueButton.setOnAction(
        event -> {
          ViewController control = new ViewController(root);
          try {
            control.StageTwo(username);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    return continueButton;
  }

  /** Settings Button. */
  public Button settingsButton() {
    Image settButtonImage =
        new Image(LoginView.class.getResourceAsStream("/assets/buttons/settingsButton.png"));
    ImageView settButtonImageView = new ImageView(settButtonImage);
    settButtonImageView.setFitHeight(30);
    settButtonImageView.setPreserveRatio(true);

    // Active settings Button (button on hover)
    Image sbuttonHoverImage =
        new Image(
            UserOpenBookView.class.getResourceAsStream("/assets/buttons/settingsButtonActive.png"));
    ImageView sbuttonHoverImageView = new ImageView(sbuttonHoverImage);
    sbuttonHoverImageView.setFitHeight(30);
    sbuttonHoverImageView.setPreserveRatio(true);

    Button settingsButton = new Button();
    settingsButton.setGraphic(settButtonImageView);
    settingsButton.getStyleClass().add("clearButton");
    StackPane.setMargin(settingsButton, new Insets(75, 228, 0, 0));

    // Hover Logic
    settingsButton
        .hoverProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                settingsButton.setGraphic(sbuttonHoverImageView);
              } else {
                settingsButton.setGraphic(settButtonImageView);
              }
            });

    DatabaseUtilProviderImpl databaseUtilProvider = new DatabaseUtilProviderImpl();
    UserDataHandler userDataHandler = new UserDataHandler();
    ProfileSettingView profileSettingView =
        new ProfileSettingView(gridContainer, username, userDataHandler);

    // Button Action on click.
    settingsButton.setOnAction(
        event -> {
          System.out.println("Going to settings..." + username);
          settingsButton.setDisable(true); // Disable button after click.
          profileSettingView.showProfileSettingScene();
        });

    return settingsButton;
  }

  /** Exit Button. */
  public Button exitButton() {
    Image exitButtonImage =
        new Image(LoginView.class.getResourceAsStream("/assets/buttons/exitButton.png"));
    ImageView exitButtonImageView = new ImageView(exitButtonImage);
    exitButtonImageView.setFitHeight(30);
    exitButtonImageView.setPreserveRatio(true);

    // Active exit Button (button on hover)
    Image ebuttonHoverImage =
        new Image(
            UserOpenBookView.class.getResourceAsStream("/assets/buttons/exitButtonActive.png"));
    ImageView ebuttonHoverImageView = new ImageView(ebuttonHoverImage);
    ebuttonHoverImageView.setFitHeight(30);
    ebuttonHoverImageView.setPreserveRatio(true);

    Button exitButton = new Button();
    exitButton.setGraphic(exitButtonImageView);
    exitButton.getStyleClass().add("clearButton");
    // Set Buttons mergins
    StackPane.setMargin(exitButton, new Insets(190, 228, 0, 0));

    // Logic Handler
    exitButton.setOnAction(
        event -> {
          root.getChildren().clear();
          animation.closeAnimationBook("/assets/mainMenu/loginAnimationBook");
          PauseTransition pause = new PauseTransition(Duration.seconds(3.5));

          pause.setOnFinished(
              e -> {
                root.getChildren().clear();
                loginView.showLoginScene();
              });
          pause.play();
        });

    // Hover Logic
    exitButton
        .hoverProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                exitButton.setGraphic(ebuttonHoverImageView);
              } else {
                exitButton.setGraphic(exitButtonImageView);
              }
            });

    return exitButton;
  }
    private void handleSettingButtonInLoginScene(ActionEvent event) {
    System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("LoginOpenBookUser");
    root.getChildren().add(chatBox);
  }
}
