package radio.buetian.org.buetradio.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import radio.buetian.org.buetradio.R;

public class NotificationActivity extends AppCompatActivity {
    TextView message,header;
    private Toolbar mToolbar;
    String from;
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        message = (TextView) findViewById(R.id.tMessage);
        header = (TextView) findViewById(R.id.tHeader);

        from=getIntent().getExtras().getString("From");
        if(from.equals("Notification")) {
            if (getIntent().getExtras() != null) {
                header.setText("New Notification!");
                message.setText(getIntent().getExtras().getString("Message"));
            }
        }
        else if(from.equals("Events"))
        {
            header.setText("Events: ");
            message.setText("");
            ref = new Firebase("https://buetradio-865f1.firebaseio.com/events");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String data =dataSnapshot.getValue(String.class);
                    message.setText(data);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        else if(from.equals("Info"))
        {
            header.setText("Info: ");
            message.setText("Buet Radio\n Buet Radio\nBuet Radio\n");

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
