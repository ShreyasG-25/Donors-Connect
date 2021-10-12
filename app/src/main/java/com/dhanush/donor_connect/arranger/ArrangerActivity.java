package com.dhanush.donor_connect.arranger;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.dhanush.donor_connect.R;
import com.dhanush.donor_connect.arranger.uiArranger.feedback.HelpActivity;
import com.dhanush.donor_connect.arranger.uiArranger.home.update.UpdateActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;

public class ArrangerActivity extends AppCompatActivity {

    DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    AppCompatButton updateBtn,cancelBtn;
    Button logoutBtn;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arranger);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ArrangerActivity.this, HelpActivity.class);
                startActivity(i);
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_pending, R.id.nav_achievement,R.id.nav_arrangment)
                .setDrawerLayout(drawer)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.arranger, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            logout();
        }
    }

    public void viewNav(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        TextView navname=headerView.findViewById(R.id.nav_name);
        TextView navemail=headerView.findViewById(R.id.nav_email);
        ImageView navimg=headerView.findViewById(R.id.nav_img);

        FirebaseDatabase.getInstance().getReference("Arrangers/"+currentUser.getUid()+"/name_a").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String post = snapshot.getValue(String.class);
                navname.setText(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        navemail.setText(currentUser.getEmail());

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("img", Context.MODE_PRIVATE);
        File file = new File(directory, "profilePic.png");
        navimg.setImageDrawable(Drawable.createFromPath(file.toString()));

        updateBtn=headerView.findViewById(R.id.btnUpdate);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ArrangerActivity.this, UpdateActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout2){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    void logout(){
        AlertDialog.Builder builder=new AlertDialog.Builder(ArrangerActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_logout, null);
        builder.setView(customLayout);
        cancelBtn=customLayout.findViewById(R.id.btnCancel1);
        logoutBtn=customLayout.findViewById(R.id.btnLogout1);
        AlertDialog alert=builder.create();
        alert.show();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(ArrangerActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        viewNav();
    }
}