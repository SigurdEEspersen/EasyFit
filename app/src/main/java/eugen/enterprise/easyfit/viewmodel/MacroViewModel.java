package eugen.enterprise.easyfit.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import eugen.enterprise.easyfit.acquaintance.enums.ECalorieTarget;
import eugen.enterprise.easyfit.acquaintance.helpers.MacroResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.model.repository.MacroService;

public class MacroViewModel extends ViewModel {

    MutableLiveData<String> saveStatus;
    MutableLiveData<MacroResult> savedMacros;
    MutableLiveData<Boolean> isMale;
    MutableLiveData<Double> weight;
    MutableLiveData<Double> height;
    MutableLiveData<Integer> age;
    MutableLiveData<String> activityLevel;
    MutableLiveData<ECalorieTarget> calorieTarget;
    MutableLiveData<Integer> calculatedCalories;
    MutableLiveData<Integer> calculatedProtein;
    MutableLiveData<Integer> calculatedCarbs;
    MutableLiveData<Integer> calculatedFat;

    MacroService macroService;

    public MacroViewModel() {
        macroService = new MacroService();
        saveStatus = new MutableLiveData<>();
        savedMacros = new MutableLiveData<>();
        isMale = new MutableLiveData<>();
        weight = new MutableLiveData<>();
        height = new MutableLiveData<>();
        age = new MutableLiveData<>();
        activityLevel = new MutableLiveData<>();
        calorieTarget = new MutableLiveData<>();
        calculatedCalories = new MutableLiveData<>();
        calculatedProtein = new MutableLiveData<>();
        calculatedCarbs = new MutableLiveData<>();
        calculatedFat = new MutableLiveData<>();
    }

    public MutableLiveData<String> getSaveStatus() {
        return saveStatus;
    }

    public void setSaveStatus(MutableLiveData<String> saveStatus) {
        this.saveStatus = saveStatus;
    }

    public MutableLiveData<MacroResult> getSavedMacros() {
        return savedMacros;
    }

    public void setSavedMacros(MutableLiveData<MacroResult> savedMacros) {
        this.savedMacros = savedMacros;
    }

    public void saveMacros(MacroResult result, Context c) {
        macroService.saveMacros(result, new Callback() {
            @Override
            public void onResponse(Object o) {
                saveStatus.postValue((String) o);
            }
        }, c);
    }

    public void loadMacros(Context c) {
        macroService.loadMacros(new Callback() {
            @Override
            public void onResponse(Object o) {
                savedMacros.postValue((MacroResult) o);
            }
        }, c);
    }

    public MutableLiveData<Boolean> getIsMale() {
        return isMale;
    }

    public void setIsMale(Boolean isMale) {
        this.isMale.postValue(isMale);
    }

    public MutableLiveData<Double> getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight.postValue(weight);
    }

    public MutableLiveData<Double> getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height.postValue(height);
    }

    public MutableLiveData<Integer> getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age.postValue(age);
    }

    public MutableLiveData<String> getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel.postValue(activityLevel);
    }

    public MutableLiveData<ECalorieTarget> getCalorieTarget() {
        return calorieTarget;
    }

    public void setCalorieTarget(ECalorieTarget calorieTarget) {
        this.calorieTarget.postValue(calorieTarget);
    }

    public MutableLiveData<Integer> getCalculatedCalories() {
        return calculatedCalories;
    }

    public void setCalculatedCalories(Integer calculatedCalories) {
        this.calculatedCalories.postValue(calculatedCalories);
    }

    public MutableLiveData<Integer> getCalculatedProtein() {
        return calculatedProtein;
    }

    public void setCalculatedProtein(Integer calculatedProtein) {
        this.calculatedProtein.postValue(calculatedProtein);
    }

    public MutableLiveData<Integer> getCalculatedCarbs() {
        return calculatedCarbs;
    }

    public void setCalculatedCarbs(Integer calculatedCarbs) {
        this.calculatedCarbs.postValue(calculatedCarbs);
    }

    public MutableLiveData<Integer> getCalculatedFat() {
        return calculatedFat;
    }

    public void setCalculatedFat(Integer calculatedFat) {
        this.calculatedFat.postValue(calculatedFat);
    }
}