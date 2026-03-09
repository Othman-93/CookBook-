package cookbook.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cookbook.model.RecipeHandlerModel;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import cookbook.model.Ingredient;
import cookbook.model.RecipeDataProvider;

public class RecipeController {

  private final RecipeHandlerModel recipeHandlerModel;




  public RecipeController() {
    this.recipeHandlerModel = new RecipeHandlerModel();
  }

  public List<String> getAllRecipeNames()  {
    return recipeHandlerModel.showAllRecipeNames();
  }

  public List<String> getRecipesByTags(List<String> tags)  {
    return recipeHandlerModel.getRecipesNamesByTags(tags);
  }

  public RecipeDataProvider getRecipeDetails(String recipeName)  {
    return recipeHandlerModel.showRecipeDetails(recipeName);
  }

  public List<String> getRecipesByIngredient(List<String> ingredient)  {
    return recipeHandlerModel.getRecipesByIngredient(ingredient);
  }

  public List<String> searchRecipesByName(String letter)  {
    return recipeHandlerModel.searchRecipesByName(letter);
  }

  public List<String> getAllTagsNames()  {
    return recipeHandlerModel.getAllTagsNames();
  }

  public List<String> getAllIngredientsNames()  {
    return recipeHandlerModel.getAllIngredientsNames();
  }

  public void addToFavorites(int userId, String recipeName)  {
    recipeHandlerModel.addRecipeToFavorites(userId, recipeName);
  }

  public boolean removeFromFavorites(int userId, String recipeName)  {
    return recipeHandlerModel.removeRecipeFromFavorites(userId, recipeName);
  }

  public void addTagToRecipe(String text, String recipeName) {
    recipeHandlerModel.addTagsToRecipes(text, recipeName);
  }

  public List<Ingredient> getIngredintForRecipe(String recipeName) {
    return recipeHandlerModel.getIngredientsForRecipe(recipeName);
  }

  public List<String> getTagsForRecipe(String recipeName) {
    return recipeHandlerModel.getTagsForRecipe(recipeName);
  }

  public List<String> getInstructions(String recipName) {
    return recipeHandlerModel.getInstructionForRecipe(recipName);
  }

  public boolean isFavorite(String userName, String recipeName) {
    return recipeHandlerModel.isFavorite(userName, recipeName);
  }

  public boolean addNewRecipe(RecipeDataProvider recipe, List<String> tags,
                              List<Ingredient> ingridents, String instruction) {
    // String for storing error messages.
    String errorMessage = "";
    // Check if recipe exists already.
    if (isExisted(recipe.getName())) {
      errorMessage += "Recipe already exists\n";
    }
    List<String> errors = collectErrors(recipe, tags, ingridents, instruction);
    //check if there is a missed data.
    if (errors.size() > 0) {
      for (String error : errors) {
        errorMessage += error + "\n";
      }
    }
    if (errorMessage.isEmpty() || errorMessage == "") {
      if (!recipeHandlerModel.addNewRecipe(recipe, tags, ingridents, instruction)) {
        showMessage(Alert.AlertType.ERROR, "Error", "An unexpected error occured");
        return false;
      } else {
        showMessage(Alert.AlertType.INFORMATION, "Success", "Recipe successfully added!");
        return true;
      }
    } else {
      showMessage(Alert.AlertType.ERROR, "Error", errorMessage);
      return false;
    }
  }

  private List<String> collectErrors(RecipeDataProvider recipe, List<String> tags, List<Ingredient> ingridents,
      String instruction) {

    List<String> errors = new ArrayList<>();
    if (recipe.getName() == null || recipe.getName().isEmpty()) {
      errors.add("Recipe name cannot be empty");
    }

    if (ingridents.isEmpty()) {
      errors.add("No ingredients added");
    }

    if (tags.isEmpty()) {
      errors.add("No tags added");
    }

    if (recipe.getPrepTime() == 0) {
      errors.add("Invalid value for prep time");
    }

    if (recipe.getCookTime() == 0) {
      errors.add("Invalid value for cook time");
    }

    if (instruction == null || instruction.isEmpty()) {
      errors.add("Instruction cannot be empty");
    }

    if (recipe.getDescription() == null || recipe.getDescription().isEmpty()) {
      errors.add("Short description cannot be empty");
    }

    return errors;
  }

  private boolean isExisted(String recipeName) {
    return recipeHandlerModel.isExisted(recipeName);
  }

  private void showMessage(Alert.AlertType type, String title, String message) {
    Platform.runLater(() -> {
      Alert alert = new Alert(type);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
    });
  }

}
