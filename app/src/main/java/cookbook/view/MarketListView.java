package cookbook.view;



import cookbook.model.DatabaseUtilProviderImpl;
import cookbook.model.RecipeHandlerModel;
import cookbook.model.UserDataHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;






/**
 * shopping list ui.
 */

public class MarketListView {
  // attributes 
  private StackPane root;
  private RecipeHandlerModel recipeHandlerModel;
  private List<String> ingredientsList;
  private List<String> shoppingList;
  private ListView<String> ingredientsListView;
  private ListView<String> shoppingListView;
  private List<String> favoriteIngredients;
  private Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 16);
  private final String username;



  /**
   * constructor
   * call methods to add and show ingredients.
   * 
   */
  public MarketListView(StackPane root, String username) {
    this.root = root;
    this.username = username;
    DatabaseUtilProviderImpl databaseUtilProviderImpl = new DatabaseUtilProviderImpl();
    this.recipeHandlerModel = new RecipeHandlerModel();
    this.shoppingList = new ArrayList<>();
    this.favoriteIngredients = new ArrayList<>();
    // Initialize shoppingListView here
    shoppingListView = new ListView<>();  
    shoppingListView.setItems(FXCollections.observableArrayList(shoppingList)); 
  }

  /**
   * create ui for lists. 
   */
  public void setupMarketScene() {

    PauseTransition pause = new PauseTransition(Duration.seconds(2.2));
    pause.setOnFinished(e -> {

      // to contain both lists
      HBox mainContainer = new HBox(40);
      mainContainer.setMaxHeight(390);
      mainContainer.setPadding(new Insets(0, 0, 0, 150));
      // add ingredients list to the left
      mainContainer.getChildren().add(0, ingredientsBox(username));
      // add shoppings list to right
      mainContainer.getChildren().add(1, shoppingBox());

      root.getChildren().add(mainContainer);
    });

    pause.play();
  
  }


  private GridPane ingredientsBox(String username) {
    // gridpane to hold the ingredients list on the left side of the scene
    GridPane leftSide = new GridPane();
    leftSide.setAlignment(Pos.CENTER);
    DatabaseUtilProviderImpl dbProvider = new DatabaseUtilProviderImpl();
    RecipeHandlerModel recipeModel = new RecipeHandlerModel();
    UserDataHandler userHandler = new UserDataHandler();
    int userId = userHandler.getUserIdFromUsername(username);
    

    ingredientsListView = new ListView<>();
    ingredientsListView.setItems(FXCollections.observableArrayList(
        recipeModel.getFavIngredientsNames(userId)));
    ingredientsListView.setPrefWidth(200);
    leftSide.add(ingredientsListView, 0, 0);


    // set font and change colors
    ingredientsListView.setCellFactory(list -> new ListCell<String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
              setText(null);
              setGraphic(null);
          } else {
              Text text = new Text(item);
              text.setFont(pixelFont);
              text.getStyleClass().add("bold");
              setGraphic(text);
  
              // Set color to white initially
              text.setFill(Color.WHITE);
  
              // Change color to red when hovering
              text.setOnMouseEntered(event -> text.setFill(Color.RED));
  
              // Change color back to white when mouse exits
              text.setOnMouseExited(event -> text.setFill(Color.WHITE));
          }
      }
    });

    // Add event handler for double-click on ingredients
    ingredientsListView.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        String selectedIngredient = ingredientsListView.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
          if (shoppingList.contains(selectedIngredient)) {
            // Display message if ingredient already exists in shopping list
            showAlert(Alert.AlertType.INFORMATION, "Information", 
                "Ingredient already in shopping list", 
                              "The ingredient \"" 
                                + selectedIngredient + "\" is already in the shopping list.");
          } else {
            // Add ingredient to shopping list and display message
            addIngredientToShoppingList(selectedIngredient);
            
          }
        }
      }
    });


    return leftSide;
  }




  /**
   * Creates a GridPane for the shopping list on the right side of the scene.
   */
  private GridPane shoppingBox() {
    GridPane rightSide = new GridPane();
    rightSide.setAlignment(Pos.CENTER);

    // Create a ListView to display the shopping list
    shoppingListView = new ListView<>();
    shoppingListView.setItems(FXCollections.observableArrayList(shoppingList));
    shoppingListView.setPrefWidth(200);
    shoppingListView.setPrefHeight(500);
    // shoppingListView.setPadding(new Insets(0, 10, 0, 0));

    // Create buttons for adding, removing, and adding to favorites
    HBox buttonBox = new HBox(10);
    Button addButton = createAddButton(rightSide);
    Button removeButton = createRemoveButton();
    ToggleButton favButton = createFavsButton();
    buttonBox.getChildren().addAll(addButton, removeButton, favButton);
    buttonBox.setAlignment(Pos.CENTER);

    // fix font
    shoppingListView.setCellFactory(list -> new ListCell<String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
              setText(null);
              setGraphic(null);
          } else {
              Text text = new Text(item);
              // Set font
              text.setFont(pixelFont);
              text.getStyleClass().add("bold");
  
              // Bind text color to selection state
              text.fillProperty().bind(
                  Bindings.when(selectedProperty())
                          .then(Color.RED)
                          .otherwise(Color.BLACK)
              );
  
              setGraphic(text);
          }
      }
    });
    

    // Set margins for the button box and shopping list view
    GridPane.setMargin(buttonBox, new Insets(30, 0, 10, 30)); 
    GridPane.setMargin(shoppingListView, new Insets(0, 0, 100, 40)); 

    // Add the button box and shopping list view to the right side GridPane
    rightSide.add(buttonBox, 0, 0);
    rightSide.add(shoppingListView, 0, 1);

    return rightSide;
  }


  private ToggleButton createFavsButton() {
    // load active and non-active button image for favourites ingredients
    Image favBnImage = new Image(getClass().getResourceAsStream(
          "/assets/stageTwo/market/buttons/favoriteStar.png"));
    Image favBnActiveImage = new Image(getClass().getResourceAsStream(
          "/assets/stageTwo/market/buttons/favoriteStarActive.png"));

    // imageview for favorites button
    ImageView favImageView = createImageView(favBnImage, 25);
    ImageView favActiveImageView = createImageView(favBnActiveImage, 25);

    // Button favButton = createButton(favImageView, favActiveImageView);
    ToggleButton favButton = new ToggleButton();
    favButton.setGraphic(favImageView);
    favButton.getStyleClass().add("clearButton");

    // DatabaseUtilProviderImpl dbProvider = new DatabaseUtilProviderImpl();
    RecipeHandlerModel recipeModel = new RecipeHandlerModel();
    UserDataHandler userHandler = new UserDataHandler();
    int userId = userHandler.getUserIdFromUsername(username);


    // Add action for toggling favorites
    favButton.setOnAction(event -> {
      String selectedIngredient = shoppingListView.getSelectionModel().getSelectedItem();
      if (selectedIngredient != null) {
        if (favoriteIngredients != null && favoriteIngredients.contains(selectedIngredient)) {
          favoriteIngredients.remove(selectedIngredient);
          if (recipeModel.removeIngredientFromFavorites(userId, selectedIngredient)) {
            showAlert(Alert.AlertType.INFORMATION, "Favorite Removed", null, 
                          "Ingredient removed from favorites: " + selectedIngredient);
          } else {
            showAlert(Alert.AlertType.ERROR, "Error", null, 
                "Failed to remove ingredient from favorites: " + selectedIngredient);
          }
          favButton.setGraphic(favImageView); 
          // once user removes ingredient from favorites, button becomes "inactive"
        } else {
          favoriteIngredients.add(selectedIngredient);
          if (recipeModel.addIngredientToFavorites(userId, selectedIngredient)) {
            showAlert(Alert.AlertType.INFORMATION, "Favorite Added", null, 
                          "Ingredient added to favorites: " + selectedIngredient);
          } else {
            showAlert(Alert.AlertType.ERROR, "Error", null, 
                          "Failed to add ingredient to favorites: " + selectedIngredient);
          }
          favButton.setGraphic(favActiveImageView); // Set button as active
        }
      } else {
        showAlert(Alert.AlertType.WARNING, "No Selection", null, 
                  "No ingredient selected to add to favorites");
      }
      // Update the button appearance based on the selected ingredient
      favButton.setSelected(favoriteIngredients.contains(selectedIngredient));

      // Refresh the ingredients list view immediately
      refreshIngredientsListView();
      
      // Update the button appearance based on the selected ingredient
      String currentSelectedIngredient = shoppingListView.getSelectionModel().getSelectedItem();
      if (currentSelectedIngredient != null && favoriteIngredients != null
           && favoriteIngredients.contains(currentSelectedIngredient)) {
        favButton.setGraphic(favActiveImageView);
        favButton.setSelected(true);
      } else {
        favButton.setGraphic(favImageView);
        favButton.setSelected(false);
      }
    });
    

    // Add listener for selection change in the ListView
    shoppingListView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
        // Update the button appearance based on the newly selected ingredient
        // if ingredient is fav, button is active
        if (newValue != null && favoriteIngredients != null 
            && favoriteIngredients.contains(newValue)) {
          favButton.setGraphic(favActiveImageView);
          favButton.setSelected(true);
        } else {
          favButton.setGraphic(favImageView);
          favButton.setSelected(false);
        }
      });
    return favButton;
  }

  private void refreshIngredientsListView() {
    // DatabaseUtilProviderImpl dbProvider = new DatabaseUtilProviderImpl();
    RecipeHandlerModel recipeModel = new RecipeHandlerModel();
    UserDataHandler userHandler = new UserDataHandler();
    int userId = userHandler.getUserIdFromUsername(username);

    // Fetch the latest list of favorite ingredients
    List<String> favoriteIngredients = recipeModel.getFavIngredientsNames(userId);

    // Update the ingredients list view
    ingredientsListView.setItems(FXCollections.observableArrayList(favoriteIngredients));
  }


  



  private Button createRemoveButton() {
    // load images for remove button
    Image removeBnImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/market/buttons/minus.png"));
    Image removeBnActiveImage = new Image(getClass().getResourceAsStream(
          "/assets/stageTwo/market/buttons/minusActive.png"));

    // image view for remove button
    ImageView removeBnImageView = createImageView(removeBnImage, 25);
    ImageView removeActiveImageView = createImageView(removeBnActiveImage, 25);

    // create remove button to remove ingredient from shopping list
    Button removeButton = createButton(removeBnImageView, removeActiveImageView);

    removeButton.setOnAction(event -> {
      String selectedIngredient = shoppingListView.getSelectionModel().getSelectedItem();
      if (selectedIngredient != null) {
        shoppingList.remove(selectedIngredient);
        // Update the shopping list view
        shoppingListView.getItems().remove(selectedIngredient);
      } else {
        showAlert(Alert.AlertType.WARNING, "No Ingredient Selected", null, 
                      "No ingredient selected to remove");
      }
    });
    return removeButton;
  }



  private Button createAddButton(GridPane parent) {

    // load images for active and non-active buttons
    Image addBnImage = new Image(getClass().getResourceAsStream(
          "/assets/stageTwo/market/buttons/plus.png"));
    Image addBnActiveImage = new Image(getClass().getResourceAsStream(
          "/assets/stageTwo/market/buttons/plusActive.png"));

    // imageview 
    ImageView addBnView = createImageView(addBnImage, 25);
    ImageView addBnActImageView = createImageView(addBnActiveImage, 25);

    // create add button
    Button addButton = createButton(addBnView, addBnActImageView);

    addButton.setOnAction(event -> {
      TextField textField = new TextField();
      textField.setPromptText("Enter ingredient");
      textField.setMaxWidth(120);
      // Set margins to adjust position
      GridPane.setMargin(textField, new Insets(10, 0, 40, 50));
      // Add TextField below the buttons
      parent.add(textField, 0, 2); 
      
      // Add listener to the TextField
      textField.setOnAction(e -> {
        String ingredient = textField.getText().trim();
        if (!ingredient.isEmpty()) {
          if (!shoppingList.contains(ingredient)) {
            addIngredientToShoppingList(ingredient);
          } else {
            showAlert(Alert.AlertType.WARNING, "Duplicate Ingredient", null, 
                              "The ingredient \""
                               + ingredient + "\" is already in the shopping list.");
          }
        }
        // Remove the TextField after use
        parent.getChildren().remove(textField); 

      });
    });

    return addButton;
  }

  private void addIngredientToShoppingList(String selectedItem) {
    // Add the selected ingredient to the shopping list
    shoppingList.add(selectedItem); 

    // Update the shopping list view
    shoppingListView.getItems().add(selectedItem); 
  }


  private ImageView createImageView(Image image, double height) {
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(height);
    imageView.setPreserveRatio(true);
    return imageView;
  }


  // method to create buttons
  private Button createButton(ImageView buttonView, ImageView hoverButtonView) {
    // Create button.
    Button button = new Button();
    button.setGraphic(buttonView);
    button.getStyleClass().add("clearButton");
    button.setStyle("-fx-padding: 0;");

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

  /**
   * this method fetcjes the neded ingrediamce from the weakly list.
   */
  


  public void initializeWithWeeklyIngredients(String username) {
    // DatabaseUtilProviderImpl databaseUtilProviderImpl = new DatabaseUtilProviderImpl();
    UserDataHandler userDataHandler = new UserDataHandler();
    int userId = userDataHandler.getUserIdFromUsername(username);

    if (userId > 0) { // Check if user ID was found
      Map<String, List<String>> weeklyIngredients = 
            recipeHandlerModel.getWeeklyDinnerListIngredients(userId);
    
      for (List<String> ingredients : weeklyIngredients.values()) {
        for (String ingredient : ingredients) {
          if (!shoppingList.contains(ingredient)) { 
            shoppingList.add(ingredient);
          }
        }
      }
    
      // Update the shopping list view after fetching
      shoppingListView.setItems(FXCollections.observableArrayList(shoppingList));
    } else {
      // Handle case where user ID wasn't found (e.g., display an error message)
      System.err.println("Error: User not found.");
    }
  }

  private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header); // Header can be null
    alert.setContentText(message);
    alert.showAndWait();
  }


  
}