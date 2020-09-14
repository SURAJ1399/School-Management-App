package com.alramlawi.schoolapp.admin.settings;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alramlawi.schoolapp.HttpParse;
import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.elements.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {
    JSONArray jsonArray = null;
    User[] userArray;
    ArrayList<User> userList = new ArrayList<>();
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    String finalResult,NameHolder,PasswordHolder,pass1,pass2,pass3,Idholder;
    HttpParse httpParse = new HttpParse();
    boolean CheckEditText;
    Button update;
    EditText old_pass, new_pass, confirm_pass;
    String HttpURL = "https://schoolmanager000.000webhostapp.com/UserLogin/update_LoginAdmin.php";


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        old_pass = root.findViewById(R.id.old_password_edit);
        new_pass = root.findViewById(R.id.new_password_edit);
        confirm_pass = root.findViewById(R.id.conf_password_edit);
        update = root.findViewById(R.id.update_btn);

        PasswordHolder = getActivity().getIntent().getExtras().getString("password");
        NameHolder = getActivity().getIntent().getExtras().getString("name");


        new GetUser().execute();
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckPassword();
                if(CheckEditText){
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Data", MODE_PRIVATE);
                    sharedPreferences.edit().remove("lock").apply();
                    updatePass(Idholder, NameHolder, pass2);
                }
                else {
                    Toast.makeText(getActivity(), "Please fill all fields!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;

    }

    public void CheckPassword(){
        pass1 = old_pass.getText().toString();
        pass2 = new_pass.getText().toString();
        pass3 = confirm_pass.getText().toString();

        if(!pass1.equals(PasswordHolder)|| TextUtils.isEmpty(pass2)|| TextUtils.isEmpty(pass3)|| !pass2.equals(pass3))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }

    public void updatePass(final String ID, final String NAME, final String PASSWORD){

        class UpdateInfoClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Loading Data", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("id",params[0]);
                hashMap.put("name",params[1]);
                hashMap.put("password",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(getActivity(),httpResponseMsg.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        UpdateInfoClass sendnfoClass = new UpdateInfoClass();

        sendnfoClass.execute(ID,NAME,PASSWORD);
    }

    private class GetUser extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://schoolmanager000.000webhostapp.com/UserLogin/get_admin.php");
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
            Gson gson = new Gson ();
            userArray = gson.fromJson(jsonArray.toString(), User[].class);
            userList = new ArrayList<User>();
            for (User user : userArray){
                userList.add(user);
                if(user.getName().equals(NameHolder))
                    if(user.getPassword().equals(PasswordHolder)) {
                        Idholder = String.valueOf(user.getId());
                    }
            }

            super.onPostExecute(s);
        }
    }

}