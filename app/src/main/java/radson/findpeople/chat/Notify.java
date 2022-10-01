package radson.findpeople.chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import radson.findpeople.Config;
import radson.findpeople.MainActivity;
import radson.findpeople.R;

/**
 * Created by Belal on 3/18/2016.
 */
//Class extending service as it is a service that will run in background
public class Notify extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Opening sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        final SharedPreferences.Editor edr = sharedPreferences.edit();

        SharedPreferences shared = getSharedPreferences("shp", MODE_PRIVATE);
        final SharedPreferences.Editor editr = shared.edit();
        Firebase fire = new Firebase("https://fiery-inferno-2061.firebaseio.com/" + shared.getString("Receiver",null) );

        //Adding a valueevent listener to firebase
        //this will help us to  track the value changes on firebase
        fire.addValueEventListener(new ValueEventListener() {

            //This method is called whenever we change the value in firebase
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //Getting the value from firebase
                //We stored none as a initial value
                int msssgg = Integer.parseInt(snapshot.child("msg").getValue().toString());editr.putInt("notvalue",msssgg);editr.commit();
                // String msg= ""+msssgg;
                //String msg = snapshot.child("msg").getValue().toString();
               /* int msss = Integer.parseInt(snapshot.child("ms").getValue().toString());
                edr.putInt("msgvalue",msss ); edr.commit();
                String ms = "" + msss;*/
                //So if the value is none we will not create any notification
                // if (msg.equals("none")) return;
                //if (ms.equals("none"))                    return;
                //If the value is anything other than none that means a notification has arrived
                //calling the method to show notification
                //String msg is containing the msg that has to be shown with the notification
                //showNotification(msg);
                //showms(ms);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });

        return START_STICKY;
    }



    public void showms(String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/publish/?dev_acc=08706893336144449211#AppListPlace"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("AAppppp");
        builder.setContentText(msg);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(2, builder.build());
    }
    private void showNotification(String msg){
        //Creating a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent , 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("AAppppp");
        builder.setContentText(msg);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notifysnd);
        builder.setSound(sound);
        //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)        builder.setSound(alarmSound);
        Notification notification = builder.build();
        //notification.sound = Uri.parse("android.resource://"                + context.getPackageName() + "/" + R.raw.siren);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}

