package eugen.enterprise.easyfit.model.data_objects;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PrefferedExerciseDao {

    @Insert
    void insert(PrefferedExercise prefferedExercise);

    @Query("DELETE FROM PrefferedExercise WHERE muscleGroup =:muscleGroup")
    void deleteMuscleGroupPreferredExercise(String muscleGroup);

    @Query("SELECT * FROM PrefferedExercise")
    PrefferedExercise[] selectAll();

    @Query("SELECT * FROM PrefferedExercise WHERE muscleGroup =:muscleGroup")
    PrefferedExercise[] getMuscleGroupPreferredExercises(String muscleGroup);
}
