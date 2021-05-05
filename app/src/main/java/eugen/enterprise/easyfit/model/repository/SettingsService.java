package eugen.enterprise.easyfit.model.repository;

import android.content.Context;

import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;

public class SettingsService {
    public void loadExercises(Callback callback, Context c) {
        Runnable runnable = () -> {
            try {
                callback.onResponse(true);
            } catch (Exception e) {
                callback.onResponse(false);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
