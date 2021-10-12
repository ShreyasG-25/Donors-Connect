package com.dhanush.donor_connect.arranger.uiArranger.home.update;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.dhanush.donor_connect.R;
import com.dhanush.donor_connect.arranger.arrangerRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class UpdateActivity extends AppCompatActivity {

    private static final int PICK_IMAGE2 = 201;
    private static final int TAKE_IMAGE2 = 202;
    Bitmap bmp=null;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    SharedPreferences pref;

    ImageView imgview;
    EditText tname,temail,tnumber,tnumber2,taddress,tpin,tnewpass,tconfirmpass;
    Spinner cityspn,organizationspn;
    Button browsebtn,takebtn,savebtn1,savebtn2;

    String[] orgs,cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        pref=getSharedPreferences("arranger",MODE_PRIVATE);

        tname=findViewById(R.id.etName2);
        temail=findViewById(R.id.etEmail2);
        tnumber=findViewById(R.id.etContact2);
        tnumber2=findViewById(R.id.etAlternateContact2);
        taddress=findViewById(R.id.etAddress2);
        tpin=findViewById(R.id.etPincode2);
        tnewpass=findViewById(R.id.upNewPassword);
        tconfirmpass=findViewById(R.id.upConfirmPassword);
        cityspn=findViewById(R.id.sCity2);
        organizationspn=findViewById(R.id.sOrganization2);

        cities = new String[]{"Bantwal", "Kasargod", "Konaje", "Mangalore", "Moodabidre", "Puttur", "Surathkal", "Udupi", "Ullal", "Vamanjoor", "Vitla"};
        orgs = new String[]{"BJP", "Bajarangadal", "Congress", "HYS", "Jagarana Vedhike", "Lions club", "PFI", "RSS", "Rotery club", "SDPI", "VHP", "Vishwa Ganigara Chawadi", "Yuva Shakthi", "Self Motivated", "Other"};

        ArrayAdapter city=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,cities);
        ArrayAdapter orgsadapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,orgs);

        cityspn.setAdapter(city);
        organizationspn.setAdapter(orgsadapter);

        imgview=findViewById(R.id.imgView2);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("img", Context.MODE_PRIVATE);
        File file = new File(directory, "profilePic.png");
        imgview.setImageDrawable(Drawable.createFromPath(file.toString()));
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(UpdateActivity.this);
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_image, null);
                builder.setView(customLayout);
                takebtn=customLayout.findViewById(R.id.btnTakePicture);
                browsebtn=customLayout.findViewById(R.id.btnBrowsePic);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();
                takebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent,TAKE_IMAGE2);
                        alert.cancel();
                    }
                });
                browsebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browseIntent=new Intent();
                        browseIntent.setType("image/*");
                        browseIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(browseIntent,PICK_IMAGE2);
                        alert.cancel();
                    }
                });
            }
        });

        read();
        savebtn1=findViewById(R.id.btnSave1);
        savebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(UpdateActivity.this);
                builder.setTitle("Confirm Action")
                        .setMessage("Are you sure you want to change profile?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateInfo();
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
        savebtn2=findViewById(R.id.btnSave2);
        savebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(UpdateActivity.this);
                builder.setTitle("Confirm Action")
                        .setMessage("Are you sure you want to change password?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updatePass();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (requestCode){
                case PICK_IMAGE2:
                    try {
                        bmp= MediaStore.Images.Media.getBitmap(UpdateActivity.this.getContentResolver(),data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TAKE_IMAGE2:
                    bmp= (Bitmap) data.getExtras().get("data");
                    break;
            }
            imgview.setImageBitmap(bmp);
        }
    }

    void imageStore(){
        //mauth
        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File directory= contextWrapper.getDir("img", Context.MODE_PRIVATE);
        File file=new File(directory,"profilePic.png");

            FileOutputStream fos=null;
            try {
                fos=new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG,100,fos);
                fos.flush();
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }

    }

    void read(){
        FirebaseDatabase.getInstance().getReference("Arrangers/"+currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                                arrangerRequest AR = snapshot.getValue(arrangerRequest.class);
                                tname.setText(AR.name_a);
                                temail.setText(AR.email_a);
                                tnumber.setText(AR.contact_a);
                                tnumber2.setText(AR.contact2_a);
                                taddress.setText(AR.address_a);
                                tpin.setText(AR.pin_a);
                                cityspn.setSelection(Arrays.asList(cities).indexOf(AR.city_a));
                                organizationspn.setSelection(Arrays.asList(orgs).indexOf(AR.organization_a));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void updateInfo(){
        String name = tname.getText().toString().trim();
        String number = tnumber.getText().toString().trim();
        String number2 = tnumber2.getText().toString().trim();
        String email = temail.getText().toString().trim();
        String address = taddress.getText().toString().trim();
        String pin = tpin.getText().toString().trim();
        String organization=organizationspn.getSelectedItem().toString();
        String city=cityspn.getSelectedItem().toString();

        if(name.isEmpty()){
            tname.setError("Name is required");
            tname.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            temail.setError("Please provide valid email");
            temail.requestFocus();
            return;
        }
        if(number.isEmpty()){
            tnumber.setError("Contact Number is required");
            tnumber.requestFocus();
            return;
        }

        currentUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    String temp=pref.getString("email",null);
                    if(temp!=null){
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email",email);
                        editor.apply();
                    }

                    arrangerRequest arranger_data=new arrangerRequest(name,email,number,number2,address,city,pin,organization);
                    FirebaseDatabase.getInstance().getReference("Arrangers/"+currentUser.getUid())
                            .setValue(arranger_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UpdateActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                if(bmp!=null){
                                    imageStore();}
                                finish();
                            }else {
                                Toast.makeText(UpdateActivity.this, "Failed to update!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(UpdateActivity.this, "Failed to update!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void updatePass(){
        String password =tnewpass.getText().toString().trim();
        String confirmpass =tconfirmpass.getText().toString().trim();
        if(password.isEmpty()){
            tnewpass.setError("Password is required");
            tnewpass.requestFocus();
            return;
        }
        if(password.length()<8){
            tnewpass.setError("Minimum password length should be 8 characters");
            tnewpass.requestFocus();
            return;
        }
        if(!confirmpass.equals(password)){
            tconfirmpass.setError("Password Not matching");
            tconfirmpass.requestFocus();
            return;
        }
        currentUser.updatePassword(confirmpass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    String temp=pref.getString("pass",null);
                    if(temp!=null){
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("pass",confirmpass);
                        editor.apply();
                    }
                    Toast.makeText(UpdateActivity.this, "Password has been successfully changed", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(UpdateActivity.this, "Failed to change password!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}