package com.alramlawi.schoolapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alramlawi.schoolapp.admin.Admin;
import com.alramlawi.schoolapp.parent.Parent;
import com.alramlawi.schoolapp.student.Student;
import com.alramlawi.schoolapp.teacher.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    CardView teacher, student, parents, admin;
    String finalResult ,HttpURL;
    int place = 0;
    final Context context = this;
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    Dialog dialog;
    String UserHolder = "";
    String PasswordHolder = "";
    EditText name, pword;
    TextView title;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Intent intent = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String db_password = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(context);
        UserHolder = "Me";
        PasswordHolder = "Www";
        teacher = findViewById(R.id.teacherBN);
        student = findViewById(R.id.studentBn);
        parents = findViewById(R.id.parentsBn);
        admin = findViewById(R.id.adminBn);

        FirebaseMessaging.getInstance().subscribeToTopic("general").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                              @Override
                                                                                              public void onComplete(@NonNull Task<Void> task) {

                                                                                              }
                                                                                          });


                sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        teacher.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db_password = sharedPreferences.getString("lock", "");
                if(db_password.equals("teacher_open")){
                    UserHolder = sharedPreferences.getString("name", "");
                    PasswordHolder = sharedPreferences.getString("password", "");
                    intent = new Intent(MainActivity.this, Teacher.class);
                    intent.putExtra("name", UserHolder);             // move to new activity
                    intent.putExtra("password", PasswordHolder);     // move to new activity
                    startActivity(intent);

                }else{
                    place = 1;
                    teacherDialog();
                }

/*
                intent = new Intent(MainActivity.this, Teacher.class);
                intent.putExtra("name", UserHolder);
                intent.putExtra("password", PasswordHolder);
                startActivity(intent);

 */



            }
        });
        student.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db_password = sharedPreferences.getString("lock", "");
                if(db_password.equals("student_open")){
                    UserHolder = sharedPreferences.getString("name", "");
                    PasswordHolder = sharedPreferences.getString("password", "");
                    intent = new Intent(MainActivity.this, Student.class);
                    intent.putExtra("name", UserHolder);             // move to new activity
                    intent.putExtra("password", PasswordHolder);     // move to new activity
                    startActivity(intent);

                }else{
                    place = 2;
                    studentDialog();
                }

            }
        });
        parents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db_password = sharedPreferences.getString("lock", "");
                if(db_password.equals("parent_open")){
                    UserHolder = sharedPreferences.getString("name", "");
                    PasswordHolder = sharedPreferences.getString("password", "");
                    intent = new Intent(MainActivity.this, Parent.class);
                    intent.putExtra("name", UserHolder);             // move to new activity
                    intent.putExtra("password", PasswordHolder);     // move to new activity
                    startActivity(intent);

                }else{
                    place = 3;
                    parentsDialog();
                }
            }
        });
        admin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db_password = sharedPreferences.getString("lock", "");
                if(db_password.equals("admin_open")){
                    UserHolder = sharedPreferences.getString("name", "");
                    PasswordHolder = sharedPreferences.getString("password", "");
                    intent = new Intent(MainActivity.this, Admin.class);
                    intent.putExtra("name", UserHolder);             // move to new activity
                    intent.putExtra("password", PasswordHolder);     // move to new activity
                    startActivity(intent);

                }else{
                    place = 4;
                    adminDialog();
                }

            }
        });
    }

    public void teacherDialog () {
        HttpURL = "https://schoolmanager000.000webhostapp.com/UserLogin/LoginTeacher.php";
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_login);
        dialog.show();
        title = dialog.findViewById(R.id.dialog_title);
        name = dialog.findViewById(R.id.edit_username);
        pword = dialog.findViewById(R.id.edit_password);
        title.setText("Teacher Login");
        Button loginBn = dialog.findViewById(R.id.bnlogin);
        loginBn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    UserLoginFunction(UserHolder, PasswordHolder);
                }
                else {

                    Toast.makeText(MainActivity.this, "Please fill all fields!!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void studentDialog() {
        HttpURL = "https://schoolmanager000.000webhostapp.com/Students/LoginStudent.php";
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_login);
        dialog.show();
        title = dialog.findViewById(R.id.dialog_title);
        name = dialog.findViewById(R.id.edit_username);
        pword = dialog.findViewById(R.id.edit_password);
        title.setText("Student Login");
        Button loginBn = dialog.findViewById(R.id.bnlogin);
        loginBn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    UserLoginFunction(UserHolder, PasswordHolder);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please fill all fields!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void parentsDialog() {
        HttpURL = "https://schoolmanager000.000webhostapp.com/UserLogin/LoginParents.php";
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_login);
        dialog.show();
        title = dialog.findViewById(R.id.dialog_title);
        name = dialog.findViewById(R.id.edit_username);
        pword = dialog.findViewById(R.id.edit_password);
        title.setText("Parent Login");
        Button loginBn = dialog.findViewById(R.id.bnlogin);
        loginBn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    UserLoginFunction(UserHolder, PasswordHolder);
                }
                else {

                    Toast.makeText(MainActivity.this, "Please fill all fields!!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void adminDialog() {
        HttpURL = "https://schoolmanager000.000webhostapp.com/UserLogin/LoginAdmin.php";
        //dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_login);
        dialog.show();
        title = dialog.findViewById(R.id.dialog_title);
        name = dialog.findViewById(R.id.edit_username);
        pword = dialog.findViewById(R.id.edit_password);
        title.setText("Admin Login");
        Button loginBn = dialog.findViewById(R.id.bnlogin);
        loginBn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    UserLoginFunction(UserHolder, PasswordHolder);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please fill all fields!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void CheckEditTextIsEmptyOrNot(){

        UserHolder = name.getText().toString();
        PasswordHolder = pword.getText().toString();

        if(TextUtils.isEmpty(UserHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }


    public void UserLoginFunction(final String username, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(MainActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("name",params[0]);
                hashMap.put("password",params[1]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){
                    dialog.cancel();
                    switch (place) {
                        case 1:
                            intent = new Intent(MainActivity.this, Teacher.class);
                            editor.putString("lock", "teacher_open");                   // save to database
                            editor.commit();
                            break;
                        case 2:
                            intent = new Intent(MainActivity.this, Student.class);
                            editor.putString("lock", "student_open");                   // save to database
                            editor.commit();
                            break;
                        case 3:
                            intent = new Intent(MainActivity.this, Parent.class);
                            editor.putString("lock", "parent_open");                   // save to database
                            editor.commit();
                            break;
                        case 4:
                            intent = new Intent(MainActivity.this, Admin.class);
                            editor.putString("lock", "admin_open");                   // save to database
                            editor.commit();
                            break;
                        default: place =0;
                    }
                    intent.putExtra("name", UserHolder);             // move to new activity
                    intent.putExtra("password", PasswordHolder);     // move to new activity
                    editor.putString("name", UserHolder);                   // save to database
                    editor.putString("password", PasswordHolder);                   // save to database
                    editor.commit();
                    startActivity(intent);
                }
                else{
                    title.setText("");
                    Toast.makeText(MainActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(username,password);
    }



}
