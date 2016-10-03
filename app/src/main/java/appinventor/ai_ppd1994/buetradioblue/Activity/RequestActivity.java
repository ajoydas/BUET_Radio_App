package appinventor.ai_ppd1994.buetradioblue.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import appinventor.ai_ppd1994.buetradioblue.R;

public class RequestActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    EditText nameField,batchField,requestField;
    Button send;
    FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private Button view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("requests");

        nameField= (EditText) findViewById(R.id.eName);
        batchField= (EditText) findViewById(R.id.eBatch);
        requestField= (EditText) findViewById(R.id.eRequest);
        send= (Button) findViewById(R.id.bSend);
        view=(Button) findViewById(R.id.bView);
        if(mAuth.getCurrentUser().getEmail()!=null) {

            if (mAuth.getCurrentUser().getEmail().equals("ajoydas1996@gmail.com")) {
                view.setVisibility(View.VISIBLE);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RequestActivity.this, NotificationActivity.class);
                        intent.putExtra("From", "Requests");
                        finish();
                        startActivity(intent);
                    }
                });
            } else {
                view.setVisibility(View.GONE);
            }
        }
        else {
            view.setVisibility(View.GONE);
        }
        nameField.setEnabled(false);
        if(mAuth.getCurrentUser()!=null) {
            nameField.setText(mAuth.getCurrentUser().getDisplayName());
        }

        nameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        batchField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        requestField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameField.getText().toString().equals("")||requestField.getText().toString().equals("")||nameField.getText()==null||requestField.getText()==null){
                    Toast.makeText(RequestActivity.this, "Name or request can't be empty.", Toast.LENGTH_SHORT).show();
                }
                else {
                    //mRef = new Firebase("https://buetradio-865f1.firebaseio.com/requests");

                    ref.push().setValue("(" + nameField.getText() + "," + batchField.getText() + "," + requestField.getText() + ")", new DatabaseReference.CompletionListener()
                    {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(RequestActivity.this, "Request Submitted Successfully", Toast.LENGTH_SHORT).show();
                                requestField.setText("");
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


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
