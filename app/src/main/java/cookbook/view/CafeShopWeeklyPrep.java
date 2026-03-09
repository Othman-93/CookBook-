package cookbook.view;

import cookbook.model.DatabaseUtilProviderImpl;
import cookbook.model.RecipeHandlerModel;
import cookbook.model.UserDataHandler;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;


/**
 * This class is for the UltimateCookbook Animation.
 */
public class CafeShopWeeklyPrep {

  // Class fields.
  private StackPane root;
  private Map<String, List<ComboBox<String>>> dayComboBoxesMap = new HashMap<>();
  private Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 17);
  private final String username;
  private ChatBox chatBox;

  /**
   * Constructor for the UltimateCookbook class.
   *
   * @param root the StackPane root.
   */
  public CafeShopWeeklyPrep(StackPane root, String username) {
    this.root = root;
    this.username = username;
  }


  /**
  * This method sets up and displays a graphical representation otline for weekly prep. 
  */

  public void weeklyOutlinetView(LocalDate date) {

    // Load the background image (open book).
    Image layout = new Image(MarketView.class.getResourceAsStream(
        "/assets/stageTwo/cafeshop/notePageOutline.png"
    ));
    //implamenting style to image
    ImageView layoutView =  new ImageView(layout);
    layoutView.setFitHeight(435);
    layoutView.setPreserveRatio(true);
    layoutView.setId("Delete");

    layoutView.setTranslateX(-123);
    layoutView.setTranslateY(-24);

    Label weeklyDinnerLabel = new Label(date.toString());

    weeklyDinnerLabel.setTranslateX(4);
    weeklyDinnerLabel.setTranslateY(-136);
    weeklyDinnerLabel.setId("Delete");

    //set Font
    weeklyDinnerLabel.setFont(pixelFont);

    root.getChildren().addAll(layoutView, weeklyDinnerLabel);
    initializeWeekCombos(date);
    setupSubmitButton(date);
  }

  private void initializeWeekCombos(LocalDate date) {
    
    DatabaseUtilProviderImpl databaseUtilProviderImpl = new DatabaseUtilProviderImpl();
    RecipeHandlerModel recipeHandlerModel = new RecipeHandlerModel();

    List<String> options = recipeHandlerModel.showAllRecipeNames(); 
    createDailyComboBoxes("Monday", -70, -52, options, date);
    createDailyComboBoxes("Tuesday", -70, 55, options, date.plusDays(1));
    createDailyComboBoxes("Wednesday", 10, -52, options, date.plusDays(2));
    createDailyComboBoxes("Thursday", 10, 55, options, date.plusDays(3));
    createDailyComboBoxes("Friday", 90, -52, options, date.plusDays(4));
    createDailyComboBoxes("Weekend", 90, 55, options, date.plusDays(5));

    Buttons MainButtons = new Buttons(root);

    // Event handler for settingButton
    MainButtons.setSettingButtonAction(event -> handleSettingButtonInLoginScene(event));

    // Event handler for infoButton
    MainButtons.setInfoButtonAction(event -> handleInfoButtonInLoginScene(event));

  }

  private List<ComboBox<String>> createDailyComboBoxes(String day, int startY, int startX, List<String> items, LocalDate date) {
    Label label = new Label(day);
    label.setTranslateX(startX);
    label.setTranslateY(startY - 22);
    label.setFont(pixelFont);

    List<ComboBox<String>> comboBoxes = new ArrayList<>();

    for (int i = 0; i < 4; i++) {
      ComboBox<String> comboBox = new ComboBox<>();
      comboBox.getItems().addAll(items);
      comboBox.setPromptText(" ");
      comboBox.setPrefWidth(100);
      comboBox.getStyleClass().add("clearButton");
      comboBox.setId("WeeklyPrep" + day + i);
      comboBox.setTranslateX(startX + 10);
      comboBox.setTranslateY(startY + (i * 12));
      comboBox.setId("Delete");
      root.getChildren().add(comboBox);
      comboBoxes.add(comboBox);
      comboBox.setOnAction(event -> {
        String selectedRecipe = comboBox.getValue();
        if (selectedRecipe != null && !selectedRecipe.isEmpty()) {
          DatabaseUtilProviderImpl dbProvider = new DatabaseUtilProviderImpl();
          RecipeHandlerModel recipeModel = new RecipeHandlerModel();
          UserDataHandler userHandler = new UserDataHandler();
          int userId = userHandler.getUserIdFromUsername(username);
          String dayOfWeek = day.toLowerCase(); 
          boolean addedToWeeklyList = recipeModel.addRecipeToWeeklyDinnerList(userId, date, dayOfWeek, selectedRecipe);

        }
      });
    }
    dayComboBoxesMap.put(day, comboBoxes);

    root.getChildren().add(label);
    return comboBoxes;
  }

  private void setupSubmitButton(LocalDate date) {
    Button submitButton = new Button("Submit Choices");
    submitButton.setTranslateX(0);
    submitButton.setTranslateY(145);
    submitButton.setId("Delete");
    submitButton.setOnAction(event -> {
      initializeWeekCombos(date);
      showAlert("Choices Submitted", "Your choices have been submitted successfully.");
    });
    root.getChildren().add(submitButton);
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
  private void handleSettingButtonInLoginScene(ActionEvent event) {
     System.out.println("Setting");
  }

  private void handleInfoButtonInLoginScene(ActionEvent event) {
    chatBox = new ChatBox(root);
    //set specific dialog, all dialogs you can see at the ChatBox class
    chatBox.setupChatBox("StageTwoCafeshopWeekly");
    root.getChildren().add(chatBox);
  }
 
}
