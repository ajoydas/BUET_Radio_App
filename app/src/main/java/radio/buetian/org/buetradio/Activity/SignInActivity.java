package radio.buetian.org.buetradio.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import radio.buetian.org.buetradio.Fragment.MainFragment;
import radio.buetian.org.buetradio.R;

public class SignInActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {
    private static final int RC_SIGN_IN = 1;
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Toolbar mToolbar;
    String redirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        redirect=getIntent().getExtras().getString("From");

/*        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        // If using in a fragment
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(SignInActivity.this, "Login Successfull!",
                        Toast.LENGTH_LONG).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("Facebook Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("Facebook Error");
            }


        });*/




        signInButton = (SignInButton) findViewById(R.id.bGoogle);
        mAuth = FirebaseAuth.getInstance();


        mAuthListener= new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    if (redirect.equals("Signin")||redirect.equals("Profile")) {
                        finish();
                        startActivity(new Intent(SignInActivity.this, ProfileActivity.class));
                    }
                    else if (redirect.equals("Chatroom")) {
                        finish();
                        startActivity(new Intent(SignInActivity.this, ChatActivity.class));
                    }
                    else if (redirect.equals("Request")) {
                        finish();
                        startActivity(new Intent(SignInActivity.this, RequestActivity.class));
                    }

                }
            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplication())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(SignInActivity.this, "Authentication Some failure",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(SignInActivity.this, "Login Successful.",
                                    Toast.LENGTH_SHORT).show();

                            // Enter your code after the Sign In complete
                            if(mAuth.getCurrentUser()!=null)
                            {
                                //startActivity(new Intent(SignInActivity.this,ProfileActivity.class));
                                if(mAuth.getCurrentUser()!=null) {
                                    if (redirect.equals("Signin")||redirect.equals("Profile")) {
                                        finish();
                                        startActivity(new Intent(SignInActivity.this, ProfileActivity.class));
                                    }
                                    else if (redirect.equals("Chatroom")) {
                                        finish();
                                        startActivity(new Intent(SignInActivity.this, ChatActivity.class));
                                    }
                                    else if (redirect.equals("Request")) {
                                        finish();
                                        startActivity(new Intent(SignInActivity.this, RequestActivity.class));
                                    }

                                }
                            }

                        }



                        // ...
                    }
                });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }




    /*private void handleFacebookAccessToken(AccessToken token) {

        System.out.println("Access "+token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignInActivity.this, "Authentication Successfull.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this,ProfileActivity.class));

                        }
                        // ...
                    }
                });
    }*/



}
