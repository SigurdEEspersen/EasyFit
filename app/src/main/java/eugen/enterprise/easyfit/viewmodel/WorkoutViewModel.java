package eugen.enterprise.easyfit.viewmodel;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.helpers.NotificationIndex;
import eugen.enterprise.easyfit.acquaintance.helpers.SetResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.model.repository.WorkoutService;
import eugen.enterprise.easyfit.view.activities.MainActivity;
import eugen.enterprise.easyfit.viewmodel.services.CountdownService;

public class WorkoutViewModel extends ViewModel {

    private MutableLiveData<Boolean> addedSetResult;
    private MutableLiveData<NotificationIndex> notificationListener;
    private MutableLiveData<Integer> pauseCountdown;
    private MutableLiveData<Integer> openedMuscleGroup;
    private MutableLiveData<List<NotificationIndex>> usedPauses;
    private WorkoutService workoutService;

    public WorkoutViewModel() {
        addedSetResult = new MutableLiveData<>();
        notificationListener = new MutableLiveData<>();
        pauseCountdown = new MutableLiveData<>();
        openedMuscleGroup = new MutableLiveData<>();
        usedPauses = new MutableLiveData<>();
        usedPauses.postValue(new ArrayList<>());
        workoutService = new WorkoutService();
    }

    public LiveData<Boolean> getAddedSetResult() {
        return addedSetResult;
    }

    public void setAddedSetResult(Boolean bool) {
        addedSetResult.postValue(bool);
    }

    public void addSetResult(SetResult result, Context c) {
        workoutService.saveSetResult(result, new Callback() {
            @Override
            public void onResponse(Object o) {
                addedSetResult.postValue((Boolean) o);
            }
        }, c);
    }

    public void addNotificationListener(NotificationIndex notificationIndex) {
        notificationListener.postValue(notificationIndex);
        Intent intent = new Intent(MainActivity.getAppContext(), CountdownService.class);
        intent.putExtra("duration", notificationIndex.getPauseDuration());
        intent.putExtra("muscleGroupIndex", notificationIndex.getMuscleGroupIndex());
        intent.putExtra("exerciseIndex", notificationIndex.getExerciseIndex());
        intent.putExtra("setIndex", notificationIndex.getSetIndex());
        MainActivity.getAppContext().startService(intent);
    }

    public MutableLiveData<NotificationIndex> getNotificationListener() {
        return notificationListener;
    }

    public MutableLiveData<Integer> getPauseCountdown() {
        return pauseCountdown;
    }

    public void setOpenedMuscleGroup(Integer index) {
        openedMuscleGroup.postValue(index);
    }

    public MutableLiveData<Integer> getOpenedMuscleGroup() {
        return openedMuscleGroup;
    }

    public void addUsedPause(NotificationIndex usedPause) {
        usedPauses.getValue().add(usedPause);
    }

    public MutableLiveData<List<NotificationIndex>> getUsedPauses() {
        return usedPauses;
    }
}