package com.dhanush.donor_connect.arranger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.dhanush.donor_connect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth2;

    EditText emailtxt,passwordtxt;
    Button loginbtn, gotosignupbtn;
    CheckBox checkBox;
    SharedPreferences pref;
    TextView forgottxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailtxt=findViewById(R.id.etEmail);
        passwordtxt=findViewById(R.id.etPassword);
        loginbtn=findViewById(R.id.btnLogin);
        gotosignupbtn=findViewById(R.id.btnSignup);
        checkBox=findViewById(R.id.checkBox);
        pref=getSharedPreferences("arranger",MODE_PRIVATE);
        String temp=pref.getString("email",null);
        if(temp!=null){
            checkBox.setChecked(true);
            checkB();
        }
        loginbtn.setOnClickListener(this);
        gotosignupbtn.setOnClickListener(this);
        mAuth2=FirebaseAuth.getInstance();
        forgottxt=findViewById(R.id.txtForgot);
        forgottxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forget();
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSignup:
                Intent signupIntet = new Intent(this, RegistrationActivity.class);
                startActivity(signupIntet);
                finish();
                break;
            case R.id.btnLogin:
                login();
                break;
        }
    }

    private void login() {
        String email = emailtxt.getText().toString().trim();
        String password = passwordtxt.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailtxt.setError("Please provide valid email");
            emailtxt.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordtxt.setError("Password is required");
            passwordtxt.requestFocus();
            return;
        }

    mAuth2.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(LoginActivity.this, ArrangerActivity.class);
                startActivity(loginIntent);
                remember();
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Failed to Login, Check email & password", Toast.LENGTH_LONG).show();
            }
        }
    });

    }

    void remember(){
        if(checkBox.isChecked()){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("email",emailtxt.getText().toString());
            editor.putString("pass",passwordtxt.getText().toString());
            editor.apply();
        }else if(!checkBox.isChecked()){
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
        }
    }

    void checkB(){
        String t1=pref.getString("email",null);
        String t2=pref.getString("pass",null);
        emailtxt.setText(t1);
        passwordtxt.setText(t2);
    }

    void forget(){
        final String[] email = {emailtxt.getText().toString().trim()};
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        final EditText editxt = new EditText(this);
        editxt.setText(email[0]);
        builder.setTitle("Enter the Email")
                .setMessage("We will email you a link to reset your password.")
                .setView(editxt)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String temp = editxt.getText().toString();
                        mAuth2.sendPasswordResetEmail(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Request sent, Please check your Email.", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Failed!! Try again", Toast.LENGTH_SHORT).show();
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