package radson.findpeople.chat;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Prosen on 4/6/2016.
 */
public class ChatLife extends Application{

    private static final String TAG = "ChatLife";

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.setAndroidContext(getApplicationContext());
    }
}
