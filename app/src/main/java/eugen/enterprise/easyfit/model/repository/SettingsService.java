package eugen.enterprise.easyfit.model.repository;

import android.content.Context;

import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.model.DatabaseAccess;
import eugen.enterprise.easyfit.model.data_objects.ExerciseDao;

public class SettingsService {
    public void loadExercises(Callback callback, Context c) {
        Runnable runnable = () -> {
            ExerciseDao exerciseDao = DatabaseAccess.getInstance().getDatabase(c).exerciseDao();
            callback.onResponse(exerciseDao.selectAll());
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
