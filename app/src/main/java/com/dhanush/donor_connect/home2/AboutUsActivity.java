package com.dhanush.donor_connect.home2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhanush.donor_connect.R;
import com.dhanush.donor_connect.arranger.ArrangerActivity;
import com.dhanush.donor_connect.arranger.uiArranger.feedback.HelpActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView img1,img2,img3;

    String url1="https://media-exp1.licdn.com/dms/image/C4E03AQFQ4DNtXR7UrQ/profile-displayphoto-shrink_200_200/0/1621839702796?e=1634169600&v=beta&t=Yzl_ZoIUFC94zUf0FHQ_F4VcFoNoQ28gpZTIAkkx6pg";
    String url2="https://media-exp1.licdn.com/dms/image/C4E03AQF8SBZ3sdAbrg/profile-displayphoto-shrink_200_200/0/1622781446592?e=1634169600&v=beta&t=9I2wglsMPJnkU1SPUrFp2SkVRsix3cClvmfU3Kj-7S4";
    String url3="https://media-exp1.licdn.com/dms/image/C5603AQHyZTtMO3boPA/profile-displayphoto-shrink_200_200/0/1622695459930?e=1634169600&v=beta&t=RBKOJwjCcsihir3v_kFlYkl9I8DXWREP3F693rx2DDk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        this.setTitle("Team Code Brigade");
        img1=findViewById(R.id.imgAU1);
        img2=findViewById(R.id.imgAU2);
        img3=findViewById(R.id.imgAU3);

        new DownloadImageTask(img1)
                .execute(url1);
        new DownloadImageTask(img2)
                .execute(url2);
        new DownloadImageTask(img3)
                .execute(url3);

        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AboutUsActivity.this, HelpActivity.class);
                startActivity(i);
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bm = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bm = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return bm;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnk1:
                Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://in.linkedin.com/in/shreyas-g-2000"));
                startActivity(i1);
                break;
            case R.id.lnk2:
                Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://in.linkedin.com/in/dhanush-kumar-u"));
                startActivity(i2);
                break;
            case R.id.lnk3:
                Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://in.linkedin.com/in/shravansherigar99"));
                startActivity(i3);
                break;
        }
    }
}