package com.alramlawi.schoolapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;


import static android.content.Context.MODE_PRIVATE;

public class Logout extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Data", MODE_PRIVATE);
        sharedPreferences.edit().remove("lock").apply();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);

        return null;
    }
}
