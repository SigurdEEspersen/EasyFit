package eugen.enterprise.easyfit.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.helpers.SetResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.acquaintance.interfaces.IExercise;
import eugen.enterprise.easyfit.acquaintance.interfaces.IPreferredExercise;
import eugen.enterprise.easyfit.model.repository.SettingsService;

public class SettingsViewModel extends ViewModel {
    private SettingsService settingsService;
    private MutableLiveData<IExercise[]> exercises;
    private MutableLiveData<Boolean> setPreferredExerciseStatus;
    private MutableLiveData<IPreferredExercise[]> savedPrefferedExercises;
    private MutableLiveData<List<SetResult>> setResults;

    public SettingsViewModel() {
        settingsService = new SettingsService();
        exercises = new MutableLiveData<>();
        setPreferredExerciseStatus = new MutableLiveData<>();
        savedPrefferedExercises = new MutableLiveData<>();
        setResults = new MutableLiveData<>();
    }

    public void loadExercises(Context c) {
        settingsService.loadExercises(new Callback() {
            @Override
            public void onResponse(Object o) {
                exercises.postValue((IExercise[]) o);
            }
        }, c);
    }

    public MutableLiveData<IExercise[]> getExercises() {
        return exercises;
    }

    public void setPreferredExercise(String exerciseName, EMuscleGroup muscleGroup, Context c) {
        settingsService.setPrefferedExercise(exerciseName, muscleGroup, c, new Callback() {
            @Override
            public void onResponse(Object o) {
                setPreferredExerciseStatus.postValue((Boolean) o);
            }
        });
    }

    public MutableLiveData<Boolean> getSetPreferredExerciseStatus() {
        return setPreferredExerciseStatus;
    }

    public void loadSavedPrefferedExercises(Context c) {
        settingsService.loadPrefferedExercises(c, new Callback() {
            @Override
            public void onResponse(Object o) {
                savedPrefferedExercises.postValue((IPreferredExercise[]) o);
            }
        });
    }

    public MutableLiveData<IPreferredExercise[]> getSavedPrefferedExercises() {
        return savedPrefferedExercises;
    }

    public void loadSetResults(Context c) {
        settingsService.loadSetResults(c, new Callback() {
            @Override
            public void onResponse(Object o) {
                setResults.postValue((List<SetResult>) o);
            }
        });
    }

    public MutableLiveData<List<SetResult>> getSetResults() {
        return setResults;
    }
}
