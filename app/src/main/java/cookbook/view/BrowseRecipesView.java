package cookbook.view;

import cookbook.model.RecipeDataProvider;
import cookbook.model.RecipeHandlerModel;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * This class is for the Browsing Recipes UI.
 */
public class BrowseRecipesView {

  // Attributes.
  private StackPane root;
  private String username;
  private TextField searchTextField;
  private TextField recipeSearchTextField;
  private RecipeHandlerModel recipeHandlerModel;
  private boolean ingredientsTabActive;
  private boolean tagsTabActive;
  private List<String> ingredientsList;
  private List<String> tagsList;
  private List<String> activeIngredientsList;
  private List<String> activeTagsList;
  private List<String> recipesList;
  private ListView<String> filtersListView;
  private ListView<String> activeFiltersListView;
  private ListView<String> recipesListView;
  private SetupTooltip setupTooltip;
  private HBox bookPagesContainer;
  private BookAnimation animation;

  /**
   * The constructor for the BrowseRecipesView class.
   *
   * @param root the root StackPane.
   * @param username the user username.
   */
  public BrowseRecipesView(StackPane root, String username) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.username = username;
    this.recipeHandlerModel = new RecipeHandlerModel();
    this.ingredientsTabActive = true;
    this.tagsTabActive = false;
    this.ingredientsList = recipeHandlerModel.getAllIngredientsNames();
    this.tagsList = recipeHandlerModel.getAllTagsNames();
    this.activeIngredientsList = new ArrayList<>();
    this.activeTagsList = new ArrayList<>();
    this.recipesList = new ArrayList<>();
    this.setupTooltip = new SetupTooltip();
  }

  /**
   * This method creates the UI for browsing recipes
   * and adds it to the root StackPane.
   */
  public void createBrowseRecipesScreen() {

    // HBox that will contain left and right side of open book.
    bookPagesContainer = new HBox(64);
    // Set max height so we can still press Go back button.
    bookPagesContainer.setMaxHeight(390);
    // Set padding so children end up centered on open book pages.
    bookPagesContainer.setPadding(new Insets(0, 0, 0, 122));
    // Add left page content to HBox.
    bookPagesContainer.getChildren().add(0, createLeftPageContent());
    // Add right page content to HBox.
    bookPagesContainer.getChildren().add(1, createRightPageContent());

    // Add HBox to root StackPane.
    root.getChildren().add(bookPagesContainer);
  }

  /**
   * This method creates left page content of open book.
   *
   * @return GridPane containing left page content.
   */
  private GridPane createLeftPageContent() {

    // GridPane that contains left page content of the book.
    GridPane leftPageGrid = new GridPane();
    // Center GridPane.
    leftPageGrid.setAlignment(Pos.CENTER);

    // Create TextField for searching.
    searchTextField = createTextField(30, 110, "Search...");

    // Create and add search tabs HBox to the leftPageGrid GridPane.
    leftPageGrid.add(createTabsBox(), 0, 0);

    // Load search panel image.
    Image searchPanelImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/librarySearchPanel.png"
        ));
    // Create ImageView for search panel image.
    ImageView searchPanelView = createImageView(searchPanelImage, 260);
    // Add search panel view to the leftPageGrid GridPane.
    leftPageGrid.add(searchPanelView, 0, 1);

    // Create and add search panel HBox to leftPageGrid GridPane.
    leftPageGrid.add(createSearchPanelBox(), 0, 1);

    // Set padding for search TextField.
    searchTextField.setPadding(new Insets(6, 0, 0, 6));
    setupSearchFilter(searchTextField, filtersListView, ingredientsList);
    // Add searchTextField to leftPageGrid GridPane.
    leftPageGrid.add(searchTextField, 0, 1);
    // Set the vertical alignment of the searchTextField to TOP.
    GridPane.setValignment(searchTextField, VPos.TOP);
    // Set the horizontal alignment of the searchTextField to LEFT.
    GridPane.setHalignment(searchTextField, HPos.LEFT);

    // Create and add Apply button to leftPageGrid GridPane.
    leftPageGrid.add(createSearchButtonsBox(), 0, 2);

    return leftPageGrid;
  }

  /**
   * This method creates a HBox for the search by tab Buttons on left page.
   *
   * @return a HBox containing the search tab Buttons.
   */
  private HBox createTabsBox() {
    // Load Ingredient and Tag button images.
    Image ingredientsTabImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/searchByIngredient.png"
        ));
    Image ingredientsTabActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/searchByIngredientActive.png"
        ));
    Image tagsTabImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/searchByTag.png"
    ));
    Image tagsTabActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/searchByTagActive.png"
        ));

    // Create ImageViews for Ingredient and Tag button images.
    ImageView ingredientsTabView = createImageView(ingredientsTabImage, 23);
    ImageView ingredientsTabActiveView = createImageView(ingredientsTabActiveImage, 23);
    ImageView tagsTabView = createImageView(tagsTabImage, 23);
    ImageView tagsTabActiveView = createImageView(tagsTabActiveImage, 23);

    // Create Ingredient and Tag buttons.
    Button ingredientsTabButton = createTabButton(ingredientsTabActiveView);
    Button tagsTabButton = createTabButton(tagsTabView);

    // Logic for pressing Ingredients tab button.
    ingredientsTabButton.setOnAction(event -> {
      ingredientsTabActive = true;
      tagsTabActive = false;
      ingredientsTabButton.setGraphic(ingredientsTabActiveView);
      tagsTabButton.setGraphic(tagsTabView);
      searchTextField.setText("");
      updateListView(filtersListView, ingredientsList);
      updateListView(activeFiltersListView, activeIngredientsList);
      setupSearchFilter(searchTextField, filtersListView, ingredientsList);
    });

    // Logic for pressing Tags tab button.
    tagsTabButton.setOnAction(event -> {
      ingredientsTabActive = false;
      tagsTabActive = true;
      ingredientsTabButton.setGraphic(ingredientsTabView);
      tagsTabButton.setGraphic(tagsTabActiveView);
      searchTextField.setText("");
      updateListView(filtersListView, tagsList);
      updateListView(activeFiltersListView, activeTagsList);
      setupSearchFilter(searchTextField, filtersListView, tagsList);
    });

    // HBox for the two search options.
    HBox searchTabsContainer = new HBox();
    // Add Ingredients and Tags button to searchTabsContainer HBox.
    searchTabsContainer.getChildren().add(0, ingredientsTabButton);
    searchTabsContainer.getChildren().add(1, tagsTabButton);

    return searchTabsContainer;
  }

  /**
   * This method creates a HBox containing the ListViews and Buttons in search panel.
   *
   * @return a HBox containing the ListViews and Buttons in search panel.
   */
  private HBox createSearchPanelBox() {
    // Load search panel action images.
    Image rightArrowImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/arrowRight.png"
        ));
    Image rightArrowActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/arrowRightActive.png"
        ));
    Image leftArrowImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/arrowLeft.png"
        ));
    Image leftArrowActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/arrowLeftActive.png"
        ));
    Image resetIconImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/clearButtonSmall.png"
        ));
    Image resetIconActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/clearButtonSmallActive.png"
        ));

    // Create ImageViews for search panel action images.
    ImageView rightArrowView = createImageView(rightArrowImage, 22);
    ImageView rightArrowActiveView = createImageView(rightArrowActiveImage, 22);
    ImageView leftArrowView = createImageView(leftArrowImage, 22);
    ImageView leftArrowActiveView = createImageView(leftArrowActiveImage, 22);
    ImageView resetIconView = createImageView(resetIconImage, 22);
    ImageView resetIconActiveView = createImageView(resetIconActiveImage, 22);

    // HBox for containing the search panel ListViews and Buttons.
    HBox panelBox = new HBox(7);

    // Create filters ListView.
    filtersListView = createListView(
        recipeHandlerModel.getAllIngredientsNames(), 110, 250, 36,
        9, 12, false
        );

    // Add filtersListView to panelBox HBox.
    panelBox.getChildren().add(filtersListView);

    // Create search panel action buttons.
    Button rightArrowButton = createButton(rightArrowView, rightArrowActiveView);
    Button leftArrowButton = createButton(leftArrowView, leftArrowActiveView);
    Button resetButton = createButton(resetIconView, resetIconActiveView);

    // Logic for pressing rightArrowButton.
    rightArrowButton.setOnAction(event -> {
      moveToActiveFiltersListView();
      if (ingredientsTabActive) {
        setupSearchFilter(searchTextField, filtersListView, ingredientsList);
      } else if (tagsTabActive) {
        setupSearchFilter(searchTextField, filtersListView, tagsList);
      }
    });
    // Logic for pressing leftArrowButton.
    leftArrowButton.setOnAction(event -> {
      moveToFiltersListView();
      if (ingredientsTabActive) {
        setupSearchFilter(searchTextField, filtersListView, ingredientsList);
      } else if (tagsTabActive) {
        setupSearchFilter(searchTextField, filtersListView, tagsList);
      }
    });
    // Logic for pressing resetButton.
    resetButton.setOnAction(event -> {
      clearActiveFilters();
    });

    // VBox for containing the search panel action buttons.
    VBox panelActionsContainer = new VBox(10);
    panelActionsContainer.getChildren().add(0, rightArrowButton);
    panelActionsContainer.getChildren().add(1, leftArrowButton);
    panelActionsContainer.getChildren().add(2, resetButton);
    // Center children in the arrowsContainer VBox
    panelActionsContainer.setAlignment(javafx.geometry.Pos.CENTER);
    // Add padding to arrowsContainer VBox.
    panelActionsContainer.setPadding(new Insets(0, 0, 50, 0));

    // Add panelActionsContainer to panelBox HBox.
    panelBox.getChildren().add(panelActionsContainer);

    // Create active filters ListView.
    activeFiltersListView = createListView(
        null, 100, 250, 8, 5, 12, false
        );

    // Add activeFiltersListView to panelBox HBox.
    panelBox.getChildren().add(activeFiltersListView);

    // Ensure only one item can be hightlighted at a time.
    filtersListView.setOnMouseClicked(event -> {
      if (event.getClickCount() == 1) {
        String selectedItem = filtersListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
          activeFiltersListView.getSelectionModel().clearSelection();
        }
      }
    });
    activeFiltersListView.setOnMouseClicked(event -> {
      if (event.getClickCount() == 1) {
        String selectedItem = activeFiltersListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
          filtersListView.getSelectionModel().clearSelection();
        }
      }
    });

    return panelBox;
  }

  /**
   * This method creates a VBox containing the apply button.
   *
   * @return a VBox containing the apply button.
   */
  private VBox createSearchButtonsBox() {
    // Load search button images.
    Image searchButtonImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonBiggerSearch.png"
        ));
    Image searchButtonActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonBiggerSearchActive.png"
        ));
    Image searchAllButtonImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonBiggerSearchAll.png"
        ));
    Image searchAllButtonActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonBiggerSearchAllActive.png"
        ));


    // Create ImageViews for search images.
    ImageView searchButtonView = createImageView(searchButtonImage, 30);
    ImageView searchButtonActiveView = createImageView(searchButtonActiveImage, 30);
    ImageView searchAllButtonView = createImageView(searchAllButtonImage, 30);
    ImageView searchAllButtonActiveView = createImageView(searchAllButtonActiveImage, 30);

    // Create search buttons.
    Button searchButton = createButton(searchButtonView, searchButtonActiveView);
    Button searchAllButton = createButton(searchAllButtonView, searchAllButtonActiveView);

    // Logic for pressing search button.
    searchButton.setOnAction(event -> {
      recipeSearchTextField.setText("");
      recipesList.clear();
      if (activeIngredientsList.isEmpty() && activeTagsList.isEmpty()) {
        recipesList.clear();
      } else {
        recipesList.addAll(recipeHandlerModel.getRecipesByIngreAndTags(activeTagsList, activeIngredientsList));
      }
      if (!recipesList.isEmpty()) {
        sortRecipesByFavorite();
      }
      updateListView(recipesListView, recipesList);
      setupSearchFilter(recipeSearchTextField, recipesListView, recipesList);
    });

    // Logic for pressing search all button.
    searchAllButton.setOnAction(event -> {
      recipeSearchTextField.setText("");
      recipesList.clear();
      recipesList = recipeHandlerModel.showAllRecipeNames();
      if (!recipesList.isEmpty()) {
        sortRecipesByFavorite();
      }
      updateListView(recipesListView, recipesList);
      setupSearchFilter(recipeSearchTextField, recipesListView, recipesList);
    });

    // VBox for the search buttons.
    VBox actionOptionsContainer = new VBox(5);
    actionOptionsContainer.getChildren().add(0, searchButton);
    actionOptionsContainer.getChildren().add(1, searchAllButton);
    // Center children in the actionOptionsContainer HBox.
    actionOptionsContainer.setAlignment(javafx.geometry.Pos.CENTER);
    // Add padding to actionOptionsContainer HBox.
    actionOptionsContainer.setPadding(new Insets(15, 0, 0, 0));

    return actionOptionsContainer;
  }

  /**
   * This method creates the right page content.
   *
   * @return StackPane with right page content.
   */
  private StackPane createRightPageContent() {

    // StackPane that contains the book right page content.
    StackPane rightPageContainer = new StackPane();
    rightPageContainer.setPadding(new Insets(0, 0, 10, 0));

    // Load the recipe panel image.
    Image recipePanelImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/recipesPanel.png"
        ));
    // Create ImageView for recipe panel image.
    ImageView recipePanelView = createImageView(recipePanelImage, 350);

    StackPane.setMargin(recipePanelView, new Insets(0, 0, 30, 0));

    // Add Recipe panel ImageView to StackPane rightPageContainer.
    rightPageContainer.getChildren().add(recipePanelView);

    // Create recipeSearchTextField.
    recipeSearchTextField = createTextField(30, 228, "Search...");

    StackPane.setMargin(recipeSearchTextField, new Insets(0, 0, 347, 2));

    // Create recipes ListView.
    recipesListView = createListView(
        null, 220, 305, 0, 2, 14, true
        );

    // Create the Timeline object
    Timeline appearAfterOpen = new Timeline();

    // Double click logic (Go to recipe view)
    recipesListView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
      if (event.getClickCount() == 2) {
        String recipeName = recipesListView.getSelectionModel().getSelectedItem();
        if (recipeName != null) {
          appearAfterOpen.getKeyFrames().clear();
          appearAfterOpen.getKeyFrames().add(
              new KeyFrame(Duration.seconds(2), e -> {
                RecipeView recipeView = new RecipeView(root, username, recipeName);
                bookPagesContainer.getChildren().clear();
                recipeView.displayRecipeView();
              })
          );
          removeSections(root, "Delete");
          removeSections(root, "DeleteAnimation");
          animation.openPageAnimation("assets/animation/pageFlipAnimation", 522);
          appearAfterOpen.play();
        }
      }
    });

    // recipesListView.setStyle("-fx-background-color: white;");
    StackPane.setMargin(recipesListView, new Insets(0, 0, 10, 0));

    // Add ListView and search text field to rightPageContainer StackPane.
    rightPageContainer.getChildren().addAll(recipesListView, recipeSearchTextField);

    // Load the add recipe images.
    Image addRecipeImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonBiggerAddRecipe.png"
        ));
    Image addRecipeActiveImage = new Image(getClass().getResourceAsStream(
        "/assets/stageTwo/library/buttons/libraryGoldenButtonBiggerAddRecipeActive.png"
        ));
    // Create ImageViews for add recipe images.
    ImageView addRecipeView = createImageView(addRecipeImage, 30);
    ImageView addRecipeActiveView = createImageView(addRecipeActiveImage, 30);
    // Create add recipe button.
    Button addRecipeButton = createButton(addRecipeView, addRecipeActiveView);

    // Logic for pressing add recipe button.
    addRecipeButton.setOnAction(event -> {
      appearAfterOpen.getKeyFrames().clear();
      appearAfterOpen.getKeyFrames().add(
          new KeyFrame(Duration.seconds(2), e -> {
            CreateRecipeView createRecipeView = new CreateRecipeView(root, username);
            bookPagesContainer.getChildren().clear();
            createRecipeView.displayCreateRecipeView();
          })
      );
      removeSections(root, "Delete");
      removeSections(root, "DeleteAnimation");
      animation.openPageAnimation("assets/animation/pageFlipAnimation", 522);
      appearAfterOpen.play();
    });

    StackPane.setAlignment(addRecipeButton, Pos. BOTTOM_CENTER);
    StackPane.setMargin(addRecipeButton, new Insets(370, 0, 0, 0));

    rightPageContainer.getChildren().add(addRecipeButton);

    return rightPageContainer;
  }

  /**
   * This method creates a ListView.
   *
   * @param width ListView width.
   * @param height ListView height.
   * @param topPadding ListView top padding.
   * @param leftPadding ListView left padding.
   * @param fontSize ListView font size.
   * @return created ListView.
   */
  private ListView<String> createListView(List<String> list,
                                          double width,
                                          double height,
                                          double topPadding,
                                          double leftPadding,
                                          int fontSize,
                                          boolean isRecipeList) {
    // Create the ListView.
    ListView<String> listView = new ListView<>();
    // Check if List provided is not null.
    if (list != null) {
      // Convert List to ObservableList.
      ObservableList<String> observableList = FXCollections.observableArrayList(list);
      // Set items.
      listView.setItems(observableList);
    }
    // Align everything.
    listView.setPrefSize(width, height);
    listView.setMaxHeight(height);
    listView.setMaxWidth(width);
    listView.setPadding(new Insets(topPadding, 0, 0, leftPadding));
    // Style scrollbars.
    listView.getStyleClass().add("browse-recipes-list-view");

    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), fontSize);
    // Use a cell factory to set the font.
    listView.setCellFactory(lv -> new ListCell<String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(null);
            getStyleClass().remove("favorite");
          } else if (!empty && item != null) {
            setText(item);
            setFont(pixelFont);  // Set the font for each item
            getStyleClass().remove("favorite");
            if (isRecipeList) {
              setupTooltip.showTooltipDis(this, getItemDescription(item));
              boolean isFavorite = recipeHandlerModel.isFavorite(username, item);
              if (isFavorite) {
                getStyleClass().add("favorite");
              } else {
                getStyleClass().remove("favorite");
              }
            }
          }
        }
    });

    return listView;
  }

  /**
   * This method updates an existing ListView with new items.
   *
   * @param listView the ListView to update.
   * @param items new ListView items.
   */
  private void updateListView(ListView<String> listView, List<String> items) {
    if (items != null) {
      ObservableList<String> observableList = FXCollections.observableArrayList(items);
      listView.setItems(observableList);
    } else {
      listView.setItems(FXCollections.observableArrayList());
    }
  }

  /**
   * This method moves selected filtersListView item to activeFiltersListView.
   */
  private void moveToActiveFiltersListView() {
    String selectedItem = filtersListView.getSelectionModel().getSelectedItem();
    if (selectedItem != null && !selectedItem.isEmpty()) {
      filtersListView.getItems().remove(selectedItem);
      activeFiltersListView.getItems().add(selectedItem);
      filtersListView.getSelectionModel().clearSelection();
      if (ingredientsTabActive) {
        ingredientsList.remove(selectedItem);
        activeIngredientsList.add(selectedItem);
      } else if (tagsTabActive) {
        tagsList.remove(selectedItem);
        activeTagsList.add(selectedItem);
      }
    }
  }

  /**
   * This method moves selected activeFiltersListView item back to filtersListView.
   */
  private void moveToFiltersListView() {
    String selectedItem = activeFiltersListView.getSelectionModel().getSelectedItem();
    if (selectedItem != null && !selectedItem.isEmpty()) {
      activeFiltersListView.getItems().remove(selectedItem);
      filtersListView.getItems().add(selectedItem);
      activeFiltersListView.getSelectionModel().clearSelection();
      if (ingredientsTabActive) {
        activeIngredientsList.remove(selectedItem);
        ingredientsList.add(selectedItem);
      } else if (tagsTabActive) {
        activeTagsList.remove(selectedItem);
        tagsList.add(selectedItem);
      }
    }
  }

  /**
   * This method clears all active filters.
   */
  private void clearActiveFilters() {
    if (ingredientsTabActive) {
      ingredientsList.addAll(activeIngredientsList);
      activeIngredientsList.clear();
      updateListView(filtersListView, ingredientsList);
      updateListView(activeFiltersListView, activeIngredientsList);
      setupSearchFilter(searchTextField, filtersListView, ingredientsList);
    } else if (tagsTabActive) {
      tagsList.addAll(activeTagsList);
      activeTagsList.clear();
      updateListView(filtersListView, tagsList);
      updateListView(activeFiltersListView, activeTagsList);
      setupSearchFilter(searchTextField, filtersListView, tagsList);
    }
  }

  /**
   * Sets up a filter on the provided ListView based on the text input in the provided TextField.
   *
   * @param searchField the TextField used for searching.
   * @param listView the ListView we want to search/filter in.
   * @param listToFilter the list we filter from.
   */
  private void setupSearchFilter(TextField searchField,
                               ListView<String> listView,
                               List<String> listToFilter) {

    ObservableList<String> originalItems = FXCollections.observableArrayList(listToFilter);
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null || newValue.isEmpty() || newValue.equals("")) {
        listView.setItems(originalItems);
      } else {
        ObservableList<String> filteredItems = FXCollections.observableArrayList();
        for (String item : originalItems) {
          if (item.toLowerCase().contains(newValue.toLowerCase())) {
            filteredItems.add(item);
          }
        }
        listView.setItems(filteredItems);
      }
    });
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
   * This method creates a TextField with presets.
   *
   * @param prefHeight the preferred height to set.
   * @param maxWidth the max width to set.
   * @return a TextField with presets.
   */
  private TextField createTextField(double prefHeight, double maxWidth, String promptText) {
    TextField textField = new TextField();
    textField.setPrefHeight(prefHeight);
    textField.setPrefWidth(maxWidth);
    textField.setMaxWidth(maxWidth);
    textField.setPromptText(promptText);
    // CSS styling.
    textField.getStyleClass().add("fieldBox");
    textField.setStyle("-fx-prompt-text-fill: #000000;");
    // Font.
    Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/FreePixel.ttf"), 14);
    textField.setFont(pixelFont);
    return textField;
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
   * This method creates a button for the search by tabs.
   *
   * @param buttonView Button graphic.
   * @return button with presets.
   */
  private Button createTabButton(ImageView buttonView) {
    // Create button.
    Button button = new Button();
    button.setGraphic(buttonView);
    button.getStyleClass().add("clearButton");
    button.setStyle("-fx-padding: 0;");
    return button;
  }

  private String getItemDescription(String recipeName) {
    // Call the method to retrieve recipe details
    RecipeDataProvider recipeData = recipeHandlerModel.showRecipeDetails(recipeName);
    if (recipeData != null) {
      // If recipe details are found, return the description
      return recipeData.getDescription();
    } else {
      // If recipe details are not found, return a default message
      return "No description available for " + recipeName;
    }
  }
  public void removeSections(StackPane stackPane, String id) {
    stackPane.getChildren().removeIf(node -> id.equals(node.getId()));
  }

  /**
   * This method sorts recipeList by favorite recipes first.
   */
  private void sortRecipesByFavorite() {
    List<String> favoritesList = new ArrayList<>();
    List<String> nonFavoritesList = new ArrayList<>();
    for (String recipe : recipesList) {
      if (recipeHandlerModel.isFavorite(username, recipe)) {
        favoritesList.add(recipe);
      } else {
        nonFavoritesList.add(recipe);
      }
    }
    recipesList.clear();
    recipesList.addAll(favoritesList);
    recipesList.addAll(nonFavoritesList);
  }
}
