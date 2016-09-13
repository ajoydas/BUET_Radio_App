package radio.buetian.org.buetradio.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import radio.buetian.org.buetradio.Objects.ChatMessage;
import radio.buetian.org.buetradio.R;

public class ChatActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private Button comment;
    private EditText write;
    FirebaseAuth mAuth;
    private int flag;
    private String profilePic;
    private ProgressDialog progressDialog;
    private ListView listView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //profilePic=getIntent().getExtras().getString("Photo");
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        int flag=0;
        mAuth=FirebaseAuth.getInstance();

        comment = (Button) findViewById(R.id.bComment);
        write = (EditText) findViewById(R.id.eComment);


        FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder> adapter;

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("chatroom");

        //ref = new Firebase("https://buetradio-865f1.firebaseio.com/chatroom");
        //Query query=new Firebase("https://buetradio-865f1.firebaseio.com/chatroom");
        listView = (ListView) findViewById(R.id.chatList);
        ListAdapter ladapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message, ref)
        {

            protected void populateView(View view, ChatMessage chatMessage, int i) {
                TextView nameText = (TextView) view.findViewById(R.id.user);
                TextView messageText = (TextView) view.findViewById(R.id.message);
                //ImageView photo = (ImageView) view.findViewById(R.id.iPhoto);
                nameText.setText(chatMessage.getUser());
                messageText.setText(chatMessage.getMessage());
                //chatMessageViewHolder.photo.setImageBitmap(StringToBitMap(chatMessage.getPhotoUrl()));
                //photo.setImageBitmap(getImageBitmap(chatMessage.getPhotoUrl()));
            }
        };
        listView.setAdapter(ladapter);


/*        RecyclerView recycler = (RecyclerView) findViewById(R.id.messages_recycler);
        recycler.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);*/

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        //StrictMode.setThreadPolicy(policy);


/*        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder>(ChatMessage.class, R.layout.message,ChatMessageViewHolder.class,ref) {
            @Override
            protected void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, ChatMessage chatMessage, int i) {
                chatMessageViewHolder.nameText.setText(chatMessage.getUser());
                chatMessageViewHolder.messageText.setText(chatMessage.getMessage());
                //chatMessageViewHolder.photo.setImageBitmap(StringToBitMap(chatMessage.getPhotoUrl()));
                chatMessageViewHolder.photo.setImageBitmap(getImageBitmap(chatMessage.getPhotoUrl()));

            }

        };
        recycler.setAdapter(adapter);

        recycler.scrollToPosition(recycler.getAdapter().getItemCount()-1);*/

        write.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage chat=new ChatMessage();
                chat.setUser(mAuth.getCurrentUser().getDisplayName());
                chat.setMessage(write.getText().toString());
                //chat.setPhotoUrl(mAuth.getCurrentUser().getPhotoUrl().toString());
                ref.push().setValue(chat);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        //ImageView photo;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView)itemView.findViewById(R.id.user);
            messageText = (TextView) itemView.findViewById(R.id.message);
           // photo= (ImageView) itemView.findViewById(R.id.iPhoto);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,StartActivity.class);
        startActivity(intent);

    }
}
