package eugen.enterprise.easyfit.model.repository;

import android.content.Context;

import java.sql.Date;

import eugen.enterprise.easyfit.acquaintance.helpers.SetResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.model.DatabaseAccess;
import eugen.enterprise.easyfit.model.data_objects.SetResultDao;
import eugen.enterprise.easyfit.model.data_objects.SetResultData;

public class WorkoutService {
    public void saveSetResult(SetResult result, Callback callback, Context c) {
        Runnable runnable = () -> {
            try {
                SetResultData data = new SetResultData();
                data.setExerciseId(result.getExerciseId());
                data.setReps(result.getReps());
                data.setWeight(result.getWeight());
                data.setDate(new Date(System.currentTimeMillis()));

                SetResultDao setResultDao = DatabaseAccess.getInstance().getDatabase(c).setResultDao();
                setResultDao.saveSetResult(data);

                callback.onResponse(true);
            } catch (Exception e) {
                callback.onResponse(false);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
