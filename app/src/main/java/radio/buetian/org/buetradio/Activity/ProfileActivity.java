package radio.buetian.org.buetradio.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import radio.buetian.org.buetradio.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView name,email;
    ImageView imageView;
    Button logout,chat;
    FirebaseAuth firebase;
    private Bitmap bm;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        name = (TextView) findViewById(R.id.tName);
        email = (TextView) findViewById(R.id.tEmail);
        imageView = (ImageView) findViewById(R.id.image);
        logout = (Button) findViewById(R.id.bLogout);
        chat = (Button) findViewById(R.id.bChat);
        chat.setEnabled(false);

        firebase = FirebaseAuth.getInstance();

        if(firebase.getCurrentUser()!=null) {
            name.setText(firebase.getCurrentUser().getDisplayName());
            email.setText(firebase.getCurrentUser().getEmail());

            new LoadImage().execute();


            System.out.println(firebase.getCurrentUser().getPhotoUrl());
        }

        logout.setOnClickListener(this);
        chat.setOnClickListener(this);

    }



    private class LoadImage extends AsyncTask
    {


        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                bm = getImageBitmap(firebase.getCurrentUser().getPhotoUrl().toString());
            }
            catch (Exception e)
            {
                System.out.println("Profile pic fetch error.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            imageView.setImageBitmap(bm);
            chat.setEnabled(true);
        }
    }

    //Bitmap to string converter
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            //Toast.makeText(this,"Network Error",Toast.LENGTH_SHORT).show();
            bm=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.ic_google);
        }

        return bm;
    }

    @Override
    public void onClick(View view) {
        if(view==logout)
        {
            firebase.signOut();
            Intent intent=new Intent(this,SignInActivity.class);
            intent.putExtra("From","Signin");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK );
            finish();
            startActivity(intent);
        }
        if(view==chat)
        {
            Intent intent=new Intent(this,ChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK );
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK );
        finish();
        startActivity(intent);
    }
}
