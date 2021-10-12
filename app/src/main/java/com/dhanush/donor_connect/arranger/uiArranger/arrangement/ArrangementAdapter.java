package com.dhanush.donor_connect.arranger.uiArranger.arrangement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.dhanush.donor_connect.R;
import com.dhanush.donor_connect.arranger.uiArranger.home.BloodRequest;
import com.dhanush.donor_connect.arranger.uiArranger.home.eligible.EligibleDonorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class ArrangementAdapter extends RecyclerView.Adapter<ArrangementAdapter.MyViewHolder>{
    private final List<BloodRequest> listData2;
    private Context context;


    public ArrangementAdapter(List<BloodRequest> listData) {
        this.listData2 = listData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View listItem=inflater.inflate(R.layout.bloodeinfo_layout2,parent,false);
        ArrangementAdapter.MyViewHolder holder=new ArrangementAdapter.MyViewHolder(listItem);
        context= parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            BloodRequest b=listData2.get(position);
                holder.txtbloodgroup.setText("Blood Group : " + b.getBlood_r());
                holder.txtunit.setText("Units : " + b.getUnit_r());
                holder.txthospital.setText("Hospital : " + b.getHospital_r());
                holder.txtpatient.setText("Patient Name : " + b.getName_r());
                holder.txtcontact.setText("Contact : "+b.getContact_r());
                holder.txtdate.setText("Due date : " + b.getLast_r());
                holder.btna.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("Confirm Action")
                                .setMessage("Please confirm that required number of units have been arranged")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Requests");
                                        ref.orderByChild("id_r").equalTo(b.getId_r()).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                BloodRequest request_data=new BloodRequest(null,b.getBlood_r(),b.getUnit_r(),b.getHospital_r(),b.getName_r(),b.getLast_r(),b.getContact_r(),b.getCity_r(),b.getArrangement_r());
                                                FirebaseDatabase.getInstance().getReference("Arranged")
                                                        .push().setValue(request_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            snapshot.getRef().removeValue();
                                                            Toast.makeText(context, "Done Successfully", Toast.LENGTH_LONG).show();
                                                        }else {
                                                            Toast.makeText(context, "Not Done Successfully", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                                            @Override
                                            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                                            @Override
                                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(context, "Not Done Successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                    listData2.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, getItemCount());
                                            }
                                        }, 300);

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
                });
                holder.btnb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, EligibleDonorActivity.class);
                        i.putExtra("blood_group", b.getBlood_r());
                        i.putExtra("r_uid", b.getId_r());
                        v.getContext().startActivity(i);
                    }
                });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listData2.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView txtbloodgroup, txtunit,txthospital,txtpatient,txtdate,txtcontact;
        Button btna,btnb;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.linear_layout2);
            txtbloodgroup=itemView.findViewById(R.id.tvBloodGroup22);
            txtunit=itemView.findViewById(R.id.tvUnits2);
            txthospital=itemView.findViewById(R.id.tvGender2);
            txtdate=itemView.findViewById(R.id.tvDate2);
            txtpatient=itemView.findViewById(R.id.tvPatientName2);
            txtcontact=itemView.findViewById(R.id.tvPatientContact2);
            btna=itemView.findViewById(R.id.btnarrangment2);
            btnb=itemView.findViewById(R.id.btnEdit);
        }
    }

}
