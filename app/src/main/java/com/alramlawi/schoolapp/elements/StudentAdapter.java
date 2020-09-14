package com.alramlawi.schoolapp.elements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.elements.Student;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {

    Context context;
    int resource;

    public StudentAdapter(@NonNull Context context, int resource, @NonNull List<Student> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getPosition(@Nullable Student item) {
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

        TextView tvId = (TextView)convertView.findViewById(R.id.id_t);
        TextView tvName = (TextView)convertView.findViewById(R.id.name_t);
        TextView tvDays = (TextView)convertView.findViewById(R.id.count_on_t);
        TextView tvLeavs = (TextView)convertView.findViewById(R.id.count_off_t);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox_t);

        final Student currentStudent = getItem(position);

        tvId.setText(String.valueOf(currentStudent.getId()));
        tvName.setText(currentStudent.getName());
        tvDays.setText(String.valueOf(currentStudent.getCount_on()));
        tvLeavs.setText(String.valueOf(currentStudent.getCount_off()));
        //checkBox.

        return convertView;
    }
}
