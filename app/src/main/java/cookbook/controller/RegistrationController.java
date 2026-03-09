package cookbook.controller;

import cookbook.model.DatabaseUtilProviderImpl;
import cookbook.model.User;
import cookbook.model.UserDataHandler;

import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 * This class is for registration logic.
 */
public class RegistrationController {

  // Class fields.
  private UserDataHandler userDataHandler;  // UserDataHandler object.

  // Constants for maximum character lengths based on database VARCHAR limits.
  private final int MAX_FIRST_NAME_LENGTH = 50;
  private final int MAX_LAST_NAME_LENGTH = 50;
  private final int MAX_USERNAME_LENGTH = 50;
  private final int MAX_PASSWORD_LENGTH = 255;


  /**
   * The constructor for the RegistrationController class.
   */
  public RegistrationController() {
    // userDataHandler object to access database.
    this.userDataHandler = new UserDataHandler();
  }

  /**
   * Method to start the Registration process.
   *
   * @param fname user's first name.
   * @param lname user's last name.
   * @param username user's username (must be unique).
   * @param password user's password.
   */
  public void registerNewUser(String fname, String lname, String username, String password) {

    // List for storing error messages.
    ArrayList<String> errors = new ArrayList<>();

    //Validation.
    // Checks if registration values are within database VARCHAR limits.
    if (!isValidLength(fname, MAX_FIRST_NAME_LENGTH)) {
      errors.add("First name must be between 1-50 characters long");
    }
    if (!isValidLength(lname, MAX_LAST_NAME_LENGTH)) {
      errors.add("Last name must be between 1-50 characters long");
    }
    if (!isValidLength(username, MAX_USERNAME_LENGTH)) {
      errors.add("Username must be between 1-50 characters long");
    }
    if (!isValidLength(password, MAX_PASSWORD_LENGTH)) {
      errors.add("Password must be between 1-255 characters long");
    }

    // Checks if username already taken.
    if (isUserNameTaken(username)) {
      errors.add("Username already taken");
    }

    // Check if any errors, if so display them to user and return.
    if (!errors.isEmpty()) {
      // Join all error messages.
      String errorMessage = String.join("\n", errors);
      // Registration failed pop up.
      showAlert(Alert.AlertType.ERROR, "Registration failed!", errorMessage);
      return;
    }

    // If all registration values valid instantiate new UserModel and try adding to database.
    User newUser = new User(username, password, fname, lname);
    boolean userIsAdded = userDataHandler.addUserToDatabase(newUser);

    // If user not added to database.
    if (!userIsAdded) {
      // Registration failed pop up.
      showAlert(Alert.AlertType.ERROR, "Registration failed!", "An unexpected error occured");
      return;
    }

    // If registration was successful display pop up.
    showAlert(Alert.AlertType.INFORMATION,
        "Registration successful",
              "Thank you for joining us " + fname + "!");
  }

  /**
   * This method checks whether username taken or not.
   *
   * @param username the username to check.
   * @return true if taken or false if available.
   */
  private boolean isUserNameTaken(String username) {
    return userDataHandler.checkUserExists(username);
  }

  /**
   * This method checks if a string is within a certain character length limit.
   *
   * @param string string to check.
   * @param maxLength the max allowed string length.
   * @return true if within limit and not empty, else false.
   */
  private boolean isValidLength(String string, int maxLength) {
    return string != null && !string.isEmpty() && string.length() <= maxLength;
  }

  /**
   * This method display an alert to user.
   *
   * @param type Alert type.
   * @param title Alert title.
   * @param message Alert message.
   */
  private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
