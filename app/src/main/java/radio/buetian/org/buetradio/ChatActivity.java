package radio.buetian.org.buetradio;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ChatActivity extends AppCompatActivity {

    private Firebase ref;
    private Button comment;
    private EditText write;
    FirebaseAuth mAuth;
    private int flag;
    private String profilePic;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        profilePic=getIntent().getExtras().getString("Photo");
        int flag=0;
        mAuth=FirebaseAuth.getInstance();

        comment = (Button) findViewById(R.id.bComment);
        write = (EditText) findViewById(R.id.eComment);


        FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder> adapter;

        ref = new Firebase("https://buetradio-865f1.firebaseio.com/chat");
        //Query query=new Firebase("https://buetradio-865f1.firebaseio.com/chatroom");




        RecyclerView recycler = (RecyclerView) findViewById(R.id.messages_recycler);
        recycler.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder>(ChatMessage.class, R.layout.message,ChatMessageViewHolder.class,ref) {
            @Override
            protected void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, ChatMessage chatMessage, int i) {
                chatMessageViewHolder.nameText.setText(chatMessage.getUser());
                chatMessageViewHolder.messageText.setText(chatMessage.getMessage());
                //chatMessageViewHolder.photo.setImageBitmap(StringToBitMap(chatMessage.getPhotoUrl()));
                chatMessageViewHolder.photo.setImageBitmap(getImageBitmap(chatMessage.getPhotoUrl()));

            }

        };
        recycler.setAdapter(adapter);

        recycler.scrollToPosition(recycler.getAdapter().getItemCount()-1);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage chat=new ChatMessage();
                chat.setUser(mAuth.getCurrentUser().getDisplayName());
                chat.setMessage(write.getText().toString());
                chat.setPhotoUrl(mAuth.getCurrentUser().getPhotoUrl().toString());
                ref.push().setValue(chat);
            }
        });
    }
    Bitmap bm= null;

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            progressDialog=ProgressDialog.show(ChatActivity.this, "Refreshing......","Please wait...",true,true);
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
        return bm;
    }

   /* private Bitmap getImageBitmap(String url) {
        //Bitmap bm = null;
        new LoadImage(url).execute();
        return bm;
    }
    private class LoadImage extends AsyncTask

    {
        String url=null;

        public LoadImage(String url) {
            this.url = url;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
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
                System.out.println("Net Error!");
            }
            return null;
        }
    }*/

    private static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView nameText;
        ImageView photo;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView)itemView.findViewById(R.id.user);
            messageText = (TextView) itemView.findViewById(R.id.message);
            photo= (ImageView) itemView.findViewById(R.id.iPhoto);
        }
    }

    //String to Bitmap
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
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
}
