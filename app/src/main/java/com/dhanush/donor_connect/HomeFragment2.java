package com.dhanush.donor_connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dhanush.donor_connect.home2.AboutUsActivity;
import com.dhanush.donor_connect.home2.FindDonorActivity;


public class HomeFragment2 extends Fragment {

    Button btn,btn2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        View v= inflater.inflate(R.layout.fragment_home2, container, false);
        btn=v.findViewById(R.id.btnneed);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), FindDonorActivity.class);
                startActivity(i);
            }
        });
        btn2 = v.findViewById(R.id.button4);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), AboutUsActivity.class);
                startActivity(i);
            }
        });
        return v;
    }

}