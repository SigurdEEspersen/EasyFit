package eugen.enterprise.easyfit.model.repository;

import android.content.Context;

import eugen.enterprise.easyfit.acquaintance.enums.ECalorieTarget;
import eugen.enterprise.easyfit.acquaintance.helpers.MacroResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.model.DatabaseAccess;
import eugen.enterprise.easyfit.model.data_objects.Macros;
import eugen.enterprise.easyfit.model.data_objects.MacrosDao;

public class MacroService {
    public void saveMacros(MacroResult result, Callback callback, Context c) {
        Runnable runnable = () -> {
            Macros data = new Macros();
            data.setMale(result.isMale());
            data.setCalorieTarget(result.getCalorieTarget().name());
            data.setWeight(result.getWeight());
            data.setHeight(result.getHeight());
            data.setAge(result.getAge());
            data.setActivity(result.getActivity());
            data.setCalories(result.getCalories());
            data.setProteins(result.getProteins());
            data.setCarbs(result.getCarbs());
            data.setFat(result.getFat());

            MacrosDao macrosDao = DatabaseAccess.getInstance().getDatabase(c).macrosDao();
            macrosDao.deleteSavedMacros();
            macrosDao.saveMacros(data);

            callback.onResponse("Macros saved");
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void loadMacros(Callback callback, Context c) {
        Runnable runnable = () -> {
            MacrosDao macrosDao = DatabaseAccess.getInstance().getDatabase(c).macrosDao();
            Macros[] savedMacros = macrosDao.loadSavedMacros();

            if (savedMacros.length == 0) {
                callback.onResponse(null);
                return;
            }

            Macros macros = savedMacros[0];

            MacroResult loadedMacros = new MacroResult();
            loadedMacros.setMale(macros.isMale());

            if (macros.getCalorieTarget().equals("MaxCalorieDeficit")) {
                loadedMacros.setCalorieTarget(ECalorieTarget.MaxCalorieDeficit);
            } else if (macros.getCalorieTarget().equals("CalorieDeficit")) {
                loadedMacros.setCalorieTarget(ECalorieTarget.CalorieDeficit);
            } else if (macros.getCalorieTarget().equals("Maintain")) {
                loadedMacros.setCalorieTarget(ECalorieTarget.Maintain);
            } else if (macros.getCalorieTarget().equals("CalorieSurplus")) {
                loadedMacros.setCalorieTarget(ECalorieTarget.CalorieSurplus);
            } else if (macros.getCalorieTarget().equals("MaxCalorieSurplus")) {
                loadedMacros.setCalorieTarget(ECalorieTarget.MaxCalorieSurplus);
            }

            loadedMacros.setWeight(macros.getWeight());
            loadedMacros.setHeight(macros.getHeight());
            loadedMacros.setAge(macros.getAge());
            loadedMacros.setActivity(macros.getActivity());
            loadedMacros.setCalories(macros.getCalories());
            loadedMacros.setProteins(macros.getProteins());
            loadedMacros.setCarbs(macros.getCarbs());
            loadedMacros.setFat(macros.getFat());

            callback.onResponse(loadedMacros);
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
