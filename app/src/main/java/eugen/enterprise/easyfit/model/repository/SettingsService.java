package eugen.enterprise.easyfit.model.repository;

import android.content.Context;

import java.util.ArrayList;

import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.helpers.SetResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.model.DatabaseAccess;
import eugen.enterprise.easyfit.model.data_objects.ExerciseDao;
import eugen.enterprise.easyfit.model.data_objects.PrefferedExercise;
import eugen.enterprise.easyfit.model.data_objects.PrefferedExerciseDao;
import eugen.enterprise.easyfit.model.data_objects.SetResultDao;
import eugen.enterprise.easyfit.model.data_objects.SetResultData;

public class SettingsService {
    public void loadExercises(Callback callback, Context c) {
        Runnable runnable = () -> {
            ExerciseDao exerciseDao = DatabaseAccess.getInstance().getDatabase(c).exerciseDao();
            callback.onResponse(exerciseDao.selectAll());
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void loadPrefferedExercises(Context c, Callback callback) {
        Runnable runnable = () -> {
            PrefferedExerciseDao prefferedExerciseDao = DatabaseAccess.getInstance().getDatabase(c).prefferedExerciseDao();
            callback.onResponse(prefferedExerciseDao.selectAll());
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void loadSetResults(Context c, Callback callback) {
        Runnable runnable = () -> {
            SetResultDao setResultDao = DatabaseAccess.getInstance().getDatabase(c).setResultDao();
            SetResultData[] arr = setResultDao.loadSavedSetResults();
            ArrayList<SetResult> resultList = new ArrayList<>();
            for (SetResultData setResultData : arr) {
                SetResult setResult = new SetResult();
                setResult.setWeight(setResultData.getWeight());
                setResult.setReps(setResultData.getReps());
                setResult.setExerciseId(setResultData.getExerciseId());
                setResult.setDate(setResultData.getDate());
                resultList.add(setResult);
            }

            callback.onResponse(resultList);
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void setPrefferedExercise(String exerciseName, EMuscleGroup muscleGroup, Context c, Callback callback) {
        Runnable runnable = () -> {
            try {
                PrefferedExerciseDao prefferedExerciseDao = DatabaseAccess.getInstance().getDatabase(c).prefferedExerciseDao();
                if (exerciseName.equals("None")) {
                    prefferedExerciseDao.deleteMuscleGroupPreferredExercise(muscleGroup.name());
                    callback.onResponse(true);
                    return;
                }

                PrefferedExercise[] savedPreferredExercise = prefferedExerciseDao.getMuscleGroupPreferredExercises(muscleGroup.name());
                if (savedPreferredExercise != null && savedPreferredExercise.length > 0 && savedPreferredExercise[0].getName().equals(exerciseName)) {
                    return;
                }

                prefferedExerciseDao.deleteMuscleGroupPreferredExercise(muscleGroup.name());

                PrefferedExercise prefferedExercise = new PrefferedExercise();
                prefferedExercise.setName(exerciseName);
                prefferedExercise.setMuscleGroup(muscleGroup);
                prefferedExerciseDao.insert(prefferedExercise);

                callback.onResponse(true);
            } catch (Exception e) {
                callback.onResponse(false);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
