package cookbook.view;

import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;


import cookbook.model.DatabaseUtilProviderImpl;
import cookbook.model.RecipeHandlerModel;
import cookbook.model.UserDataHandler;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * This class is for the UltimateCookbook Animation.
 */
public class CafeShopView {

  // Class fields.
  private BookAnimation animation;  // Book animation.
  private StackPane root;
  private List<ToggleButton> toggleButtons = new ArrayList<>();
  private Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 17);
  private CafeShopWeeklyPrep outline;
  private final String username;
  private ChatBox chatBox;

  /**
   * Constructor for the UltimateCookbook class.
   *
   * @param root the StackPane root.
   */
  public CafeShopView(StackPane root, String username) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.outline = new CafeShopWeeklyPrep(root, username);
    this.username = username;
  }

  //Controller to unToggle and Toggle button on the press
  private void manageToggleButtons(ToggleButton activeButton) {
    for (ToggleButton button : toggleButtons) {
        if (button != activeButton) {
            button.setSelected(false);
            button.setDisable(true);
        } else {
            button.setDisable(true); 
        }
    }
    
    //Enable other buttons after 5 seconds, but not the active one
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), ae -> {
        for (ToggleButton button : toggleButtons) {
            if (button != activeButton) {
                button.setDisable(false);
            }
        }
    }));
    timeline.play();
}

private void allRecipes() {
  Timeline appearAfterOpen = new Timeline();
  appearAfterOpen.getKeyFrames().add(
      new KeyFrame(Duration.seconds(1), event -> {
        //This thing
        weeklyListView();
    })
  );
  appearAfterOpen.play();
}

  /**
   * This method creates a animation for the book to apear.
   */

  public void startingButton() {
    // Load the background image (open book).
    Image noteBook = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/cafeshop/noteBook.png"
    ));
    //implamenting style to image
    ImageView noteBookView =  new ImageView(noteBook);
    noteBookView.setFitHeight(60);
    noteBookView.setPreserveRatio(true);
    
    //creating button
    Button noteBookButton = new Button();
    noteBookButton.setGraphic(noteBookView);
    noteBookButton.getStyleClass().add("clearButton");
    noteBookButton.setId("DeleteStartButton");

    // Initial translation positions based on initial margins
    noteBookButton.setTranslateX(330);

    //adding button to the scene
    root.getChildren().add(noteBookButton);

    //Animation for the button
    TranslateTransition noteBookTransition = new TranslateTransition(
        Duration.seconds(1), noteBookButton);
          noteBookTransition.setFromY(5);
          noteBookTransition.setToY(-10);
          noteBookTransition.setInterpolator(Interpolator.EASE_IN);
          noteBookTransition.setAutoReverse(true);
          noteBookTransition.setCycleCount(TranslateTransition.INDEFINITE);
    // Start the noteBook animation
    noteBookTransition.play();

    //Preperation for the book to be deploide
    Image noteImage = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/market/noteAnimation/frame1.gif"
    ));

    //implamenting style to image
    ImageView noteView =  new ImageView(noteImage);
    noteView.setFitHeight(1);
    noteView.setPreserveRatio(true);
    
    root.getChildren().add(noteView);

  
    //Start postion og the book
    Timeline timeline = new Timeline();
    KeyValue kvStartX = new KeyValue(noteView.translateXProperty(), 330);
    KeyValue kvStartY = new KeyValue(noteView.translateYProperty(), 5);
    
    //after animation position of the book
    KeyValue kvEndX = new KeyValue(noteView.translateXProperty(), -123);
    KeyValue kvEndY = new KeyValue(noteView.translateYProperty(), -18);

    //the size of the book (the zoom in animtion)
    KeyValue kvStart = new KeyValue(noteView.fitHeightProperty(), 0);
    KeyValue kvEnd = new KeyValue(noteView.fitHeightProperty(), 422);
    
    //the main keyframe for timing the things above
    KeyFrame kfStart = new KeyFrame(Duration.ZERO, kvStartX, kvStartY, kvStart);
    KeyFrame kfEnd = new KeyFrame(Duration.seconds(1), kvEndX, kvEndY, kvEnd);

    timeline.getKeyFrames().addAll(kfStart, kfEnd);

    //execute after zoom-in
    timeline.getKeyFrames().add(
        new KeyFrame(Duration.seconds(0.99), event -> {

          //Removing button
          Parent parent = noteView.getParent();
          ((Pane) parent).getChildren().remove(noteView);

          FavBookmark();
          displaySavedWeekly();

          //Executing BookAnimation class and method in it.
          animation.openAnimationBook("/assets/stageTwo/market/noteAnimation", 
              false, 24, ".gif", -123, 422);

          //stop button levitation animation
          noteBookTransition.stop();

          //after animation activate a weeks bookmarks
          weeklyOne();
          weeklyTwo();
          weeklyThree();
          weeklyFour();

          //Start Play a things inside of appearAfterOpen
          allRecipes();
            

        })
    );

    Buttons MainButtons = new Buttons(root);

    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));

    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));

    //that's a action of the button
    noteBookButton.setOnAction(event -> {
      timeline.play();
      removeSections(root, "DeleteStartButton");
    });
  }

  private void weeklyOne() {
    // Load the background image (open book).

    Image weekly = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/cafeshop/bookMarkWeek1.png"
    ));
    Image weeklyActive = new Image(MarketView.class.getResourceAsStream(
      "/assets/stageTwo/cafeshop/bookMarkWeek1Active.png"
  ));
    //implamenting style to image
    ImageView weeklyView =  new ImageView(weekly);
    weeklyView.setFitHeight(70);
    weeklyView.setPreserveRatio(true);
    
    //creating button
    ToggleButton weeklyButton = new ToggleButton();
    weeklyButton.setGraphic(weeklyView);
    weeklyButton.getStyleClass().add("clearButton");

    // Initial translation positions based on initial margins
    weeklyButton.setTranslateX(140); //129 is a pixel for covering a line
    weeklyButton.setTranslateY(-120); // 75 pixel lower to make a same gap

    toggleButtons.add(weeklyButton);

    //On regular and on active basis
    weeklyButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        weeklyView.setImage(weeklyActive);
        weeklyButton.setTranslateX(150);
      } else {
        weeklyView.setImage(weekly);
        weeklyButton.setTranslateX(140);
      }
  });
  
  Timeline appearAfterFlip = new Timeline();
  appearAfterFlip.getKeyFrames().add(
      new KeyFrame(Duration.seconds(1), event -> {
        //This thing
        LocalDate startDateOfWeek1 = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        outline.weeklyOutlinetView(startDateOfWeek1);
        
    })
  );
  
  weeklyButton.setOnAction(event -> {
    //Logic if only allow changing if not already selected
    if (!weeklyButton.isSelected()) {
        weeklyButton.setSelected(true);
        manageToggleButtons(weeklyButton);
    }
    manageToggleButtons(weeklyButton);
    removeSections(root, "DeleteAnimation");
    animation.openAnimationBook("/assets/stageTwo/cafeshop/noteBookPageFlip", false, 24, ".gif", -123, 422);
    appearAfterFlip.play();
    removeSections(root, "Delete");
  });

    //adding button to the scene
    root.getChildren().add(weeklyButton);
  }

  private void weeklyTwo() {
    // Load the background image (open book).
    Image weekly = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/cafeshop/bookMarkWeek2.png"
    ));
    Image weeklyActive = new Image(MarketView.class.getResourceAsStream(
      "/assets/stageTwo/cafeshop/bookMarkWeek2Active.png"
  ));
    //implamenting style to image
    ImageView weeklyView =  new ImageView(weekly);
    weeklyView.setFitHeight(70);
    weeklyView.setPreserveRatio(true);
    
    //creating button
    ToggleButton weeklyButton = new ToggleButton();
    weeklyButton.setGraphic(weeklyView);
    weeklyButton.getStyleClass().add("clearButton");

    // Initial translation positions based on initial margins
    weeklyButton.setTranslateX(140); //129 is a pixel for covering a line
    weeklyButton.setTranslateY(-45); // 75 pixel lower to make a same gap

    //On regular and on active basis
    weeklyButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        weeklyView.setImage(weeklyActive);
        weeklyButton.setTranslateX(150);
      } else {
        weeklyView.setImage(weekly);
        weeklyButton.setTranslateX(140);
      }
  });

  toggleButtons.add(weeklyButton);

  //String weekDay = new String("Week2");
  Timeline appearAfterFlip = new Timeline();
  appearAfterFlip.getKeyFrames().add(
      new KeyFrame(Duration.seconds(1), event -> {
        //This thing
        LocalDate startDateOfWeek2 = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusDays(7);
        outline.weeklyOutlinetView(startDateOfWeek2);
    })
  );
  weeklyButton.setOnAction(event -> {
    //Logic if only allow changing if not already selected
    if (!weeklyButton.isSelected()) {
        weeklyButton.setSelected(true);
        manageToggleButtons(weeklyButton);
    }
    manageToggleButtons(weeklyButton);
    removeSections(root, "DeleteAnimation");
    animation.openAnimationBook("/assets/stageTwo/cafeshop/noteBookPageFlip", false, 24, ".gif", -123, 422);
    appearAfterFlip.play();
    removeSections(root, "Delete");

});
    //adding button to the scene
    root.getChildren().add(weeklyButton);
  }

  private void weeklyThree() {
    // Load the background image (open book).
    Image weekly = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/cafeshop/bookMarkWeek3.png"
    ));
    Image weeklyActive = new Image(MarketView.class.getResourceAsStream(
      "/assets/stageTwo/cafeshop/bookMarkWeek3Active.png"
  ));
    //implamenting style to image
    ImageView weeklyView =  new ImageView(weekly);
    weeklyView.setFitHeight(70);
    weeklyView.setPreserveRatio(true);
    
    //creating button
    ToggleButton weeklyButton = new ToggleButton();
    weeklyButton.setGraphic(weeklyView);
    weeklyButton.getStyleClass().add("clearButton");

    // Initial translation positions based on initial margins
    weeklyButton.setTranslateX(140); //129 is a pixel for covering a line
    weeklyButton.setTranslateY(30); // 75 pixel lower to make a same gap

    //On regular and on active basis
    weeklyButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        weeklyView.setImage(weeklyActive);
        weeklyButton.setTranslateX(150);
      } else {
        weeklyView.setImage(weekly);
        weeklyButton.setTranslateX(140);
      }
  });

  toggleButtons.add(weeklyButton);
  //String weekDay = new String("Week3");
  Timeline appearAfterFlip = new Timeline();
  appearAfterFlip.getKeyFrames().add(
      new KeyFrame(Duration.seconds(1), event -> {
        //This thing
        LocalDate startDateOfWeek3 = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusDays(14);
        outline.weeklyOutlinetView(startDateOfWeek3);
    })
  );
  weeklyButton.setOnAction(event -> {
    //Logic if only allow changing if not already selected
    if (!weeklyButton.isSelected()) {
        weeklyButton.setSelected(true);
        manageToggleButtons(weeklyButton);
    }
    manageToggleButtons(weeklyButton);
    removeSections(root, "DeleteAnimation");
    animation.openAnimationBook("/assets/stageTwo/cafeshop/noteBookPageFlip", false, 24, ".gif", -123, 422);
    appearAfterFlip.play();
    removeSections(root, "Delete"); // Deletes nodes
});
    //adding button to the scene
    root.getChildren().add(weeklyButton);
  }

  private void weeklyFour() {
    // Load the background image (open book).
    Image weekly = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/cafeshop/bookMarkWeek4.png"
    ));
    Image weeklyActive = new Image(MarketView.class.getResourceAsStream(
      "/assets/stageTwo/cafeshop/bookMarkWeek4Active.png"
  ));
    //implamenting style to image
    ImageView weeklyView =  new ImageView(weekly);
    weeklyView.setFitHeight(70);
    weeklyView.setPreserveRatio(true);
    
    //creating button
    ToggleButton weeklyButton = new ToggleButton();
    weeklyButton.setGraphic(weeklyView);
    weeklyButton.getStyleClass().add("clearButton");

    // Initial translation positions based on initial margins
    weeklyButton.setTranslateX(140); //129 is a pixel for covering a line
    weeklyButton.setTranslateY(105); // 75 pixel lower to make a same gap

    //On regular and on active basis
    weeklyButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        weeklyView.setImage(weeklyActive);
        weeklyButton.setTranslateX(150);
      } else {
        weeklyView.setImage(weekly);
        weeklyButton.setTranslateX(140);
      }
  });

  toggleButtons.add(weeklyButton);

  
  Timeline appearAfterFlip = new Timeline();
  appearAfterFlip.getKeyFrames().add(
      new KeyFrame(Duration.seconds(1), event -> {
        //This thing
        LocalDate startDateOfWeek4 = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusDays(21);
        outline.weeklyOutlinetView(startDateOfWeek4);
    })
  );
  weeklyButton.setOnAction(event -> {
    //Logic if only allow changing if not already selected
    if (!weeklyButton.isSelected()) {
        weeklyButton.setSelected(true);
        manageToggleButtons(weeklyButton);
    }
    manageToggleButtons(weeklyButton);
    removeSections(root, "DeleteAnimation"); //Deletes Animation nodes #Important placement
    animation.openAnimationBook("/assets/stageTwo/cafeshop/noteBookPageFlip", false, 24, ".gif", -123, 422);
    appearAfterFlip.play();
    removeSections(root, "Delete"); //Deletes all other nodes
});
    //adding button to the scene
    root.getChildren().add(weeklyButton);
  }

  private void FavBookmark() {
    // Load the background image (open book).
    Image favBook = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/cafeshop/bookMarkFavoriteActive.png"
    ));
    //implamenting style to image
    ImageView favBookView =  new ImageView(favBook);
    favBookView.setFitHeight(40);
    favBookView.setPreserveRatio(true);
    
    //creating button
    ToggleButton favBookButton = new ToggleButton();
    favBookButton.setGraphic(favBookView);
    favBookButton.getStyleClass().add("clearButton");
    favBookButton.setSelected(true);

    // Initial translation positions based on initial margins
    favBookButton.setTranslateX(60);
    favBookButton.setTranslateY(-190); 

    //On regular and on active basis
    favBookButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        favBookButton.setTranslateY(-190);
      } else {
        favBookButton.setTranslateY(-180);
      }
  });

  toggleButtons.add(favBookButton);

    //Adding label
    Label favlLabel = new Label("Favorite Recipes");
    favlLabel.setTranslateX(0);
    favlLabel.setTranslateY(-125);

    //set Font
    favlLabel.setFont(pixelFont);
    // Create an ObservableList to hold the recipes
    ObservableList<String> fav = FXCollections.observableArrayList();   
    ListView<String> listViewe = new ListView<>();

    // Bind the ObservableList to the ListView
    listViewe.setItems(fav);

    RecipeHandlerModel recipeModel = new RecipeHandlerModel();
    UserDataHandler userHandler = new UserDataHandler();
    int userId = userHandler.getUserIdFromUsername(username);

    List<String> favnames = recipeModel.getFavoriteRecipesForUser(userId); 

    fav.addAll(favnames);

    VBox layout = new VBox();
    layout.getStyleClass().add("transparent-container");
    // layout.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    layout.getChildren().add(listViewe);
    StackPane.setMargin(layout, new Insets(200, 290, 170, 310));

  Timeline appearAfterFlip = new Timeline();
  appearAfterFlip.getKeyFrames().add(
      new KeyFrame(Duration.seconds(1), event -> {
        removeSections(root, "Delete");
        root.getChildren().addAll(layout, favlLabel);
    })
  );

  favBookButton.setOnAction(event -> {
    //Logic if only allow changing if not already selected
    if (!favBookButton.isSelected()) {
        favBookButton.setSelected(true);
        manageToggleButtons(favBookButton);
    }
    
    manageToggleButtons(favBookButton);
    removeSections(root, "DeleteAnimation");
    animation.openAnimationBook("/assets/stageTwo/cafeshop/noteBookPageFlip", false, 24, ".gif", -123, 422);

    appearAfterFlip.play();
});

    //adding button to the scene
    root.getChildren().add(favBookButton);
  }

  private void displaySavedWeekly() {
    // Load the background image (open book).
    Image BookmarkSaved = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/cafeshop/bookMarkSavedActive.png"
    ));
    //implamenting style to image
    ImageView BookmarkSavedView =  new ImageView(BookmarkSaved);
    BookmarkSavedView.setFitHeight(40);
    BookmarkSavedView.setPreserveRatio(true);
    
    //creating button
    ToggleButton BookmarkSavedButton = new ToggleButton();
    BookmarkSavedButton.setGraphic(BookmarkSavedView);
    BookmarkSavedButton.getStyleClass().add("clearButton");
    BookmarkSavedButton.setSelected(true);

    // Initial translation positions based on initial margins
    BookmarkSavedButton.setTranslateX(-10);
    BookmarkSavedButton.setTranslateY(-180); 

    //On regular and on active basis
    BookmarkSavedButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        BookmarkSavedButton.setTranslateY(-190);
      } else {
        BookmarkSavedButton.setTranslateY(-180);
      }
  });

  toggleButtons.add(BookmarkSavedButton);

  Label WeekSLabel = new Label("User saved weekly list");
  WeekSLabel.setId("Delete");
  WeekSLabel.setTranslateX(0);
  WeekSLabel.setTranslateY(-125);

  //set Font
  WeekSLabel.setFont(pixelFont);
  // Create an ObservableList to hold the recipes
  ObservableList<String> wekS = FXCollections.observableArrayList();
  
  ListView<String> listViewe = new ListView<>();

  // Bind the ObservableList to the ListView
  listViewe.setItems(wekS);

 

  VBox layout = new VBox();
  layout.setId("Delete");
  layout.getStyleClass().add("transparent-container");
  // layout.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
  layout.getChildren().add(listViewe);
  StackPane.setMargin(layout, new Insets(200, 290, 170, 310));

  Timeline appearAfterFlip = new Timeline();
  appearAfterFlip.getKeyFrames().add(
      new KeyFrame(Duration.seconds(1), event -> {
        removeSections(root, "Delete");
        root.getChildren().addAll(layout, WeekSLabel);
    })
  );

  BookmarkSavedButton.setOnAction(event -> {
    //Logic if only allow changing if not already selected
    if (!BookmarkSavedButton.isSelected()) {
        BookmarkSavedButton.setSelected(true);
        manageToggleButtons(BookmarkSavedButton);
    }
    manageToggleButtons(BookmarkSavedButton);
    animation.openAnimationBook("/assets/stageTwo/cafeshop/noteBookPageFlip", false, 24, ".gif", -123, 422);
    refreshWeeklyRecipesListView(wekS);  // Call to refresh the list
    appearAfterFlip.play();
});

    //adding button to the scene
    root.getChildren().add(BookmarkSavedButton);
  }

  /**
 * Fetches the saved weekly recipes and updates the provided ObservableList.
 */
private void refreshWeeklyRecipesListView(ObservableList<String> wekS) {
  RecipeHandlerModel recipeModel = new RecipeHandlerModel();
  UserDataHandler userHandler = new UserDataHandler();
  int userId = userHandler.getUserIdFromUsername(username);
  List<String> recipes = recipeModel.getWeeklyRecipesForUser(userId);
  wekS.setAll(recipes);
}

  private void weeklyListView() {
    Label weeklyDinnerLabel = new Label("Recipes List");

    weeklyDinnerLabel.setTranslateX(0);
    weeklyDinnerLabel.setTranslateY(-125);

    //set Font
    weeklyDinnerLabel.setFont(pixelFont);

  

    // Setting a placeholder for the empty list
    Label placeholderLabel = new Label("Problem with database, Check your connection");
    placeholderLabel.setFont(pixelFont);
    placeholderLabel.setWrapText(true);
    placeholderLabel.setMaxWidth(Double.MAX_VALUE);

    ListView<String> listView = new ListView<>();

    //Set style
    listView.setPlaceholder(placeholderLabel);

    // Create an ObservableList to hold the recipes
    ObservableList<String> recipes = FXCollections.observableArrayList();

    // Bind the ObservableList to the ListView
    listView.setItems(recipes);

    RecipeHandlerModel recipeHandlerModel = new RecipeHandlerModel();
    
    List<String> recipeNames = recipeHandlerModel.showAllRecipeNames();  


    recipes.addAll(recipeNames);


    VBox layout = new VBox();
    layout.getStyleClass().add("transparent-container");
    // layout.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    layout.getChildren().add(listView);
    StackPane.setMargin(layout, new Insets(200, 290, 170, 310));

    root.getChildren().addAll(layout, weeklyDinnerLabel);
  }
  public void removeSections(StackPane stackPane, String id) {
    stackPane.getChildren().removeIf(node -> id.equals(node.getId()));
  }

  private void handleSettingButtonInLoginScene(ActionEvent event) {
    System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("StageTwoCafeshop");
    root.getChildren().add(chatBox);
  }
}
