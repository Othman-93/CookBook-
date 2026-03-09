package cookbook.view;

import java.util.List;

import cookbook.model.RecipeHandlerModel;
import cookbook.model.CommentsDataHandler;
import cookbook.model.DatabaseUtilProviderImpl;
import cookbook.model.UserDataHandler;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * This class is for the Restaurant View Recipe Animation.
 */
public class RestaurantViewRecipe {

  // Class fields.
  private VBox friendsSectionView;
  private VBox commentsSection;
  private BookAnimation animation;  // Book animation.
  private StackPane root;
  private Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 17);
  private List<String> userList;
  private CommentsDataHandler commentsDataHandler;
  private RecipeHandlerModel recipeHandlerModel;
  private UserDataHandler userDataHandler;


  /**
   * Constructor for the Restaurant View Recipe class.
   *
   * @param root the StackPane root.
   */
  public RestaurantViewRecipe(StackPane root, String username,UserDataHandler userDataHandler) {
    this.root = root;
    this.animation = new BookAnimation(root);
    DatabaseUtilProviderImpl databaseUtilProvider = new DatabaseUtilProviderImpl();
    this.userDataHandler = userDataHandler; 
    userList = userDataHandler.getComboListUserNames();
    this.commentsDataHandler = new CommentsDataHandler();
    this.recipeHandlerModel = new RecipeHandlerModel();
    userDataHandler = new UserDataHandler();
  }

  /**
   * This method creates a animation for the book to apear.
   */
   
  public void setMenuForCommunication(String recipeName, String username) {
    // Create the Label for Friends Recommendation
    Label friendsRecommendation = new Label(recipeName);
    friendsRecommendation.setFont(pixelFont);
   
    // Create the ComboBox for Friends Recommendation
    ObservableList<String> observableList = FXCollections.observableArrayList(userList);
    ComboBox<String> friendsRecommendationCombo = new ComboBox<>();
    friendsRecommendationCombo.setItems(observableList);
    friendsRecommendationCombo.setPromptText("Select a Friend to recommend");
   
    // Create the TextArea for comments
    TextArea commentBox = new TextArea();
    commentBox.setPromptText("Add a message to your friend...");
    commentBox.setWrapText(true);
    commentBox.setPrefRowCount(1);
    commentBox.setPrefHeight(10);
   
    // Create the Share Button
    Button shareButton = new Button("Share");
    shareButton.setOnAction(event -> {
      String selectedFriend = friendsRecommendationCombo.getSelectionModel().getSelectedItem();
      String comment = commentBox.getText();
      if (selectedFriend != null && !selectedFriend.isEmpty()) {
        int recipeId = recipeHandlerModel.getRecipeIdByName(recipeName);
        shareRecipe(username, selectedFriend, recipeId, comment);
      } else {
        System.out.println("Please select a friend to share with.");
        playBounceAnimation(friendsRecommendationCombo);
      }
    });
   
    // Create VBox for Friends Recommendation section
    VBox friendsSectionView = new VBox(0);
    friendsSectionView.getStyleClass().add("transparent-container");
    // friendsSectionView.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-padding: 5px;");
    friendsSectionView.setAlignment(Pos.CENTER);
    friendsSectionView.setId("VboxReplace");
    friendsSectionView.getChildren().addAll(friendsRecommendation, friendsRecommendationCombo, commentBox, shareButton);
    StackPane.setMargin(friendsSectionView, new Insets(120, 135, 345, 420));
   
    // Create the Label for Comments
    Label commentsLabel = new Label("Comments");
    commentsLabel.setFont(pixelFont);
   
    // Create the Buttons for Friends and Public comments
    Button friendsButton = new Button("Friends");
    Button publicButton = new Button("Public");
    HBox commentTypeButtons = new HBox(10, friendsButton, publicButton);
    commentTypeButtons.setAlignment(Pos.CENTER);
    
    // Create the ListView to display comments
    ListView<String> friendsCommentsListView = new ListView<>();
    List<String> friendComments = commentsDataHandler.getSharedComments(username, recipeHandlerModel.getRecipeIdByName(recipeName));
    friendsCommentsListView.getItems().addAll(friendComments);
    friendsCommentsListView.setPlaceholder(new Label("No comments yet."));
    friendsCommentsListView.setCellFactory((Callback<ListView<String>, ListCell<String>>) new Callback<ListView<String>, ListCell<String>>() {
        @Override
           public ListCell<String> call(ListView<String> param) {
            return new ListCell<String>() {
                @Override
                   protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                      setText(null);
                      setGraphic(null);
                    } else {
                    Text text = new Text(item);
                    text.setStyle("-fx-font-size: 10px;");
                    setGraphic(text);
                    setFont(pixelFont);
                    }
                }
            };
        }
    });
   
    ListView<String> publicCommentsListView = new ListView<>();
    int recipeId = recipeHandlerModel.getRecipeIdByName(recipeName);
    List<String> publicComments = commentsDataHandler.getLatestComments(recipeId);
    publicCommentsListView.getItems().addAll(publicComments);
    publicCommentsListView.setPlaceholder(new Label("No comments yet."));
    publicCommentsListView.setVisible(false);
    publicCommentsListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
        @Override
           public ListCell<String> call(ListView<String> param) {
            return new ListCell<String>() {
              @Override
                   protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                       if (empty || item == null) {
                           setText(null);
                           setGraphic(null);
                       } else {
                           Text text = new Text(item);
                           text.setStyle("-fx-font-size: 10px;");
                           setGraphic(text);
                           setFont(pixelFont);
                       }
                   }
               };
           }
       });
   
       // Create a StackPane to stack the ListView components
       StackPane commentsStackPane = new StackPane();
       commentsStackPane.getChildren().addAll(friendsCommentsListView, publicCommentsListView);
       VBox.setVgrow(commentsStackPane, Priority.ALWAYS);
   
       // Toggle comments visibility
       friendsButton.setOnAction(event -> {
           friendsCommentsListView.setVisible(true);
           publicCommentsListView.setVisible(false);
       });
       publicButton.setOnAction(event -> {
           friendsCommentsListView.setVisible(false);
           publicCommentsListView.setVisible(true);
       });
   
       // Create the TextArea for comment input
       TextArea commentInputBox = new TextArea(); //todo this adds to the needed box
       commentInputBox.setPromptText("Add your comment...");
       commentInputBox.setWrapText(true);
       commentInputBox.setPrefRowCount(1);
       commentInputBox.setPrefHeight(20); 
   
       // Handle Enter key press to send message
       commentInputBox.setOnKeyPressed(event -> {
           if (event.getCode() == KeyCode.ENTER) {
               String comment = commentInputBox.getText().trim();
               if (!comment.isEmpty()) {
                int userId = userDataHandler.getUserIdFromUsername(username);
                int recipeIdForComment = recipeHandlerModel.getRecipeIdByName(recipeName);

                boolean isCommentAdded = commentsDataHandler.addComment(userId, recipeIdForComment, comment);
                if (isCommentAdded) {
                   if (friendsCommentsListView.isVisible()) {
                       friendsCommentsListView.getItems().add(username + ": " + comment);
                   } else {
                       publicCommentsListView.getItems().add(username + ": " + comment);
                   }
                   commentInputBox.clear();
                } else {
                    System.out.println("Failed to add comment.");
                }
                event.consume();
               }
           }
       });
   
       // Create VBox for Comments section
       VBox commentsSection = new VBox(0);
       commentsSection.getStyleClass().add("transparent-container");
       commentsSection.setAlignment(Pos.CENTER);
       commentsSection.setId("VboxReplace");
       commentsSection.getChildren().addAll(commentsLabel, commentTypeButtons, commentsStackPane, commentInputBox);
       StackPane.setMargin(commentsSection, new Insets(257, 127, 120, 420));
       
    

       // Add all sections to the root StackPane
       root.getChildren().addAll(friendsSectionView, commentsSection);
   }

private void shareRecipe(String senderUsername, String recipientUsername, int recipeId, String message) {
  boolean isShared = commentsDataHandler.addSharedRecipe(senderUsername, recipientUsername, recipeId, message);
    if (isShared) {
      System.out.println("Recipe shared successfully!");
    } else {
      System.out.println("Failed to share the recipe.");
    }
}

private void playBounceAnimation(ComboBox<String> comboBox) {
    TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), comboBox);
    translateTransition.setFromY(0);
    translateTransition.setByY(-10);
    translateTransition.setAutoReverse(true);
    translateTransition.setCycleCount(4);
    translateTransition.play();
}
public void removeSections(StackPane stackPane, String id) {
    stackPane.getChildren().removeIf(node -> id.equals(node.getId()));
}

}
