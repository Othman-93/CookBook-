package cookbook.view;

import java.util.List;

import cookbook.model.CommentsDataHandler;
import cookbook.model.DatabaseUtilProviderImpl;
import cookbook.model.RecipeHandlerModel;
import cookbook.model.UserDataHandler;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * This class is for the Restaurant View Animation.
 */
public class RestaurantView {

  // Class fields.
  private BookAnimation animation;  // Book animation.
  private StackPane root;
  private Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 17);
  private String username;
  private List<String> recipesRecomended;
  private List<String> recipesFavorite;
  private List<String> recipesList;
  private RestaurantViewRecipe removeChildren;
  private RecipeHandlerModel recipeHandlerModel;
  private CommentsDataHandler CommentsDataHandler;
  private UserDataHandler userHandler;
  private ChatBox chatBox;

  /**
   * Constructor for the Restaurant View class.
   *
   * @param root the StackPane root.
   */
  public RestaurantView(StackPane root, String username) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.username = username;
    UserDataHandler userDataHandler = new UserDataHandler();
    this.removeChildren = new RestaurantViewRecipe(root, username, userDataHandler);

    
    this.userHandler = new UserDataHandler();
    DatabaseUtilProviderImpl databaseUtilProviderImpl = new DatabaseUtilProviderImpl();
    this.recipeHandlerModel = new RecipeHandlerModel();
    this.CommentsDataHandler = new CommentsDataHandler();

    //coneverting a username to the ID
    int userId = userHandler.getUserIdFromUsername(username);

    this.recipesRecomended = new ArrayList<>();
    List<Integer> recommendedRecipeIds = CommentsDataHandler.getSharedRecipes(username);
    for (Integer recipeId : recommendedRecipeIds) {
        String recipeName = recipeHandlerModel.getRecipeNameById(recipeId);
        if (recipeName != null) {
            recipesRecomended.add(recipeName);
        }
    }

    this.recipesFavorite = new ArrayList<>();
    recipesFavorite = recipeHandlerModel.getFavoriteRecipesForUser(userId);

    this.recipesList = new ArrayList<>();
    recipesList = recipeHandlerModel.showAllRecipeNames();
    
}

  /**
   * This method creates a animation for the book to apear.
   */

  public void startingButton() {
    // Load the background image (open book).
    Image noteBook = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/restaurant/menu.png"
    ));
    //implamenting style to image
    ImageView noteBookView =  new ImageView(noteBook);
    noteBookView.setFitHeight(160);
    noteBookView.setPreserveRatio(true);
    
    //creating button
    Button noteBookButton = new Button();
    noteBookButton.setGraphic(noteBookView);
    noteBookButton.getStyleClass().add("clearButton");

    // Initial translation positions based on initial margins
    noteBookButton.setTranslateX(-145);
    noteBookButton.setTranslateZ(1);

    //adding button to the scene
    root.getChildren().add(noteBookButton);

    //MainButtons for setting and Information
    mainButtonInisiolizer();

    //Animation for the button
    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), noteBookButton);
    scaleTransition.setFromX(1.0);
    scaleTransition.setFromY(1.0);
    scaleTransition.setToX(1.2);  
    scaleTransition.setToY(1.2);  
    scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
    scaleTransition.setAutoReverse(true);
    scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);

    // Start the pulsing animation
    scaleTransition.play();

    //Preperation for the book to be deploide
    Image noteImage = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/restaurant/restaurantMenu.png"
    ));

    //implamenting style to image
    ImageView noteView =  new ImageView(noteImage);
    noteView.setFitHeight(1);
    noteView.setPreserveRatio(true);
    noteView.setTranslateZ(1);
    
    root.getChildren().add(noteView);

  
    //Start postion og the book
    Timeline timeline = new Timeline();
    KeyValue kvStartX = new KeyValue(noteView.translateXProperty(), -145);
    KeyValue kvStartY = new KeyValue(noteView.translateYProperty(), 5);
    
    //after animation position of the book
    KeyValue kvEndX = new KeyValue(noteView.translateXProperty(), 0);
    KeyValue kvEndY = new KeyValue(noteView.translateYProperty(), -18);

    //the size of the book (the zoom in animtion)
    KeyValue kvStart = new KeyValue(noteView.fitHeightProperty(), 0);
    KeyValue kvEnd = new KeyValue(noteView.fitHeightProperty(), 522);
    
    //the main keyframe for timing the things above
    KeyFrame kfStart = new KeyFrame(Duration.ZERO, kvStartX, kvStartY, kvStart);
    KeyFrame kfEnd = new KeyFrame(Duration.seconds(1), kvEndX, kvEndY, kvEnd);

    timeline.getKeyFrames().addAll(kfStart, kfEnd);

    //execute after zoom-in
    timeline.getKeyFrames().add(
        new KeyFrame(Duration.seconds(0.99), event -> {

          //Removing button
          // Parent parent = noteView.getParent();
          // ((Pane) parent).getChildren().remove(noteView);

          //Executing BookAnimation class and method in it.
          // animation.openAnimationBook("/assets/stageTwo/market/noteAnimation", 
          //     false, 24, ".gif", -123);

          //WIll set menu when animation for it done

          //stop button levitation animation
          scaleTransition.stop();

          //Start Play a things inside of the menu

          setLabelsOnMenu(username);

          //todo
            

        })
    );

    //that's a action of the button
    noteBookButton.setOnAction(event -> {
      timeline.play();
      // restaurantPane.getChildren().clear();
    });
    
  }

 private void setLabelsOnMenu(String username) {

    // Create the Label for Friends Recommendation
    Label friendsRecommendation = new Label("Recommendation");
    friendsRecommendation.setFont(pixelFont);

    // Create the ListView for Friends Recommendation
    ListView<String> friendsRecommendationList = new ListView<>();
    ObservableList<String> recommendedRecipesObservable = FXCollections.observableArrayList(recipesRecomended);
    friendsRecommendationList.setItems(recommendedRecipesObservable);
    friendsRecommendationList.setPlaceholder(new Label("Your friends haven't recommended anything yet!"));
    friendsRecommendationList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
        @Override
        public ListCell<String> call(ListView<String> param) {
            return new ListCell<String>() {
                private Text text;

                {
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && !isEmpty()) {
                            removeChildren.removeSections(root, "VboxReplace");
                            removeChildren.removeSections(root, "commentsSection");
                            openRecipeDetails(username, getItem());

                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        text = new Text(item);
                        text.setWrappingWidth(param.getWidth() - 20);
                        setGraphic(text);
                        setFont(pixelFont);
                    }
                }
            };
        }
    });

    // Create VBox for Friends Recommendation section
    VBox friendsSection = new VBox(2);
    friendsSection.getStyleClass().add("transparent-container");
    // friendsSection.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-padding: 5px;");
    friendsSection.setAlignment(Pos.CENTER);
    friendsSection.getChildren().addAll(friendsRecommendation, friendsRecommendationList);
    StackPane.setMargin(friendsSection, new Insets(120, 415, 350, 135));

    // Create the Label for Chef's Favorite
    Label favorite = new Label("Chef's Favorite");
    favorite.setFont(pixelFont);

    // Create the ListView for Chef's Favorite
    ListView<String> favoriteList = new ListView<>();
    ObservableList<String> favoriteRecipesObservable = FXCollections.observableArrayList(recipesFavorite);
    favoriteList.getItems().addAll(favoriteRecipesObservable); // todo
    favoriteList.setPlaceholder(new Label("You haven't added any recipes to your favorites list. Go to the library to add some!"));
    favoriteList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
        @Override
        public ListCell<String> call(ListView<String> param) {
            return new ListCell<String>() {
                private Text text;

                {
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && !isEmpty()) {
                            removeChildren.removeSections(root, "VboxReplace");
                            openRecipeDetails(username, getItem());
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        text = new Text(item);
                        text.setWrappingWidth(param.getWidth() - 20);
                        setGraphic(text);
                        setFont(pixelFont);
                    }
                }
            };
        }
    });

    // Create VBox for Chef's Favorite section
    VBox favoriteSection = new VBox(2);
    favoriteSection.getStyleClass().add("transparent-container");
    // favoriteSection.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-padding: 5px;");
    favoriteSection.setAlignment(Pos.CENTER);
    favoriteSection.getChildren().addAll(favorite, favoriteList);
    StackPane.setMargin(favoriteSection, new Insets(240, 415, 240, 125));

    // Create the Label for All Recipes
    Label allRecipes = new Label("Menu");
    allRecipes.setFont(pixelFont);

    // Create the ListView for All Recipes
    ListView<String> allRecipesList = new ListView<>();
    ObservableList<String> allRecipesObservable = FXCollections.observableArrayList(recipesList);
    allRecipesList.getItems().addAll(allRecipesObservable); // Done
    allRecipesList.setPlaceholder(new Label("There are no recipes. Check your database connection."));
    allRecipesList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
        public ListCell<String> call(ListView<String> param) {
            return new ListCell<String>() {
                private Text text;

                {
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && !isEmpty()) {
                            removeChildren.removeSections(root, "VboxReplace");
                            removeChildren.removeSections(root, "commentsSection");
                            openRecipeDetails(username, getItem());
                            
                            
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        text = new Text(item);
                        text.setWrappingWidth(param.getWidth() - 20);
                        setGraphic(text);
                        setFont(pixelFont);
                    }
                }
            };
        }
    });

    // Create VBox for All Recipes section
    VBox allRecipesSection = new VBox(2);
    allRecipesSection.getStyleClass().add("transparent-container");
    // allRecipes.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-padding: 5px;");
    allRecipesSection.setAlignment(Pos.CENTER);
    allRecipesSection.getChildren().addAll(allRecipes, allRecipesList);
    StackPane.setMargin(allRecipesSection, new Insets(360, 415, 125, 120));

    // Add all sections to the root StackPane
    root.getChildren().addAll(friendsSection, favoriteSection, allRecipesSection);
}

private void openRecipeDetails(String username, String recipeName) {
    System.out.println("Username: " + username + ", Recipe Name: " + recipeName);
    UserDataHandler userDataHandler = new UserDataHandler();
    RestaurantViewRecipe selectRecipe = new RestaurantViewRecipe(root, username, userDataHandler);
    selectRecipe.setMenuForCommunication(recipeName, username);
}
  private void updateListView(ListView<String> listView, List<String> items) {
    if (items != null) {
      ObservableList<String> observableList = FXCollections.observableArrayList(items);
      listView.setItems(observableList);
    } else {
      listView.setItems(FXCollections.observableArrayList());
    }
  }
    private void handleSettingButtonInLoginScene(ActionEvent event) {
    System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("StageTwoRestaurant");
    root.getChildren().add(chatBox);
  }
  private void mainButtonInisiolizer(){
    Buttons MainButtons = new Buttons(root);
    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));
    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));

  }

}
