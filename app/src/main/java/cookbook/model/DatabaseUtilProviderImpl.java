package cookbook.model;


/**
 * This class implements the DatabaseUtilProvider interface.
 */ 
public class DatabaseUtilProviderImpl implements DatabaseUtilProvider {

  private final DatabaseUtil databaseUtil;

  /**
 * TThis method gets the databse cridintials.
 */ 
  
  public DatabaseUtilProviderImpl() {
    PersonalizedDatabase Personalized = new PersonalizedDatabase();
    String schemaName = Personalized.schemaName;
    String dbUser = Personalized.dbUser;
    String dbPassword = Personalized.dbPassword;
    String dbUrl = "jdbc:mysql://localhost/" + schemaName + "?user=" + dbUser + "&password="+ dbPassword + "&useSSL=false";

    DatabaseConnectionProvider databaseConnectionProvider =
        new DatabaseConnectionProvider(dbUrl, dbUser, dbPassword);
    databaseUtil = new DatabaseUtil(databaseConnectionProvider);
  }

  @Override
    public DatabaseUtil getDatabaseUtil() {
    return databaseUtil;
  }
}