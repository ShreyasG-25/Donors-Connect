package com.dhanush.donor_connect.home2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.dhanush.donor_connect.R;
import com.dhanush.donor_connect.arranger.uiArranger.home.BloodRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FindDonorActivity extends AppCompatActivity implements View.OnClickListener {

    EditText datetxt, nametxt, numbertxt, bloodunittxt;
    Button btn;
    Spinner bloodSP,citySP,hostSP;
    DatePickerDialog dateDialog;
    Calendar calendar;

    String[] items = {"O -", "O +", "A +", "A -", "B +", "B -", "AB +", "AB -"};
    String[] cites = {"Bantwal", "Kasargod", "Konaje", "Mangalore", "Moodabidre", "Puttur", "Surathkal", "Udupi", "Ullal", "Vamanjoor", "Vitla"};
    String[] hospitals = {"AJ Hospital","KS Hegde Deralakatte","Yenepoya Deralakatte", "Indiana Pumpwell", "KMC Jyothi","KMC Attavara","Wenlock","Kanachur Deralakatte","Unity Falnir","Father muller Kankanady","City hospital Puttur"};

    Date c = Calendar.getInstance().getTime();
    int thisYear=c.getYear()+1900;
    int thisMonth=c.getMonth()+1;
    int thiday=c.getDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_donor);
        datetxt=findViewById(R.id.txtdate);
        nametxt=findViewById(R.id.ettxtpatientname);
        numbertxt=findViewById(R.id.etxtcontact);
        bloodunittxt=findViewById(R.id.etxtnumunits);
        bloodSP = findViewById(R.id.reqblood);
        citySP=findViewById(R.id.reqcity);
        hostSP=findViewById(R.id.reqhospital);
        ArrayAdapter aditems = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items);
        ArrayAdapter adcity = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, cites);
        ArrayAdapter adhos = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, hospitals);
        bloodSP.setAdapter(aditems);
        citySP.setAdapter(adcity);
        hostSP.setAdapter(adhos);

        datetxt = findViewById(R.id.txtdate);
        datetxt.setOnClickListener(this);
        calendar = Calendar.getInstance();
        dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                datetxt.setText(dateFormat.format(date));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        btn=findViewById(R.id.btnsubmit);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtdate:
                dateDialog.show();
                break;
            case R.id.btnsubmit:
                send();
                break;
        }
    }

    public void send(){
        String date = datetxt.getText().toString().trim();
        String name = nametxt.getText().toString().trim();
        String number = numbertxt.getText().toString().trim();
        String unit = bloodunittxt.getText().toString().trim();
        String hospital=hostSP.getSelectedItem().toString();
        String blood=bloodSP.getSelectedItem().toString();
        String city=citySP.getSelectedItem().toString();
        String ar="null".trim();

        if(name.isEmpty()){
            nametxt.setError("Name is required");
            nametxt.requestFocus();
            return;
        }
        if(number.isEmpty()){
            numbertxt.setError("Number is required");
            numbertxt.requestFocus();
            return;
        }
        else if(number.length()!=10){
            numbertxt.setError("Enter valid number");
            numbertxt.requestFocus();
            return;
        }

        if(unit.isEmpty() || Integer.parseInt(unit)<1){
            bloodunittxt.setError("Number of unit is required");
            bloodunittxt.requestFocus();
            return;
        }
        if(date.isEmpty()){
            datetxt.setError("Date is required");
            datetxt.requestFocus();
            return;
        }
        else{
            boolean flag=false;
            String[] a;
            a = date.split("-");
            if(thisYear>Integer.parseInt(a[2])){
                flag=true;
            }
            else if(thisYear==Integer.parseInt(a[2])){
                if(thisMonth>Integer.parseInt(a[1])){
                    flag=true;
                }
                else if(thisMonth==Integer.parseInt(a[1])){
                    if(thiday>Integer.parseInt(a[0])){
                        flag=true;
                    }
                }
            }
            if(flag){
                datetxt.setError("Invalid date");
                datetxt.requestFocus();
                return;
            }

        }

        DatabaseReference mDatabase;
        mDatabase =FirebaseDatabase.getInstance().getReference("Requests");
        String key_d=mDatabase.push().getKey();

        BloodRequest request_data=new BloodRequest(key_d,blood,unit,hospital,name,date,number,city,ar);
        assert key_d != null;
        mDatabase.child(key_d).setValue(request_data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(FindDonorActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(FindDonorActivity.this, "Failed to Send Request!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}