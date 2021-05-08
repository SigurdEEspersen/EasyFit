package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.interfaces.IPreferredExercise;

@Entity
public class PrefferedExercise implements IPreferredExercise {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected int id;

    @ColumnInfo(name = "muscleGroup")
    protected String muscleGroup;

    @ColumnInfo(name = "name")
    protected String name;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setMuscleGroup(EMuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup.name();
    }
}
