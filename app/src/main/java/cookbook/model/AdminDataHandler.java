package cookbook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import cookbook.controller.UserController;


/**
 * Class to access database
 * to handle all operations related to adminController.
 */
public class AdminDataHandler {

  private final DatabaseUtilProvider databaseUtilProvider;
  private final UserDataHandler userDataHandler;

  public AdminDataHandler() {
    
    this.databaseUtilProvider = new DatabaseUtilProviderImpl(); 
    this .userDataHandler = new UserDataHandler(){
      
    };
  }

 
  /**
   * Adding user to the system by admin.

   * @param user an user object to be added.
   * @param dbModel the connection to database.
   */
  public void addUser(User user) {
    String insertQuery = "INSERT INTO users (username, password, first_name, last_name)"
                           + "VALUES (?, ?, ?, ?);";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
            PreparedStatement stmt = connection.prepareStatement(insertQuery)) {

      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getFirstName());
      stmt.setString(4, user.getLastName());
      stmt.execute();

    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  
   /**
   * delete uder
   * 
   */
  
  public void deleteUser(String username) {
    int userId = userDataHandler.getUserIdFromUsername(username);

    if (userId == 0) {
      System.out.println("User not found.");
      return; // Exit the method if user not found
    }

    String deleteCommentsQuery = "DELETE FROM comments WHERE user_id = ?";
    String deleteFavoritesQuery = "DELETE FROM favorites WHERE user_id = ?";
    String deleteFavoriteIngredientsQuery = "DELETE FROM favorite_ingredients WHERE user_id = ?";
    String deleteWeeklyDinnerListsQuery = "DELETE FROM weeklydinnerlists WHERE userId = ?";
    String deleteUserQuery = "DELETE FROM users WHERE id = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement deleteCommentsStmt = connection.prepareStatement(deleteCommentsQuery);
         PreparedStatement deleteFavoritesStmt = connection.prepareStatement(deleteFavoritesQuery);
         PreparedStatement deleteFavoriteIngredientsStmt = connection.prepareStatement(deleteFavoriteIngredientsQuery);
         PreparedStatement deleteWeeklyDinnerListsStmt = connection.prepareStatement(deleteWeeklyDinnerListsQuery);
         PreparedStatement deleteUserStmt = connection.prepareStatement(deleteUserQuery)) {

      connection.setAutoCommit(false); // Start a transaction

      // Set parameters for each prepared statement
      deleteCommentsStmt.setInt(1, userId);
      deleteFavoritesStmt.setInt(1, userId);
      deleteFavoriteIngredientsStmt.setInt(1, userId);
      deleteWeeklyDinnerListsStmt.setInt(1, userId);
      deleteUserStmt.setInt(1, userId);

      // Execute the DELETE statements
      deleteCommentsStmt.executeUpdate();
      deleteFavoritesStmt.executeUpdate();
      deleteFavoriteIngredientsStmt.executeUpdate();
      deleteWeeklyDinnerListsStmt.executeUpdate();
      deleteUserStmt.executeUpdate();

        // Commit the transaction
        connection.commit();
        System.out.println("User deleted successfully.");
    } catch (SQLException e) {
      System.out.println(e);

    }
}

  /**
   * modifying username of a user by admin.

   * @param user object to be updated.
   * @param dbModel the connection to database.
   * @param newValue the new username. 
   */
  public void modifyUsername(User user, String newValue) {
    String modifyQuery = "update users set username = ? where username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();

            PreparedStatement stmt = connection.prepareStatement(modifyQuery)) {

      stmt.setString(1, newValue);
      stmt.setString(2, user.getUsername());
      stmt.execute();

    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * modifying password of a user by admin.

   * @param user object to be updated.
   * @param dbModel the connection to database.
   * @param newValue the new password. 
   */
  public void modifyPassword(User user, String newValue) {
    String modifyQuery = "update users set password = ? where username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();

            PreparedStatement stmt = connection.prepareStatement(modifyQuery)) {

      stmt.setString(1, newValue);
      stmt.setString(2, user.getUsername());
      stmt.execute();

    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * modifying first name of a user by admin.

   * @param user object to be updated.
   * @param dbModel the connection to database.
   * @param newValue the new first name. 
   */
  public void modifyFirstName(User user, String newValue) {
    String modifyQuery = "update users set first_name = ? where username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();

            PreparedStatement stmt = connection.prepareStatement(modifyQuery)) {

      stmt.setString(1, newValue);
      stmt.setString(2, user.getUsername());
      stmt.execute();

    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * modifying password of a user by admin.

   * @param user object to be updated.
   * @param dbModel the connection to database.
   * @param newValue the new last name. 
   */
  public void modifyLastName(User user,  String newValue) {
    String modifyQuery = "update users set last_name = ? where username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement stmt = connection.prepareStatement(modifyQuery)) {

      stmt.setString(1, newValue);
      stmt.setString(2, user.getUsername());
      stmt.execute();

    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  
  /**
   * it gives the user admin prevliges.

   * 
   */

  public void modifyAdminStatus(User user, boolean isAdmin) {
    String modifyQuery = "UPDATE users SET isAdmin = ? WHERE username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement stmt = connection.prepareStatement(modifyQuery)) {

      stmt.setBoolean(1, isAdmin);
      stmt.setString(2, user.getUsername());
      stmt.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e);
    }
  }


  /**
   * This method to fetch all users names.
   */
  public ArrayList<String> getAllUsersname() {
    String slectQuery = "select username from users where isAdmin = ?";
    ArrayList<String> usernames = new ArrayList<String>();

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement stmt = connection.prepareStatement(slectQuery)) {
    
      stmt.setInt(1, 0);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        usernames.add(rs.getString("username"));
      }
      return usernames;
      
    } catch (SQLException e) {
      System.out.println(e);
    }
    return usernames;

  }

  /**
   * This method is to  get admin first name.
   */

  public String getAdminFirstName(String username) {
    String query = "SELECT first_name FROM users WHERE username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
          PreparedStatement stmt = connection.prepareStatement(query)) {

      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        return rs.getString("first_name");
      }

    } catch (SQLException e) {
      System.out.println(e);
    }

    return null;
  }

  /**
   * This method is to  get admin first name.
   */

  public String getAdminLastName(String username) {
    String query = "SELECT last_name FROM users WHERE username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
          PreparedStatement stmt = connection.prepareStatement(query)) {

      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        return rs.getString("last_name");
      }

    } catch (SQLException e) {
      System.out.println(e);
    }

    return null;
  }

  /**
  * This method is to update admin first name.
  */

  public boolean updateAdminFirstName(String username, String newFirstName) {
    String updateQuery = "UPDATE users SET first_name = ? WHERE username = ?";
    
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
            PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
        
      stmt.setString(1, newFirstName);
      stmt.setString(2, username);
        
      int rowsAffected = stmt.executeUpdate();
      return rowsAffected > 0;
        
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
  * This method is to update admin last name.
  */

  public boolean updateAdminLastName(String username, String newLastName) {
    String updateQuery = "UPDATE users SET last_name = ? WHERE username = ?";
    
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
            PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
        
      stmt.setString(1, newLastName);
      stmt.setString(2, username);
        
      int rowsAffected = stmt.executeUpdate();
      return rowsAffected > 0;
        
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
  * This method is to update user password.
  */

  public boolean updateAdminPassword(String username, String newPassword) {
    String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
    
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
            PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
        
      stmt.setString(1, newPassword);
      stmt.setString(2, username);
        
      int rowsAffected = stmt.executeUpdate();
      return rowsAffected > 0;
        
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }




}
