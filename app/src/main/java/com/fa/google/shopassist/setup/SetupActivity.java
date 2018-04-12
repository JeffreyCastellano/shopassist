package com.fa.google.shopassist.setup;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.estimote.sdk.SystemRequirementsChecker;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.globals.AppState;

public class SetupActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.gsa_prefs), 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText txtFirstName = (EditText)findViewById(R.id.txt_first_name);
        txtFirstName.setText(sharedPreferences.getString("first_name", "Marc"));

        EditText txtLastName = (EditText)findViewById(R.id.txt_last_name);
        txtLastName.setText(sharedPreferences.getString("last_name", "Vanlerberghe"));

        EditText txtEmail = (EditText)findViewById(R.id.txt_email_name);
        txtEmail.setText(sharedPreferences.getString("email", "mvanlerberghe@gmail.com"));

        ImageButton btnWholeFoods=(ImageButton)findViewById(R.id.btn_setup_whole_foods);
        ImageButton btnTarget=(ImageButton)findViewById(R.id.btn_setup_target);

        btnWholeFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePrefs();

                AppState.getInstance().strLockZoneId="wholefoods";
                proceedToMain();

            }
        });

        btnTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePrefs();

                AppState.getInstance().strLockZoneId="target";
                proceedToMain();
            }
        });

    }


    @Override protected void onResume() {
        super.onResume();

//        if (SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
//
//        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            this.checkCamera();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkCamera (){
        if (
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},   123);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    public void savePrefs(){


        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.gsa_prefs), 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText txtFirstName = (EditText)findViewById(R.id.txt_first_name);
        String firstName = txtFirstName.getText().toString();

        EditText txtLastName = (EditText)findViewById(R.id.txt_last_name);
        String lastName = txtLastName.getText().toString();

        editor.putString("first_name", firstName);
        editor.putString("last_name", lastName);
        editor.putString("email", ((EditText) findViewById(R.id.txt_email_name)).getText().toString());
        editor.commit();

    }

    public void proceedToMain(){


        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        Bundle options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.abc_fade_in, R.anim.abc_fade_out).toBundle();
        startActivity(intent,options);
    }

    public void onSaveUserClicked(View v) {

        this.savePrefs();

       this.proceedToMain();
    }

}
