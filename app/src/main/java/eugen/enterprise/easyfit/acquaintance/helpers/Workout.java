package eugen.enterprise.easyfit.acquaintance.helpers;

import java.io.Serializable;
import java.util.List;

import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutLoad;
import eugen.enterprise.easyfit.acquaintance.interfaces.IMuscleGroup;

public class Workout implements Serializable {
    List<IMuscleGroup> muscleGroups;
    EWorkoutLoad workoutLoad;
    int setsPrExercise;

    public List<IMuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(List<IMuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public EWorkoutLoad getWorkoutLoad() {
        return workoutLoad;
    }

    public void setWorkoutLoad(EWorkoutLoad workoutLoad) {
        this.workoutLoad = workoutLoad;
    }

    public int getSetsPrExercise() {
        return setsPrExercise;
    }

    public void setSetsPrExercise(int setsPrExercise) {
        this.setsPrExercise = setsPrExercise;
    }
}
