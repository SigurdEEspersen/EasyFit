package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ExerciseDao {

    @Insert
    void insertAll(Exercise... exercises);

    @Query("SELECT * FROM Exercise")
    Exercise[] selectAll();

    @Query("SELECT * FROM Exercise WHERE muscleGroup =:muscleGroup")
    Exercise[] getMuscleGroupExercises(String muscleGroup);
}
