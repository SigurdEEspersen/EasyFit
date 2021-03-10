package eugen.enterprise.easyfit.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import java.util.List;

import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutDuration;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutExtras;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutSplit;
import eugen.enterprise.easyfit.model.repository.WorkoutGenerationService;

public class PlanViewModel extends ViewModel {

    private WorkoutGenerationService workoutGenerationService;

    public PlanViewModel() {
        workoutGenerationService = new WorkoutGenerationService();
    }

    public void createWorkout(EWorkoutSplit workoutSplit, EWorkoutDuration workoutDuration, List<EMuscleGroup> muscleGroups, EWorkoutExtras workoutExtras, Context c) {
        workoutGenerationService.createWorkout(workoutSplit, workoutDuration, muscleGroups, workoutExtras, new Callback() {
            @Override
            public void onResponse(Object o) {
                //TODO
            }
        }, c);
    }
}