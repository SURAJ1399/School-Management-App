package com.alramlawi.schoolapp.student.inbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alramlawi.schoolapp.R;
import com.alramlawi.schoolapp.Student_att;
import com.alramlawi.schoolapp.elements.Approval;
import com.alramlawi.schoolapp.student.InboxAdapter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
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

public class InboxFragment extends Fragment{
    JSONArray jsonArray = null;
    JSONObject jsonObject;
    ProgressDialog progressDialog;
    ListView list;
    Approval [] inboxArray;
    private InboxAdapter adapter;
    String name = "std_1";
    String password = "1";
    String tableHolder ="";
    CarouselView carouselView;
    List<String> mImagesu;
    Button button;
    Intent intent;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_inbox, container, false);
        list = root.findViewById(R.id.inboxList);

        button = root.findViewById(R.id.std_att_btn);

        password = getActivity().getIntent().getExtras().getString("password");
        name = getActivity().getIntent().getExtras().getString("name");
        //tableHolder = getActivity().getIntent().getExtras().getString("name");
        carouselView=root.findViewById(R.id.carouselView);
        mImagesu=new ArrayList<>();
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%201.jpg?alt=media&token=6de60205-85f1-4a53-b44e-197ffc04a47c");
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%202.jpg?alt=media&token=1dfa23a2-0f1c-4518-9f45-94e7b538a4c5");
        //     mImagesu.add("https://h5p.org/sites/default/files/styles/medium-logo/public/logos/image-hotspots-icon-color.png?itok=u1SjnAjm");
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%203.jpg?alt=media&token=836bbc5f-4f09-4891-a34c-cff6f149f816");

        carouselView.setPageCount(mImagesu.size());
        carouselView.setImageListener(imageListener);





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), Student_att.class);
                intent.putExtra("name", name);
                intent.putExtra("tableName", tableHolder);
                getContext().startActivity(intent);
            }
        });
        root.findViewById(R.id.track).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url2 = "https://www.google.com/android/find?u=0";
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse(url2));
                startActivity(i2);

            }
        });


        new GetTableName().execute();
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
                URL url = new URL("https://schoolmanager000.000webhostapp.com/Notes/get_student_inbox.php");
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
            adapter = new InboxAdapter(getActivity(), R.layout.item_inbox, inboxList);
            list.setAdapter(adapter);
            progressDialog.dismiss();
            super.onPostExecute(s);
        }


    }

    private class GetTableName extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("loading student infos");
            dialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://schoolmanager000.000webhostapp.com/Students/get_student_info.php");
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
            super.onPostExecute(s);
            dialog.dismiss();
            for (int i = 0; i <= jsonArray.length(); i++) {
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    if ((jsonObject.getString("name").equals(name))&& (jsonObject.getString("password").equals(password))) {
                        tableHolder = jsonObject.getString("tableName");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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




