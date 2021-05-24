package eugen.enterprise.easyfit.viewmodel.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.view.activities.MainActivity;
import eugen.enterprise.easyfit.viewmodel.WorkoutViewModel;

public class CountdownService extends Service {

    private Thread thread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new Thread(() -> {
            int countdown = (int) intent.getExtras().get("duration");
            WorkoutViewModel viewModel = new ViewModelProvider(MainActivity.getMainActivity()).get(WorkoutViewModel.class);

            try {
                while (countdown > 0) {
                    viewModel.getPauseCountdown().postValue(countdown--);
                    Thread.sleep(1000);
                }

                viewModel.getPauseCountdown().postValue(0);

                Intent notificationIntent = new Intent(MainActivity.getAppContext(), MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.getAppContext(), 0, notificationIntent, 0);
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
            } catch (InterruptedException e) {
                e.printStackTrace();
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
