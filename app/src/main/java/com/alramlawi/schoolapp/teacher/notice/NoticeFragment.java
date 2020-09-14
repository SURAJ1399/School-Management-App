package com.alramlawi.schoolapp.teacher.notice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alramlawi.schoolapp.HttpParse;
import com.alramlawi.schoolapp.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import static android.app.Activity.RESULT_OK;

public class NoticeFragment extends Fragment {
    Context context;
    View root;
    Button Send;
    RadioButton note, assignment, all_students, all_parents, custom_student, custom_parent;
    EditText Title, Message;
    String Title_Holder, Type_Holder, Target_Holder, Message_Holder;
    String finalResult;
    String HttpURL = "https://schoolmanager000.000webhostapp.com/Notes/add_inbox_admin.php";
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Button SelectButton, UploadButton;
    EditText PdfNameEditText ;
    CarouselView carouselView;
    List<String> mImagesu;
    private TextView tv;
    private String upload_URL = "https://schoolmanager000.000webhostapp.com/AndroidPdfUpload/volley_upload.php?";
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    String url = "https://www.google.com";
    String displayName = "untitled";
    String Url_Holder = "";
    String timeHolder = "";
    String timeHold = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notice, container, false);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
        SimpleDateFormat dd = new SimpleDateFormat("HH:mm");
        timeHolder = df.format(c);
        timeHold = dd.format(c);
        timeHolder = (timeHolder+" "+timeHold);

        Title = root.findViewById(R.id.edit_title);
        Message = root.findViewById(R.id.edit_message);
        Send = root.findViewById(R.id.btnSend_note);

        carouselView=root.findViewById(R.id.carouselView);
        mImagesu=new ArrayList<>();
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%201.jpg?alt=media&token=6de60205-85f1-4a53-b44e-197ffc04a47c");
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%202.jpg?alt=media&token=1dfa23a2-0f1c-4518-9f45-94e7b538a4c5");
   //     mImagesu.add("https://h5p.org/sites/default/files/styles/medium-logo/public/logos/image-hotspots-icon-color.png?itok=u1SjnAjm");
        mImagesu.add("https://firebasestorage.googleapis.com/v0/b/icsblr.appspot.com/o/slider%203.jpg?alt=media&token=836bbc5f-4f09-4891-a34c-cff6f149f816");

        carouselView.setPageCount(mImagesu.size());
        carouselView.setImageListener(imageListener);



        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getTypeAndtarget();
                CheckEditTextIsEmptyOrNot();
                if (CheckEditText) {
                    SendNote(Title_Holder,Type_Holder, Target_Holder, Message_Holder, Url_Holder,timeHolder);
                } else {
                    Toast.makeText(getActivity(), "Please fill all fields !!", Toast.LENGTH_LONG).show();
                }
            }
        });

        SelectButton = root.findViewById(R.id.btn_PDF_add);
        //UploadButton = root.findViewById(R.id.btn_PDF_upload);
        PdfNameEditText = root.findViewById(R.id.edit_PDF_name);

        SelectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        startActivityForResult(intent,1);
                    }
                });

        return  root;
    }

    public void CheckEditTextIsEmptyOrNot(){

        Title_Holder = Title.getText().toString();
        Message_Holder = Message.getText().toString();

        if(TextUtils.isEmpty(Title_Holder) || TextUtils.isEmpty(Message_Holder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }

    public void SendNote(final String Title, final String Type, final String Target, final String Message, final String PDFurl, final String DATE){

        class SendNoteClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(getActivity(),"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(getActivity(),httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("title",params[0]);

                hashMap.put("type",params[1]);

                hashMap.put("target",params[2]);

                hashMap.put("textt",params[3]);

                hashMap.put("url",params[4]);

                hashMap.put("date",params[5]);




                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        SendNoteClass sendNoteClass = new SendNoteClass();

        sendNoteClass.execute(Title,Type,Target,Message,PDFurl,DATE);
    }

    public void getTypeAndtarget() {
        note = root.findViewById(R.id.note);
        assignment = root.findViewById(R.id.assignment);
        all_students = root.findViewById(R.id.all_students);
        all_parents = root.findViewById(R.id.all_parents);
        //custom_student = root.findViewById(R.id.custom_student);
        //custom_parent = root.findViewById(R.id.custom_parent);

        if (note.isChecked()) {
            Type_Holder = "Note";
        } else
            Type_Holder = "Assignment";

        if (all_students.isChecked()) {
            Target_Holder = "All Students";
        } else if (all_parents.isChecked()) {
            Target_Holder = "All Parents";
        }
       /* } else if (custom_student.isChecked()) {
            Target_Holder = "custom_student";
        } else
            Target_Holder = "custom_parent";

        */
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            displayName = (PdfNameEditText.getText().toString() + ".pdf");
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        uploadPDF(displayName,uri);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                //displayName = myFile.getName();
            }
        }
        Url_Holder = ("https://schoolmanager000.000webhostapp.com/AndroidPdfUpload/uploads/"+displayName);

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadPDF(final String pdfname, Uri pdffile){

        Url_Holder = ("https://schoolmanager000.000webhostapp.com/AndroidPdfUpload/uploads/"+displayName);
        InputStream iStream = null;
        try {

            iStream = getActivity().getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Toast.makeText(context.getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                jsonObject.toString().replace("\\\\","");

                                if (jsonObject.getString("status").equals("true")) {
                                    Log.d("come::: >>>  ","yessssss");
                                    arraylist = new ArrayList<HashMap<String, String>>();
                                    JSONArray dataArray = jsonObject.getJSONArray("data");


                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataobj = dataArray.getJSONObject(i);
                                        url = dataobj.optString("pathToFile");
                                        tv.setText(url);
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();

                    params.put("filename", new DataPart(pdfname ,inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(volleyMultipartRequest);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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