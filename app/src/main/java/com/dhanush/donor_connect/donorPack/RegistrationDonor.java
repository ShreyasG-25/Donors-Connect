package com.dhanush.donor_connect.donorPack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.dhanush.donor_connect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RegistrationDonor extends AppCompatActivity implements View.OnClickListener {

    EditText datetxt, nametxt, numbertxt, emailtxt, addresstxt, pintxt,lasttxt;
    DatePickerDialog dateDialog1,dateDialog2;
    Calendar calendar;
    Button register;
    Spinner bloodSP, citySP;
    RadioGroup radioGroup;
    RadioButton genderbtn;
    BottomNavigationView bottomNavigationView;

    String[] cites = {"Bantwal", "Kasargod", "Konaje", "Mangalore", "Moodabidre", "Puttur", "Surathkal", "Udupi", "Ullal", "Vamanjoor", "Vitla"};
    String[] items = {"O -", "O +", "A +", "A -", "B +", "B -", "AB +", "AB -"};

    Date c = Calendar.getInstance().getTime();
    int thisYear=c.getYear()+1900;
    int thisMonth=c.getMonth()+1;
    int thiday=c.getDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_donor);
        nametxt = findViewById(R.id.fullname);
        numbertxt = findViewById(R.id.mblno);
        emailtxt = findViewById(R.id.email);
        addresstxt = findViewById(R.id.address);
        pintxt = findViewById(R.id.pin);
        radioGroup=findViewById(R.id.rGroup);
        bloodSP = findViewById(R.id.bloodty);
        citySP = findViewById(R.id.city);
        ArrayAdapter aditems = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items);
        ArrayAdapter adcity = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, cites);
        bloodSP.setAdapter(aditems);
        citySP.setAdapter(adcity);

        datetxt = findViewById(R.id.btnDate);
        datetxt.setOnClickListener(this);
        calendar = Calendar.getInstance();
        dateDialog1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                datetxt.setText(dateFormat.format(date));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        lasttxt = findViewById(R.id.btnDate2);
        lasttxt.setOnClickListener(this);
        calendar = Calendar.getInstance();
        dateDialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                lasttxt.setText(dateFormat.format(date));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        register = findViewById(R.id.btnregister);
        register.setOnClickListener(this);
        bottomNavigationView= findViewById(R.id.bottom_navigation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDate:
                dateDialog1.show();
                break;
            case R.id.btnDate2:
                dateDialog2.show();
                break;
            case R.id.btnregister:
                store();
                break;
        }
    }

    public void store() {
        String date = datetxt.getText().toString().trim();
        String last=lasttxt.getText().toString().trim();
        String name = nametxt.getText().toString().trim();
        String number = numbertxt.getText().toString().trim();
        String email = emailtxt.getText().toString().trim();
        String address = addresstxt.getText().toString().trim();
        String pin = pintxt.getText().toString().trim();
        String blood=bloodSP.getSelectedItem().toString();
        String city=citySP.getSelectedItem().toString();
        genderbtn=radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
        String gender=genderbtn.getText().toString().trim();

        if(name.isEmpty()){
            nametxt.setError("Name is required");
            nametxt.requestFocus();
            return;
        }

        if(date.isEmpty()){
            datetxt.setError("DOB is required");
            datetxt.requestFocus();
            return;
        }else{
            String doby=date.substring(date.length()-4);
            Log.d("date ", "yob :"+doby+" ,current year :"+ thisYear);
            int age=thisYear-Integer.parseInt(doby);
            if(age<18){
                datetxt.setError("Donors should be above 18");
                datetxt.requestFocus();
                return;
            }
        }

        if(number.isEmpty()){
            numbertxt.setError("Contact number is required");
            numbertxt.requestFocus();
            return;
        }else if(number.length()!=10){
            numbertxt.setError("Enter valid number");
            numbertxt.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailtxt.setError("Email is required");
            emailtxt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtxt.setError("Please provide valid email");
            emailtxt.requestFocus();
            return;
        }

        if(last.isEmpty()){
            lasttxt.setError("Last blood donation date is required");
            lasttxt.requestFocus();
            return;
        }
        else
        {
            boolean flag=false;
            String[] a;
            a = last.split("-");
            if(thisYear<Integer.parseInt(a[2])){
                flag=true;
            }
            else if(thisYear==Integer.parseInt(a[2])){
                if(thisMonth<Integer.parseInt(a[1])){
                    flag=true;
                }
                else if(thisMonth==Integer.parseInt(a[1])){
                    if(thiday<Integer.parseInt(a[0])){
                        flag=true;
                    }
                }
            }
            if(flag){
                lasttxt.setError("Invalid date");
                lasttxt.requestFocus();
                return;
            }
        }

        donor donor_data=new donor(name,gender,date,blood,number,email,address,city,pin,last);
        FirebaseDatabase.getInstance().getReference("Donors")
                .push().setValue(donor_data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistrationDonor.this, "Registration completed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(RegistrationDonor.this, "Failed to register!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}