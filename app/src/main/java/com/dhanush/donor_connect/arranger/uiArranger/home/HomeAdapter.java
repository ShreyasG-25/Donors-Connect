package com.dhanush.donor_connect.arranger.uiArranger.home;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;
import com.dhanush.donor_connect.arranger.uiArranger.home.eligible.EligibleDonorActivity;
import com.dhanush.donor_connect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
    private final List<BloodRequest>listData;
    private Context context;
    private String r_Uid;

    public HomeAdapter(List<BloodRequest> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View listItem=inflater.inflate(R.layout.bloodeinfo_layout,parent,false);
        MyViewHolder holder=new MyViewHolder(listItem);
        context=parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            BloodRequest b=listData.get(position);
                holder.txtbloodgroup.setText("Blood Group : " + b.getBlood_r());
                holder.txtunit.setText("Units : " + b.getUnit_r());
                holder.txthospital.setText("Hospital : " + b.getHospital_r());
                holder.txtpatient.setText("Patient Name : " + b.getName_r());
                holder.txtcontact.setText("Contact : "+b.getContact_r());
                holder.txtdate.setText("Due date : " + b.getLast_r());
                holder.btna.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, EligibleDonorActivity.class);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String arrangement= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Requests").child(b.getId_r()).child("arrangement_r");
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.getValue().toString().equals("null")){
                                            mDatabase.setValue(arrangement).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(context, "Successfully arranged", Toast.LENGTH_SHORT).show();

                                                        i.putExtra("blood_group", b.getBlood_r());
                                                        i.putExtra("r_uid", b.getId_r());
                                                        v.getContext().startActivity(i);
                                                        notifyItemRangeChanged(position, getItemCount());

                                                    }else {
                                                        Toast.makeText(context, "Failed to Arrange!!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(context, "Sorry, This request is already arranged", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }, 500);
                    }
                });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
    LinearLayout linearLayout;
    TextView txtbloodgroup, txtunit,txthospital,txtpatient,txtdate,txtcontact;
    Button btna;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.linear_layout2);
            txtbloodgroup=itemView.findViewById(R.id.tvBloodGroup2);
            txtunit=itemView.findViewById(R.id.tvUnits);
            txthospital=itemView.findViewById(R.id.tvGender);
            txtdate=itemView.findViewById(R.id.tvDate);
            txtpatient=itemView.findViewById(R.id.tvPatientName);
            txtcontact=itemView.findViewById(R.id.tvPatientContact);
            btna=itemView.findViewById(R.id.btnarrangment);
        }
    }
}
