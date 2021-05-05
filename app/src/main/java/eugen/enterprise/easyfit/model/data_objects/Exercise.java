package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import eugen.enterprise.easyfit.acquaintance.enums.EExerciseType;
import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.interfaces.IExercise;

@Entity
public class Exercise implements IExercise {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected int id;

    @ColumnInfo(name = "muscleGroup")
    protected String muscleGroup;

    @ColumnInfo(name = "name")
    protected String name;

    @ColumnInfo(name = "duration")
    protected int durationSeconds;

    @ColumnInfo(name = "pauseDuration")
    protected int pauseDurationSeconds;

    @ColumnInfo(name = "exercise_type")
    protected String exerciseType;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getMuscleGroup() {
        return muscleGroup;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDurationSeconds() {
        return durationSeconds;
    }

    @Override
    public int getPauseDurationSeconds() {
        return pauseDurationSeconds;
    }

    @Override
    public String getExerciseType() {
        return exerciseType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setPauseDurationSeconds(int pauseDurationSeconds) {
        this.pauseDurationSeconds = pauseDurationSeconds;
    }

    public void setExerciseType(EExerciseType exerciseType) {
        this.exerciseType = exerciseType.name();
    }

    public void setMuscleGroup(EMuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup.name();
    }
}
