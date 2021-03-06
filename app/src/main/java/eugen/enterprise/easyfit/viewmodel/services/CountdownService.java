package eugen.enterprise.easyfit.viewmodel.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.helpers.NotificationIndex;
import eugen.enterprise.easyfit.view.activities.MainActivity;
import eugen.enterprise.easyfit.viewmodel.WorkoutViewModel;

public class CountdownService extends Service {

    private Thread thread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new Thread(() -> {
            if (intent == null) {
                return;
            }

            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyApp::MyWakelockTag");
            wakeLock.acquire(10*60*1000L /*10 minutes*/);

            int countdown = (int) intent.getExtras().get("duration");
            int muscleGroupIndex = (int) intent.getExtras().get("muscleGroupIndex");
            int exerciseIndex = (int) intent.getExtras().get("exerciseIndex");
            int setIndex = (int) intent.getExtras().get("setIndex");
            WorkoutViewModel viewModel = new ViewModelProvider(MainActivity.getMainActivity()).get(WorkoutViewModel.class);

            try {
                while (countdown > 0) {
                    viewModel.getPauseCountdown().postValue(countdown--);
                    Thread.sleep(1000);
                }

                viewModel.getPauseCountdown().postValue(0);
                viewModel.addUsedPause(new NotificationIndex(muscleGroupIndex, exerciseIndex, setIndex, 0));

                Intent notificationIntent = new Intent(this, MainActivity.class);
                //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.getAppContext(), "PauseChannel")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Pause is over")
                        .setContentText("Let's go!")
                        .setSound(soundUri)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.getAppContext());
                notificationManager.notify(1, builder.build());
                wakeLock.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
                wakeLock.release();
            }
        });

        thread.start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
