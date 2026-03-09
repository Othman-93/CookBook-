package cookbook.model;

/**
 * This interface acts as a contract that defines how DatabaseUtil show act.
 */

public interface DatabaseUtilProvider {
  public DatabaseUtil getDatabaseUtil();
}
