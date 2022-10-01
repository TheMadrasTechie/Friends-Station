package radson.findpeople.register;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import radson.findpeople.Config;
import radson.findpeople.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    public static final String UPLOAD_URL = "http://watchmendomain.16mb.com/WatchMen/watchmen_upload_profiledata.php";
    public static final String KEY_IMAGE = "image", KEY_NAME = "name",KEY_PSWRD="pswrd",KEY_DOB="dob",KEY_SEX="sex",KEY_REG="reg_id";
    public static String sex_name;
    private Button reg_submit_btn, reg_set_date_btn;

    private RadioButton radioSexButton_male,radioSexButton_female;
    private EditText name_reg,et_pswd_reg;
    private int PICK_IMAGE_REQUEST = 1;
    private ImageView profile_pic;
    private Bitmap bitmap;



    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);


        reg_set_date_btn = (Button) rootView.findViewById(R.id.reg_setdate);
        radioSexButton_male = (RadioButton)rootView.findViewById(R.id.reg_gender_male);
        radioSexButton_female = (RadioButton)rootView.findViewById(R.id.reg_gender_female);radioSexButton_female.setChecked(true);


        reg_submit_btn = (Button)rootView.findViewById(R.id.reg_submit);
        profile_pic = (ImageView)rootView.findViewById(R.id.reg_image_view);
        name_reg = (EditText)rootView.findViewById(R.id.reg_first_name);
        et_pswd_reg = (EditText)rootView.findViewById(R.id.reg_password);


        profile_pic.setImageResource(R.drawable.ic_back);


       reg_set_date_btn.setOnClickListener(new View.OnClickListener() {    @Override    public void onClick(View arg0) {        DialogFragment newFragment = new SelectDateFragmet(); newFragment.show(getFragmentManager(), "DatePicker");    }});
        reg_submit_btn.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                check_data();           }});
       profile_pic.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showFileChooser();
       }
       });
        return rootView;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                profile_pic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void uploadImage(final String unq_id){
        if(radioSexButton_male.isChecked()){sex_name = "1";}
        if(radioSexButton_female.isChecked()){sex_name = "0";}
        final String name_stg = name_reg.getText().toString().trim();
        final String pswrd_stg = et_pswd_reg.getText().toString().trim();
        final String image = getStringImage(bitmap);
        final String dob_stg = reg_set_date_btn.getText().toString();
        final  SharedPreferences pref = getActivity().getSharedPreferences("shp", 0);
        final SharedPreferences.Editor editor = pref.edit();

        class UploadImage extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(),"Please wait...","uploading",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<String,String>();
                param.put(KEY_NAME,name_stg);
                param.put(KEY_PSWRD,pswrd_stg);

                param.put(KEY_DOB,dob_stg);
                param.put(KEY_SEX,sex_name);
                param.put(KEY_REG,unq_id);
                param.put(KEY_IMAGE,image);
                String result = rh.sendPostRequest(UPLOAD_URL, param);
                return result;
            }

        }
        UploadImage u = new UploadImage();
        u.execute();
    }
public void check_data(){

    final String name_stg = name_reg.getText().toString();
    final String pswrd_stg = et_pswd_reg.getText().toString().trim();

    final String dob_stg = reg_set_date_btn.getText().toString();
    if(name_stg.equals("")){Toast.makeText(getContext(),R.string.reg_name_empty,Toast.LENGTH_LONG).show();}else
    if((pswrd_stg.length()<8)||(16<pswrd_stg.length())){Toast.makeText(getContext(),R.string.reg_pswrd_empty,Toast.LENGTH_LONG).show();}else
    if(dob_stg.toUpperCase().equals("SET DATE")){Toast.makeText(getContext(),R.string.reg_dob_empty,Toast.LENGTH_LONG).show();}else{



    registerDevice();}
}
    private void registerDevice() {
        Firebase.setAndroidContext(getContext());
        Firebase firebase = new Firebase(Config.FIREBASE_APP);

        //Pushing a new element to firebase it will automatically create a unique id
        Firebase newFirebase = firebase.push();

        //Creating a map to store name value pair
        Map<String, Integer> val = new HashMap<>();

        //pushing msg = none in the map
        val.put("msg", 0);
        //val.put("ms", 1);
        //saving the map to firebase
        newFirebase.setValue(val);

        //Getting the unique id generated at firebase
        String uniqueId = newFirebase.getKey();

        //Finally we need to implement a method to store this unique id to our server
        sendIdToServer(uniqueId);
    }

    private void sendIdToServer(final String uniqueId) {
        //Creating a progress dialog to show while it is storing the data on server
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Registering device...");
        progressDialog.show();

        //getting the email entered


        //Creating a string request
        StringRequest req = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF,0);
                        SharedPreferences sharedPes = getActivity().getSharedPreferences("shp",0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        SharedPreferences.Editor ed = sharedPes.edit();
                        editor.putString(Config.UNIQUE_ID, uniqueId);
                        editor.putBoolean(Config.REGISTERED, true);
                        editor.apply();
                        ed.putString("sender", sharedPreferences.getString(Config.UNIQUE_ID, ""));ed.commit();
                        getActivity().startService(new Intent(getActivity(), NotificationListener.class));
                        //if the server returned the string success
                        /*if (response.trim().equalsIgnoreCase("success")) {
                            //Displaying a success toast
                            Toast.makeText(getContext(), "Registered successfully", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF,0);
                            SharedPreferences sharedPes = getActivity().getSharedPreferences("shp",0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            SharedPreferences.Editor ed = sharedPes.edit();
                            //Storing the unique id
                            editor.putString(Config.UNIQUE_ID, uniqueId);

                            //Saving the boolean as true i.e. the device is registered
                            editor.putBoolean(Config.REGISTERED, true);

                            //Applying the changes on sharedpreferences
                            editor.apply();
                            ed.putString("sender", sharedPreferences.getString(Config.UNIQUE_ID, ""));ed.commit();
                            uploadImage(sharedPreferences.getString(Config.UNIQUE_ID, ""));

                            getActivity().startService(new Intent(getActivity(), NotificationListener.class));
                        } else {
                            Toast.makeText(getContext(), "Choose a different email", Toast.LENGTH_SHORT).show();
                        }*/
                        uploadImage(uniqueId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //adding parameters to post request as we need to send firebase id and email
                params.put("firebaseid", uniqueId);
                params.put("Name", "kj");
                return params;
            }*/
        };

        //Adding the request to the queue
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }





   class SelectDateFragmet extends DialogFragment implements DatePickerDialog.OnDateSetListener {

       @Override
       public Dialog onCreateDialog(Bundle savedInstanceState) {

           final Calendar calendar = Calendar.getInstance();
           int yy = calendar.get(Calendar.YEAR);
           int mm = calendar.get(Calendar.MONTH);
           int dd = calendar.get(Calendar.DAY_OF_MONTH);

           return new DatePickerDialog(getActivity(), this, yy, mm, dd);
       }

       public void onDateSet(DatePicker view, int yy, int mm, int dd) {
           populateSetDate(yy, mm + 1, dd);
       }
       public void populateSetDate(int year, int month, int day) {

reg_set_date_btn.setText(month + "/" + day + "/" + year);

       }


   }




}

