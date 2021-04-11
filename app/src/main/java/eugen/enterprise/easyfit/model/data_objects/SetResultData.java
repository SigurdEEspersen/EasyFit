package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.sql.Date;

import eugen.enterprise.easyfit.model.Converters;

@Entity
public class SetResultData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected int setResultId;

    @ColumnInfo(name = "exercise_id")
    protected int exerciseId;

    @ColumnInfo(name = "reps")
    protected int reps;

    @ColumnInfo(name = "weight")
    protected double weight;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "date")
    protected Date date;

    public int getSetResultId() {
        return setResultId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
