package com.dhanush.donor_connect.arranger.uiArranger.home.eligible;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dhanush.donor_connect.R;
import com.dhanush.donor_connect.donorPack.donor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EligibleDonorAdapter extends RecyclerView.Adapter<EligibleDonorAdapter.MyViewHolder>{
    private final List<donor> lisData;

    public EligibleDonorAdapter(List<donor> lisData) {
        this.lisData = lisData;
    }
    Date c = Calendar.getInstance().getTime();
    int thisYear= c.getYear()+1900;
    int thisMonth= c.getMonth()+1;
    int yob,yod,mod;
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View listItem=inflater.inflate(R.layout.eligible_arranger_layout,parent,false);
        MyViewHolder holder=new MyViewHolder(listItem);
        context=parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        donor d=lisData.get(position);

        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(d.dob_d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        yob=date1.getYear()+1900;
        Date date2=null;
        try {
            date2=new SimpleDateFormat("dd-MM-yyyy").parse(d.last_d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        yod=date2.getYear()+1900;
        mod=date2.getMonth()+1;
        String lastDonated="";
        if(thisYear>yod+1){
            lastDonated="12+";
        }
        else if(thisYear==yod+1 && thisMonth>mod){
            lastDonated="12+";
        }
        else if(thisYear==yod+1){
            int temp=12+thisMonth;
            lastDonated=(temp-mod)+"";
        }
        else{
            lastDonated=(thisMonth-mod)+"";
        }

        holder.txtname.setText("Name : "+d.fullname_d);
        holder.txtgender.setText("Gender : "+d.gender_d);
        holder.txtdate.setText("Last Donation : "+lastDonated+ " month ago");
        holder.txtlocality.setText("Locality : "+d.city_d);
        holder.txtage.setText("Age : "+ (thisYear-yob));
        holder.txtbloodgroup.setText("Blood Group : "+d.bloodtype_d);
        holder.txtcontact.setText("Contact : "+d.phone_d);

        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i=new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:"+d.phone_d));
                    v.getContext().startActivity(i);
                }
        });

        holder.msgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+d.phone_d));
                i.putExtra("sms_body","This message is from Donor Connect.");
                v.getContext().startActivity(i);
            }
        });
        }


    @Override
    public int getItemCount() {
        return lisData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView txtbloodgroup,txtname,txtage,txtdate,txtlocality,txtgender,txtcontact;
        Button callbtn,msgbtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.linear_layout_donor);
            txtbloodgroup=itemView.findViewById(R.id.tvNoUnits);
            txtname=itemView.findViewById(R.id.tvReqBloodGroup);
            txtage=itemView.findViewById(R.id.tvAge);
            txtlocality=itemView.findViewById(R.id.tvPatientName);
            txtdate=itemView.findViewById(R.id.tvLastDonation);
            txtgender=itemView.findViewById(R.id.tvGender);
            txtcontact=itemView.findViewById(R.id.tvContact);
            callbtn=itemView.findViewById(R.id.btnCall);
            msgbtn=itemView.findViewById(R.id.btnMessage);
        }
    }
}