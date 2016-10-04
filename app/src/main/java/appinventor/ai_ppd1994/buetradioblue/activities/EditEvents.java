package appinventor.ai_ppd1994.buetradioblue.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import appinventor.ai_ppd1994.buetradioblue.R;

public class EditEvents extends AppCompatActivity {
    Button publish;
    EditText events;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_events);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("events");

        publish= (Button) findViewById(R.id.bPublish);
        events= (EditText) findViewById(R.id.eEvent);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data =dataSnapshot.getValue(String.class);
                events.setText(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.setValue(events.getText().toString(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null)
                        {
                            Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Fail to add.Try again",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });
    }
}
