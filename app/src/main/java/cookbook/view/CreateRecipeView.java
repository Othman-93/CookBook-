package cookbook.view;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.regex.Pattern;

import cookbook.controller.RecipeController;
import cookbook.model.Ingredient;
import cookbook.model.RecipeDataProvider;

/**
 * This class is for the create recipe UI.
 */
public class CreateRecipeView {

  // Attributes.
  private StackPane root;
  private String username;
  RecipeController recipeController;
  private TextField recipeNameTextField;
  private TextField ingredientNameTextField;
  private TextField ingredientQuantityTextField;
  private TextField ingredientUnitTextField;
  private ListView<String> ingredientsListView;
  private List<Ingredient> ingredientsList;
  private TextField tagTextField;
  private ListView<String> tagsListView;
  private List<String> tagsList;
  private TextField prepTimeTextField;
  private TextField cookTimeTextField;
  private TextArea instructionTextArea;
  private TextArea shortDescriptionTextArea;
  private BookAnimation animation;

  /**
   * The constructor for the CreateRecipeView class.
   *
   * @param root the StackPane root.
   */
  public CreateRecipeView(StackPane root, String username) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.username = username;
    this.ingredientsList = new ArrayList<>();
    this.tagsList = new ArrayList<>();
    this.recipeController = new RecipeController();
  }

  /**
   * This method displays the create recipe view to the user.
   *
   */
  public void displayCreateRecipeView() {

    // Create recipe name prompt Label.
    Label recipeNamePrompt = createLabel(250, 20,
                                         // top, right, bottom, left.
                                         new int[] {0, 295, 352, 0},  // Insets.
                                         "Recipe Name", 20);
    // Add recipe name Label to root StackPane.
    recipeNamePrompt.setId("Delete");
    root.getChildren().add(recipeNamePrompt);
    // Create recipe name TextField.
    recipeNameTextField = createTextField(250, 30,
                                                    // top, right, bottom, left.
                                                    new int[] {0, 295, 297, 0},  // Insets.
                                                    "", 18
                                                    );
    // Set max character constraint.
    recipeNameTextField.setTextFormatter(maxLengthFormatter(255));
    recipeNameTextField.setMaxWidth(250);
    // CSS styling.
    recipeNameTextField.getStyleClass().addAll("add-recipe-name-textfield");
    // Add recipe name TextField to root StackPane.
    recipeNameTextField.setId("Delete");
    root.getChildren().add(recipeNameTextField);

    // Create add ingredient prompt Label.
    Label ingredientPrompt = createLabel(250, 20,
                                         // top, right, bottom, left.
                                         new int[] {0, 295, 220, 0},  // Insets.
                                         "Add Ingredients (4 Servings)", 16);
    // Add ingredient prompt Label to root StackPane.
    ingredientPrompt.setId("Delete");
    root.getChildren().add(ingredientPrompt);
    // Add the add ingredient HBox to root StackPane.
    root.getChildren().add(createAddIngredientBox(250, 25,
                                                  // top, right, bottom, left.
                                                  new int[] {0, 295, 175, 0},  // Insets.
                                                  14
                                                  ));
    // Add ingredients ListView to root StackPane.
    root.getChildren().add(createIngredientsListView(250, 120,
                                                     // top, right, bottom, left.
                                                     new int[] {0, 295, 30, 0},  // Insets.
                                                     14
                                                     ));

    // Create add tag prompt Label.
    Label tagPrompt = createLabel(250, 20,
                                  // top, right, bottom, left.
                                  new int[] {130, 295, 0, 0},  // Insets.
                                  "Add Tags", 16);
    // Add tag prompt Label to root StackPane.
    tagPrompt.setId("Delete");
    root.getChildren().add(tagPrompt);
    // Add the add tag HBox to root StackPane.
    root.getChildren().add(createAddTagBox(250, 25,
                                           // top, right, bottom, left.
                                           new int[] {175, 295, 0, 0},  // Insets.
                                           14
                                           ));
    // Add tags ListView to root StackPane.
    root.getChildren().add(createTagsListView(250, 50,
                                              // top, right, bottom, left.
                                              new int[] {251, 295, 0, 0},  // Insets.
                                              14
                                              ));

    // Add time info HBox to root StackPane.
    root.getChildren().add(createTimeInfoBox(250, 25,
                                             // top, right, bottom, left.
                                             new int[] {350, 287, 0, 0},  // Insets.
                                             12
                                             ));

    // Create instruction prompt Label.
    Label instrutionPrompt = createLabel(250, 20,
                                  // top, right, bottom, left.
                                  new int[] {0, 0, 360, 325},  // Insets.
                                  "Instruction", 16);
    // Add instruction prompt Label to root StackPane.
    instrutionPrompt.setId("Delete");
    root.getChildren().add(instrutionPrompt);
    // Create recipe instruction TextArea.
    instructionTextArea = createTextArea(250, 190,
                                              // top, right, bottom, left.
                                              new int[] {0, 0, 150, 325},  // Insets.
                                              "write a detailed instruction of the recipe here...",
                                              14);
    // Add recipe instruction TextArea to root StackPane.
    instructionTextArea.setId("Delete");
    root.getChildren().add(instructionTextArea);

    // Create short description prompt Label.
    Label shortDescriptionPrompt = createLabel(250, 20,
                                                // top, right, bottom, left.
                                                new int[] {75, 0, 0, 325},  // Insets.
                                                "Short Description", 16);
    // Add short description prompt Label to root StackPane.
    shortDescriptionPrompt.setId("Delete");
    root.getChildren().add(shortDescriptionPrompt);
    // Create recipe short description TextArea.
    shortDescriptionTextArea = createTextArea(250, 60,
                                                  // top, right, bottom, left.
                                                  new int[] {155, 0, 0, 325},  // Insets.
                                                  "write a short description of the recipe here...",
                                                  14);
    // Add recipe short description TextArea to root StackPane.
    shortDescriptionTextArea.setId("Delete");
    root.getChildren().add(shortDescriptionTextArea);

    // Add buttons VBox to root StackPane.
    root.getChildren().add(createButtonsBox(250, 25,
                                            // top, right, bottom, left.
                                            new int[] {416, 0, 0, 420}  // Insets.
                                            ));
  }

  /**
   * This method creates the HBox for adding ingredient elements.
   *
   * @param width HBox width.
   * @param height HBox height.
   * @param margins HBox margins.
   * @param fontSize font size.
   * @return HBox with add ingredient elements.
   */
  private HBox createAddIngredientBox(double width, double height, int[] margins, int fontSize) {
    // HBox for containing the add ingredient elements.
    HBox addIngredientContainer = new HBox();
    addIngredientContainer.setId("Delete");
    // Set HBox size.
    addIngredientContainer.setPrefSize(width, height);
    addIngredientContainer.setMaxHeight(height);
    addIngredientContainer.setMaxWidth(width);
    // Align HBox.
    StackPane.setMargin(addIngredientContainer,
                        new Insets(margins[0], margins[1], margins[2], margins[3]));

    // TextField for ingredient name.
    ingredientNameTextField = createTextField(130, height,
                                                       // top, right, bottom, left.
                                                       new int[] {0, 0, 0, 0},  // Insets.
                                                       "ingredient...", fontSize
                                                       );
    // Set max character length.
    ingredientNameTextField.setTextFormatter(maxLengthFormatter(255));
    ingredientNameTextField.setStyle("-fx-border-width: 2px 0 0 2px;");
    // TextField for ingredient quantity.
    ingredientQuantityTextField = createTextField(45, height,
                                                          // top, right, bottom, left.
                                                          new int[] {0, 0, 0, 0},  // Insets.
                                                          "qty...", fontSize
                                                          );
    // Set TextFormatter to only accept positive double and with max character length.
    ingredientQuantityTextField.setTextFormatter(positiveDoublesFormatter(100000));
    ingredientQuantityTextField.setStyle("-fx-border-width: 2px 0 0 2px;");
    // TextField for ingredient unit.
    ingredientUnitTextField = createTextField(55, height,
                                                        // top, right, bottom, left.
                                                        new int[] {0, 0, 0, 0},  // Insets.
                                                        "unit...", fontSize
                                                        );
    // Set max character length.
    ingredientUnitTextField.setTextFormatter(maxLengthFormatter(255));
    ingredientUnitTextField.setStyle("-fx-border-width: 2px 2px 0 2px;");

    // Load add button image.
    Image addButtonImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/plusButtonLibrary.png"
        ));
    // Create add button ImageView.
    ImageView addButtonView = createImageView(addButtonImage, height);

    // Create add button.
    Button addButton = new Button();
    addButton.setGraphic(addButtonView);
    addButton.getStyleClass().add("clearButton");
    addButton.setStyle("-fx-padding: 0;");

    // Logic for pressing button.
    addButton.setOnAction(event -> {
      // Check so no field is empty.
      if (!ingredientNameTextField.getText().isEmpty() &&
          !ingredientQuantityTextField.getText().isEmpty() &&
          !ingredientUnitTextField.getText().isEmpty()) {

        String ingredientName = ingredientNameTextField.getText();
        double quantity = Double.parseDouble(ingredientQuantityTextField.getText());
        String unit = ingredientUnitTextField.getText();
        // Create ingredient object and add to ingredienstList.
        Ingredient ingredient = new Ingredient(ingredientName, unit, quantity);
        ingredientsList.add(ingredient);
        // Update ingredient ListView.
        String ingredientInfo = quantity + " " + unit + "\t" + ingredientName;
        updateListView(ingredientsListView, ingredientInfo);
        ingredientNameTextField.clear();
        ingredientQuantityTextField.clear();
        ingredientUnitTextField.clear();
      }
    });

    // Add elements to addIngredientsContainer.
    ingredientNameTextField.setId("Delete");
    ingredientQuantityTextField.setId("Delete");
    ingredientUnitTextField.setId("Delete");
    addButton.setId("Delete");
    addIngredientContainer.getChildren().addAll(ingredientNameTextField,
                                                ingredientQuantityTextField,
                                                ingredientUnitTextField,
                                                addButton);
    return addIngredientContainer;
  }

  /**
   * This method creates a ListView for the added ingredients.
   *
   * @param width ListView width.
   * @param height ListView height.
   * @param margins ListView margins.
   * @param fontSize font size.
   * @return the ingredients ListView.
   */
  private ListView<String> createIngredientsListView(double width, double height, int[] margins, int fontSize) {
    // Instantiate ListView.
    ingredientsListView = new ListView<>();
    ingredientsListView.setId("Delete");
    // CSS styling.
    Label background = new Label();
    background.setId("Delete");
    ingredientsListView.getStyleClass().add("add-recipe-listview");
    background.getStyleClass().add("add-recipe-listview-background");
    // ListView cell factory.
    setCellFactory(ingredientsListView, fontSize);
    // Set ListView and background size.
    ingredientsListView.setPrefSize(width, height);
    ingredientsListView.setMaxWidth(width);
    ingredientsListView.setMaxHeight(height);
    background.setPrefSize(width, height);
    background.setMaxWidth(width);
    background.setMaxHeight(height);
    // Align ListView and background.
    StackPane.setMargin(ingredientsListView,
        new Insets(margins[0], margins[1], margins[2], margins[3]));
    StackPane.setMargin(background,
        new Insets(margins[0], margins[1], margins[2], margins[3]));
    // Add listview background to root.
    root.getChildren().add(background);
    // Remove item from listview when double clicked.
    ingredientsListView.setOnMouseClicked(event -> removeFromListView(event, ingredientsListView, false));
    return ingredientsListView;
  }

  /**
   * This method creates the HBox for adding tag elements.
   *
   * @param width HBox width.
   * @param height HBox height.
   * @param margins HBox margins.
   * @param fontSize font size.
   * @return HBox with add tag elements.
   */
  private HBox createAddTagBox(double width, double height, int[] margins, int fontSize) {
    // HBox for containing the add tag elements.
    HBox addTagContainer = new HBox();
    addTagContainer.setId("Delete");
    // Set HBox size.
    addTagContainer.setPrefSize(width, height);
    addTagContainer.setMaxHeight(height);
    addTagContainer.setMaxWidth(width);
    // Align HBox.
    StackPane.setMargin(addTagContainer,
                        new Insets(margins[0], margins[1], margins[2], margins[3]));

    // TextField for tag name.
    tagTextField = createTextField(230, height,
                                             // top, right, bottom, left.
                                             new int[] {0, 0, 0, 0},  // Insets.
                                             "tag...", fontSize
                                             );
    tagTextField.setStyle("-fx-border-width: 2px 2px 0px 2px;");
    // Set text formatter for max character length.
    tagTextField.setTextFormatter(maxLengthFormatter(50));
    // Load add button image.
    Image addButtonImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/plusButtonLibrary.png"
        ));
    // Create add button ImageView.
    ImageView addButtonView = createImageView(addButtonImage, height);

    // Create add button.
    Button addButton = new Button();
    addButton.setGraphic(addButtonView);
    addButton.getStyleClass().add("clearButton");
    addButton.setStyle("-fx-padding: 0;");

    // Logic for pressing button.
    addButton.setOnAction(event -> {
      String tag = tagTextField.getText();
      tagsList.add(tag);
      updateListView(tagsListView, tag);
      tagTextField.clear();
    });

    // Add elements to addTagContainer.
    tagTextField.setId("Delete");
    addButton.setId("Delete");
    addTagContainer.getChildren().addAll(tagTextField,
                                         addButton);

    return addTagContainer;
  }

  /**
   * This method creates a ListView for the added tags.
   *
   * @param width ListView width.
   * @param height ListView height.
   * @param margins ListView margins.
   * @param fontSize font size.
   * @return the tags ListView.
   */
  private ListView<String> createTagsListView(double width, double height, int[] margins, int fontSize) {
    // Instantiate ListView.
    tagsListView = new ListView<>();
    tagsListView.setId("Delete");
    // Background.
    Label background = new Label();
    background.setId("Delete");
    // CSS styling.
    tagsListView.getStyleClass().add("add-recipe-listview");
    background.getStyleClass().add("add-recipe-listview-background");
    // Set ListView size.
    tagsListView.setPrefSize(width, height);
    tagsListView.setMaxWidth(width);
    tagsListView.setMaxHeight(height);
    background.setPrefSize(width, height);
    background.setMaxWidth(width);
    background.setMaxHeight(height);
    // Align ListView.
    StackPane.setMargin(tagsListView, new Insets(margins[0], margins[1], margins[2], margins[3]));
    StackPane.setMargin(background, new Insets(margins[0], margins[1], margins[2], margins[3]));
    // Add background to root StackPane.
    root.getChildren().add(background);
    // Remove item from listview when double clicked.
    tagsListView.setOnMouseClicked(event -> removeFromListView(event, tagsListView, true));
    return tagsListView;
  }

  /**
   * This method creates HBox containing time info TextFields.
   *
   * @param width HBox width.
   * @param height HBox height.
   * @param margins HBox margins.
   * @param fontSize font size.
   * @return a HBox.
   */
  private HBox createTimeInfoBox(double width, double height, int[] margins, int fontSize) {
    // Create HBox for containing time info TextFields.
    HBox timeInfoBox = new HBox(0);
    timeInfoBox.setId("Delete");
    // Set HBox size.
    timeInfoBox.setPrefSize(width, height);
    timeInfoBox.setMaxHeight(height);
    timeInfoBox.setMaxWidth(width);
    // Align HBox.
    StackPane.setMargin(timeInfoBox, new Insets(margins[0], margins[1], margins[2], margins[3]));

    // Create prep time prompt Label.
    Label prepTimePrompt = createLabel(width / 8 * 2, height,
                                       // top, right, bottom, left.
                                       new int[] {0, 0, 0, 0},  // Insets.
                                       "Prep Time", fontSize);
    // CSS styling.
    prepTimePrompt.getStyleClass().add("add-recipe-small-label");
    // Create prep time TextField.
    prepTimeTextField = createTextField(width / 8 * 1.9, height,
                                                  // top, right, bottom, left.
                                                  new int[] {0, 0, 0, 0},  // Insets.
                                                  "minutes...", fontSize);
    // CSS styling.
    prepTimeTextField.getStyleClass().add("add-recipe-small-textfield");
    // Set margin for the prepTimeTextField to add space after it.
    HBox.setMargin(prepTimeTextField, new Insets(0, 2, 0, 0));
    // Create cook time prompt Label.
    Label cookTimePrompt = createLabel(width / 8 * 2, height,
                                       // top, right, bottom, left.
                                       new int[] {0, 0, 0, 0},  // Insets.
                                       "Cook Time", 12);
    cookTimePrompt.setId("Delete");
    // CSS styling.
    cookTimePrompt.getStyleClass().add("add-recipe-small-label");
    // Create cook time TextField.
    cookTimeTextField = createTextField(width / 8 * 1.9, height,
                                                  // top, right, bottom, left.
                                                  new int[] {0, 0, 0, 0},  // Insets.
                                                  "minutes...", fontSize);
    // CSS styling.
    cookTimeTextField.getStyleClass().add("add-recipe-small-textfield");

    // Set TextFormatter to the time TextFields so they only accept positive integers.
    prepTimeTextField.setTextFormatter(positiveIntegersFormatter(100000));
    cookTimeTextField.setTextFormatter(positiveIntegersFormatter(100000));

    // Add Labels and TextFields to HBox.
    cookTimeTextField.setId("Delete");
    cookTimePrompt.setId("Delete");
    prepTimeTextField.setId("Delete");
    prepTimePrompt.setId("Delete");
    timeInfoBox.getChildren().addAll(prepTimePrompt,
                                     prepTimeTextField,
                                     cookTimePrompt,
                                     cookTimeTextField);
    return timeInfoBox;
  }

  /**
   * This method creates a Button container VBox.
   *
   * @param width VBox width.
   * @param height Button height.
   * @param margins VBox margins.
   * @return Button container VBox.
   */
  private VBox createButtonsBox(double width, double height, int[] margins) {
    // Create VBox for containing Buttons.
    VBox buttonsContainer = new VBox(4);
    buttonsContainer.setId("Delete");
    // buttonsContainer.setStyle("-fx-border-color: black");
    // Set VBox size.
    buttonsContainer.setMaxWidth(width);
    // Align VBox.
    StackPane.setMargin(buttonsContainer,
        new Insets(margins[0], margins[1], margins[2], margins[3]));

    // Load Button images.
    Image clearButtonImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonClear.png"));
    Image clearButtonActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonClearActive.png"));
    Image saveButtonImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonSave.png"));
    Image saveButtonActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonSaveActive.png"));
    Image exitButtonImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonBiggerExit.png"));
    Image exitButtonActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonBiggerExitActive.png"));

    // Create Button ImageViews.
    ImageView clearButtonView = createImageView(clearButtonImage, height);
    ImageView clearButtonActiveView = createImageView(clearButtonActiveImage, height);
    ImageView applyButtonView = createImageView(saveButtonImage, height);
    ImageView applyButtonActiveView = createImageView(saveButtonActiveImage, height);
    ImageView exitButtonView = createImageView(exitButtonImage, height);
    ImageView exitButtonActiveView = createImageView(exitButtonActiveImage, height);

    // Create Buttons.
    Button clearButton = createButton(clearButtonView, clearButtonActiveView);
    Button saveButton = createButton(applyButtonView, applyButtonActiveView);
    Button exitButton = createButton(exitButtonView, exitButtonActiveView);

    // Logic when pressing clearButton.
    clearButton.setOnAction(event -> {
      recipeNameTextField.clear();
      ingredientNameTextField.clear();
      ingredientQuantityTextField.clear();
      ingredientUnitTextField.clear();
      tagTextField.clear();
      prepTimeTextField.clear();
      cookTimeTextField.clear();
      instructionTextArea.clear();
      shortDescriptionTextArea.clear();
      ingredientsListView.getItems().clear();
      tagsListView.getItems().clear();
      ingredientsList.clear();
      tagsList.clear();
    });

    // Logic when pressing saveButton.
    saveButton.setOnAction(event -> {

      String recipeName = recipeNameTextField.getText();
      int prepTime = 0;
      int cookTime = 0;
      if (!prepTimeTextField.getText().isEmpty() && !cookTimeTextField.getText().isEmpty()) {
        prepTime = Integer.parseInt(prepTimeTextField.getText());
        cookTime = Integer.parseInt(cookTimeTextField.getText());
      }
      int servings = 4;
      String instruction = instructionTextArea.getText();
      String shortDescription = shortDescriptionTextArea.getText();

      RecipeDataProvider recipe = new RecipeDataProvider(recipeName,
           shortDescription, prepTime, cookTime, servings);

      if (recipeController.addNewRecipe(recipe, tagsList, ingredientsList, instruction)) {
        recipeNameTextField.clear();
        ingredientNameTextField.clear();
        ingredientQuantityTextField.clear();
        ingredientUnitTextField.clear();
        tagTextField.clear();
        prepTimeTextField.clear();
        cookTimeTextField.clear();
        instructionTextArea.clear();
        shortDescriptionTextArea.clear();
        ingredientsListView.getItems().clear();
        tagsListView.getItems().clear();
        ingredientsList.clear();
        tagsList.clear();
      }

    });

    Timeline appearAfterOpen = new Timeline();
    appearAfterOpen.getKeyFrames().add(
        new KeyFrame(Duration.seconds(2), event -> {
          //This thing
          BrowseRecipesView browseRecipesView = new BrowseRecipesView(root, username);
          browseRecipesView.createBrowseRecipesScreen();
      })
    );
    // Logic for pressing exitButton.
    exitButton.setOnAction(event -> {
      removeSections(root, "Delete");
      removeSections(root, "DeleteAnimation");
      animation.openPageAnimation("assets/animation/pageFlipAnimation", 522);
      appearAfterOpen.play();
    });

    // Add Buttons to VBox.
    exitButton.setId("Delete");
    saveButton.setId("Delete");
    clearButton.setId("Delete");
    buttonsContainer.getChildren().addAll(clearButton, saveButton, exitButton);

    return buttonsContainer;
  }

  /**
   * This method creates a TextArea.
   *
   * @param width TextArea width.
   * @param height TextArea height.
   * @param margins TextArea margins.
   * @param fontSize TextArea font size.
   * @return a TextArea.
   */
  private TextArea createTextArea(double width, double height, int[] margins,
                                  String text, int fontSize) {
    // Create TextArea.
    TextArea textArea = new TextArea();
    textArea.setId("Delete");
    textArea.setPromptText(text);
    // CSS styling.
    textArea.getStyleClass().add("add-recipe-textarea");
    // Enable wrap text.
    textArea.setWrapText(true);
    // Set TextArea size.
    textArea.setPrefSize(width, height);
    textArea.setMaxHeight(height);
    textArea.setMaxWidth(width);
    // Set font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    textArea.setFont(pixelFont);
    // Align TextArea.
    StackPane.setMargin(textArea, new Insets(margins[0], margins[1], margins[2], margins[3]));
    return textArea;
  }

  /**
   * This method is used to update ListView items.
   *
   * @param listView ListView to update.
   * @param item String to add to ListView.
   */
  private void updateListView(ListView<String> listView, String item) {
    listView.getItems().add(item);
  }

  /**
   * This method creates a TextField.
   *
   * @param width TextField width.
   * @param height TextField height.
   * @param margins TextField margins.
   * @param text TextField text.
   * @param fontSize TextField font size.
   * @return a TextField.
   */
  private TextField createTextField(double width, double height, int[] margins,
                                    String text, int fontSize) {
    // Create TextField.
    TextField textField = new TextField();
    textField.setId("Delete");
    textField.setPromptText(text);
    // CSS styling.
    textField.getStyleClass().add("add-recipe-textfield");
    // Set TextField size.
    textField.setPrefSize(width, height);
    // Set font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    textField.setFont(pixelFont);
    // Align TextField.
    StackPane.setMargin(textField, new Insets(margins[0], margins[1], margins[2], margins[3]));
    return textField;
  }

  /**
   * This method creates a Label.
   *
   * @param width Label width.
   * @param height Label height.
   * @param margins Label margins.
   * @param text Label text.
   * @param fontSize Label font size.
   * @return a Label.
   */
  private Label createLabel(double width, double height, int[] margins, String text, int fontSize) {
    // Create Label.
    Label label = new Label(text);
    label.setId("Delete");
    // Set Label size.
    label.setPrefSize(width, height);
    // Set font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    label.setFont(pixelFont);
    // Align Label.
    StackPane.setMargin(label, new Insets(margins[0], margins[1], margins[2], margins[3]));
    label.getStyleClass().add("add-recipe-label");
    return label;
  }

  /**
   * This method creates an ImageView.
   *
   * @param image the image to use.
   * @return an ImageView with presets.
   */
  private ImageView createImageView(Image image, double height) {
    ImageView imageView = new ImageView(image);
    imageView.setId("Delete");
    imageView.setFitHeight(height);
    imageView.setPreserveRatio(true);
    return imageView;
  }

  /**
   * This method creates a Button with hover property.
   *
   * @param buttonView the buttonView.
   * @param hoverButtonView the hover buttonView.
   * @return a Button with hover property.
   */
  private Button createButton(ImageView buttonView, ImageView hoverButtonView) {
    // Create button.
    Button button = new Button();
    button.setId("Delete");
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
   * This method creates a TextFormatter enforcing max character length.
   *
   * @param maxLength max number of characters.
   * @return TextFormatter with max character length.
   */
  private TextFormatter<String> maxLengthFormatter(int maxLength) {
    // Create a TextFormatter with the length constraint.
    TextFormatter<String> lengthFormatter = new TextFormatter<>(change -> {
        String newText = change.getControlNewText();
        if (newText.length() <= maxLength) {
            return change; // Accept change.
        }
        return null; // Reject change.
    });

    return lengthFormatter;
  }

  /**
   * This method creates a TextFormatter that only accepts positive integer input with max length.
   *
   * @param maxLength max number of characters.
   * @return TextFormatter only accepting positive integers and with max character length.
   */
  private TextFormatter<String> positiveIntegersFormatter(int maxLength) {
    // Define a pattern for positive integers.
    Pattern validIntegerPattern = Pattern.compile("\\d*");
    // Create a TextFormatter with the pattern.
    TextFormatter<String> integerFormatter = new TextFormatter<>(change -> {
        String newText = change.getControlNewText();
        if (validIntegerPattern.matcher(newText).matches() && newText.length() <= maxLength) {
            return change; // Accept change.
        }
        return null; // Reject change.
    });

    return integerFormatter;
  }

  /**
   * This method creates a TextFormatter that only accepts positive double input with max length.
   *
   * @param maxLength max number of characters.
   * @return TextFormatter only accepting positive doubles and with max character length.
   */
  private TextFormatter<String> positiveDoublesFormatter(int maxLength) {
    // Define a pattern for positive doubles.
    Pattern validDoublePattern = Pattern.compile("\\d*\\.?\\d*");
    // Create a TextFormatter with the pattern.
    TextFormatter<String> doubleFormatter = new TextFormatter<>(change -> {
        String newText = change.getControlNewText();
        if (validDoublePattern.matcher(newText).matches() && newText.length() <= maxLength) {
            return change; // Accept change.
        }
        return null; // Reject change.
    });

    return doubleFormatter;
  }

  public void removeSections(StackPane stackPane, String id) {
    stackPane.getChildren().removeIf(node -> id.equals(node.getId()));
  }

  /**
   * This method sets the font for cells in a listview.
   *
   * @param listView the listview we want to change.
   * @param fontSize cell font size.
   */
  private void setCellFactory(ListView<String> listView, int fontSize) {
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    // Use a cell factory to set the font.
    listView.setCellFactory(lv -> new ListCell<String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(null);
          } else {
            setText(item);
            setFont(pixelFont);  // Set the font for each item.
          }
        }
    });
  }

  /**
   * Removes item from listview and list when double clicked.
   *
   * @param event mouse event.
   * @param listView the listview to remove from.
   * @param isTagsListView used to know what list to remove from.
   */
  private void removeFromListView(MouseEvent event, ListView<String> listView, boolean isTagsListView) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
      // Get the selected index.
      int selectedIndex = listView.getSelectionModel().getSelectedIndex();
      if (selectedIndex != -1) {
        // Remove the item at the selected index.
        listView.getItems().remove(selectedIndex);
        listView.getSelectionModel().clearSelection();
        // Also remove from lists.
        if (isTagsListView) {
          tagsList.remove(selectedIndex);
        } else {
          ingredientsList.remove(selectedIndex);
        }
      }
    }
  }

}
