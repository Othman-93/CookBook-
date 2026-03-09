package cookbook.controller;


import java.util.ArrayList;
import java.util.List;
import cookbook.model.RecipeHandlerModel;
import cookbook.model.User;
import cookbook.model.UserDataHandler;

/**
 * This class handles user information and interacts
 *  with the user data handler for various operations.
 */
public class UserController {
  private List<RecipeHandlerModel> favoriteRecipes; 
  private final UserDataHandler userDataHandler;


  /**
   * Constructor for user controller.
   * 
   * @param username - The username of the user.
   * @param password - The password of the user.
   * @param firstName - The first name of the user.
   * @param lastName - The last name of the user.
   * @param userDataHandler - The user data handler for database operations.
   */
  public UserController(UserDataHandler userDataHandler) {
    this.favoriteRecipes = new ArrayList<>();
    this.userDataHandler = userDataHandler;
  }

  /**
   * Registers a new user in the database.
   * 
   * @param username - The username for the new user.
   * @param password - The password for the new user.
   * @param firstName - The first name for the new user.
   * @param lastName - The last name for the new user.
   * @return true if the user is registered successfully, false otherwise.
   */
   public boolean registerUser(User user, String lastName) {
    return userDataHandler.addUserToDatabase(user);
  }


  public List<RecipeHandlerModel> getFavoritRecipes() {
    return favoriteRecipes;
  }


  public void setFavoritRecipes(List<RecipeHandlerModel> favoritRecipes) {
    this.favoriteRecipes = favoritRecipes;
  }

  // User Login Methods
  public boolean validateUsername(String username) {
    return userDataHandler.checkUserExists(username);
  }

  public boolean validateLogin(String username, String password) {
    return userDataHandler.checkPassword(username, password);
  }

  // User Information Methods
  public boolean isAdmin(String username) {
    return userDataHandler.isAdmin(username);
  }

  public String[] getUserFullname(String username) {
    return userDataHandler.getUserFullname(username);
  }

  // User Management Methods note this for all user names
  public String[] getUsernames() {
    return userDataHandler.getUserNames();
  }

  public boolean updateFirstName(String username, String newFirstName) {
    return userDataHandler.updateFirstName(username, newFirstName);
  }

  public boolean updateLastName(String username, String newLastName) {
    return userDataHandler.updateLastName(username, newLastName);
  }

  public boolean updatePassword(String username, String newPassword) {
    return userDataHandler.updatePassword(username, newPassword);
  }


    
}
