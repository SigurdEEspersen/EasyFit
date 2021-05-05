package eugen.enterprise.easyfit.acquaintance.interfaces;

public interface IExercise {
    int getId();

    String getMuscleGroup();

    String getName();

    int getDurationSeconds();

    int getPauseDurationSeconds();

    String getExerciseType();
}
