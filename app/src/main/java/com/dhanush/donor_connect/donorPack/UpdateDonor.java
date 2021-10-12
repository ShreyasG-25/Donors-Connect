package com.dhanush.donor_connect.donorPack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.dhanush.donor_connect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UpdateDonor extends AppCompatActivity {

    EditText txEmail,txContact,txdate;
    Button btnUpd;
    DatePickerDialog date1;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donor);
        txEmail=findViewById(R.id.emailUpdate);
        txContact=findViewById(R.id.mblnoUpdate);
        txdate=findViewById(R.id.btnDate2Update);
        btnUpd=findViewById(R.id.btnregisterUpdate);
        calendar = Calendar.getInstance();
        txdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date1= new DatePickerDialog(UpdateDonor.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        txdate.setText(dateFormat.format(date));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                date1.show();
            }
        });
        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store();
            }
        });
    }

    void store(){
        String date = txdate.getText().toString().trim();
        String number = txContact.getText().toString().trim();
        String email = txEmail.getText().toString().trim();

        if(number.isEmpty()){
            txContact.setError("Contact number is required");
            txContact.requestFocus();
            return;
        }else if(number.length()!=10){
            txContact.setError("Enter valid number");
            txContact.requestFocus();
            return;
        }

        if(email.isEmpty()){
            txEmail.setError("Email is required");
            txEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txEmail.setError("Please provide valid email");
            txEmail.requestFocus();
            return;
        }

        if(date.isEmpty()){
            txdate.setError("Last blood donation date is required");
            txdate.requestFocus();
            return;
        }

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Donors");
        ref.orderByChild("email_d").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot requestsnapshot:snapshot.getChildren()){
                        donor b=requestsnapshot.getValue(donor.class);
                        if(b.phone_d.equals(number)){
                            ref.child(requestsnapshot.getKey()).child("last_d")
                                    .setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(UpdateDonor.this, "Updated last donation date successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(UpdateDonor.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else
                        {
                            Toast.makeText(UpdateDonor.this, "Enter Proper Contact number!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateDonor.this, "Enter Proper Email!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}