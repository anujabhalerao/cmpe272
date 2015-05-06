package com.example.vinothbaalaji.pollencarefinal;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


public class LoginActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Button login;
    boolean mSignIn;
    boolean mSignInClicked;
    private static final int RC_SIGN_IN = 0;
    private boolean mIntentInProgress;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.email_sign_in_button){
            if(!mGoogleApiClient.isConnected()){
                mGoogleApiClient.connect();
            }
            else if(mGoogleApiClient.isConnected()){
                try{
                    if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                        String personName = currentPerson.getDisplayName();
                        MainActivity.lbl_guest.setText(personName);
                    }
                }
                catch(Exception e){
                }
            }
                mSignIn = true;
                mGoogleApiClient.connect();
        }
    }
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

        if (requestCode == RC_SIGN_IN) {
            if (responseCode == RESULT_OK) {
                Intent device = new Intent(LoginActivity.this,DevicesActivity.class);
                startActivity(device);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                mSignInClicked = false;
            }
            else if (responseCode == RESULT_CANCELED){
                Intent main = new Intent(LoginActivity.this,MainActivity.class);
                mGoogleApiClient.disconnect();
            }
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.reconnect();
            }
        }
    }

    protected void onStart() {
        super.onStart();
        if(!mGoogleApiClient.isConnected())
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIntentInProgress && connectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }

    }
}
