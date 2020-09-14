package com.alramlawi.schoolapp.parent.leavings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alramlawi.schoolapp.HttpParse;
import com.alramlawi.schoolapp.MainActivity;
import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.Student_att;
import com.alramlawi.schoolapp.admin.approvals.ApprovalsViewModel;
import com.alramlawi.schoolapp.parent.Parent;
import com.alramlawi.schoolapp.parent.settings.SettingsFragment;
import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LeavingsFragment extends Fragment {
    JSONArray jsonArray = null;
    JSONObject jsonObject;
    String parent_name="";
    String parent_pass="";

    ArrayList<String> dateList = new ArrayList<String>();
    Button Send;
    EditText Request_txt;
    TextView name,indicate;
    String ID_Holder = "id";
    String Name_Holder = "name";
    String Request_Holder;

    String finalResult;
    String HttpURL = "https://schoolmanager000.000webhostapp.com/Notes/add_inbox_admin_from_parent.php";
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String timeHolder = "";
    String timeHold = "";
    String state = "";
    Intent intent;
    int loop =0;
    SharedPreferences sharedPreferences;
    CarouselView carouselView;
    List<String> mImagesu;
    SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_leavings, container, false);
        jsonObject = new JSONObject();
        name = root.findViewById(R.id.edit_stdName);
        indicate = root.findViewById(R.id.requst_state_indicat);
        Request_txt = root.findViewById(R.id.leaving_text);
        Send = root.findViewById(R.id.send_request);
        parent_name = getActivity().getIntent().getExtras().getString("name");
        parent_pass = getActivity().getIntent().getExtras().getString("password");

        sharedPreferences = getActivity().getSharedPreferences("Payment", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("par_name", parent_name);
        editor.putString("par_password", parent_pass);
        editor.commit();

       carouselView=root.findViewById(R.id.carouselView);
        mImagesu=new ArrayList<>();
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%201.jpg?alt=media&token=6de60205-85f1-4a53-b44e-197ffc04a47c");
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%202.jpg?alt=media&token=1dfa23a2-0f1c-4518-9f45-94e7b538a4c5");
        //     mImagesu.add("https://h5p.org/sites/default/files/styles/medium-logo/public/logos/image-hotspots-icon-color.png?itok=u1SjnAjm");
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%203.jpg?alt=media&token=836bbc5f-4f09-4891-a34c-cff6f149f816");

        carouselView.setPageCount(mImagesu.size());
        carouselView.setImageListener(imageListener);




        new GetStudent().execute();


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
        SimpleDateFormat dd = new SimpleDateFormat("HH:mm");
        timeHolder = df.format(c);
        timeHold = dd.format(c);
        timeHolder = (timeHolder+" "+timeHold);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmptyOrNot();
                if (CheckEditText) {
                    SendNote( ID_Holder, "Leaving Request", Name_Holder, Request_Holder, "",timeHolder);
                } else {
                    Toast.makeText(getActivity(), "Please fill request form !!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return  root;
    }

    public void CheckEditTextIsEmptyOrNot(){
        Request_Holder = Request_txt.getText().toString();

        if(TextUtils.isEmpty(Request_Holder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }

    public void SendNote(final String Title, final String Type, final String Target, final String Message, final String PDFurl,final String Date){
        class SendNoteClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(),"Loading Data",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(getActivity(),httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("title",params[0]);
                hashMap.put("type",params[1]);
                hashMap.put("target",params[2]);
                hashMap.put("textt",params[3]);
                hashMap.put("url",params[4]);
                hashMap.put("date",params[5]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        SendNoteClass sendNoteClass = new SendNoteClass();
        sendNoteClass.execute(Title,Type,Target,Message,PDFurl,Date);
    }

    private class GetStudent extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("loading student infos");
            dialog.show();
            //progressDialog =   ProgressDialog.show(getContext(),"loading student infos",null,true,true);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://schoolmanager000.000webhostapp.com/Students/get_student_info.php");
                URLConnection urlConnection = url.openConnection();
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while((line = bufferedReader.readLine()) != null){
                    jsonArray = new JSONArray(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            for (int i = 0; i <= jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        if ((jsonObject.getString("parent").equals(parent_name))&& (jsonObject.getString("parPass").equals(parent_pass))) {
                            Name_Holder = jsonObject.getString("name");
                            ID_Holder = jsonObject.getString("id");
                            state = jsonObject.getString("req_state");
                            name.setText("STD NAME: " + Name_Holder + "  ,   ID: " + ID_Holder);

                            editor.putString("std_name", Name_Holder); // to payment fragment
                            editor.putString("std_id", ID_Holder);     // to payment fragment
                            editor.commit();

                            intent = new Intent(getContext(), SettingsFragment.class);
                            intent.putExtra("std_id", ID_Holder);
                            if (state.equals("1")) { // was approved
                                indicate.setText("Your request was approved");
                                indicate.setBackground(ContextCompat.getDrawable(getContext(), R.color.blue_white1));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getActivity())
                    .load(mImagesu.get(position))
                    .centerCrop()
                    .placeholder(R.color.demo_dark_transparent)
                    .into(imageView);
        }
    };;
}
