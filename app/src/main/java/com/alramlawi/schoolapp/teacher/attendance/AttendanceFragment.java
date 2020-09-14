package com.alramlawi.schoolapp.teacher.attendance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.alramlawi.schoolapp.HttpParse;
import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.elements.Student;
import com.alramlawi.schoolapp.elements.StudentAdapter;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
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

public class AttendanceFragment extends Fragment {
    JSONArray jsonArray = null;
    ProgressDialog progressDialog;
    ListView list;
    Button confirm_button, datePik;
    String finalResult, NameHolder, PasswordHolder, timeHolder;
    private StudentAdapter adapter;
    Student[] studentsArray;
    ArrayList<Student> studentList = new ArrayList<>();
    boolean load = false;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String clas = "class_1";
    String state = "off";

    String HttpURL2 = "";
    String HttpURL4 = "";


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_attendance, container, false);
        list = root.findViewById(R.id.student_list);
        confirm_button = root.findViewById(R.id.confirm_button);
        datePik = root.findViewById(R.id.date_piker);
        final Date_PickerDialog pickerdialog = new Date_PickerDialog();


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        timeHolder = df.format(c);
        PasswordHolder = getActivity().getIntent().getExtras().getString("password");
        NameHolder = getActivity().getIntent().getExtras().getString("name");
        datePik.setText(timeHolder);
        //new GetAllstudents().execute();

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDate(timeHolder);
            }
        });

        return root;
    }

    private class GetAllstudents extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Loading Students info", null, true, true);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://schoolmanager000.000webhostapp.com/Students/get_"+clas+"_std.php");
                URLConnection urlConnection = url.openConnection();
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
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
            Gson gson = new Gson();
            studentsArray = gson.fromJson(jsonArray.toString(), Student[].class);
            studentList = new ArrayList<Student>();
            for (Student student : studentsArray) {
                studentList.add(student);
            }
            adapter = new StudentAdapter(getActivity(), R.layout.item_student, studentList);
            list.setAdapter(adapter);
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    private void storeAttendance() {
        //progressDialog = ProgressDialog.show(getContext(), "checking date", null, true, true);
        load = true;
        state = "off";
        Attendance("teacher", NameHolder);
        Attendance("date", timeHolder);
        for (Student student : studentList) {
            int position = adapter.getPosition(student);
            CheckBox cb = list.getChildAt(position).findViewById(R.id.checkbox_t);
            if (cb.isChecked()) {
                state = "on";
            }else {
                state = "off";
            }
            Attendance(student.getTableName(), state);  // sort by student tableName
            //Attendance(student.getName(), state);     // sort by student name
        }
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();

    }

    public void checkDate(final String Date) {
        HttpURL2 = "https://schoolmanager000.000webhostapp.com/Attendance/"+clas+"_checkDay.php";

        class CheckClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getContext(), "checking date", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("date", params[0]);
                finalResult = httpParse.postRequest(hashMap, HttpURL2);
                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                //progressDialog.dismiss();
                if (httpResponseMsg.equalsIgnoreCase("once per day !!")) {
                    Toast.makeText(getContext(), httpResponseMsg, Toast.LENGTH_LONG).show();
                } else {
                    storeAttendance();
                }
            }

        }

        CheckClass userLoginClass = new CheckClass();

        userLoginClass.execute(Date);
    }

    public void Attendance(final String key, final String value) {
        HttpURL4 = "https://schoolmanager000.000webhostapp.com/Attendance/update_"+clas+"_attendance.php";

        class StoreAttendance extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
  //              progressDialog.dismiss();
                Toast.makeText(getContext(), httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("key", params[0]);
                hashMap.put("value", params[1]);
                finalResult = httpParse.postRequest(hashMap, HttpURL4);
                return finalResult;
            }
        }
        StoreAttendance storeAttendance = new StoreAttendance();
        storeAttendance.execute(key, value);
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
                new GetAllstudents().execute();
                return(true);
            }
            case R.id.menu_class_2:{
                clas = "class_2";
                new GetAllstudents().execute();
                return(true);
            }
            case R.id.menu_class_3:{
                clas = "class_3";
                new GetAllstudents().execute();
                return(true);
            }
            case R.id.menu_class_4:{
                clas = "class_4";
                new GetAllstudents().execute();
                return(true);
            }
            case R.id.menu_class_5:{
                clas = "class_5";
                new GetAllstudents().execute();
                return(true);
            }
        }
        return(super.onOptionsItemSelected(item));
    }

    public class Date_PickerDialog extends DialogFragment {
        public Dialog onCreateDialog(Bundle saveInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            android.app.DatePickerDialog dialog;
            DateSetting date_setting = new DateSetting(getActivity());
            dialog = new android.app.DatePickerDialog(getActivity(), date_setting, year, month, day);
            return dialog;
        }


    }

    public class DateSetting implements DatePickerDialog.OnDateSetListener {
        Context context;

        public DateSetting(Context context) {
            this.context = context;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            datePik.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            timeHolder = (dayOfMonth + "-" + (month + 1) + "-" + year);
        }
    }



}


