package com.alramlawi.schoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

public class Student_att extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ArrayAdapter <String> aa;
    JSONArray jsonArray = null;
    JSONObject jsonObject;

    String nameHolder="";
    String classHolder="";
    String tableHolder="";
    String teacherHolder="";
    String [] teachers = {"Chose", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"};
    //String [] teachers = null;

    ProgressDialog progressDialog;
    ListView datelist,statList;
    ArrayList<String> dateList = new ArrayList<String>();
    ArrayList<String> teachers_array = new ArrayList<String>();
    TextView studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        datelist = findViewById(R.id.List9);

        new GetAllteachers().execute();

        Spinner spin = findViewById(R.id.teacher_name3);
        spin.setOnItemSelectedListener(this);
        aa = new ArrayAdapter(this,R.layout.item_spinner,teachers);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        nameHolder = getIntent().getExtras().getString("name");
        classHolder = getIntent().getExtras().getString("class");
        tableHolder = getIntent().getExtras().getString("tableName");
        studentName = findViewById(R.id.attStudentName);
        studentName.setText(nameHolder);

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        teacherHolder =  teachers[position];
        new GetAllteachers().execute();
        new GetAllstudents().execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
       // teacherName.setText(teacherHolder);

    }

    private class GetAllstudents extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Student_att.this,"Loading Students info",null,true,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://schoolmanager000.000webhostapp.com/Attendance/get_attendance.php");
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
                jsonObject = new JSONObject();
                String  date ;
                String  state ;
                dateList.clear();

                for(int i=0; i<=jsonArray.length(); i++) {
                   try {
                       jsonObject = jsonArray.getJSONObject(i);
                       state = jsonObject.getString(tableHolder);
                       if(jsonObject.getString("teacher").equals(teacherHolder)){
                           date = jsonObject.getString("date");
                           dateList.add(date+"                                       "+state);
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                }
            Collections.reverse(dateList); // ADD THIS LINE TO REVERSE ORDER!
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_attendance, R.id.textView10, dateList);
            datelist.setAdapter(adapter1);
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }


    private class GetAllteachers extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(Student_att.this,"Loading Teachers info",null,true,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://schoolmanager000.000webhostapp.com/UserLogin/get_teacher.php");
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
            jsonObject = new JSONObject();
            teachers_array.clear();
            for(int i=0; i<=jsonArray.length(); i++) {
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    teachers [i+1] = jsonObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }


}




