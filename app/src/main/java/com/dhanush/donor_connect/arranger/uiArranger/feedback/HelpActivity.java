package com.dhanush.donor_connect.arranger.uiArranger.feedback;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.dhanush.donor_connect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class HelpActivity extends AppCompatActivity {

    private EditText feedbacktxt,emailtxt;
    private Button sendbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Feedback");
        setContentView(R.layout.activity_help);
        feedbacktxt=findViewById(R.id.etFeedback);
        emailtxt=findViewById(R.id.etFeedEmail);
        sendbtn=findViewById(R.id.btnSend);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review=feedbacktxt.getText().toString().trim();
                String email=emailtxt.getText().toString().trim();
                if(review.isEmpty()){
                    feedbacktxt.setError("Review is required");
                    feedbacktxt.requestFocus();
                    return;
                }
                Feedback feedback=new Feedback(review,email);
                FirebaseDatabase.getInstance().getReference("Feedback")
                        .push().setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(HelpActivity.this, "Feedback Sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }
}