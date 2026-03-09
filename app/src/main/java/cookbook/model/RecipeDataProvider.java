package cookbook.model;

/**
 * this class is for prividing rcipes info.
 */
public class RecipeDataProvider {
  private String name;
  private String description;
  private int prepTime;
  private int cookTime;
  private int servings;

  /**
 * the class constractors.
 */
  public RecipeDataProvider(String name,
      String description, int prepTime, int cookTime, int servings) {
    this.name = name;
    this.description = description;
    this.prepTime = prepTime;
    this.cookTime = cookTime;
    this.servings = servings;
  }

  // Getters and setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getPrepTime() {
    return prepTime;
  }

  public void setPrepTime(int prepTime) {
    this.prepTime = prepTime;
  }

  public int getCookTime() {
    return cookTime;
  }

  public void setCookTime(int cookTime) {
    this.cookTime = cookTime;
  }

  public int getServings() {
    return servings;
  }

  public void setServings(int servings) {
    this.servings = servings;
  }
}