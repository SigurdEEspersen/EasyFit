package eugen.enterprise.easyfit.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import eugen.enterprise.easyfit.acquaintance.helpers.Workout;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutDuration;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutExtras;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutSplit;
import eugen.enterprise.easyfit.model.repository.WorkoutGenerationService;

public class PlanViewModel extends ViewModel {

    private WorkoutGenerationService workoutGenerationService;
    private MutableLiveData<Workout> workout;
    private MutableLiveData<EWorkoutSplit> selectedSplit;
    private MutableLiveData<EWorkoutDuration> selectedDuration;
    private MutableLiveData<List<EMuscleGroup>> selectedMuscleGroups;
    private MutableLiveData<EWorkoutExtras> selectedExtras;
    private MutableLiveData<Boolean> preWorkout;
    private MutableLiveData<Integer> extrasDuration;

    public PlanViewModel() {
        workoutGenerationService = new WorkoutGenerationService();
        workout = new MutableLiveData<>();
        selectedSplit = new MutableLiveData<>();
        selectedDuration = new MutableLiveData<>();
        selectedMuscleGroups = new MutableLiveData<>();
        selectedExtras = new MutableLiveData<>();
        preWorkout = new MutableLiveData<>(false);
        extrasDuration = new MutableLiveData<>(10);
    }

    public void createWorkout(Context c) {
        workoutGenerationService.createWorkout(selectedSplit.getValue(), selectedDuration.getValue(),
                selectedMuscleGroups.getValue(), selectedExtras.getValue(), preWorkout.getValue(), extrasDuration.getValue(), new Callback() {
            @Override
            public void onResponse(Object o) {
                workout.postValue((Workout) o);
            }
        }, c);
    }

    public MutableLiveData<Workout> getWorkout() {
        return workout;
    }

    public MutableLiveData<EWorkoutSplit> getSelectedSplit() {
        return selectedSplit;
    }

    public void setSelectedSplit(EWorkoutSplit selectedSplit) {
        this.selectedSplit.postValue(selectedSplit);
    }

    public MutableLiveData<EWorkoutDuration> getSelectedDuration() {
        return selectedDuration;
    }

    public void setSelectedDuration(EWorkoutDuration selectedDuration) {
        this.selectedDuration.postValue(selectedDuration);
    }

    public MutableLiveData<List<EMuscleGroup>> getSelectedMuscleGroups() {
        return selectedMuscleGroups;
    }

    public void setSelectedMuscleGroups(List<EMuscleGroup> selectedMuscleGroups) {
        this.selectedMuscleGroups.postValue(selectedMuscleGroups);
    }

    public MutableLiveData<EWorkoutExtras> getSelectedExtras() {
        return selectedExtras;
    }

    public void setSelectedExtras(EWorkoutExtras selectedExtras) {
        this.selectedExtras.postValue(selectedExtras);
    }

    public void setSelectedExtras(MutableLiveData<EWorkoutExtras> selectedExtras) {
        this.selectedExtras = selectedExtras;
    }

    public MutableLiveData<Boolean> getPreWorkout() {
        return preWorkout;
    }

    public void setPreWorkout(Boolean preWorkout) {
        this.preWorkout.postValue(preWorkout);
    }

    public MutableLiveData<Integer> getExtrasDuration() {
        return extrasDuration;
    }

    public void setExtrasDuration(Integer extrasDuration) {
        this.extrasDuration.postValue(extrasDuration);
    }
}