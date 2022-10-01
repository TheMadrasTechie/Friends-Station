package radson.findpeople.recyclercardview;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.net.URL;

import radson.findpeople.Config;
import radson.findpeople.chat.Chat_Name_Fragment;

/**
 * Created by Belal on 10/29/2015.
 */
public class GetBitmap extends AsyncTask<Void,Void,Void> {

    private Context context;
    private String[] urls;
    private ProgressDialog loading;
    private Chat_Name_Fragment mainActivity;

    public GetBitmap(Context context, Chat_Name_Fragment mainActivity, String[] urls){
        this.context = context;
        this.urls = urls;
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(context,"Downloading Image","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        loading.dismiss();
        mainActivity.showData();
    }

    @Override
    protected Void doInBackground(Void... params) {
        for(int i=0; i<urls.length; i++){
            Config.bitmaps[i] = getImage(urls[i]);
        }
        return null;
    }

    private Bitmap getImage(String bitmapUrl){
        URL url;
        Bitmap image = null;
        try {
            url = new URL(bitmapUrl);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }catch(Exception e){}
        return image;
    }
}
