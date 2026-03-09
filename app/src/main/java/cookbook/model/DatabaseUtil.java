package cookbook.model;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * this class is is for  grtting and closing the conniction.
 */

public class DatabaseUtil {

  private final DatabaseConnectionProvider connectionProvider;

  public DatabaseUtil(DatabaseConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  public Connection getConnection() throws SQLException {
    return connectionProvider.getConnection(); 
  }

  

  /**
 * this method  is is for closing the conniction.
 */
  public void closeConnection(Connection conn) throws SQLException {
    if (conn != null) {
      conn.close();
    }
  }
}