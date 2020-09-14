package com.alramlawi.schoolapp.elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alramlawi.schoolapp.HttpParse;
import com.alramlawi.schoolapp.R;

import java.util.HashMap;
import java.util.List;

public class RequestAdapter extends ArrayAdapter<Approval> {

    Button approveBN2;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Context context;
    String finalResult;
    String HttpURL = "https://schoolmanager000.000webhostapp.com/Students/add_tage_to_student_info.php";
    int resource;
    boolean done =false;

    public RequestAdapter(@NonNull Context context, int resource, @NonNull List<Approval> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {




       // convertView = LayoutInflater.from(context).inflate(resource, parent, false  );

        convertView = LayoutInflater.from(context).inflate(resource, parent, false  );

        TextView tvType = (TextView)convertView.findViewById(R.id.msg_type2);
        TextView tvDate = (TextView)convertView.findViewById(R.id.msg_date2);
        TextView tvTarget = (TextView)convertView.findViewById(R.id.msg_target2);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.msg_title2);
        TextView tvTxt = (TextView)convertView.findViewById(R.id.msg_details2);
        approveBN2 = (Button) convertView.findViewById(R.id.approveBN2);

        final Approval currentApprove = getItem(position);


        if(currentApprove.getReq_state().equals("1")){
            approveBN2.setText("Approved");
            approveBN2.setBackground(ContextCompat.getDrawable(context, R.color.colorDisable));
        }
        approveBN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentApprove.getReq_state().equals("0") & !done){
                    SendNote(String.valueOf(currentApprove.getId()),currentApprove.getTitle(),currentApprove.getType(),
                            currentApprove.getTarget(), currentApprove.getTxt(), currentApprove.getUrl(), currentApprove.getDate());
                }else if(done){
                    approveBN2.setText("Approved");
                    approveBN2.setBackground(ContextCompat.getDrawable(context, R.color.colorDisable));
                    currentApprove.setReq_state("1");
                    notifyDataSetChanged();
                    done = false;
                }}
        });


            tvTitle.setText(  "Student ID : "+ currentApprove.getTitle());
            tvTarget.setText("Student Name  : "+ currentApprove.getTarget());

        tvType.setText( "Message Type : "+ currentApprove.getType());
        tvTxt.setText(currentApprove.getTxt());
        tvDate.setText(currentApprove.getDate());



        return convertView;
    }


    public void SendNote(final String Id, final String Title, final String Type, final String Target, final String Message, final String Url, final String Date){

        class SendNoteClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(context, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(context,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                if(httpResponseMsg.equalsIgnoreCase("Approved Successfully")){
                    done =true;
                }
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("id",params[0]);
                hashMap.put("title",params[1]);
                hashMap.put("type",params[2]);
                hashMap.put("target",params[3]);
                hashMap.put("textt",params[4]);
                hashMap.put("url",params[5]);
                hashMap.put("date",params[6]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }

        SendNoteClass sendNoteClass = new SendNoteClass();

        sendNoteClass.execute(Id,Title,Type,Target,Message,Url,Date);
    }

}
