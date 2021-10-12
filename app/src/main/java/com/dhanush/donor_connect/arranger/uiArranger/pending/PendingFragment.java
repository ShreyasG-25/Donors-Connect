package com.dhanush.donor_connect.arranger.uiArranger.pending;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.dhanush.donor_connect.R;
import com.dhanush.donor_connect.arranger.uiArranger.home.BloodRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PendingFragment extends Fragment {
    public TextView pendingtxt,apostxt,anegtxt,bpostxt,bnegtxt,abpostxt,abnegtxt,opostxt,onegtxt;
    int ap=0,an=0,bp=0,bn=0,abp=0,abn=0,op=0,on=0;
    int count=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pending, container, false);

        pendingtxt=v.findViewById(R.id.tvPendingReq);
        apostxt=v.findViewById(R.id.tvApos);
        anegtxt=v.findViewById(R.id.tvAneg);
        bpostxt=v.findViewById(R.id.tvBpos);
        bnegtxt=v.findViewById(R.id.tvBneg);
        abpostxt=v.findViewById(R.id.tvABpos);
        abnegtxt=v.findViewById(R.id.tvABneg);
        opostxt=v.findViewById(R.id.tvOpos);
        onegtxt=v.findViewById(R.id.tvOneg);
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Requests");
        nm.orderByChild("arrangement_r").equalTo("null").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BloodRequest b=snapshot.getValue(BloodRequest.class);
                count++;
                int units= Integer.parseInt(b.getUnit_r());
                switch (b.getBlood_r()){
                    case "A +":
                        ap=ap+units;
                        break;
                    case "A -":
                        an+=units;
                        break;
                    case "B +":
                        bp+=units;
                        break;
                    case "B -":
                        bn+=units;
                        break;
                    case "AB +":
                        abp+=units;
                        break;
                    case "AB -":
                        abn+=units;
                        break;
                    case "O +":
                        op+=units;
                        break;
                    case "O -":
                        on+=units;
                        break;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pendingtxt.setText(pendingtxt.getText()+" "+count);
                apostxt.setText(ap+"");
                anegtxt.setText(an+"");
                bpostxt.setText(bp+"");
                bnegtxt.setText(bn+"");
                abpostxt.setText(abp+"");
                abnegtxt.setText(abn+"");
                opostxt.setText(op+"");
                onegtxt.setText(on+"");
            }
        }, 500);

        return v;

    }
}