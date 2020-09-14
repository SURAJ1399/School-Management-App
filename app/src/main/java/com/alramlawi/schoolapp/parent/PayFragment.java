package com.alramlawi.schoolapp.parent;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.alramlawi.schoolapp.HttpParse;
import com.alramlawi.schoolapp.R;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class PayFragment extends Fragment {
    private static final String TAG = PayFragment.class.getSimpleName();

    boolean payed = false;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Button razorpay;
    TextView name, id, clas, fee;
    float payment_fee = 0;
    Button status;
    String std_name="";
    String std_id = "";
    String room_holder = "";
    SharedPreferences sharedPreferences;// = getActivity().getSharedPreferences("Payment", MODE_PRIVATE);
    SharedPreferences.Editor editor;// = sharedPreferences.edit();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pay, container, false);
        name = root.findViewById(R.id.payment_name);
        id = root.findViewById(R.id.payment_id);
        clas = root.findViewById(R.id.payment_class);
        fee = root.findViewById(R.id.payment_fee);
        status=root.findViewById(R.id.status);
        razorpay = root.findViewById(R.id.payment_pay);
        Checkout.preload(getActivity().getApplicationContext());

        sharedPreferences = getActivity().getSharedPreferences("Payment", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        std_name = sharedPreferences.getString("std_name", "NULL");
        std_id = sharedPreferences.getString("std_id", "NULL");
        String par_name = sharedPreferences.getString("par_name", "NULL");
        String par_pass = sharedPreferences.getString("par_password", "NULL");

        get_std_class(par_name, par_pass);

        name.setText("STUDENT NAME :   " + std_name );
        id.setText("STUDENT ID        :   " + std_id );



        razorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   check_payment(std_id, std_name, room_holder);
                startPayment(1);
            }
        });

        return root;



    }


    public void get_std_class(final String username, final String password){

        class Get_class_name extends AsyncTask<String,Void,String> {
            String HttpURL = "https://schoolmanager000.000webhostapp.com/Students/get_parent_class.php";
            ProgressDialog progressDialog;
            String finalResult;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(),"Loading Student Class",null,true,true);
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("parent",params[0]);
                hashMap.put("parPass",params[1]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                room_holder = httpResponseMsg;
                clas.setText("STUDENT ROOM : " + room_holder );
                editor.putString("std_room", room_holder);
                editor.commit();
                getFee(room_holder);
            }
        }

        Get_class_name getclassname = new Get_class_name();

        getclassname.execute(username,password);
    }
    private void getFee(final String room) {

        class Get_FEE extends AsyncTask<String,Void,String> {
            String HttpURL = "https://schoolmanager000.000webhostapp.com/Students/get_fee.php";
            ProgressDialog progressDialog;
            String finalResult;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(),"Loading Student Class",null,true,true);
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("class_room",params[0]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(httpResponseMsg);
                    String amount = object.getString("amount");
                    fee.setText("FEE AMOUNT : " + amount );
                    payment_fee = Float.parseFloat(amount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Get_FEE get_fee = new Get_FEE();
        get_fee.execute(room);
    }

    public void startPayment(final float amount) {
        /**
         * Instantiate Checkout
         */
        String key = "rzp_live_pdo06Q1zOunz60";

        final Checkout checkout = new Checkout();
        checkout.setKeyID(key);

        /**
         * Set your logo here
         */
        //checkout.setImage(R.drawable.ic_star_24dp);

        /**
         * Reference to current activity
         */
        final FragmentActivity activity = this.getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "SCHOOL");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Fee Payment");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", amount*100);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    public void check_payment(final String id, final String name , final String room){

        class PaymentClass extends AsyncTask<String,Void,String> {
            String HttpURL = "https://schoolmanager000.000webhostapp.com/Students/check_payment.php";
            ProgressDialog progressDialog;
            String finalResult;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(),"checking",null,true,true);
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("std_id",params[0]);
                hashMap.put("std_name",params[1]);
                hashMap.put("std_room",params[2]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                if(httpResponseMsg.equalsIgnoreCase("Data Matched")) {
                    Toast.makeText(getActivity(), "PAID FOR THIS MONTH", Toast.LENGTH_SHORT).show();
                    status.setBackgroundColor(Color.GREEN);
                    status.setText("Paid for this month");

                }else{
                    startPayment(payment_fee);
                }

            }
        }

        PaymentClass userLoginClass = new PaymentClass();

        userLoginClass.execute(id, name, room);
    }


}