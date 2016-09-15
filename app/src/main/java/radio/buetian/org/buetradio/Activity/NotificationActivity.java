package radio.buetian.org.buetradio.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import radio.buetian.org.buetradio.Fragment.FragmentDrawerNotification;
import radio.buetian.org.buetradio.R;

public class NotificationActivity extends AppCompatActivity {
    TextView message,header;
    Button option;
    private Toolbar mToolbar;
    String from;
    private FragmentDrawerNotification mDrawerFragment;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        setupDrawer();
        mAuth = FirebaseAuth.getInstance();
        message = (TextView) findViewById(R.id.tMessage);
        header = (TextView) findViewById(R.id.tHeader);
        option= (Button) findViewById(R.id.bOption);

        database = FirebaseDatabase.getInstance();


        from=getIntent().getExtras().getString("From");
        if(from.equals("Notification")) {
            if (getIntent().getExtras() != null) {
                header.setText("New Notification!");
                message.setText(getIntent().getExtras().getString("Message"));
            }
            option.setVisibility(View.INVISIBLE);
        }
        else if(from.equals("Events"))
        {
            ref = database.getReference("events");
            header.setText("Events: ");
            message.setText("");
            //System.out.println(mAuth.getCurrentUser().getEmail());
            //System.out.println("Events Auth"+ref.getAuth());
            //ref = new Firebase("https://buetradio-865f1.firebaseio.com/events");
            if(mAuth.getCurrentUser()!=null) {
                if (mAuth.getCurrentUser().getEmail().equals("ajoydas1996@gmail.com")) {
                    option.setVisibility(View.VISIBLE);
                    option.setText("Edit");
                    option.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Todo edit
                            finish();
                            startActivity(new Intent(NotificationActivity.this,EditEvents.class));

                        }
                    });
                }
                else {
                    option.setVisibility(View.INVISIBLE);
                }
            }
            else {
                option.setVisibility(View.INVISIBLE);
            }

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String data =dataSnapshot.getValue(String.class);
                    message.setText(data);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else if(from.equals("Info"))
        {
            option.setVisibility(View.GONE);
            header.setText("Info: ");
            message.setText("Buet Radio\n Buet Radio\nBuet Radio\n");

        }
        else if(from.equals("Requests"))
        {
            header.setText("Requests: ");
            message.setText("");
            ref = database.getReference("requests");
            //System.out.println(mAuth.getCurrentUser().getEmail());
            //System.out.println("Events Auth"+ref.getAuth());
            //ref = new Firebase("https://buetradio-865f1.firebaseio.com/events");
            if(mAuth.getCurrentUser().getEmail().equals("ajoydas1996@gmail.com"))
            {
                option.setVisibility(View.VISIBLE);
                option.setText("Clear All");
                option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Todo edit
                    }
                });
            }
            else
            {
                option.setVisibility(View.GONE);
            }

            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    message.append("\n");
                    message.append(dataSnapshot.getValue().toString());
                    Toast.makeText(getApplicationContext(),"New Request added.",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Toast.makeText(getApplicationContext(),"Request deleted.Please refresh.",Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ref.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null)
                            {
                                Toast.makeText(getApplicationContext(),"Deleted Successfully.Please Refresh",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Fail to delete.Try again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }

    }

    private void setupDrawer() {
//        mToolbar = (Toolbar) findViewById(R.id.app_bar);
//        mContainerToolbar = (ViewGroup) findViewById(R.id.container_app_bar);
        //set the Toolbar as ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setup the NavigationDrawer
        mDrawerFragment = (FragmentDrawerNotification)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void onDrawerSlide(float slideOffset) {

    }
    public void onDrawerItemClicked(int index) {
        if (index == 0) {

            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("From","Signin");
                finish();
                startActivity(intent);
            }
        }else if(index==1)
        {
            Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
            intent.putExtra("Stream","http://87.117.217.103:38164");
            intent.putExtra("Player","Channel 1");
            finish();
            startActivity(intent);
        }
        else if(index==2)
        {
            Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
            intent.putExtra("Stream","http://87.117.217.103:38164");
            intent.putExtra("Player","Channel 2");
            finish();
            startActivity(intent);
        }
        else if(index==3)
        {
            Intent intent=new Intent(getApplicationContext(),WebLoad.class);
            intent.putExtra("Url","https://soundcloud.com/buet-radio");
            finish();
            startActivity(intent);
        }
        else if(index==4)
        {
            Intent intent=new Intent(getApplicationContext(),WebLoad.class);
            intent.putExtra("Url","http://buetradio.com/archive.html");
            finish();
            startActivity(intent);
        }
        else if (index==5)
        {
            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                //finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("From","Chatroom");
                //finish();
                startActivity(intent);
            }
        }
        else if (index==6)
        {
            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(getApplicationContext(),RequestActivity.class);
                //finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("From","Request");
                //finish();
                startActivity(intent);
            }
        }
        else if (index==7)
        {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "appinventor.ai_ppd1994.buetradioblue")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "appinventor.ai_ppd1994.buetradioblue")));
            }
        }
    }
}
