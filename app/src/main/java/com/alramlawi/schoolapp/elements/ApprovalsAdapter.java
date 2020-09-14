package com.alramlawi.schoolapp.elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.alramlawi.schoolapp.elements.Approval;

import java.util.HashMap;
import java.util.List;

public class ApprovalsAdapter extends ArrayAdapter<Approval> {
    Button approveBN;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Context context;
    String finalResult;
    String HttpURL = "https://schoolmanager000.000webhostapp.com/Notes/add_inbox_parent_student.php";
    int resource;
    boolean done =false;

    public ApprovalsAdapter(@NonNull Context context, int resource, @NonNull List<Approval> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getPosition(@Nullable Approval item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(resource, parent, false  );

        TextView tvType = (TextView)convertView.findViewById(R.id.msg_type1);
        TextView tvDate = (TextView)convertView.findViewById(R.id.msg_date1);
        TextView tvTarget = (TextView)convertView.findViewById(R.id.msg_target1);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.msg_title1);
        TextView tvTxt = (TextView)convertView.findViewById(R.id.msg_details1);
        TextView pdf = (TextView)convertView.findViewById(R.id.pdf_path1);
        approveBN = (Button) convertView.findViewById(R.id.approveBN1);
        Button share = convertView.findViewById(R.id.share_btn1);


        final Approval currentApprove = getItem(position);



        if(currentApprove.getState().equals("1")){
            approveBN.setText("Approved");
            approveBN.setBackground(ContextCompat.getDrawable(context, R.color.colorDisable));
        }
        approveBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentApprove.getState().equals("0") & !done){
                if(currentApprove.getType().equals("Leaving Request")){
                    HttpURL = "https://schoolmanager000.000webhostapp.com/Notes/add_inbox_teacher.php"; //teacher
                }else {
                    HttpURL = "https://schoolmanager000.000webhostapp.com/Notes/add_inbox_parent_student.php";
                }
                SendNote(String.valueOf(currentApprove.getId()),currentApprove.getTitle(),currentApprove.getType(),
                        currentApprove.getTarget(), currentApprove.getTxt(), currentApprove.getUrl(), currentApprove.getDate());
                //Setapproved("0");
            }else if(done){
                    approveBN.setText("Approved");
                    approveBN.setBackground(ContextCompat.getDrawable(context, R.color.colorDisable));
                    currentApprove.setState("1");
                    notifyDataSetChanged();
                    done = false;
                }}
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentApprove.getUrl().equals("")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentApprove.getUrl()));
                    getContext().startActivity(browserIntent);
                }
            }
            });

        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, currentApprove.getUrl());
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                if (sendIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    getContext().startActivity(sendIntent);
                }
            }
        });

        if(currentApprove.getType().equals("Leaving Request")){
            tvTitle.setText(  "Student ID : "+ currentApprove.getTitle());
            tvTarget.setText("Student Name  : "+ currentApprove.getTarget());
        }else {
            tvTitle.setText(  "Message Title  : "+ currentApprove.getTitle());
            tvTarget.setText("Target Group  : "+ currentApprove.getTarget());
        }
        tvType.setText( "Message Type : "+ currentApprove.getType());
        tvTxt.setText(currentApprove.getTxt());
        tvDate.setText(currentApprove.getDate());
        if(!currentApprove.getUrl().equals("")){

            pdf.setText("DOWNLOAD PDF");
        }



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
