package cookbook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cookbook.controller.UserController;

/**
 * Class to access the database.
 * Handles all operations related to registration and login functionalities.
 */
public class UserDataHandler {

  private final DatabaseUtilProvider databaseUtilProvider;

  public UserDataHandler() {
    this.databaseUtilProvider = new DatabaseUtilProviderImpl() {
      
    };
  }

  /**
   * Adds a new user to the database (Registration).
   *
   * @param user the new user to add.
   * @return true if the query is successful, false otherwise.
   */
  public boolean addUserToDatabase(User user) {
    String insertQuery = "INSERT INTO users (username, password, first_name, last_name) "
            + "VALUES (?, ?, ?, ?);";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement stmt = connection.prepareStatement(insertQuery)) {

      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getFirstName());
      stmt.setString(4, user.getLastName());
      stmt.execute();

      return true;

    } catch (SQLException e) {
      System.out.println(e);
      return false;
    }
  }

  /**
   * Check if user exists in database (Login).
   *
   */
  public boolean checkUserExists(String username) {
    String query = "SELECT username FROM users WHERE username = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {

      stmt.setString(1, username);
      ResultSet resultSet = stmt.executeQuery();

      return resultSet.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Check if password is correct for the given username (Login).
   *
   */
  public boolean checkPassword(String username, String password) {
    String query = "SELECT password FROM users WHERE username = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {

      stmt.setString(1, username);
      ResultSet resultSet = stmt.executeQuery();

      if (resultSet.next()) {
        String storedPassword = resultSet.getString("password");
        return storedPassword.equals(password);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Check if ther user is an admin.
   *
   */
  public boolean isAdmin(String userName) {
    String selectQuery = "select isAdmin from users where username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement stmt = connection.prepareStatement(selectQuery)) {

      stmt.setString(1, userName);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("isAdmin") == 1;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
  * This method is to fetch user full name, in order to display.
  */
  public String[] getUserFullname(String username) {
    String selectQuery = "select first_name, last_name from users where username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
    
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();

      String[] fullName = new String[2];
      if (rs.next()) {
        fullName[0] = rs.getString("first_name");
        fullName[1] = rs.getString("last_name");
      }
      return fullName;
  
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

   
  /**
  * This method is to get usernames.
  */

  public String[] getUserNames() {
    List<String> usernames = new ArrayList<>();
    String selectQuery = "SELECT username FROM users  WHERE username != 'admin1';";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        usernames.add(rs.getString("username"));
      }
      
    } catch (SQLException e) {
      e.printStackTrace();
    }
  
    return usernames.toArray(new String[0]);
  }

     
  /**
  * This method is to update user first name.
  */

  public boolean updateFirstName(String username, String newFirstName) {
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
  * This method is to update user last name.
  */

  public boolean updateLastName(String username, String newLastName) {
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

  public boolean updatePassword(String username, String newPassword) {
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

  public int getUserIdFromUsername(String username) {
    String selectQuery = "SELECT id FROM users WHERE username = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
        
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt("id");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return 0; // Default value if the user is not found
  }
  
  public List<String> getComboListUserNames() {
    List<String> usernames = new ArrayList<>();
    String selectQuery = "SELECT username FROM users  WHERE username != 'admin1';";
  
      try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
          PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          usernames.add(rs.getString("username"));
        }
        
      } catch (SQLException e) {
        e.printStackTrace();
      }
    
      return usernames;
  }






}



