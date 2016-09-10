package radio.buetian.org.buetradio.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import radio.buetian.org.buetradio.R;

public class RequestActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    EditText nameField,batchField,requestField;
    Button send;
    FirebaseAuth mAuth;
    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mAuth=FirebaseAuth.getInstance();
        nameField= (EditText) findViewById(R.id.eName);
        batchField= (EditText) findViewById(R.id.eBatch);
        requestField= (EditText) findViewById(R.id.eRequest);
        send= (Button) findViewById(R.id.bSend);
        if(mAuth.getCurrentUser()!=null) {
            nameField.setText(mAuth.getCurrentUser().getDisplayName());
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameField.getText().equals("")||requestField.getText().equals("")||nameField.getText()==null||requestField.getText()==null){
                    Toast.makeText(RequestActivity.this, "Name or request can't be empty.", Toast.LENGTH_SHORT).show();
                }
                else {
                    mRef = new Firebase("https://buetradio-865f1.firebaseio.com/requests");
                    mRef.push().setValue("(" + nameField.getText() + "," + batchField.getText() + "," + requestField.getText() + ")", new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError == null) {
                                Toast.makeText(RequestActivity.this, "Request Submitted Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(RequestActivity.this, "Request can't be submitted!Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
