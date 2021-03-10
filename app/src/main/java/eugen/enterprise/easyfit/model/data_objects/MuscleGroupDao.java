package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MuscleGroupDao {

    @Insert
    void insertAll(MuscleGroup... muscleGroups);

    @Query("SELECT * FROM MuscleGroup")
    MuscleGroup[] selectAll();

    @Query("SELECT * FROM MuscleGroup WHERE name =:name")
    MuscleGroup getMuscleGroup(String name);
}
