package com.alramlawi.schoolapp.student;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import com.alramlawi.schoolapp.HttpParse;
import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.elements.Approval;

import java.util.HashMap;
import java.util.List;

public class InboxAdapter extends ArrayAdapter<Approval> {
    Context context;
    int resource;

    public InboxAdapter(@NonNull Context context, int resource, @NonNull List<Approval> objects) {
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

        TextView tvType = (TextView)convertView.findViewById(R.id.msg_type3);
        TextView tvDate = (TextView)convertView.findViewById(R.id.msg_date3);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.msg_title3);
        TextView tvTxt = (TextView)convertView.findViewById(R.id.msg_details3);
        TextView pdf = (TextView)convertView.findViewById(R.id.pdf_path3);
        Button share = convertView.findViewById(R.id.share_btn3);
        final Approval currentApprove = getItem(position);


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


        tvTitle.setText(  "Message Title  : "+ currentApprove.getTitle());
        tvType.setText( "Message Type : "+ currentApprove.getType());
        tvTxt.setText(currentApprove.getTxt());
        tvDate.setText(currentApprove.getDate());

        if(!currentApprove.getUrl().equals("")){
            pdf.setText("DOWNLOAD");
        }

        return convertView;
    }
}
