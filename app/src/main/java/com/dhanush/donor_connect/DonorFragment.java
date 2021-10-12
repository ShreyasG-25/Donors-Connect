package com.dhanush.donor_connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dhanush.donor_connect.donorPack.RegistrationDonor;
import com.dhanush.donor_connect.donorPack.UpdateDonor;

public class DonorFragment extends Fragment {

    Button btnreg,btnupd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Donor");
        View v= inflater.inflate(R.layout.fragment_donor, container, false);
        btnreg=v.findViewById(R.id.btnReg);
        btnupd=v.findViewById(R.id.btnUpd);
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), RegistrationDonor.class);
                startActivity(i);
            }
        });
        btnupd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), UpdateDonor.class);
                startActivity(i);
            }
        });
        return v;
    }

}
