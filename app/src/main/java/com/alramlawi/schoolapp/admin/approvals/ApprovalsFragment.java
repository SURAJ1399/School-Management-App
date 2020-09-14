package com.alramlawi.schoolapp.admin.approvals;

import android.app.ProgressDialog;
import android.icu.text.Transliterator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.elements.Approval;
import com.alramlawi.schoolapp.elements.ApprovalsAdapter;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

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
import java.util.List;

public class ApprovalsFragment extends Fragment {
    Transliterator.Position position = null;
    JSONArray jsonArray = null;
    JSONObject jsonObject = null;
    ProgressDialog progressDialog;
    ListView list;
    Approval [] approvalsArray;
    private ApprovalsAdapter adapter;
    CarouselView carouselView;
    List<String>mImagesu;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_approvals, container, false);
        list = root.findViewById(R.id.approvalList);
        carouselView=root.findViewById(R.id.carouselView);
        mImagesu=new ArrayList<>();
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%201.jpg?alt=media&token=6de60205-85f1-4a53-b44e-197ffc04a47c");
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%202.jpg?alt=media&token=1dfa23a2-0f1c-4518-9f45-94e7b538a4c5");
        //     mImagesu.add("https://h5p.org/sites/default/files/styles/medium-logo/public/logos/image-hotspots-icon-color.png?itok=u1SjnAjm");
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%203.jpg?alt=media&token=836bbc5f-4f09-4891-a34c-cff6f149f816");

        carouselView.setPageCount(mImagesu.size());
        carouselView.setImageListener(imageListener);



        new GetAllmessages().execute();
        return root;
    }

    private class GetAllmessages extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"Loading Notes",null,true,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            //1------make connection with database (web services)
            try {
                URL url = new URL("https://schoolmanager000.000webhostapp.com/Notes/get_admin_inbox.php");
                URLConnection urlConnection = url.openConnection();
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while((line = bufferedReader.readLine()) != null){
                    jsonArray = new JSONArray(line);
                }
                Log.d("result", jsonArray.toString());

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
            //2------ convert data from json to java objects
            ArrayList<Approval> approvalList = new ArrayList<>();
            Gson gson = new Gson ();
            approvalsArray = gson.fromJson(jsonArray.toString(), Approval[].class);
            approvalList = new ArrayList<Approval>();
            for (Approval note: approvalsArray){
                approvalList.add(note);
            }
            Collections.reverse(approvalList); // ADD THIS LINE TO REVERSE ORDER!
            adapter = new ApprovalsAdapter(getActivity(), R.layout.item_approve, approvalList);
            list.setAdapter(adapter);
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getActivity())
                    .load(mImagesu.get(position))
                    .centerCrop()
                    .placeholder(R.color.demo_dark_transparent)
                    .into(imageView);
        }
    };;

}
