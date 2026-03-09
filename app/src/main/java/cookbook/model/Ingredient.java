package cookbook.model;

public class Ingredient {
  private String name; 
  private String unit; 
  private double quantity;

  public Ingredient(String name, String unit, double quantity) {
    this.name = name;
    this.unit = unit;
    this.quantity = quantity;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public double getQuantity() {
    return quantity;
  }


}
