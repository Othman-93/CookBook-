package cookbook.view;

import java.util.Arrays;

import cookbook.model.AdminDataHandler;
import cookbook.model.DatabaseUtilProviderImpl;
import cookbook.model.UserDataHandler;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

/** Ui for when the book opens. */
public class AdminUserPanel {

  // Attributes.
  private StackPane root;
  private BookAnimation animation;
  private ObservableList<String> userList = FXCollections.observableArrayList();
  private ListView<String> userListView = new ListView<>();
  private Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/font/FREEPIXEL.ttf"), 15);
  private StringProperty searchUser = new SimpleStringProperty();
  private String adminUsername;

  /**
   * The Constructor.
   *
   * @param root the StackPane root.
   */
  public AdminUserPanel(StackPane root, String adminUsername) {
    this.root = root;
    this.animation = new BookAnimation(root);
    this.adminUsername = adminUsername;
    DatabaseUtilProviderImpl databaseUtilProvider = new DatabaseUtilProviderImpl();
    UserDataHandler userDataHandler = new UserDataHandler();
    userList = FXCollections.observableArrayList();

    String[] userFirstName = userDataHandler.getUserNames();

    userList.addAll(Arrays.asList(userFirstName));

    userListView.setItems(userList);

    userListView.setCellFactory(
        lv -> new ListCell<String>() {
          @Override
            protected void updateItem(String usernameList, boolean empty) {
              super.updateItem(usernameList, empty);
              getStyleClass().remove("list-cell");
              if (empty || usernameList == null) {
                setText(null);
                setGraphic(null);
              } else {
                userListView.getStyleClass().add("list-cell");
                setText(usernameList);
                setFont(pixelFont);
                getStyleClass().add("list-cell");
              }
            }
          });
    userListView.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2 && !event.isConsumed()) {
            event.consume();
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
              handleDoubleClick(selectedUser);
            }
          }
        });
  }

  /**
   * create a ui for scene after login book opens.
   */
  public void showUserPanel() {
    Image welcomeSign = new Image(AdminUserPanel.class.getResourceAsStream(
          "/assets/mainMenu/panelAccount.png"
          ));
    ImageView welcomeSignImage = new ImageView(welcomeSign);
    welcomeSignImage.setFitHeight(270);
    welcomeSignImage.setPreserveRatio(true);

    StackPane.setMargin(welcomeSignImage, new Insets(15, -255, 0, 0));

    root.getChildren().addAll(welcomeSignImage);
    searchUser();
  }

  /**
   * Search for user method.
   */
  private void searchUser() {
    TextField searchBar = new TextField();
    searchBar.setPromptText("Search...");
    searchBar.setFont(pixelFont);
    searchBar.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            searchUser.set(searchBar.getText());
            filterUsers(searchBar.getText());
          }
        });
    searchBar.getStyleClass().add("admin-text-field");

    // Adding Users into the view
    VBox layout = new VBox();
    layout.getStyleClass().add("transparent-container");
    layout.getChildren().addAll(searchBar, userListView);
    StackPane.setMargin(layout, new Insets(173, 190, 170, 440));
    root.getChildren().add(layout);
  }

  /**
   * Filter users.
   *
   * @param filter filter to use.
   */
  private void filterUsers(String filter) {
    ObservableList<String> filteredList = FXCollections.observableArrayList();
    if (filter == null || filter.isEmpty()) {
      filteredList.setAll(userList);
    } else {
      for (String user : userList) {
        if (user.toLowerCase().contains(filter.toLowerCase())) {
          filteredList.add(user);
        }
      }
    }
    userListView.setItems(filteredList);
  }

  public StringProperty searchUserProperty() {
    return searchUser;
  }

  /**
   * This method handles double click action.
   *
   * @param usernameList users usernameList.
   */
  private void handleDoubleClick(String usernameList) {
    DatabaseUtilProviderImpl databaseUtilProvider = new DatabaseUtilProviderImpl();
    UserDataHandler userDataHandler = new UserDataHandler();
    AdminDataHandler adminDataHandler = new AdminDataHandler();
    AdminUserSetting usernameListSetting = new AdminUserSetting(root, adminUsername,
                     userDataHandler, adminDataHandler);
    root.getChildren().clear();
    animation.openPageAnimation("assets/animation/pageFlipAnimation",422);

    PauseTransition pause = new PauseTransition(Duration.seconds(1.7));
    pause.setOnFinished(e -> {
      root.getChildren().clear();
      usernameListSetting.showUserSettingPanel(usernameList);
    });
    pause.play();
  }
}
