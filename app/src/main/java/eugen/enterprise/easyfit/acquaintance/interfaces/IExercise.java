package eugen.enterprise.easyfit.acquaintance.interfaces;

public interface IExercise {
    int getId();

    String getMuscleGroupId();

    String getName();

    int getDurationSeconds();

    int getPauseDurationSeconds();

    String getExerciseType();
}
