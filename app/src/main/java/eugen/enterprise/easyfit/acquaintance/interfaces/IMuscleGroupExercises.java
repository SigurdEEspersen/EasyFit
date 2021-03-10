package eugen.enterprise.easyfit.acquaintance.interfaces;

import java.util.List;

import eugen.enterprise.easyfit.model.data_objects.Exercise;
import eugen.enterprise.easyfit.model.data_objects.MuscleGroup;

public interface IMuscleGroupExercises {
    MuscleGroup getMuscleGroup();

    List<Exercise> getExercises();
}
