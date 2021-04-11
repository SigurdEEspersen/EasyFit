package eugen.enterprise.easyfit.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import eugen.enterprise.easyfit.model.data_objects.Exercise;
import eugen.enterprise.easyfit.model.data_objects.ExerciseDao;
import eugen.enterprise.easyfit.model.data_objects.Macros;
import eugen.enterprise.easyfit.model.data_objects.MacrosDao;
import eugen.enterprise.easyfit.model.data_objects.MuscleGroup;
import eugen.enterprise.easyfit.model.data_objects.MuscleGroupDao;
import eugen.enterprise.easyfit.model.data_objects.SetResultDao;
import eugen.enterprise.easyfit.model.data_objects.SetResultData;

@Database(entities = {MuscleGroup.class, Exercise.class, Macros.class, SetResultData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MuscleGroupDao muscleGroupDao();
    public abstract ExerciseDao exerciseDao();
    public abstract MacrosDao macrosDao();
    public abstract SetResultDao setResultDao();
}
