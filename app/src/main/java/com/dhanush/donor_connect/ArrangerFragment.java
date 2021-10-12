package com.dhanush.donor_connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dhanush.donor_connect.arranger.LoginActivity;
import com.dhanush.donor_connect.arranger.RegistrationActivity;

public class ArrangerFragment extends Fragment implements View.OnClickListener {

    Button btnLogin, btnRegister;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Arranger");
        View v= inflater.inflate(R.layout.fragment_arranger, container, false);
        btnLogin=v.findViewById(R.id.btnLoginPage);
        btnRegister=v.findViewById(R.id.btnRegisterPage);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        return v;
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnLoginPage:
                Intent loginIntent=new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.btnRegisterPage:
                Intent registerIntent=new Intent(getActivity(), RegistrationActivity.class);
                startActivity(registerIntent);
                break;
        }
    }

}