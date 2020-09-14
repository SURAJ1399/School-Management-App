package com.alramlawi.schoolapp.elements;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.Student_att;

import java.util.List;

public class StatAdapter extends ArrayAdapter<Student> {

    Context context;
    int resource;
    Intent intent;
    public StatAdapter(@NonNull Context context, int resource, @NonNull List<Student> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(resource, parent, false  );

        TextView tvId = (TextView)convertView.findViewById(R.id.tvId);
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        TextView tvDays = (TextView)convertView.findViewById(R.id.attendanceCount);
        TextView tvLeavs = (TextView)convertView.findViewById(R.id.tvLeaves);
        final Student currentStudent = getItem(position);

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), Student_att.class);
                intent.putExtra("name", currentStudent.getName());
                intent.putExtra("tableName", currentStudent.getTableName());
                //intent.putExtra("name", currentStudent.getName());  intent database column here

                getContext().startActivity(intent);
            }
        });
        tvId.setText(String.valueOf(currentStudent.getId()));
        tvName.setText(currentStudent.getName());
        tvDays.setText(String.valueOf(currentStudent.getCount_on()));
        tvLeavs.setText(String.valueOf(currentStudent.getCount_off()));



        return convertView;
    }
}
