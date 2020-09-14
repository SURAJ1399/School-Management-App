package com.alramlawi.schoolapp.parent.view;

import android.app.ProgressDialog;
import android.icu.text.Transliterator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.elements.Approval;
import com.alramlawi.schoolapp.parent.ParentAdapter;
import com.alramlawi.schoolapp.student.InboxAdapter;
import com.alramlawi.schoolapp.student.InboxData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

public class ViewFragment extends Fragment {
    Transliterator.Position position = null;
    JSONArray jsonArray = null;
    JSONObject jsonObject = null;
    ProgressDialog progressDialog;
    ListView list;
    Approval [] inboxArray;
    private ParentAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view, container, false);
        list = root.findViewById(R.id.viewList);


        new GetAllmessages().execute();
        return root;
    }


    private class GetAllmessages extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"Loading Data",null,true,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            //1------make connection with database (web services)
            try {
                URL url = new URL("https://schoolmanager000.000webhostapp.com/Notes/get_parent_student_inbox.php");
                URLConnection urlConnection = url.openConnection();
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while((line = bufferedReader.readLine()) != null){
                    jsonArray = new JSONArray(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayList<Approval> inboxList = new ArrayList<>();
            Gson gson = new Gson ();
            inboxArray = gson.fromJson(jsonArray.toString(), Approval[].class);
            inboxList = new ArrayList<Approval>();
            for (Approval note: inboxArray){
                inboxList.add(note);
            }
            Collections.reverse(inboxList); // ADD THIS LINE TO REVERSE ORDER!
            adapter = new ParentAdapter(getActivity(), R.layout.item_view1, inboxList);
            list.setAdapter(adapter);
            progressDialog.dismiss();
            super.onPostExecute(s);
        }


    }


}




