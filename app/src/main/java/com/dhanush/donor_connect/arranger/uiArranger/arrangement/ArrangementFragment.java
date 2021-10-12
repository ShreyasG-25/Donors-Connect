package com.dhanush.donor_connect.arranger.uiArranger.arrangement;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dhanush.donor_connect.R;
import com.dhanush.donor_connect.arranger.uiArranger.home.BloodRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ArrangementFragment extends Fragment {

    private List<BloodRequest> listData2;
    private RecyclerView rv;
    private ArrangementAdapter adapter;
    String arr1= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_arrangement, container, false);
        rv=v.findViewById(R.id.recyclerView2);
        swipeRefreshLayout=v.findViewById(R.id.swipeRefresh2);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        listData2=new ArrayList<>();
        arr1=FirebaseAuth.getInstance().getCurrentUser().getUid();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        listData2.clear();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Requests");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()){
                        BloodRequest l=npsnapshot.getValue(BloodRequest.class);
                        if(l.getArrangement_r().equals(arr1)) {
                            listData2.add(l);
                        }
                    }
                    adapter=new ArrangementAdapter(listData2);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                onResume();
            }
        });
    }
}