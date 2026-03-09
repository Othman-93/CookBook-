package cookbook.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Statement;
import java.util.Locale;
import java.util.Map;


/**
 * This clas is the base for receipes info.
 */
public class RecipeHandlerModel {
  private final DatabaseUtilProvider databaseUtilProvider;
    
  public RecipeHandlerModel() {
    this.databaseUtilProvider = new DatabaseUtilProviderImpl();
  }

  
  /**
   * This method is for listing all recipes.
   */

  public List<String> showAllRecipeNames() {
    List<String> recipeNames = new ArrayList<>();
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection()) {
      String sql = "SELECT name FROM recipes";
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        String name = resultSet.getString("name");
        recipeNames.add(name);
      }
      return recipeNames;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * This method is for listing recipes by tags.
   */
  public List<String> getRecipesNamesByTags(List<String> tags) {
    List<String> recipesNames = new ArrayList<>();

    //Fixing a (?, ?) for the query.
    String tagsNumbers = "(";
    for (int i = 0; i < tags.size(); i++) {
      tagsNumbers += "?";
      if (i < tags.size() - 1) { //as long as it is not the last tag.
        tagsNumbers += ", ";
      }
    }
    tagsNumbers += ")";

    String collectQuery = " select r.name from recipes r inner join"
                    + " recipetags rt on r.id = rt.recipe_id"
                    + " join tags t on rt.tag_id = t.id where t.name in " + tagsNumbers
                    + " group by r.id, r.name"
                    + " HAVING COUNT(DISTINCT t.name) = " + tags.size();

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement stmt = connection.prepareStatement(collectQuery)) {

      for (int i = 0; i < tags.size(); i++) {
        stmt.setString(i + 1, tags.get(i));
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String name = rs.getString("name");
        recipesNames.add(name);
      }
      return recipesNames;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * This method is for listing  recipes details.
   */

  public RecipeDataProvider showRecipeDetails(String recipeName)  {
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection()) {
      String sql = "SELECT * FROM recipes WHERE name = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, recipeName);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        // Display recipe details
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        int prepTime = resultSet.getInt("prep_time");
        int cookTime = resultSet.getInt("cook_time");
        int servings = resultSet.getInt("servings");

        return new RecipeDataProvider(name, description, prepTime, cookTime, servings);
      } 
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * This method is for listing  recipes by ingredince.
   */

  public List<String> getRecipesByIngredient(List<String> ingredients) {
    List<String> recipes = new ArrayList<>();

    
    String ingredientsNum = "(";
    for (int i = 0; i < ingredients.size(); i++) {
      ingredientsNum += "?";
      if (i < ingredients.size() - 1) { //as long as it is not the last tag.
        ingredientsNum += ", ";
      }
    }
    ingredientsNum += ")";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection()) {
  
      String collectQuery = " select r.name from recipes r inner join"
          + " recipe_ingredients ri on r.id = ri.recipe_id"
          + " join ingredients i on ri.ingredient_id = i.id where i.name in " + ingredientsNum
          + " group by r.id, r.name"
          + " HAVING COUNT(DISTINCT i.name) = " + ingredients.size();
      PreparedStatement stmt = connection.prepareStatement(collectQuery);

      for (int i = 0; i < ingredients.size(); i++) {
        stmt.setString(i + 1, ingredients.get(i));
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String name = rs.getString("name");
        recipes.add(name);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return recipes;
  }

  /**
   * Search recipes by name starting with a specific letter, case-insensitively.
   */
  public List<String> searchRecipesByName(String letter) {
    List<String> recipes = new ArrayList<>();
    String query = "SELECT name FROM recipes WHERE LOWER(name) LIKE ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query)) {
      pstmt.setString(1, letter.toLowerCase() + "%");

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          recipes.add(rs.getString("name"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return recipes;
  }

  /**
   * Method to fetch all tags names in the database.
   */
  public List<String> getAllTagsNames() { 
    List<String> tagsNames = new ArrayList<>();
    String selsectQuery = "select name from tags";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(selsectQuery)) {
        
      ResultSet rs = pstmt.executeQuery();
      
      while (rs.next()) {
        String name = rs.getString("name");
        name.toLowerCase();

        //to avoid dupplicates
        if (!tagsNames.contains(name)) {
          tagsNames.add(name); 
        }
        
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tagsNames;
  }

  /**
   * Method to fetch fav ingredients names in the database.
   */
  public List<String> getFavIngredientsNames(int userId) { 
    List<String> ingredientsNames = new ArrayList<>();
    String selsectQuery = 
        "SELECT name " 
        + "FROM (" 
        + "    SELECT i.name, MAX(fi.ingredient_id) AS ingredient_id " 
        + "    FROM ingredients i " 
        + "    LEFT JOIN favorite_ingredients fi ON i.id = fi.ingredient_id AND fi.user_id = ? " 
        + "    GROUP BY i.name" 
        + ") subquery " 
        + "ORDER BY " 
        + "    CASE WHEN ingredient_id IS NULL THEN 1 ELSE 0 END, " 
        + "    name ASC;";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(selsectQuery)) {
      
      pstmt.setInt(1, userId);
      ResultSet rs = pstmt.executeQuery();
      
      while (rs.next()) {
        String name = rs.getString("name");
        // name.toLowerCase();
        
        //to avoid dupplicates
        if (!ingredientsNames.contains(name)) {
          ingredientsNames.add(name); 
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ingredientsNames;
  }

  /**
   * Method to fetch all ingredients names in the database.
   */

  public List<String> getAllIngredientsNames() { 
    List<String> ingredientsNames = new ArrayList<>();
    String selsectQuery = "SELECT name From ingredients";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(selsectQuery)) {
      
      ResultSet rs = pstmt.executeQuery();
      
      while (rs.next()) {
        String name = rs.getString("name");
        // name.toLowerCase();
        
        //to avoid dupplicates
        if (!ingredientsNames.contains(name)) {
          ingredientsNames.add(name); 
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ingredientsNames;
  }


  /**
   * Get id from the name of the recipe.
   */
  public int getRecipeIdByName(String recipeName) {
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection()) {
      String sql = "SELECT id FROM recipes WHERE name = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, recipeName);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt("id");
      } else {
        throw new SQLException("Recipe not found: " + recipeName);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return -1; // Indicating an error
    }
  }

  /**
  * Add a recipe to favorites table in the database.
  * don't forget some sort of condition to check if the recipe is already in the favorites.
  */
  public boolean addRecipeToFavorites(int userId, String recipeName) {
    int recipeId = getRecipeIdByName(recipeName);
    if (recipeId == -1) {
      return false; // Recipe not found
    }
    String sql = "INSERT INTO favorites (user_id, recipe_id) VALUES (?, ?)";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      pstmt.setInt(2, recipeId);
      try {
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
      } catch (SQLIntegrityConstraintViolationException e) {
        // Duplicate entry detected, handle as needed
        System.out.println("Recipe with ID " + recipeId 
            + " is already in favorites for user " + userId);
        return false;
      }
    } catch (SQLException e) {
      System.out.println("SQL Error: " + e.getMessage());
      return false;
    }
  }

  /**
  * Remove a recipe from favorites table in the database.
  */
  public boolean removeRecipeFromFavorites(int userId, String recipeName) {
    int recipeId = getRecipeIdByName(recipeName);
    if (recipeId == -1) {
      return false; // Recipe not found
    }
    String sql = "DELETE FROM favorites WHERE user_id = ? AND recipe_id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      pstmt.setInt(2, recipeId);
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      System.out.println("SQL Error: " + e.getMessage());
      return false;
    }
  }
  
  public String getRecipeNameById(int recipeId) {
    String recipeName = null;
    String sql = "SELECT name FROM recipes WHERE id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, recipeId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            recipeName = rs.getString("name");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return recipeName;
  }

  /**
 * Get favorite recipes for a user.
 */
  public List<String> getFavoriteRecipesForUser(int userId) {
    List<String> favoriteRecipes = new ArrayList<>();
    String sql = "SELECT r.name FROM recipes r INNER "
         + "JOIN favorites f ON r.id = f.recipe_id WHERE f.user_id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String name = rs.getString("name");
        favoriteRecipes.add(name);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return favoriteRecipes;
  }

  
  /**
 * Get Recipes By ingredients And Tags .
 */
  public List<String> getRecipesByIngreAndTags(List<String> tags, List<String> ingredients){
    List<String> recipeTags = new ArrayList<>();
    List<String> recipeIngr = new ArrayList<>();
    List<String> recipeByBoth = new ArrayList<>();

    if (tags.isEmpty() && ingredients.isEmpty()) {
      return null;

    } else if (tags.isEmpty() || ingredients.isEmpty()) {

      if (tags.isEmpty()) {
        return recipeIngr = getRecipesByIngredient(ingredients);
      } else {
        return recipeTags = getRecipesNamesByTags(tags);
      }

    } 
    
    recipeTags = getRecipesNamesByTags(tags);
    recipeIngr = getRecipesByIngredient(ingredients);

    for (String name : recipeTags) {  
      if (recipeIngr.contains(name)) {
        recipeByBoth.add(name);
      }      
    }

    return recipeByBoth;

  }

 
  /**
 * add Recipe To Weekly Dinner List .
 */

  public boolean addRecipeToWeeklyDinnerList(int userId,
      LocalDate date, String dayOfWeek, String recipeName) {
    int recipeId = getRecipeIdByName(recipeName);
    if (recipeId == -1) {
      System.out.println("Recipe '" + recipeName + "' not found.");
      return false;
    }

    String sql = "INSERT INTO weeklydinnerlists (UserId, RecipeId, Date, " + 
        dayOfWeek + "Dishes) VALUES (?, ?, ?, ?)";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
          PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      pstmt.setInt(2, recipeId);
      pstmt.setDate(3, Date.valueOf(date)); // Convert LocalDate to SQL Date
      pstmt.setString(4, recipeName);
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
  /**
 * get Weekly Recipes ForUser .
 */

  public List<String> getWeeklyRecipesForUser(int userId) {
    List<String> weeklyRecipes = new ArrayList<>();

    String sql = "SELECT r.name, w.date " 
                + "FROM weeklydinnerlists w "
                 + "INNER JOIN recipes r ON w.RecipeId = r.id " 
                 +  "WHERE w.UserId = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String name = rs.getString("name");
        String date = rs.getString("date");
        weeklyRecipes.add(name + " - " + date);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return weeklyRecipes;
}

  /**
 * Method to adjust the servings of a recipe and fetch adjusted ingredient quantities.
 */
public Map<String, Map<String, Object>> adjustRecipeServingsAndGetIngredients(String recipeName, int newServings) {
    // Retrieve the recipe ID using its name
    int recipeId = getRecipeIdByName(recipeName);
  
    // Check if recipeId is valid (-1 indicates an error)
    if (recipeId == -1) {
      System.out.println("Recipe not found: " + recipeName);
      return null; // or throw an exception
    }
  
    // Retrieve the original servings for the recipe
    int originalServings = getOriginalServings(recipeId);

    // Calculate the serving ratio
    double servingRatio = (double) newServings / originalServings;
    servingRatio = Math.round(servingRatio * 100.0) / 100.0;

    // Fetch adjusted ingredient quantities
    return getAdjustedIngredientQuantities(recipeId, servingRatio);
  }


  /**
     * Method to retrieve the original number of servings for a recipe.
     */
  public int getOriginalServings(int recipeId) {
    // Assuming there's a column for servings in the recipes table
    String sql = "SELECT servings FROM recipes WHERE id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
           PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, recipeId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("servings");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0; // Default to 0 if no servings found (error handling)
  }

  /**
   * Method to fetch adjusted ingredient quantities based on serving ratio.
   */
  private Map<String, Map<String, Object>> getAdjustedIngredientQuantities(int recipeId, double servingRatio) {
    Map<String, Map<String, Object>> adjustedIngredients = new HashMap<>();
    String sql = "SELECT i.name AS ingredient_name, ri.quantity, i.unit " 
                + "FROM recipe_ingredients ri " 
                + "INNER JOIN ingredients i ON ri.ingredient_id = i.id " 
                + "WHERE ri.recipe_id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, recipeId);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String ingredientName = rs.getString("ingredient_name");
        double quantity = rs.getDouble("quantity");
        String unit = rs.getString("unit");

        double adjustedQuantity = Math.round(quantity * servingRatio * 100.0) / 100.0; 
        Map<String, Object> quantityWithUnit = new HashMap<>();
        quantityWithUnit.put("quantity", adjustedQuantity);
        quantityWithUnit.put("unit", unit);

        adjustedIngredients.put(ingredientName, quantityWithUnit);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return adjustedIngredients;
}




  
  
  /**
 * Gets the ingredients for all recipes in the user's weekly dinner list for the specified date.
 */

  public Map<String, List<String>> getWeeklyDinnerListIngredients(int userId) {
    Map<String, List<String>> result = new HashMap<>();

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection()) {

      String sql = "SELECT r.name AS recipe_name, i.name AS ingredient_name " 
                 + "FROM weeklydinnerlists w " 
                 + "INNER JOIN recipes r ON w.RecipeId = r.id " 
                 + "INNER JOIN recipe_ingredients ri ON r.id = ri.recipe_id " 
                 + "INNER JOIN ingredients i ON ri.ingredient_id = i.id " 
                 + "WHERE w.UserId = ?"; 

      try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, userId);


        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
          String recipeName = rs.getString("recipe_name");
          String ingredientName = rs.getString("ingredient_name");

          result.computeIfAbsent(recipeName, k -> new ArrayList<>()).add(ingredientName);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * Method to add tag/s to a recipe.
    */
  public boolean addTagsToRecipes(String tag, String recipeName) {

    String recipeQuery = "SELECT id FROM recipes WHERE name = ?";
    String insertTagQuery = "INSERT INTO tags (name) VALUES (?)";
    String insertRecipeTagQuery = "INSERT INTO recipetags (recipe_id, tag_id) VALUES (?, ?)";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement recipeStmt = connection.prepareStatement(recipeQuery);
        PreparedStatement insertTagStmt = connection.prepareStatement(insertTagQuery,
               Statement.RETURN_GENERATED_KEYS);
        PreparedStatement insertRecipeTagStmt = connection.prepareStatement(insertRecipeTagQuery)) {

      // Find the recipe ID
      recipeStmt.setString(1, recipeName);
      ResultSet rs = recipeStmt.executeQuery();
      
      int recipeId = 0;
      if (rs.next()) {
        recipeId = rs.getInt("id");
      }

      // Insert new tag in db.
      tag = capitalizeFirstLetter(tag);
      insertTagStmt.setString(1, tag);
      insertTagStmt.executeUpdate();
      rs = insertTagStmt.getGeneratedKeys();

      int tagId = 0;
      if (rs.next()) {
        tagId = rs.getInt(1);

        // Link new tag to the recipe
        insertRecipeTagStmt.setInt(1, recipeId);
        insertRecipeTagStmt.setInt(2, tagId);
        insertRecipeTagStmt.executeUpdate();
      }
      return true;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Method to add ingridenets to a recipe. 
   */
  public boolean addIngridentsToRecipe(List<Ingredient> ingredients, String recipeName) { 
    String recipeQuery = "SELECT id FROM recipes WHERE name = ?";
    String insertIngredentQuery = "INSERT INTO ingredients (name, unit) VALUES (?, ?)";
    String insertIngredintRecipe = "INSERT INTO recipe_ingredients"
                            + "(recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement recipeStmt = connection.prepareStatement(recipeQuery);
        PreparedStatement insertIngredienttmt = connection.prepareStatement(insertIngredentQuery,
                  Statement.RETURN_GENERATED_KEYS);
        PreparedStatement insertRecipeIngredienttmt = 
            connection.prepareStatement(insertIngredintRecipe)) {

      // Find the recipe ID
      recipeStmt.setString(1, recipeName);
      ResultSet rs = recipeStmt.executeQuery();
      
      int recipeId = 0;
      if (rs.next()) {
        recipeId = rs.getInt("id");
      }

      // Process each ingredient.
      for (Ingredient ingredient : ingredients) {
        String ingredentName = capitalizeFirstLetter(ingredient.getName());
      
        insertIngredienttmt.setString(1, ingredentName);
        insertIngredienttmt.setString(2, ingredient.getUnit());
        insertIngredienttmt.executeUpdate();
        rs = insertIngredienttmt.getGeneratedKeys();

        int ingredientId = 0;
        if (rs.next()) {
          ingredientId = rs.getInt(1);
        }

        // Link new ingredient to the recipe
        insertRecipeIngredienttmt.setInt(1, recipeId);
        insertRecipeIngredienttmt.setInt(2, ingredientId);
        insertRecipeIngredienttmt.setDouble(3, ingredient.getQuantity());
        insertRecipeIngredienttmt.executeUpdate();
      }
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Inserting new recipe to db. 
   */
  public boolean addNewRecipe(RecipeDataProvider recipe, List<String> tags,
       List<Ingredient> ingridents, String instruction) {
        
    String insertQuery = "INSERT INTO recipes (name, description, prep_time, cook_time, servings)"
                            + "VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {

      //fixing formatting
      recipe.setName(capitalizeFirstLetter(recipe.getName()));

      pstmt.setString(1, recipe.getName());
      pstmt.setString(2, recipe.getDescription());
      pstmt.setInt(3, recipe.getPrepTime());
      pstmt.setInt(4, recipe.getCookTime());
      pstmt.setInt(5, recipe.getServings());
      pstmt.execute();

      //adding ingredients to a recipe.
      if (!addIngridentsToRecipe(ingridents, recipe.getName())) {
        return false;
      }
      
      //adding tags to a recipe.
      for (String tag : tags) {
        if (!addTagsToRecipes(tag, recipe.getName())) {
          return false;
        }
      }
      
      //adding instructions to recipe. 
      if (!addInstructionToRecipes(recipe.getName(), instruction)) {
        return false;
      }

      return true;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }


  private boolean addInstructionToRecipes(String recipeName, String instruction) {
    String collectQuery = "select id from recipes where name = ? ";
    String insertQuery = "insert into instructions (recipe_id, step_number, instruction)"
                        +  "values (?, 1, ?)";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement collectStmt = connection.prepareStatement(collectQuery)) {

      collectStmt.setString(1, recipeName);
      ResultSet rs = collectStmt.executeQuery();

      if (rs.next()) {
        int recipeId = rs.getInt("id");
        PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
        insertStmt.setInt(1, recipeId);
        insertStmt.setString(2, instruction);
        insertStmt.executeUpdate();

      }
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }


  /**
   * To formatt all new data in the db. 
   */
  private  String capitalizeFirstLetter(String input) {
    return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
  }

  private int getIngredientIdByName(String ingredientName) {
    String sql = "SELECT id FROM ingredients WHERE name = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, ingredientName);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt("id");
      } else {
        throw new SQLException("Ingredient not found: " + ingredientName);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return -1; // Indicating an error
    }
  }

  /**
   * Adds an ingredient to the user's favorites.
   * 
   */
  public boolean addIngredientToFavorites(int userId, String ingredientName) {
    int ingredientId = getIngredientIdByName(ingredientName);
    if (ingredientId == -1) {
      return false; // Ingredient not found
    }
    String sql = "INSERT INTO favorite_ingredients (user_id, ingredient_id) VALUES (?, ?)";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      pstmt.setInt(2, ingredientId);
      try {
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
      } catch (SQLIntegrityConstraintViolationException e) {
        // Duplicate entry detected, handle as needed
        System.out.println("Ingredient with ID " + ingredientId 
                + " is already in favorites for user " + userId);
        return false;
      }
    } catch (SQLException e) {
      System.out.println("SQL Error: " + e.getMessage());
      return false;
    }
  }

  /**
   * Removes an ingredient from the user's favorites.
   */
  public boolean removeIngredientFromFavorites(int userId, String ingredientName) {
    int ingredientId = getIngredientIdByName(ingredientName);
    if (ingredientId == -1) {
      return false; // Ingredient not found
    }
    String sql = "DELETE FROM favorite_ingredients WHERE user_id = ? AND ingredient_id = ?";
    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      pstmt.setInt(2, ingredientId);
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      System.out.println("SQL Error: " + e.getMessage());
      return false;
    }
  }

  public List<Ingredient> getIngredientsForRecipe(String recipeName) {
    List<Ingredient> ingredients = new ArrayList<>();
    String collectQuery = "SELECT i.id, i.name, i.unit, ri.quantity " +
                 "FROM ingredients i " +
                 "JOIN recipe_ingredients ri ON i.id = ri.ingredient_id " +
                 "JOIN recipes r ON ri.recipe_id = r.id " +
                 "WHERE r.name = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(collectQuery)) {
      pstmt.setString(1, recipeName);
      ResultSet rs = pstmt.executeQuery();
    
      //adding each ingredint.
      while (rs.next()) {
        String name = rs.getString("name");
        String unit = rs.getString("unit");
        double quantity = rs.getDouble("quantity");
        ingredients.add(new Ingredient(name, unit, quantity));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return ingredients;
}

  public List<String> getTagsForRecipe(String recipeName) { 
    List<String> tags = new ArrayList<>();
    String collectQuery = "select t.name from tags t " 
                          + "JOIN recipetags rt ON t.id = rt.tag_id " 
                          + "JOIN recipes r ON rt.recipe_id = r.id " 
                          + "WHERE r.name = ?";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
    PreparedStatement pstmt = connection.prepareStatement(collectQuery)) {
    pstmt.setString(1, recipeName);
    ResultSet rs = pstmt.executeQuery();

    while (rs.next()) {
      tags.add(rs.getString("name"));
    }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return tags;

  }

  public List<String> getInstructionForRecipe(String recipeName) { 

    List<String> instructions = new ArrayList<>();
    String collectQuery = "select instruction from instructions i " 
                          + "join recipes r on i.recipe_id = r.id " 
                          + "where r.name = ? " 
                          + "order by i.step_number ";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(collectQuery)) {

      pstmt.setString(1, recipeName);
      ResultSet rs = pstmt.executeQuery();
      
      while (rs.next()) {
        instructions.add(rs.getString("instruction"));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return instructions;
  }

  /**
   * This method is to check whether a recipe is a user's favorite.
   */
  public boolean isFavorite(String userName, String recipeName) {
    String selectQuery = "select count(*) as count "
                        + "from favorites f "
                        + "join users u on f.user_id = u.id "
                        + "join recipes r on f.recipe_id = r.id "
                        + "where u.username = ? and r.name = ? ";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {


      pstmt.setString(1, userName);
      pstmt.setString(2, recipeName);
      ResultSet rs = pstmt.executeQuery();
  
      if (rs.next()) {
        int count = rs.getInt("count"); 
        if (count > 0) { //return true if column exists.
          return true;
        }
        
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }


  public boolean isExisted(String recipeName) {
    String selectQuery = "select * from recipes where lower(name) = lower(?)";

    try (Connection connection = databaseUtilProvider.getDatabaseUtil().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {

      pstmt.setString(1, recipeName);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) { 
        return true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  // public static void main(String[] args) {
    // RecipeDataProvider recipe = new RecipeDataProvider("Pizza", "Good", 1, 2, 3);
    // String instru = "Bla bla bla";
    // RecipeHandlerModel rh = new RecipeHandlerModel();
    // List tags = new ArrayList<>();
    // List ing = new ArrayList<>();
    // tags.add("main cource");
    // tags.add("italian");
    // ing.add("Tometo");
    // ing.add("Mozzarela");
    // System.out.println(rh.isExisted("banana Bread"));
    // rh.addNewRecipe(recipe, tags, ing, instru);
  // }
}



