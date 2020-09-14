package com.alramlawi.schoolapp.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alramlawi.schoolapp.MessageActivity;
import com.alramlawi.schoolapp.R;


import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SmsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SmsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SmsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SmsFragment newInstance(String param1, String param2) {
        SmsFragment fragment = new SmsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sms, container, false);
        final EditText message,number;
        Button send;
        message=view.findViewById(R.id.xmessage);
        number=view.findViewById(R.id.number);
        send=view.findViewById(R.id.sendbutton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                OkHttpClient client=new OkHttpClient();
                String  url = "https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=qO3Yw24kVkepMeKVkhXkIA&senderid=SMSTST&channel=2&DCS=0&flashsms=0&" +
                        "number="+"91"+number.getText().toString().trim()+"&text="+message.getText().toString()+"\n"+"ICSBLR"+"&route=1";

                Request request=new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        // Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                        if(response.isSuccessful())
                        {
                            //Toast.makeText(MessageActivity.this, "Message Sent To Number", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                Toast.makeText(getActivity(), "Sent", Toast.LENGTH_SHORT).show();

            }
        });
return  view;

    }
}
