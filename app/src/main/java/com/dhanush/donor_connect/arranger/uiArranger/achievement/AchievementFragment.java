package com.dhanush.donor_connect.arranger.uiArranger.achievement;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AchievementFragment extends Fragment {

    int countF=0,countU=0;
    TextView familyCount,unitCount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achievement, container, false);
        familyCount=root.findViewById(R.id.tvHelpFamily);
        unitCount=root.findViewById(R.id.tvArrangeBlood);
        String arrangement= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Arranged");
        nm.orderByChild("arrangement_r").equalTo(arrangement).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BloodRequest b=snapshot.getValue(BloodRequest.class);
                countF++;
                countU=countU+Integer.parseInt(b.getUnit_r());
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
                familyCount.setText(""+countF);
                unitCount.setText(""+countU+" units");
            }
        }, 300);
        return root;
    }
}