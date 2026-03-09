package cookbook.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is for esaplshing database connection.
 */

public class DatabaseConnectionProvider {
  private  String dbUrl; 
  private  final String dbUser;
  private  final String dbPassword;

  /**
   * this method does the conncetion to the database.
   */

  public DatabaseConnectionProvider(String dbUrl, String dbUser, String dbPassword) {
    this.dbUrl = dbUrl;
    this.dbUser = dbUser;
    this.dbPassword = dbPassword;
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
  }

  /**
   * this method does CLOSING  conncetion FROM the database.
   */

  public void closeConnection(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
    
