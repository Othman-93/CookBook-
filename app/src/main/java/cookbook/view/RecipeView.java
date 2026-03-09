package cookbook.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cookbook.controller.RecipeController;
import cookbook.model.CommentsDataHandler;
import cookbook.model.RecipeHandlerModel;
import cookbook.model.UserDataHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * This class is for the Recipe display UI.
 */
public class RecipeView {

  // Attributes.
  private StackPane root;
  private String username;
  private String recipeName;
  private boolean isFavorite;
  private Label ingredientsLabel;
  private TextField tagTextField;
  private HBox tagsContainer;
  private ScrollPane commentsScrollPane;
  private RecipeController recipeController;
  private UserDataHandler userHandler;
  private RecipeHandlerModel recipeHandlerModel;
  private CommentsDataHandler commentsDataHandler;
  private BookAnimation animation;

  /**
   * RecipeView class constructor.
   */
  public RecipeView(StackPane root, String username, String recipeName) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.username = username;
    this.recipeName = recipeName;
    this.tagsContainer = new HBox(5);
    this.recipeController = new RecipeController();
    this.userHandler = new UserDataHandler();
    this.recipeHandlerModel = new RecipeHandlerModel();
    this.commentsDataHandler = new CommentsDataHandler();
  }

  /**
   * This method displays a recipe to the user.
   */
  public void displayRecipeView() {
    // Add "Back" button to root StackPane.
    root.getChildren().add(createBackButton(60,
                                            // top, right, bottom, left.
                                            new int[] {0, 0, 0, 25}  // Insets.
    ));
    // Add recipe name Label to root StackPane.
    root.getChildren().add(createRecipeNameLabel(250, 60,
                                                 // top, right, bottom, left.
                                                 new int[] {105, 0, 0, 126},  // Insets.
                                                 22
    ));
    // Add favorite ToggleButton to root StackPane.
    root.getChildren().add(createFavoriteButton(30,
                                                // top, right, bottom, left.
                                                new int[] {100, 55, 0, 0}  // Insets.
    ));
    // Add servings ComboBox to root StackPane.
    root.getChildren().add(createServingsDropDown(250, 100,
                                                  // top, right, bottom, left.
                                                  new int[] {175, 0, 0, 126},  // Insets.
                                                  16
    ));
    // Add recipe ingredients ScrollPane to root StackPane.
    root.getChildren().add(createIngredientsScrollPane(250, 233,
                                                       // top, right, bottom, left.
                                                       new int[] {52, 0, 0, 126},  // Insets.
                                                       16
    ));
    // Add tags ScrollPane to root StackPane.
    root.getChildren().add(createTagsScrollPane(250, 50,
                                                // top, right, bottom, left.
                                                new int[] {0, 0, 100, 126},  // Insets.
                                                12
    ));
    // Add the add tag Button to root StackPane.
    root.getChildren().add(createAddTagButton(25,
                                              // top, right, bottom, left.
                                              new int[] {0, 86, 120, 0}  // Insets.
    ));
    // Add new tag TextField to root StackPane.
    tagTextField = createAddTagTextField(80, 25,
                                         // top, right, bottom, left.
                                         new int[] {0, 190, 120, 0},  // Insets.
                                         12);
    root.getChildren().add(tagTextField);
    // Add recipe description ScrollPane to root StackPane.
    root.getChildren().add(createDescriptionScrollPane(250, 400,
                                                       // top, right, bottom, left.
                                                       new int[] {105, 112, 160, 0},  // Insets.
                                                       14
    ));
    // Add recipe comments ToggleButton to root StackPane.
    root.getChildren().add(createCommentsButton(250, 50,
                                                // top, right, bottom, left.
                                                new int[] {0, 112, 100, 0},  // Insets.
                                                16
    ));
    // Add recipe comments ScrollPane to root StackPane.
    commentsScrollPane = createCommentsScrollPane(250, 400,
                                                    // top, right, bottom, left.
                                                    new int[] {105, 112, 150, 0},  // Insets.
                                                    14);
    root.getChildren().add(commentsScrollPane);
  }

  /**
   * This method creates the back button.
   *
   * @param height button height.
   * @param margins button margins.
   * @return the back button.
   */
  private Button createBackButton(double height, int[] margins) {
    // Load left arrow image.
    Image backImage = new Image(getClass().getResourceAsStream(
        "/assets/buttons/arrowLeft.png"
        ));
    // Create ImageView.
    ImageView backView = createImageView(backImage, height);
    // Create Button.
    Button backButton = new Button();
    // Style it.
    backButton.setGraphic(backView);
    backButton.setId("Delete");
    backButton.getStyleClass().add("clearButton");
    backButton.setStyle("-fx-padding: 0;");
    // Align button within StackPane.
    StackPane.setAlignment(backButton, Pos.CENTER_LEFT);
    StackPane.setMargin(backButton, new Insets(margins[0], margins[1], margins[2], margins[3]));

    Timeline appearAfterOpen = new Timeline();
    appearAfterOpen.getKeyFrames().add(
        new KeyFrame(Duration.seconds(2), event -> {
          //This thing
          BrowseRecipesView browseRecipesView = new BrowseRecipesView(root, username);
          browseRecipesView.createBrowseRecipesScreen();
      })
    );

    // Logic for pressing button.
    backButton.setOnAction(event -> {
      removeSections(root, "Delete");
      removeSections(root, "DeleteAnimation");
      animation.openPageAnimation("assets/animation/pageFlipAnimation", 522);
      appearAfterOpen.play();
    });

    return backButton;
  }

  /**
   * This method creates the recipe name Label.
   *
   * @param width label width.
   * @param height label height.
   * @param margins label margins.
   * @param fontSize label font size.
   * @return recipe name Label.
   */
  private Label createRecipeNameLabel(double width, double height, int[] margins, int fontSize) {
    // Create Label.
    Label recipeNameLabel = new Label(recipeName);
    recipeNameLabel.setWrapText(true);
    // Center text.
    recipeNameLabel.setAlignment(Pos.CENTER);
    // CSS styling.
    recipeNameLabel.getStyleClass().add("recipe-name-label");
    // Set font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    recipeNameLabel.setFont(pixelFont);
    // Set Label size.
    recipeNameLabel.setPrefSize(width, height);
    recipeNameLabel.setMaxWidth(width);
    recipeNameLabel.setMaxHeight(height);

    // Align Label within StackPane.
    StackPane.setAlignment(recipeNameLabel, Pos.TOP_LEFT);
    StackPane.setMargin(recipeNameLabel, new Insets(margins[0], margins[1], margins[2], margins[3])
    );

    return recipeNameLabel;
  }

  /**
   * This method creates the favorite recipe button.
   *
   * @param height button height.
   * @param margins button margins.
   * @return favorite recipe button.
   */
  private ToggleButton createFavoriteButton(double height, int[] margins) {
    // Load Star images.
    Image starImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/market/buttons/favoriteStar.png"
        ));
    Image activeStarImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/market/buttons/favoriteStarActive.png"
        ));
    // ImageViews for star images.
    ImageView starView = createImageView(starImage, height);
    ImageView activeStarView = createImageView(activeStarImage, height);

    // Create ToggleButton.
    ToggleButton favoriteButton = new ToggleButton();
    // CSS styling.
    favoriteButton.getStyleClass().add("clearButton");
    favoriteButton.setStyle("-fx-padding: 0;");

    // Set isFavorite to true or false depending on database.
    isFavorite = recipeController.isFavorite(username, recipeName);
    // Set initial button graphic depending on favorite or not.
    if (isFavorite) {
      favoriteButton.setGraphic(activeStarView);
    } else if (!isFavorite) {
      favoriteButton.setGraphic(starView);
    }

    // Add and remove from favorites in db.
    favoriteButton.setOnAction(event -> {
      int userId = userHandler.getUserIdFromUsername(username);
      System.out.println(username);
      if (isFavorite) {
        recipeController.removeFromFavorites(userId, recipeName);
        isFavorite = false;
        favoriteButton.setGraphic(starView);
      } else {
        recipeController.addToFavorites(userId, recipeName);
        isFavorite = true;
        favoriteButton.setGraphic(activeStarView);
      }
    });

    // Align ToggleButton within StackPane.
    StackPane.setAlignment(favoriteButton, Pos.TOP_CENTER);
    StackPane.setMargin(favoriteButton, new Insets(margins[0], margins[1], margins[2], margins[3]));


    return favoriteButton;
  }

  /**
   * This method creates the servings dropdown/ComboBox.
   *
   * @param width ComboBox width.
   * @param height ComboBox height.
   * @param margins ComboBox margins.
   * @param fontSize ComboBox font size.
   * @return recipe servings ComboBox.
   */
  private ComboBox<String> createServingsDropDown(double width, double height,
                                                  int[] margins, int fontSize) {
    // Create ComboBox.
    ComboBox<String> comboBox = new ComboBox<>();
    // CSS styling.
    comboBox.getStyleClass().add("servings-combo-box");
    // Add items.
    int recipeId = recipeHandlerModel.getRecipeIdByName(recipeName);
    int defaultServings = recipeHandlerModel.getOriginalServings(recipeId);
    // Check if default serving abnormal or not.
    if (defaultServings == 2 || defaultServings == 4 || defaultServings == 6 || defaultServings == 8) {
      comboBox.getItems().addAll("2", "4", "6", "8");
    } else {
      comboBox.getItems().addAll("2", "4", "6", "8", String.valueOf(defaultServings));
    }
    comboBox.setValue(String.valueOf(defaultServings));
    // Set ComboBox size.
    comboBox.setPrefWidth(width);
    comboBox.setMaxWidth(width);
    // Align ComboBox within StackPane.
    StackPane.setAlignment(comboBox, Pos.TOP_LEFT);
    StackPane.setMargin(comboBox, new Insets(margins[0], margins[1], margins[2], margins[3]));

    // Font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);

    // ComboBox cell factory.
    comboBox.setCellFactory(lv -> new ListCell<String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setText(null);
        } else {
          setText(item);
          setFont(pixelFont);
        }
      }
    });

    // ComboBox button cell.
    comboBox.setButtonCell(new ListCell<String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            setText("Servings: " + item);
            setFont(pixelFont);
            setStyle("-fx-padding: 2 2 2 4px;");
        }
      }
    });

    // Start with initial value default servings.
    comboBox.setValue(String.valueOf(defaultServings));
    // Add listener to ComboBox
    comboBox.setOnAction(event -> {
      int newServings = Integer.parseInt(comboBox.getValue());
      Map<String, Map<String, Object>> adjustedIngredients =
          recipeHandlerModel.adjustRecipeServingsAndGetIngredients(recipeName, newServings);
      // Handle the adjusted ingredients (e.g., display them)

      // Update the ingredients view with the adjusted ingredients
      updateIngredientsView(adjustedIngredients);
    });

    return comboBox;
  }

  private void updateIngredientsView(Map<String, Map<String, Object>> adjustedIngredients){
    // Concatenate all ingredients into a single string
    StringBuilder sb = new StringBuilder();
    adjustedIngredients.forEach((ingredient, quantityWithUnit) -> {
      Double quantity = (Double) quantityWithUnit.get("quantity");
      String unit = (String) quantityWithUnit.get("unit");
      sb.append("\n").append(quantity).append(" ").append(unit).append(" ").append(ingredient);
    });
    // Update the text of the ingredients Label
    ingredientsLabel.setText(sb.toString().trim());
  }


  /**
   * This method creates the recipe ingredients ScrollPane.
   *
   * @param width ScrollPane width.
   * @param height ScrollPane height.
   * @param margins ScrollPane margins.
   * @param fontSize ScrollPane font size.
   * @return recipe ingredients ScrollPane.
   */
  private ScrollPane createIngredientsScrollPane(double width, double height,
                                                 int[] margins, int fontSize) {

    int recipeId = recipeHandlerModel.getRecipeIdByName(recipeName);
    int defaultServings = recipeHandlerModel.getOriginalServings(recipeId);

     Map<String, Map<String, Object>> adjustedIngredients =
            recipeHandlerModel.adjustRecipeServingsAndGetIngredients(recipeName, defaultServings);

 StringBuilder sb = new StringBuilder();
 adjustedIngredients.forEach((ingredient, quantityWithUnit) -> {
  Double quantity = (Double) quantityWithUnit.get("quantity");
  String unit = (String) quantityWithUnit.get("unit");
  sb.append("\n").append(quantity).append(" ").append(unit).append(" ").append(ingredient);
});





    // Label for ingredients text.
    ingredientsLabel = new Label(sb.toString().trim());
    // Set text font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    ingredientsLabel.setFont(pixelFont);

    // ScrollPane for containing the Label.
    ScrollPane scrollPane = new ScrollPane();
    // CSS styling.
    scrollPane.getStyleClass().add("recipe-scrollpane");
    // Set ScrollPane size.
    scrollPane.setPrefSize(width, height);
    scrollPane.setMaxHeight(height);
    scrollPane.setMaxWidth(width);
    // Add Label to ScrollPane.
    scrollPane.setContent(ingredientsLabel);

    scrollPane.setId("ingredientsScrollPane"); // Set ID for ScrollPane
    // Align ScrollPane within StackPane.
    StackPane.setAlignment(scrollPane, Pos.CENTER_LEFT);
    StackPane.setMargin(scrollPane, new Insets(margins[0], margins[1], margins[2], margins[3]));

    return scrollPane;
  }

  /**
   * This method creates the recipe tags ScrollPane.
   *
   * @param width ScrollPane width.
   * @param height ScrollPane height.
   * @param margins ScrollPane margins.
   * @param fontSize ScrollPane font size.
   * @return recipe tags ScrollPane.
   */
  private ScrollPane createTagsScrollPane(double width, double height,
                                          int[] margins, int fontSize) {
    // List of the tags.
    List<String> tags = new ArrayList<>();
    tags = recipeController.getTagsForRecipe(recipeName);

    // Iterate through tags.
    for (String tag : tags) {
      // Add tag to tagsContainer HBox.
      addTagLabel(tag, fontSize);
    }

    // ScrollPane for containing the Label.
    ScrollPane scrollPane = new ScrollPane();
    // CSS styling.
    scrollPane.getStyleClass().add("recipe-scrollpane");
    scrollPane.setStyle("-fx-padding: 5 114 5 5px;");
    // Set ScrollPane size.
    scrollPane.setPrefSize(width, height);
    scrollPane.setMaxHeight(height);
    scrollPane.setMaxWidth(width);
    // Add tagsContainer HBox to ScrollPane.
    scrollPane.setContent(tagsContainer);

    // Align ScrollPane within StackPane.
    StackPane.setAlignment(scrollPane, Pos.BOTTOM_LEFT);
    StackPane.setMargin(scrollPane, new Insets(margins[0], margins[1], margins[2], margins[3]));

    return scrollPane;
  }

  /**
   * This method creates the add tag to recipe button.
   *
   * @param height Button height.
   * @param margins Button margins.
   * @return add recipe tag Button.
   */
  private Button createAddTagButton(double height, int[] margins) {
    // Load image.
    Image addTagImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/plusButtonLibrary.png"
    ));
    // Create ImageView.
    ImageView addTagView = createImageView(addTagImage, height);
    // Create button.
    Button addTagButton = new Button();
    addTagButton.setGraphic(addTagView);
    addTagButton.getStyleClass().add("clearButton");
    addTagButton.setStyle("-fx-padding: 0;");

    // Logic for pressing button.
    addTagButton.setOnAction(event -> {
      // Add new tag to tagsContainer HBox.
      addTagLabel(tagTextField.getText(), 12);
      // Add new tag to db.
      recipeController.addTagToRecipe(tagTextField.getText(), this.recipeName);
      tagTextField.clear();
    });

    // Align Button within StackPane.
    StackPane.setAlignment(addTagButton, Pos.BOTTOM_CENTER);
    StackPane.setMargin(addTagButton, new Insets(margins[0], margins[1], margins[2], margins[3]));

    return addTagButton;
  }

  /**
   * This method creates the add tag TextField.
   *
   * @param width TextField width.
   * @param height TextField height.
   * @param margins TextField margins.
   * @param fontSize TextField font size.
   *
   * @return TextField for adding tag to recipe.
   */
  private TextField createAddTagTextField(double width, double height,
                                          int[] margins, int fontSize) {
    // TextField for adding new tag name.
    TextField textField = new TextField();
    // Set TextField size.
    textField.setPrefSize(width, height);
    textField.setMaxWidth(width);
    textField.setMaxHeight(height);
    // Set text font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    textField.setFont(pixelFont);

    // Align TextField within StackPane.
    StackPane.setAlignment(textField, Pos. BOTTOM_CENTER);
    StackPane.setMargin(textField, new Insets(margins[0], margins[1], margins[2], margins[3]));

    return textField;
  }

  /**
   * This method creates the recipe description ScrollPane.
   *
   * @param width ScrollPane width.
   * @param height ScrollPane height.
   * @param margins ScrollPane margins.
   * @param fontSize ScrollPane font size.
   * @return recipe description ScrollPane.
   */
  private ScrollPane createDescriptionScrollPane(double width, double height,
                                                 int[] margins, int fontSize) {
    //String instructions to be added to Label.
    String description = "";

    List<String> instructions = new ArrayList<>();
    instructions = recipeController.getInstructions(recipeName);

    for (String instruction : instructions) {
      description += instruction;
      description += "\n\n"; //new line.
    }

    // Label for recipe description text.
    Label descriptionLabel = new Label(description);
    descriptionLabel.setWrapText(true);
    // Set description font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    descriptionLabel.setFont(pixelFont);

    // ScrollPane for containing the Label.
    ScrollPane scrollPane = new ScrollPane();
    // CSS styling.
    scrollPane.getStyleClass().add("recipe-scrollpane");
    // Set ScrollPane size.
    scrollPane.setPrefSize(width, height);
    scrollPane.setMaxHeight(height);
    scrollPane.setMaxWidth(width);
    // Add Label to ScrollPane.
    scrollPane.setContent(descriptionLabel);
    // Fit width of content to width of ScrollPane.
    scrollPane.setFitToWidth(true);

    // Align ScrollPane within StackPane.
    StackPane.setAlignment(scrollPane, Pos.CENTER_RIGHT);
    StackPane.setMargin(scrollPane, new Insets(margins[0], margins[1], margins[2], margins[3]));

    return scrollPane;
  }

  /**
   * This method creates the recipe comments ToggleButton.
   *
   * @param width ToggleButton width.
   * @param height ToggleButton height.
   * @param margins ToggleButton margins.
   * @param fontSize ToggleButton font size.
   * @return recipe comments ToggleButton.
   */
  private ToggleButton createCommentsButton(double width, double height,
                                            int[] margins, int fontSize) {
    // Create ToggleButton.
    ToggleButton commentsButton = new ToggleButton("Show Comments >");
    // Set font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    commentsButton.setFont(pixelFont);
    // CSS styling.
    commentsButton.getStyleClass().add("recipe-comments-button");
    // Set ToggleButton size.
    commentsButton.setPrefSize(width, height);
    // Align ToggleButton within StackPane.
    StackPane.setAlignment(commentsButton, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(commentsButton, new Insets(margins[0], margins[1], margins[2], margins[3]));
    // Button on click event. Decides whether comments ScrollPane visible or not.
    commentsButton.setOnAction(event -> {
      if (commentsButton.isSelected()) {
        commentsButton.setText("Hide Comments ^");
        commentsScrollPane.setVisible(true);
      } else {
        commentsButton.setText("Show Comments >");
        commentsScrollPane.setVisible(false);
      }
    });
    return commentsButton;
  }

  private ScrollPane createCommentsScrollPane(double width, double height,
                                              int[] margins, int fontSize) {
    // Label for recipe comments text.
    Label commentsLabel = new Label();
    // Get comments.
    int recipeId = recipeHandlerModel.getRecipeIdByName(recipeName);
    List<String> comments = commentsDataHandler.getLatestComments(recipeId);
    String commentsLabelText = "";
    // Iterate over comments to add to single String.
    for (String comment : comments) {
      commentsLabelText += comment + "\n\n";
    }
    if (commentsLabelText == "") {
      commentsLabelText += "No comments";
    }
    // Set text.
    commentsLabel.setText(commentsLabelText);
    commentsLabel.setWrapText(true);
    // Set description font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    commentsLabel.setFont(pixelFont);

    // ScrollPane for containing the Label.
    ScrollPane scrollPane = new ScrollPane();
    // CSS styling.
    scrollPane.getStyleClass().add("recipe-scrollpane");
    scrollPane.setStyle("-fx-border-radius: 12px 12px 0 0; -fx-background-radius: 12px 12px 0 0;");
    // Set ScrollPane size.
    scrollPane.setPrefSize(width, height);
    scrollPane.setMaxHeight(height);
    scrollPane.setMaxWidth(width);
    // Add Label to ScrollPane.
    scrollPane.setContent(commentsLabel);
    // Fit width of content to width of ScrollPane.
    scrollPane.setFitToWidth(true);

    // Align ScrollPane within StackPane.
    StackPane.setAlignment(scrollPane, Pos.CENTER_RIGHT);
    StackPane.setMargin(scrollPane, new Insets(margins[0], margins[1], margins[2], margins[3]));

    // Hide until comments ToggleButton is clicked.
    scrollPane.setVisible(false);

    return scrollPane;
  }

  /**
   * This method creates an ImageView.
   *
   * @param image the image to use.
   * @return an ImageView with presets.
   */
  private ImageView createImageView(Image image, double height) {
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(height);
    imageView.setPreserveRatio(true);
    return imageView;
  }

  /**
   * This method adds a new tag Label.
   *
   * @param tag tag to add.
   * @param fontSize Label font size.
   */
  private void addTagLabel(String tag, int fontSize) {
    // Create Label for each tag.
    Label tagLabel = new Label(tag);
    // CSS styling.
    tagLabel.getStyleClass().add("recipe-tags-label");
    // Set text font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    tagLabel.setFont(pixelFont);
    // Add Label to tagsContainer HBox.
    tagsContainer.getChildren().add(tagLabel);
  }

    public void removeSections(StackPane stackPane, String id) {
    stackPane.getChildren().removeIf(node -> id.equals(node.getId()));
  }

}
