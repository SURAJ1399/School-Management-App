package com.alramlawi.schoolapp.parent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.alramlawi.schoolapp.HttpParse;
import com.alramlawi.schoolapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.razorpay.PaymentResultListener;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;

public class Parent extends AppCompatActivity implements PaymentResultListener {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.parent_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_leavings, R.id.nav_viewing, R.id.nav_pay, R.id.nav_settings, R.id.nav_contactUs)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
//
//                switch (menuItem.getItemId()) {
//
//
//                    case R.id.track:
//
//                        String url2 = "https://www.google.com/android/find?u=0";
//                        Intent i2 = new Intent(Intent.ACTION_VIEW);
//                        i2.setData(Uri.parse(url2));
//                        startActivity(i2);
//                        break;
//
//
//                    default:
//
//                        break;
//
//                }
//
//                //   loadHomeFragment();
//
//                return true;
//            }
//        });
//


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this,"payment successfully", Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = getSharedPreferences("Payment", MODE_PRIVATE);
        String std_name = sharedPreferences.getString("std_name", "NULL");
        String std_id = sharedPreferences.getString("std_id", "NULL");
        String std_room = sharedPreferences.getString("std_room", "NULL");
        try {
            storPayment(std_id, std_name, std_room);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this,"ERROR//.... "+ s + i, Toast.LENGTH_LONG).show();
    }
    private void storPayment(String std_id, String std_name, String std_room) {

        class SendNoteClass extends AsyncTask<String,Void,String> {
            ProgressDialog progressDialog;
            String HttpURL = "https://schoolmanager000.000webhostapp.com/Students/add_payment.php";
            HashMap<String, String> hashMap = new HashMap<>();
            HttpParse httpParse = new HttpParse();
            String finalResult;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressDialog = ProgressDialog.show(getApplicationContext(),"storing payment",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                //progressDialog.dismiss();

                Toast.makeText(getApplicationContext(),httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("std_id",params[0]);
                hashMap.put("std_name",params[1]);
                hashMap.put("std_room",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        SendNoteClass sendNoteClass = new SendNoteClass();

        sendNoteClass.execute(std_id,std_name,std_room);
    }
}
