package radson.findpeople;

import android.graphics.Bitmap;

/**
 * Created by Belal on 10/29/2015.
 */
public class Config {

    public static String[] names;
    public static String[] urls;
    public static String[] REG_ID;
    public static Bitmap[] bitmaps;

    public static final String GET_URL = "http://watchmendomain.16mb.com/WatchMen/watchmen_get_prof_data.php";
    public static final String TAG_IMAGE_URL = "url";
    public static final String TAG_regId = "reg_id";
    public static final String TAG_IMAGE_NAME = "name";
    public static final String TAG_JSON_ARRAY="result";


    //Firebase app url
    public static final String FIREBASE_APP = "https://fiery-inferno-2061.firebaseio.com/";

    //Constant to store shared preferences
    public static final String SHARED_PREF = "mynotificationapp";

    //To store boolean in shared preferences for if the device is registered to not
    public static final String REGISTERED = "registered";

    //To store the firebase id in shared preferences
    public static final String UNIQUE_ID = "uniqueid";

    //register.php address in your server
    public static final String REGISTER_URL = "http://www.radson.16mb.com/insertintodbfcm.php";
    public Config(int i){
        names = new String[i];
        urls = new String[i];
        REG_ID = new String[i];
        bitmaps = new Bitmap[i];
    }
}
