package eugen.enterprise.easyfit.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    public PlanViewModel() {
        workoutGenerationService = new WorkoutGenerationService();
        workout = new MutableLiveData<>();
    }

    public void createWorkout(EWorkoutSplit workoutSplit, EWorkoutDuration workoutDuration, List<EMuscleGroup> muscleGroups, EWorkoutExtras workoutExtras, Context c) {
        workoutGenerationService.createWorkout(workoutSplit, workoutDuration, muscleGroups, workoutExtras, new Callback() {
            @Override
            public void onResponse(Object o) {
                workout.postValue((Workout) o);
            }
        }, c);
    }

    public MutableLiveData<Workout> getWorkout() {
        return workout;
    }
}