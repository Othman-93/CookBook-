package cookbook.controller;

import cookbook.model.DatabaseUtilProvider;
import cookbook.model.UserDataHandler;
import cookbook.view.UserOpenBookView;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import cookbook.view.BookAnimation;
import cookbook.view.AdminOpenBookView;


/**
 * This class is for login conniction between model and view.
 */
public class LoginController {
  private UserDataHandler userDataHandler;
  private final DatabaseUtilProvider databaseUtilProvider;

  /**
 * This class is for login conniction between model and view.
 */

  public LoginController(UserDataHandler userDataHandler,
      DatabaseUtilProvider databaseUtilProvider) {
    this.userDataHandler = userDataHandler;
    this.databaseUtilProvider = databaseUtilProvider;
  }

  public boolean checkUserName(String userName) {
    return userDataHandler.checkUserExists(userName);
  }

  public boolean checkPassword(String userName, String password) {

    return userDataHandler.checkPassword(userName, password);
  }

  /**
 * This method is to check login.
 */

  public void manageLogin(String username, String password, StackPane root, Label notification) {
    UserOpenBookView openBookView = new UserOpenBookView(root, username);
    AdminOpenBookView adminOpenBook = new AdminOpenBookView(root, username);
    BookAnimation animation = new BookAnimation(root);


    if (username.isEmpty() || password.isEmpty()) {
      System.err.println("User name or password is empty, try again");

    } else {


      boolean userExist = checkUserName(username);
      boolean correctPassword = checkPassword(username, password);

      if (userExist && correctPassword) {
        if (isAdmin(username)) {
          root.getChildren().clear();
          animation.openAnimationBook("/assets/mainMenu/loginAnimationBook", true, 17, ".png", 0, 422);

          PauseTransition pause = new PauseTransition(Duration.seconds(3.7));
          pause.setOnFinished(e -> {


            root.getChildren().clear();
            adminOpenBook.showOpenBookScene();
          });
          pause.play();
        } else {
          root.getChildren().clear();
          animation.openAnimationBook("/assets/mainMenu/loginAnimationBook", true, 17, ".png", 0, 422);

          PauseTransition pause = new PauseTransition(Duration.seconds(3.7));
          pause.setOnFinished(e -> {

            //is that personilized enough? :D
            root.getChildren().clear();
            openBookView.showOpenBookScene();
          });
          pause.play();
        }

      } else {
        notification.setText("Invalid credentials"); 
        
      }
    }
  }

  // Utility method to show an alert dialog

  private boolean isAdmin(String username) {
    return userDataHandler.isAdmin(username);
  }

  /**
   * This method is to fetch user full name, in order to display.
   */
  public String[] getUserFullname(String username) {
    return userDataHandler.getUserFullname(username);
  }
  
}


