package com.dhanush.donor_connect.arranger.uiArranger.home.eligible;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.dhanush.donor_connect.R;
import com.dhanush.donor_connect.donorPack.donor;
import com.dhanush.donor_connect.arranger.uiArranger.home.BloodRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EligibleDonorActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<donor> listData;
    private EligibleDonorAdapter adapter;
    private TextView reqName,reqBlood,reqHospital,reqContact,reqUnits;
    private Button reqArrangebtn,reqPendingbtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    Date c = Calendar.getInstance().getTime();
    int thisYear=c.getYear()+1900;
    int thisMonth=c.getMonth()+1;
    int yod=0,mod=0;
    String bloodId,bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligible_donor);
        reqName=findViewById(R.id.tvReqPatientName);
        reqBlood=findViewById(R.id.tvReqBloodType);
        reqUnits=findViewById(R.id.tvReqNoUnits);
        reqHospital=findViewById(R.id.tvReqHospital);
        reqContact=findViewById(R.id.tvReqContact);
        reqArrangebtn=findViewById(R.id.btnReqArrange);
        reqPendingbtn=findViewById(R.id.btnReqPending);
        reqPendingbtn.setOnClickListener(this);
        reqArrangebtn.setOnClickListener(this);

        recyclerView=findViewById(R.id.recyclerViewDonors);
        swipeRefreshLayout=findViewById(R.id.swipeRefresh3);

        bloodGroup= getIntent().getStringExtra("blood_group");
        bloodId=getIntent().getStringExtra("r_uid");

        final DatabaseReference pm=FirebaseDatabase.getInstance().getReference("Requests");
        pm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot requestsnapshot:snapshot.getChildren()){
                        BloodRequest b=requestsnapshot.getValue(BloodRequest.class);
                        if(requestsnapshot.getKey().equals(bloodId)) {
                            reqName.setText("Patient : " + b.getName_r());
                            reqBlood.setText("Blood Group : " + b.getBlood_r());
                            reqUnits.setText("Units : " + b.getUnit_r());
                            reqHospital.setText("Hospital : "+b.getHospital_r());
                            reqContact.setText("Contact : "+b.getContact_r());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listData.clear();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Donors");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()){
                        donor d=npsnapshot.getValue(donor.class);
                        Boolean over_3month=false;

                        if(d.bloodtype_d.equals(bloodGroup)){
                            Date date2=null;
                            try {
                                date2=new SimpleDateFormat("dd-MM-yyyy").parse(d.last_d);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            yod=date2.getYear()+1900;
                            mod=date2.getMonth()+1;

                            int dif=0;
                            if(thisYear>yod+1){
                                over_3month=true;
                            }
                            else if(thisYear==yod+1 && thisMonth>mod){
                                over_3month=true;
                            }
                            else if(thisYear==yod+1){
                                int temp=12+thisMonth;
                                dif=(temp-mod);
                            }
                            else{
                                dif=(thisMonth-mod);
                            }
                            if(dif>3){
                                over_3month=true;
                            }

                            if(over_3month){
                                listData.add(d);
                            }
                        }
                    }
                    adapter=new EligibleDonorAdapter(listData);
                    recyclerView.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnReqArrange:
                Toast.makeText(EligibleDonorActivity.this, "Arranged, Check your \"My Arrangement\"", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btnReqPending:
                cancel();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    void cancel(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("Requests").child(bloodId).child("arrangement_r");
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Confirm Action")
                .setMessage("Arrangement progress will be suspended!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.setValue("null").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EligibleDonorActivity.this, "Arrangement Cancelled", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(EligibleDonorActivity.this, "Failed to Cancel request!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
        AlertDialog alert=builder.create();
        alert.show();
    }
}