package com.alramlawi.schoolapp.admin.studentstat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alramlawi.schoolapp.MainActivity;
import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.Student_att;
import com.alramlawi.schoolapp.elements.StatAdapter;
import com.alramlawi.schoolapp.elements.Student;
import com.alramlawi.schoolapp.teacher.Teacher;
import com.alramlawi.schoolapp.teacher.attendance.AttendanceFragment;
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
import java.util.Collections;

public class StudentStatFragment extends Fragment {
    Transliterator.Position position = null;
    JSONArray jsonArray = null;
    ProgressDialog progressDialog;
    ListView list;
    Student[] statsArray;
    private StatAdapter adapter;
    Intent intent;
    String clas = "class_1";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_student_stat, container, false);
        list = root.findViewById(R.id.statList);
        intent = new Intent(getContext(), Student_att.class);

        //new GetAllstudents().execute();
        return root;
    }

    private class GetAllstudents extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"Loading Students info",null,true,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://schoolmanager000.000webhostapp.com/Students/get_"+clas+"_std.php");
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
            ArrayList<Student> studentList = new ArrayList<>();
            Gson gson = new Gson ();
            statsArray = gson.fromJson(jsonArray.toString(), Student[].class);
            studentList = new ArrayList<Student>();

            for (Student student : statsArray){
                studentList.add(student);
            }

           // Collections.reverse(studentList); // ADD THIS LINE TO REVERSE ORDER!
            adapter = new StatAdapter(getActivity(), R.layout.item_state, studentList);
            list.setAdapter(adapter);
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.classes_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_class_1:{
                clas = "class_1";
                intent.putExtra("class", clas);
                new GetAllstudents().execute();
                return(true);
            }
            case R.id.menu_class_2:{
                clas = "class_2";
                intent.putExtra("class", clas);
                new GetAllstudents().execute();
                return(true);
            }
            case R.id.menu_class_3:{
                clas = "class_3";
                intent.putExtra("class", clas);
                new GetAllstudents().execute();
                return(true);
            }
            case R.id.menu_class_4:{
                clas = "class_4";
                intent.putExtra("class", clas);
                new GetAllstudents().execute();
                return(true);
            }
            case R.id.menu_class_5:{
                clas = "class_5";
                intent.putExtra("class", clas);
                new GetAllstudents().execute();
                return(true);
            }
        }
        return(super.onOptionsItemSelected(item));
    }
}


