package cookbook.view;

import cookbook.controller.ViewController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * This class is for the welcome scene UI.
*/
public class StageTwoNavigator {

  // Attributes.
  private StackPane root;
  private ChatBox chatBox;

  /**
   * Constructor.
   */
  public  StageTwoNavigator() {
    this.root = new StackPane();  // The StackPane root.
  }

  /**
   * This method will creat the welcome scene box.
   */
  public Parent navigator(String username) {

    marketButton(username);
    coffeeButton(username);
    libraryButton(username);
    restaurantButton(username);
    ExitButton(username);

    Buttons MainButtons = new Buttons(root);

    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));

    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));

    return root;
  }

  private void marketButton(String username){

        //Creating new Button
        Button marketButton = new Button();
        marketButton.setPrefSize(150, 200);
        marketButton.getStyleClass().add("clearButton");
        //For testing
        // marketButton.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");
    
        StackPane.setMargin(marketButton, new Insets(220, 440, 0, 0));

            //Initiazing Tooltip! 
        SetupTooltip Tooltip = new SetupTooltip();
        Tooltip.Tooltip(marketButton, "tooltipMarket.png", Side.RIGHT, 80); //(Button, a picture, and what side to put tooltip, Tooltip size)

        root.getChildren().addAll(marketButton);
        //Logic behind it (NON)
        // Switch scenes to login.
        ViewController control = new ViewController(root);
        marketButton.setOnAction(event -> {
        root.getChildren().clear();
          try {
            control.StageTwoMarket(username);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  private void coffeeButton(String username){

    //Creating new Button
    Button coffeeButton = new Button();
    coffeeButton.setPrefSize(130, 180);
    coffeeButton.getStyleClass().add("clearButton");
    //For testing
    // coffeeButton.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");

    StackPane.setMargin(coffeeButton, new Insets(-210, 440, 0, 0));

        //Initiazing Tooltip! 
    SetupTooltip Tooltip = new SetupTooltip();
    Tooltip.Tooltip(coffeeButton, "tooltipCafe.png", Side.RIGHT, 80); //(Button, a picture, and what side to put tooltip, Tooltip size)

    root.getChildren().addAll(coffeeButton);
    //Logic behind it (NON)
    // Switch scenes to login.
    ViewController control = new ViewController(root);
    coffeeButton.setOnAction(event -> {
    root.getChildren().clear();
      try {
        control.StageTwoCoffee(username);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
}

  private void libraryButton(String username){

    //Creating new Button
    Button libraryButton = new Button();
    libraryButton.setPrefSize(170, 250);
    libraryButton.getStyleClass().add("clearButton");
    //For testing
    // libraryButton.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");

    StackPane.setMargin(libraryButton, new Insets(-295, -10, 0, 0));

        //Initiazing Tooltip! 
    SetupTooltip Tooltip = new SetupTooltip();
    Tooltip.Tooltip(libraryButton, "tooltipLibrary.png", Side.BOTTOM, 80); //(Button, a picture, and what side to put tooltip, Tooltip size)

    root.getChildren().addAll(libraryButton);
    //Logic behind it (NON)
    // Switch scenes to Library.
    ViewController control = new ViewController(root);
    libraryButton.setOnAction(event -> {
      root.getChildren().clear();
      try {
        control.StageTwoLibrary(username);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  private void restaurantButton(String username){

    //Creating new Button
    Button restaurantButton = new Button();
    restaurantButton.setPrefSize(210, 140);
    restaurantButton.getStyleClass().add("clearButton");
    //For testing
    // restaurantButton.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");

    StackPane.setMargin(restaurantButton, new Insets(280, -450, 0, 0));

        //Initiazing Tooltip! 
    SetupTooltip Tooltip = new SetupTooltip();
    Tooltip.Tooltip(restaurantButton, "tooltipRestaurant.png", Side.LEFT, 80); //(Button, a picture, and what side to put tooltip, Tooltip size)

    root.getChildren().addAll(restaurantButton);
    //Logic behind it (NON)
    // Switch scenes to Restaurant.
    ViewController control = new ViewController(root);
    restaurantButton.setOnAction(event -> {
      root.getChildren().clear();
      try {
        control.StageTwoRestaurant(username);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  private void ExitButton(String username){

    //Creating new Button
    Button exitButton = new Button();
    exitButton.setPrefSize(210, 140);
    exitButton.getStyleClass().add("clearButton");
    //For testing
    // exitButton.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");

    StackPane.setMargin(exitButton, new Insets(450, 0, 0, 0));

        //Initiazing Tooltip! 
    SetupTooltip Tooltip = new SetupTooltip();
    Tooltip.Tooltip(exitButton, "tooltipExit.png", Side.TOP, 80); //(Button, a picture, and what side to put tooltip, Tooltip size)

    root.getChildren().addAll(exitButton);
    //Logic behind it (NON)
    // Switch scenes to exit.
    exitButton.setOnAction(event -> {
      root.getChildren().clear();
      try {
        // Platform.exit();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  private void handleSettingButtonInLoginScene(ActionEvent event) {
    System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("StageTwoNavigation");
    root.getChildren().add(chatBox);
  }
}
