package eugen.enterprise.easyfit.acquaintance.helpers;

import eugen.enterprise.easyfit.acquaintance.enums.ECalorieTarget;

public class MacroResult {
    private boolean male;
    private ECalorieTarget calorieTarget;
    private double weight;
    private double height;
    private int age;
    private String activity;
    private int calories;
    private int proteins;
    private int carbs;
    private int fat;

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public ECalorieTarget getCalorieTarget() {
        return calorieTarget;
    }

    public void setCalorieTarget(ECalorieTarget calorieTarget) {
        this.calorieTarget = calorieTarget;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }
}
