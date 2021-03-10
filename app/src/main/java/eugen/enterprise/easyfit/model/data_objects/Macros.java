package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Macros {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected int macrosId;

    @ColumnInfo(name = "male")
    protected boolean male;

    @ColumnInfo(name = "calorieTarget")
    protected String calorieTarget;

    @ColumnInfo(name = "weight")
    protected double weight;

    @ColumnInfo(name = "height")
    protected double height;

    @ColumnInfo(name = "age")
    protected int age;

    @ColumnInfo(name = "activity")
    protected String activity;

    @ColumnInfo(name = "calories")
    protected int calories;

    @ColumnInfo(name = "proteins")
    protected int proteins;

    @ColumnInfo(name = "carbs")
    protected int carbs;

    @ColumnInfo(name = "fat")
    protected int fat;

    public int getMacrosId() {
        return macrosId;
    }

    public void setMacrosId(int macrosId) {
        this.macrosId = macrosId;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public String getCalorieTarget() {
        return calorieTarget;
    }

    public void setCalorieTarget(String calorieTarget) {
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
