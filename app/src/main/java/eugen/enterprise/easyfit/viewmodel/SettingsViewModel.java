package eugen.enterprise.easyfit.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.acquaintance.interfaces.IExercise;
import eugen.enterprise.easyfit.model.repository.SettingsService;

public class SettingsViewModel extends ViewModel {
    private SettingsService settingsService;
    private MutableLiveData<List<IExercise>> exercises;

    public SettingsViewModel() {
        settingsService = new SettingsService();
        exercises = new MutableLiveData<>();
    }

    public void LoadExercises(Context c) {
        settingsService.loadExercises(new Callback() {
            @Override
            public void onResponse(Object o) {
                //TODO
            }
        }, c);
    }

    public MutableLiveData<List<IExercise>> getExercises() {
        return exercises;
    }
}
