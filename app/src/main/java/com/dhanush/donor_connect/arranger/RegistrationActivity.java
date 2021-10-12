package com.dhanush.donor_connect.arranger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dhanush.donor_connect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 101;
    private static final int TAKE_IMAGE = 102;
    private FirebaseAuth mAuth;
    Bitmap bmp=null;

    Button btnRegister,browsebtn,takebtn;
    Spinner cityspn,organizationspn;
    EditText tname,temail,tnumber,tnumber2,taddress,tpin,tpass,tconfirmpass;
    ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_registration);

        tname=findViewById(R.id.etName);
        temail=findViewById(R.id.etEmail);
        tnumber=findViewById(R.id.etContact);
        tnumber2=findViewById(R.id.etAlternateContact);
        taddress=findViewById(R.id.etAddress);
        tpin=findViewById(R.id.etPincode);
        tpass=findViewById(R.id.etlPassword);
        tconfirmpass=findViewById(R.id.etlConfirmPassword);
        cityspn=findViewById(R.id.sCity);
        organizationspn=findViewById(R.id.sOrganization);

        String[] cities = {"Bantwal","Kasargod","Konaje","Mangalore","Moodabidre","Puttur","Surathkal","Udupi","Ullal","Vamanjoor","Vitla"};
        String[] orgs = {"Indian Red Cross Society","BJP","NSS","Congress","RSS","Jagarana Vedhike","Lions club","Bajarangadal","Rotery club","SDPI","VHP","Yuva Shakthi","Self Motivated","Other"};

        ArrayAdapter city=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,cities);
        ArrayAdapter orgsadapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,orgs);

        cityspn.setAdapter(city);
        organizationspn.setAdapter(orgsadapter);

        btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        //imagePermission

        imgview=findViewById(R.id.imgView);
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(RegistrationActivity.this);
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
                        startActivityForResult(cameraIntent,TAKE_IMAGE);
                        alert.cancel();
                    }
                });
                browsebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browseIntent=new Intent();
                        browseIntent.setType("image/*");
                        browseIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(browseIntent,PICK_IMAGE);
                        alert.cancel();
                    }
                });
            }
        });
    }

    public void register(){
        String name = tname.getText().toString().trim();
        String number = tnumber.getText().toString().trim();
        String number2 = tnumber2.getText().toString().trim();
        String email = temail.getText().toString().trim();
        String password =tpass.getText().toString().trim();
        String confirmpass =tconfirmpass.getText().toString().trim();
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
        } else if(number.length()!=10){
            tnumber.setError("Enter valid number");
            tnumber.requestFocus();
            return;
        }
        if(!number2.isEmpty()){
            if(number2.length()!=10){
                tnumber2.setError("Enter valid number");
                tnumber2.requestFocus();
                return;
            }
        }
        if(password.isEmpty()){
            tpass.setError("Password is required");
            tpass.requestFocus();
            return;
        }
        if(password.length()<8){
                tpass.setError("Minimum password length should be 8 characters");
                tpass.requestFocus();
                return;
        }
        if(!confirmpass.equals(password)){
                tconfirmpass.setError("Password Not matching");
                tconfirmpass.requestFocus();
                return;
        }

        mAuth.createUserWithEmailAndPassword(email,confirmpass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            arrangerRequest arranger_data=new arrangerRequest(name,email,number,number2,address,city,pin,organization);
                            FirebaseDatabase.getInstance().getReference("Arrangers")
                                    .setValue(arranger_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegistrationActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent loginIntent=new Intent(RegistrationActivity.this, LoginActivity.class);
                                        imageStore();
                                        startActivity(loginIntent);
                                        finish();

                                    }else {
                                        Toast.makeText(RegistrationActivity.this, "Failed to register!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegistrationActivity.this, "Failed to register!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (requestCode){
                case PICK_IMAGE:
                    try {
                        bmp= MediaStore.Images.Media.getBitmap(RegistrationActivity.this.getContentResolver(),data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TAKE_IMAGE:
                    bmp= (Bitmap) data.getExtras().get("data");
                    break;
            }
            imgview.setImageBitmap(bmp);
        }
    }

    void imageStore(){
        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File directory= contextWrapper.getDir("img", Context.MODE_PRIVATE);
        File file=new File(directory,"profilePic.png");
        if(!file.exists()){
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
    }
}