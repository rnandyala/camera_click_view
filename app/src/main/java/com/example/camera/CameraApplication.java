package com.example.camera;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CameraApplication extends Application {

    //uncaught exception handler variable
    private Thread.UncaughtExceptionHandler defaultUEH;



    private Thread.UncaughtExceptionHandler mUnCaughtExceptionHandler = new
            Thread.UncaughtExceptionHandler(){


                @Override
                public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

                    //String s = String.format("I%nam%na%nboy");

                    // here I do logging of exception to a db
String s = String.format("e.getMessage is: "+ e.getMessage()+"%n"+"stack_trace is:"+ e.getStackTrace().toString() );



                String result =     "e.getMessage is: "+ e.getMessage()+"\n"+"stack_trace is:"+ e.getStackTrace().toString();

                createNotification(result);

//createNotification();
                   /* PendingIntent myActivity = PendingIntent.getActivity(getContext(),

                            192837, new Intent(getContext(), MyActivity.class),

                            PendingIntent.FLAG_ONE_SHOT);



                    AlarmManager alarmManager;

                    alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,

                            15000, myActivity );*/

                   System.exit(2);



                    // re-throw critical exception further to the os (important)

                  defaultUEH.uncaughtException(t, e);


                  /*  AlertDialog.Builder mBuilder = new AlertDialog.Builder(getApplicationContext());
                    mBuilder.setMessage(e.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    e.getMessage();
                                    dialog.dismiss();
                                    System.exit(2);



                                    // re-throw critical exception further to the os (important)

                                    defaultUEH.uncaughtException(t, e);

                                }
                            }
                    );
                    AlertDialog alert = mBuilder.create();

                    alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                    alert.show();*/
                }
            };


    public CameraApplication() {
      defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

      Thread.setDefaultUncaughtExceptionHandler(mUnCaughtExceptionHandler);

       // super();
    }

    private String CHANNEL_ID;

    private void createNotificationChannel() {
        CharSequence channelName = CHANNEL_ID;
        String channelDesc = "channelDesc";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDesc);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            NotificationChannel currChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (currChannel == null)
                notificationManager.createNotificationChannel(channel);
        }
    }


    public void createNotification(String message) {

        CHANNEL_ID = "notification example";
        if (message != null ) {
            createNotificationChannel();

            Intent intent = new Intent(this, FormAction.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setContentTitle("pmc_camera")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(uri);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            int notificationId = (int) (System.currentTimeMillis()/4);
            notificationManager.notify(notificationId, mBuilder.build());
        }
    }
}
