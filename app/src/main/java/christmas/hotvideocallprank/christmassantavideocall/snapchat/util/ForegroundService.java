package christmas.hotvideocallprank.christmassantavideocall.snapchat.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hotvideocallprank.christmassantavideocall.R;
import christmas.hotvideocallprank.christmassantavideocall.snapchat.VideoCallActivity;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        final String jsonData = intent.getExtras().getString("item");
        int time = intent.getExtras().getInt("time");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, VideoCallActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Toast.makeText(getApplicationContext(), "Call has been scheduled!", Toast.LENGTH_LONG).show();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Call has been scheduled!")
                .setContentText("You will receive a video call on scheduled time!")
                .setSmallIcon(R.drawable.btn_call_receive)
                .setContentIntent(null)
                .build();
        startForeground(1, notification);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(getApplicationContext(), VideoCallActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtra("item", jsonData);
                getApplicationContext().startActivity(myIntent);
                stopSelf();
            }
        }, time * 1000);

        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
