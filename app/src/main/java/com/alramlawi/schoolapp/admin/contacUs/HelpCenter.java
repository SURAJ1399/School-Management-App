package com.alramlawi.schoolapp.admin.contacUs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alramlawi.schoolapp.R;

public class HelpCenter extends Fragment {

        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.help_center, container, false);

        return root;
    }
}