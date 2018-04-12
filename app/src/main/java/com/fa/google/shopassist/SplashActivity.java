package com.fa.google.shopassist;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.fa.google.shopassist.ble.BLEListener;
import com.fa.google.shopassist.ble.BLEManager;
import com.fa.google.shopassist.globals.AppState;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class SplashActivity extends ActionBarActivity implements BLEListener {

    private ImageButton btnSplash;
    TextView textTitle;

    android.os.Handler HContinue;

    Runnable RContinue;

    private boolean bAnimateIn=true;

   // private BLEManager BLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        setContentView(R.layout.activity_splash);


        btnSplash=(ImageButton)findViewById(R.id.btn_splash);
        this.textTitle=(TextView)findViewById(R.id.app_name);
       
        this.btnSplash.setVisibility(View.INVISIBLE);
        this.textTitle.setVisibility(View.INVISIBLE);


        if(AppState.getInstance().bAnimateSplash) {
            Runnable R = new Runnable() {
                @Override
                public void run() {
                    animateIn();
                }
            };
            android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(R, 1000);
        }
        else{
            Runnable R = new Runnable() {
                @Override
                public void run() {
                    launch(null);
                }
            };
            android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(R, 1000);
        }
        HContinue= new android.os.Handler();
        RContinue=new Runnable() {
            @Override
            public void run() {
                launch(null);
            }
        };
        HContinue.postDelayed(RContinue,3800);



    }


    public  void animateIn(){

        AnimationSet animScale= (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.intro_scale_in);
        Animation animFade= AnimationUtils.loadAnimation(this,R.anim.intro_slide_up_in);
        Animation animFade2= AnimationUtils.loadAnimation(this,R.anim.intro_slide_up_in);
        Animation animFadeSlow= new AlphaAnimation(0,1);
        animFadeSlow.setDuration(800);

        animFade.setStartOffset(500);
        animFade2.setStartOffset(700);
        animFadeSlow.setStartOffset(900);

        animScale.setInterpolator(new AnticipateOvershootInterpolator(.85f));
        animFade.setInterpolator(new DecelerateInterpolator(2));
        animFade2.setInterpolator(new DecelerateInterpolator(2));
        animFadeSlow.setInterpolator(new DecelerateInterpolator(2));

        this.btnSplash.startAnimation(animScale);
        this.textTitle.startAnimation(animFade);

        this.btnSplash.setVisibility(View.VISIBLE);
        this.textTitle.setVisibility(View.VISIBLE);

        bAnimateIn=false;
        AppState.getInstance().bAnimateSplash=false;//dont animate in after first time
    }


    public void launch(View v) {

        if(HContinue!=null && RContinue!=null)
            HContinue.removeCallbacks(RContinue);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.abc_fade_in, R.anim.abc_fade_out).toBundle();
        ActivityCompat.startActivity(this, intent, options);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_onboarding, menu);

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
    protected void onStop() {
        super.onStop();

        if(HContinue!=null && RContinue!=null)
            HContinue.removeCallbacks(RContinue);



    }

    @Override
    protected void onDestroy() {

        super.onDestroy();



//        AppState.getInstance().BLE.onStop();
//        AppState.getInstance().BLE.onDestroy();

    }



    @Override
    public void onChangeRegion() {

    }

    @Override
    public  void onChangeLocation(){


    }

    @Override
    protected  void onStart(){
        super.onStart();
//        AppState.getInstance().BLE.onStart();
    }

    //Activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

//        for(ActivityListener listener : this.activityListeners)
//            listener.onResult(requestCode,resultCode,data);


    }

    @Override
    protected  void onResume() {

        super.onResume();

        if(bAnimateIn && AppState.getInstance().bAnimateSplash)
            HContinue.postDelayed(RContinue, 5000);
        else {
            this.btnSplash.setVisibility(View.VISIBLE);
            this.textTitle.setVisibility(View.VISIBLE);

            HContinue.postDelayed(RContinue, 1000);
        }

    }

    @Override
    public void onBackPressed() {

        if(HContinue!=null && RContinue!=null)
            HContinue.removeCallbacks(RContinue);

        super.onBackPressed();

    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        if(intent.getExtras()!=null &&  (boolean)intent.getExtras().get("SkipIntro")==true){

            Log.d("TAG","SKIP");

            this.launch(null);
        }

    }



}

