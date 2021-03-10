package eugen.enterprise.easyfit.acquaintance.interfaces;

import java.util.List;

import eugen.enterprise.easyfit.model.data_objects.Exercise;

public interface IMuscleGroup {
    int getMuscleGroupId();

    String getName();

    List<String> getRecommendedMuscleGroups();

    List<Exercise> getExercises();
}
