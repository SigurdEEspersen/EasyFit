package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutExtras;
import eugen.enterprise.easyfit.acquaintance.interfaces.IMuscleGroup;
import eugen.enterprise.easyfit.model.Converters;

@Entity
public class MuscleGroup implements IMuscleGroup {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected int muscleGroupId;

    @ColumnInfo(name = "name")
    protected String name;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "recommended_muscle_groups")
    protected List<String> recommendedMuscleGroups;

    @Ignore
    protected List<Exercise> exercises;

    @Ignore
    protected int workoutExtraDuration;

    @Ignore
    protected boolean isWorkoutExtra;

    @Override
    public int getMuscleGroupId() {
        return muscleGroupId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getRecommendedMuscleGroups() {
        return recommendedMuscleGroups;
    }

    @Override
    public boolean isWorkoutExtra() {
        return isWorkoutExtra;
    }

    @Override
    public int getWorkoutExtraDuration() {
        return workoutExtraDuration;
    }

    public void setName(EMuscleGroup name) {
        this.name = name.name();
    }

    public void setRecommendedMuscleGroups(List<EMuscleGroup> recommendedMuscleGroups) {
        List<String> stringedRecommendations = new ArrayList<>();
        for (EMuscleGroup muscleGroup : recommendedMuscleGroups) {
            stringedRecommendations.add(muscleGroup.name());
        }
        this.recommendedMuscleGroups = stringedRecommendations;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercisesList) {
        if (exercises == null) {
            exercises = new ArrayList<>();
        }
        this.exercises = exercisesList;
    }

    public void workoutExtra(EWorkoutExtras name, int duration) {
     this.name = name.name();
     this.workoutExtraDuration = duration;
     this.isWorkoutExtra = true;
    }
}
