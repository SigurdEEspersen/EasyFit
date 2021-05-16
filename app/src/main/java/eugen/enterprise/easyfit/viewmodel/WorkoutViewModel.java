package eugen.enterprise.easyfit.viewmodel;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.helpers.NotificationIndex;
import eugen.enterprise.easyfit.acquaintance.helpers.SetResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.model.repository.WorkoutService;
import eugen.enterprise.easyfit.view.activities.MainActivity;

public class WorkoutViewModel extends ViewModel {

    private MutableLiveData<Boolean> addedSetResult;
    private MutableLiveData<NotificationIndex> notificationListener;
    private MutableLiveData<Integer> pauseCountdown;
    private WorkoutService workoutService;

    public WorkoutViewModel() {
        addedSetResult = new MutableLiveData<>();
        notificationListener = new MutableLiveData<>();
        pauseCountdown = new MutableLiveData<>();
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
        startPauseThread(notificationIndex.getPauseDuration());
    }

    public MutableLiveData<NotificationIndex> getNotificationListener() {
        return notificationListener;
    }

    public MutableLiveData<Integer> getPauseCountdown() {
        return pauseCountdown;
    }

    private void startPauseThread(int duration) {
        Thread thread = new Thread(() -> {
            int countdown = duration;

            try {
                while (countdown > 0) {
                    pauseCountdown.postValue(countdown--);
                    Thread.sleep(1000);
                }

                pauseCountdown.postValue(0);

                Intent intent = new Intent(MainActivity.getAppContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.getAppContext(), 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.getAppContext(), "PauseChannel")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Pause over")
                        .setContentText("Let's go!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.getAppContext());
                notificationManager.notify(1, builder.build());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}