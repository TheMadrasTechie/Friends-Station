package radson.findpeople.chat;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import radson.findpeople.Config;
import radson.findpeople.R;
import radson.findpeople.recyclercardview.GetBitmap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_Name_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    static View.OnClickListener myOnClickListener;
    private Config config;

    public Chat_Name_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycle, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_View);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        getData();
        myOnClickListener = new MyOnClickListener(getContext());
        return rootView;
    }


    private void getData(){
        class GetData extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getContext(), "Fetching Data", "Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                parseJSON(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(Config.GET_URL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
        }
        GetData gd = new GetData();
        gd.execute();
    }

    public void showData(){
        adapter = new CardAdapter(Config.names,Config.urls,Config.REG_ID, Config.bitmaps);
        recyclerView.setAdapter(adapter);
    }

    private void parseJSON(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            config = new Config(array.length());

            for(int i=0; i<array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                Config.names[i] = getName(j);
                Config.urls[i] = getURL(j);
                Config.REG_ID[i] = getid(j);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GetBitmap gb = new GetBitmap(getContext(),this, Config.urls);
        gb.execute();
    }

    private String getName(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_IMAGE_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getURL(JSONObject j){
        String url = null;
        try {
            url = j.getString(Config.TAG_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }
    private String getid(JSONObject j){
        String url = null;
        try {
            url = j.getString(Config.TAG_regId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }
    private static class MyOnClickListener implements View.OnClickListener{
        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;

        }

        @Override
        public void onClick(View view) {String poo =((TextView) view.findViewById(R.id.textViewName)).getText().toString();
            //int position = (int) view.getTag();
            Toast.makeText(view.getContext(), poo, Toast.LENGTH_SHORT).show();
//editor.putString("sender",((TextView) view.findViewById(R.id.textView_reg_id)).getText().toString());editor.commit();

            context.startActivity(new Intent(context, Chat_Activity.class).putExtra("sender",((TextView) view.findViewById(R.id.textView_reg_id)).getText().toString()).putExtra("TitleNAme",((TextView) view.findViewById(R.id.textView_reg_id)).getText().toString()));


        }

    }

}
