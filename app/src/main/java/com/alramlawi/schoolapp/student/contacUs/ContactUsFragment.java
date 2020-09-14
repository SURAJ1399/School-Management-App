package com.alramlawi.schoolapp.student.contacUs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.alramlawi.schoolapp.R;

public class ContactUsFragment extends Fragment {

    private ContactUsViewModel contactUsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactUsViewModel = ViewModelProviders.of(this).get(ContactUsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        /*final TextView textView = root.findViewById(R.id.text_send);
        contactUsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

         */
        return root;
    }
}