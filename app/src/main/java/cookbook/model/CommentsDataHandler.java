package cookbook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to handle comunication for comments with the database. 
 */
public class CommentsDataHandler {
  private final DatabaseUtilProvider databaseUtilProvider;

  public CommentsDataHandler() {
    this.databaseUtilProvider = new DatabaseUtilProviderImpl();
  }

  /*
   * Removes comment, user can remove their own and admin can remove all.
   */
  public boolean removeComment(int userId, int recipeId, int commentId) {
    // Check if the user is an admin
    boolean isAdmin = false;
    String adminCheckSql = "SELECT isAdmin FROM users WHERE id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(adminCheckSql)) {
        pstmt.setInt(1, userId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                isAdmin = rs.getInt("isAdmin") == 1;
            }
        }
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
        return false;
    }

    // Delete the comment if the user is the owner or an admin
    String sql = isAdmin ? "DELETE FROM comments WHERE id = ?" :
            "DELETE FROM comments WHERE user_id = ? AND recipe_id = ? AND id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
        if (isAdmin) {
            pstmt.setInt(1, commentId);
        } else {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, recipeId);
            pstmt.setInt(3, commentId);
        }
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
        return false;
    }
  }

  /**
   * Method to add a comment to a recipe.
   */
  public boolean addComment(int userId, int recipeId, String comment) {
    String sql = "INSERT INTO comments (user_id, recipe_id, comment) VALUES (?, ?, ?)";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      pstmt.setInt(2, recipeId);
      pstmt.setString(3, comment);
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      System.out.println("SQL Error: " + e.getMessage());
      return false;
    }
  }

  /**
   * Method to add a shared recipe to the database (shareablerecipes).
   */
  public boolean addSharedRecipe(String senderUsername, String recipientUsername, int recipeId, String message) {
    String sql = "INSERT INTO shareablerecipes (sender_username, recipient_username, recipe_id, message) VALUES (?, ?, ?, ?)";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, senderUsername);
      pstmt.setString(2, recipientUsername);
      pstmt.setInt(3, recipeId);
      pstmt.setString(4, message);
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      System.out.println("SQL Error: " + e.getMessage());
      return false;
    }
  }

  /**
   * Method to get shared comments for a specific recipient and recipe (shareablerecipes).
   */
  public List<String> getSharedComments(String recipientUsername, int recipeId) {
    String sql = "SELECT sender_username, message FROM shareablerecipes WHERE recipient_username = ? AND recipe_id = ?";
    List<String> comments = new ArrayList<>();
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, recipientUsername);
      pstmt.setInt(2, recipeId);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          String senderUsername = rs.getString("sender_username");
          String message = rs.getString("message");
          comments.add(senderUsername + ": " + message);
        }
      }
    } catch (SQLException e) {
      System.out.println("SQL Error: " + e.getMessage());
    }
    return comments;
  }

  /**
   * Method to get the latest comments for a specific recipe.
   */
  public List<String> getLatestComments(int recipeId) {
    String sql = "SELECT users.username, comments.comment, comments.created_at FROM comments JOIN users ON comments.user_id = users.id WHERE comments.recipe_id = ? ORDER BY comments.created_at ASC";
    List<String> comments = new ArrayList<>();
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, recipeId);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          String username = rs.getString("username");
          String comment = rs.getString("comment");
          String createdAt = rs.getString("created_at");
          comments.add(username + ": " + comment + " " + createdAt);
        }
      }
    } catch (SQLException e) {
      System.out.println("SQL Error: " + e.getMessage());
    }
    return comments;
  }

  /**
 * Method to get the list of recipes shared with a specific recipient.
 */
  public List<Integer> getSharedRecipes(String recipientUsername) {
    String sql = "SELECT recipe_id FROM shareablerecipes WHERE recipient_username = ?";
    List<Integer> recipeIds = new ArrayList<>();
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, recipientUsername);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int recipeId = rs.getInt("recipe_id");
                recipeIds.add(recipeId);
            }
        }
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
    }
    return recipeIds;
  }

    /**
     * Method to edit a comment. A user can edit their own comments and an admin can edit any comment.
     */
  public boolean editComment(int userId, int commentId, String newComment) {
    // Check if the user is an admin
    boolean isAdmin = false;
    String adminCheckSql = "SELECT isAdmin FROM users WHERE id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(adminCheckSql)) {
        pstmt.setInt(1, userId);
        try (ResultSet rs = pstmt.executeQuery()) {
          if (rs.next()) {
            isAdmin = rs.getInt("isAdmin") == 1;
          }
        }
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
        return false;
    }

    // Update the comment if the user is the owner or an admin
    String sql = isAdmin ? "UPDATE comments SET comment = ? WHERE id = ?" :
          "UPDATE comments SET comment = ? WHERE id = ? AND user_id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, newComment);
        pstmt.setInt(2, commentId);
        if (!isAdmin) {
            pstmt.setInt(3, userId);
        }
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
        return false;
    }
  }

  /**
   * Method to get the comment ID based on user ID, recipe ID, and the comment text.
   */
  public Integer getCommentId(int userId, int recipeId, String comment) {
    String sql = "SELECT id FROM comments WHERE user_id = ? AND recipe_id = ? AND comment = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, userId);
        pstmt.setInt(2, recipeId);
        pstmt.setString(3, comment);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
    }
    return null; // Return null if no comment ID is found
  }
}