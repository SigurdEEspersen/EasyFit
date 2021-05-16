package eugen.enterprise.easyfit.acquaintance.helpers;

public class NotificationIndex {
    private final int muscleGroupIndex;
    private final int exerciseIndex;
    private final int setIndex;
    private final int pauseDuration;

    public NotificationIndex(int muscleGroupIndex, int exerciseIndex, int setIndex, int pauseDuration) {
        this.muscleGroupIndex = muscleGroupIndex;
        this.exerciseIndex = exerciseIndex;
        this.setIndex = setIndex;
        this.pauseDuration = pauseDuration;
    }

    public int getMuscleGroupIndex() {
        return muscleGroupIndex;
    }

    public int getExerciseIndex() {
        return exerciseIndex;
    }

    public int getSetIndex() {
        return setIndex;
    }

    public int getPauseDuration() {
        return pauseDuration;
    }
}
