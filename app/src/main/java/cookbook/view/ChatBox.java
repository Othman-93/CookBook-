package cookbook.view;


import java.util.Arrays;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class ChatBox extends VBox {
  private Text messageText;
  private Button nextButton;
  private int messageIndex = 0;
  private List<String> currentMessages;
  private Timeline timeline;
  private StackPane tutorialPane;
  private Font pixelFont;
  private ImageView imageView;
  private ImageView nextButtonImage;
  private ImageView dialogueImageView;

  public ChatBox(StackPane tutorialPane) {
    this.tutorialPane = tutorialPane;
    this.pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 18);
    messageText = new Text();

    // Button configuration
    nextButton = new Button();
    // Button styling
    nextButtonImage = new ImageView(new Image("/assets/buttons/arrowRight.png"));
    nextButtonImage.setFitHeight(50);
    nextButton.getStyleClass().add("arrowButton");
    nextButton.setGraphic(nextButtonImage);
    nextButtonImage.setPreserveRatio(true);
    nextButton.setTranslateX(200);

    imageView = new ImageView(new Image("/assets/alertPanel.png"));

    //adding charaters to each chatbox

    dialogueImageView = new ImageView();
    dialogueImageView.setFitWidth(200);
    dialogueImageView.setPreserveRatio(true);

    // Style the ChatBox
    setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-padding: 20px;");
    messageText.setFont(pixelFont);
    imageView.setFitWidth(500);
    imageView.setPreserveRatio(true);

    setAlignment(Pos.BOTTOM_CENTER);
    setSpacing(10);

    StackPane imageTextOverlay = new StackPane();
    imageTextOverlay.getChildren().addAll(dialogueImageView, imageView, messageText, nextButton);
    imageTextOverlay.setAlignment(Pos.CENTER);

    getChildren().addAll(imageTextOverlay);
  }

  public void setupChatBox(String dialogueName) {
    loadDialogue(dialogueName);

    // Initialize with the first message
    showNextMessage();

    // Event by the button
    nextButton.setOnAction(e -> handleNextMessage());

    // Event by clicking Enter or Space
    setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
        handleNextMessage();
      }
    });

    // Event by clicking anywhere on the tutorial pane
    tutorialPane.setOnMouseClicked(e -> {
      if (e.getButton() == MouseButton.PRIMARY) {
        handleNextMessage();
      }
    });
  }

  public void loadDialogue(String dialogueName) {
    String imagePath = null;
    double translateX = 0;
    double translateY = 0;
    switch (dialogueName) {
      case "firstDialogue":
        currentMessages = Arrays.asList(
                      "Hello, I am Artur ,Welcome to Flavor Town!",
                      "Discover, create, and share recipes.",
                      "Press the logo to enter."
                      
              );
        //Charater Artur

        translateX = 180;
        translateY = -130;
        imagePath = "/assets/characters/ArturLogo.png";
        break;
      case "Login":
        currentMessages = Arrays.asList(
                      "Please Log In.",
                      "If you dont have an account.",
                      "Click the arrow next to the book to register."
                      
              );
        //Charater Artur

        translateX = 180;
        translateY = -130;
        imagePath = "/assets/characters/ArturLogo.png";
        break;
      case "Registration":
        currentMessages = Arrays.asList(
                  "Hello, I am Jenny!",
                  "Click on the button to register as a new user. "

          );
        //Charater Jenny

        translateX = 180;
        translateY = -130;
        imagePath = "/assets/characters/JennyLogo.png";
        break;
      case "RegistrationOpenBook":
        currentMessages = Arrays.asList(
                  "Hello, I am Nabil!",
                  "To register You need fill the fields above.",
                  "After you done click on register button"
                  
          );
        //Charater Nabil

        translateX = -150;
        translateY = -130;
        imagePath = "/assets/characters/NabilLogo.png";
        break;
      case "LoginOpenBookUser":
        currentMessages = Arrays.asList(
                  "Hello, I am Oskar!",
                  "To enter the app",
                  "Click the continue button.",
                  "To edit your information",
                  "Click the settings button",
                  "To return to the login screen",
                  "Click the exit button."
          );
        //Charater Oskar

        translateX = -160;
        translateY = -130;
        imagePath = "/assets/characters/OskarLogo.png";
        break;
      case "LoginOpenBookAdmin":
        currentMessages = Arrays.asList(
                  "Hello, I am Othman!",
                  "To enter the app",
                  "Click the continue button.",
                  "To edit a specific user's information",
                  "Click the accounts button.",
                  "To edit your own information",
                  "Click the sttings button.",
                  "To return to the login screen",
                  "Click the exit button."
          );
        //Charater Othman

        translateX = 160;
        translateY = -130;
        imagePath = "/assets/characters/OthmanLogo.png";
        break;
      case "LoginOpenBookAdminUserSetting":
        currentMessages = Arrays.asList(
                  "Hello, I am Nils!",
                  "To edit this account information",
                  "Make the necessary changes in the fields.",
                  "To grant this user admin privileges",
                  "Click the admin button",
                  "To delete this user",
                  "Click the delete button.",
                  "After you are done",
                  "click the apply button."

          );
        //Charater Nils

        translateX = 150;
        translateY = -130;
        imagePath = "/assets/characters/NilsLogo.png";
        break;
        
        //Stage2
      case "StageTwoNavigation":
        currentMessages = Arrays.asList(
                  "Hello its Artur again! ",
                  "In Market: Select the ingredients you need",
                  "and mark those you already have.",
                  "In Coffee Shop: Add dishes",
                  " to your weekly dinner list",
                  "and view your favorite recipes.",
                  "In Library: Add and search for recipes.",
                  "You can also create your own recipes",
                  "In Restaurant: Exchange recipes with others",
                  "and leave comments on them.",
                  "If you want to exit the app",
                  "click the exit button at the bottom of the screen."

                  
          );
        //Charater Artur

        translateX = 180;
        translateY = -130;
        imagePath = "/assets/characters/ArturLogo.png";
        break;
      case "StageTwoCafeshop":
        currentMessages = Arrays.asList(
                  "Hello its Artur again!",
                  "This is the Coffee Shop.",
                  "Click on the notebook icon",
                  "on the right side of the screen",
                  "to view your favorite recipes",
                  "and add recipes to your weekly list.",
                  "To go back to the main view",
                  "click the arrow at the bottom of the screen."
          );
        //Charater Artur

        translateX = 180;
        translateY = -130;
        imagePath = "/assets/characters/ArturLogo.png";
        break;
      case "StageTwoCafeshopWeekly":
        currentMessages = Arrays.asList(
                  "Hello, its Nabil again!",
                  "Here is the weekly list",
                  "You can select up to 4 recipes for each day.",
                  "You can plan up to 4 weeks from today's date.",
                  "After you are done",
                  "you can see your favorite recipes",
                  " by clicking the saved icon.",
                  "To go back to the main view",
                  "click the arrow at the bottom of the screen."
          );
        //Charater Nabil

        translateX = -150;
        translateY = -130;
        imagePath = "/assets/characters/NabilLogo.png";
        break;
      case "StageTwoMarket":
        currentMessages = Arrays.asList(
                  "Hello, its Jenny again!",
                  "This is the Market",
                  "where you can manage ingredients",
                  " for your favorite recipes",
                  "You can add, remove and star ingredients",
                  " on your shopping list.",
                  "To add ingredients, double-click from",
                  "the board or use + button.",
                  "Favourite ingredients may appear",
                  " at the top of the list.",
                  "To go back to the main view",
                  "click the arrow at the bottom of the screen."
          );
        //Charater Jenny

        translateX = 180;
        translateY = -130;
        imagePath = "/assets/characters/JennyLogo.png";
        break;
      case "StageTwoLibrary":
        currentMessages = Arrays.asList(
                  "Hello, its Oskar again!",
                  "Welcome to the library! Here, you can",
                  "View all recipes by clicking on search all.",
                  "Filter recipes by tags or ingredients.",
                  "Click on specific recipes to view details.",
                  "Add a recipe to your favorites",
                  "by clicking on the star button.",
                  "View the needed ingredients",
                  " and modify the servings.",
                  "Add tags to the recipes.",
                  "To add a recipe",
                  "click on the add recipe button.",
                  "To go back to the main view",
                  "click the arrow at the bottom of the screen."
          );
        //Charater Oskar

        translateX = -160;
        translateY = -130;
        imagePath = "/assets/characters/OskarLogo.png";
        break;
      case "StageTwoLibraryAddRecipes":
        currentMessages = Arrays.asList(
                  "Hello, its Othman again.",
                  "Welcome to the recipe adding book!",
                  "Here, you can:",
                  "Choose a name, servings",
                  "tags, prep time, and cook time",
                  "for your recipe.",
                  "Add instructions and descriptions.",
                  "When you're done",
                  "click on save to save your recipe."
                                              
          );
        //Charater Othman

        translateX = 160;
        translateY = -130;
        imagePath = "/assets/characters/OthmanLogo.png";
        break;
      case "StageTwoRestaurant":
        currentMessages = Arrays.asList(
                  "Hello, its Nils again!",
                  "Welcome to the restaurant! Here, you can.",
                  "Exchange recipes with other users.",
                  "Leave comments on recipes.",
                  "view public comments on it",
                  "as well as comments from your friends.",
                  "To go back to the main view",
                  "click the arrow at the bottom of the screen."
        );
        //Charater Nils
        translateX = 150;
        translateY = -130;
        imagePath = "/assets/characters/NilsLogo.png";
        break;
      default:
        currentMessages = Arrays.asList(
                    "Default message. No specific dialogue found."
            );
        //Charater Artur
        translateX = 180;
        translateY = -130;
        imagePath = "/assets/characters/ArturLogo.png";
        break;
    }
    if (imagePath != null) {
      Image dialogueImage = new Image(getClass().getResourceAsStream(imagePath));
      dialogueImageView.setImage(dialogueImage);
    }
    dialogueImageView.setTranslateX(translateX);
    dialogueImageView.setTranslateY(translateY);
    messageIndex = 0;
  }

  private void setMessage(String message) {
    if (timeline != null) {
      timeline.stop();
    }
    messageText.setText("");
    final String fullMessage = message;
    final int[] currentIndex = {0};

    timeline = new Timeline();
    KeyFrame keyFrame = new KeyFrame(Duration.millis(50), event -> {
      if (currentIndex[0] < fullMessage.length()) {
        messageText.setText(messageText.getText() + fullMessage.charAt(currentIndex[0]));
        currentIndex[0]++;
      } else {
        timeline.stop();
      }
    });

    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void handleNextMessage() {
    messageIndex++;
    if (messageIndex < currentMessages.size()) {
      showNextMessage();
    } else {
      // End of dialogue
      tutorialPane.getChildren().remove(this);
    }
  }

  private void showNextMessage() {
    setMessage(currentMessages.get(messageIndex));
  }
}
