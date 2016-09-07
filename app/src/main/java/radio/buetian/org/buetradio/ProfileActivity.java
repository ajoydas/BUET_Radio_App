package radio.buetian.org.buetradio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView name,email;
    ImageView imageView;
    Button logout,chat;
    FirebaseAuth firebase;
    private Bitmap bm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView) findViewById(R.id.tName);
        email = (TextView) findViewById(R.id.tEmail);
        imageView = (ImageView) findViewById(R.id.image);
        logout = (Button) findViewById(R.id.bLogout);
        chat = (Button) findViewById(R.id.bChat);


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
            bm=getImageBitmap(firebase.getCurrentUser().getPhotoUrl().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            imageView.setImageBitmap(bm);
        }
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
            Toast.makeText(this,"Network Error",Toast.LENGTH_SHORT).show();
        }
        return bm;
    }

    @Override
    public void onClick(View view) {
        if(view==logout)
        {
            firebase.signOut();
            finish();
            startActivity(new Intent(this,SignIn.class));
        }
        if(view==chat)
        {

            finish();
            startActivity(new Intent(this,ChatActivity.class));
        }
    }
}
