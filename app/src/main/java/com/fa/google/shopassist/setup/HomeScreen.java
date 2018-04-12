package com.fa.google.shopassist.setup;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.fa.google.shopassist.ActivityListener;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.SplashActivity;
import com.fa.google.shopassist.ble.BLEListener;
import com.fa.google.shopassist.globals.AppState;

import java.util.ArrayList;

public class HomeScreen extends ActionBarActivity implements BLEListener {

    protected RelativeLayout layoutPulldown;

    private float mPreviousY;

    float fRunnyVelY=0;

    // Activity Listeners
    public ArrayList<ActivityListener> activityListeners = new ArrayList<>();

    private boolean bNotified = false;

    boolean bChangedLocationSince=false;

    View vPulldownBG;



    RelativeLayout Container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(!AppState.getInstance().bInitialized) {
            AppState.getInstance().init(this);
            AppState.getInstance().BLE.onCreate(savedInstanceState);
            AppState.getInstance().BLE.listeners.add(this);

        }

        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_home_screen);

        layoutPulldown=(RelativeLayout)findViewById(R.id.layout_home_pulldown);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        layoutPulldown.measure(View.MeasureSpec.makeMeasureSpec(size.x,View.MeasureSpec.EXACTLY),0);

        layoutPulldown.setTranslationY(-layoutPulldown.getMeasuredHeight());


        vPulldownBG=(View)findViewById(R.id.img_home_pulldown_bg);

        vPulldownBG.setAlpha(0);

        Container= (RelativeLayout)findViewById(R.id.layout_pull_down_container);

        Container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                final float fPercent;

                float y = event.getY();

                switch (event.getAction()){


                    case MotionEvent.ACTION_MOVE:

                        float dy = y - mPreviousY;


                        float fTranslation=layoutPulldown.getTranslationY();

                        fTranslation+=dy;
                        if (fTranslation>0)
                            fTranslation=0;
                        else if (fTranslation<-layoutPulldown.getMeasuredHeight())
                            fTranslation=-layoutPulldown.getMeasuredHeight();

                        layoutPulldown.setTranslationY(fTranslation);

                        fPercent=fTranslation/-layoutPulldown.getMeasuredHeight();

                        vPulldownBG.setAlpha(1-fPercent);

                        fRunnyVelY=.8f*fRunnyVelY+(.2f*dy);

                        break;

                    case MotionEvent.ACTION_UP:

                        fPercent=layoutPulldown.getTranslationY()/-layoutPulldown.getMeasuredHeight();


                        //Log.d("TAG", "PER " +fRunnyVelY);


                        if(fRunnyVelY>15 || (fPercent<.5 && fRunnyVelY> -15))
                        {

                            ValueAnimator valueAnimator= ValueAnimator.ofFloat(layoutPulldown.getTranslationY(),0).setDuration(300);
                            valueAnimator.setInterpolator(new DecelerateInterpolator(2));
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    layoutPulldown.setTranslationY((float)animation.getAnimatedValue());
                                    vPulldownBG.setAlpha(1-(float)animation.getAnimatedValue()/-layoutPulldown.getMeasuredHeight());
                                }
                            });
                            valueAnimator.start();


                        }else
                        {
                            ValueAnimator valueAnimator= ValueAnimator.ofFloat(layoutPulldown.getTranslationY(),-layoutPulldown.getMeasuredHeight()).setDuration(300);
                            valueAnimator.setInterpolator(new DecelerateInterpolator(2));
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    layoutPulldown.setTranslationY((float)animation.getAnimatedValue());
                                    vPulldownBG.setAlpha(1-(float)animation.getAnimatedValue()/-layoutPulldown.getMeasuredHeight());
                                }
                            });
                            valueAnimator.start();

                        }


                        break;

                }

                mPreviousY = y;

                return true;
            }
        });

        findViewById(R.id.btn_launch_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAG",""+layoutPulldown.getTranslationY());
                Log.d("TAG",""+layoutPulldown.getAlpha());


                if(layoutPulldown.getTranslationY() <=-layoutPulldown.getMeasuredHeight()+5 || vPulldownBG.getAlpha()<=0.01) {
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    Bundle options = ActivityOptionsCompat.makeCustomAnimation(HomeScreen.this, R.anim.abc_fade_in, R.anim.abc_fade_out).toBundle();
                    startActivity(intent, options);
                }

            }
        });


        findViewById(R.id.btn_shopping_mode_toggle).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   ImageButton btn=(ImageButton)v;

//                  AppState.getInstance().bShopMode=!AppState.getInstance().bShopMode;


//                   if(!AppState.getInstance().bShopMode) {
//
//                       //kill the BLE instance
//                       AppState.getInstance().BLE.onStop();
//                       AppState.getInstance().BLE.onDestroy();;
//
//                       btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_shopping_toggle_off));
//                       return;
//                   }
//                   else{


                        AppState.getInstance().BLE.initializeBLE();
                        AppState.getInstance().BLE.onStart();

                       btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_shopping_toggle_on));
//                   }




                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {

                           RelativeLayout overlay = (RelativeLayout) findViewById(R.id.layout_home_overlay);
                           overlay.setVisibility(View.VISIBLE);
                           overlay.setAlpha(0f);
                           overlay.animate().alpha(1).setDuration(400);

                       }

                   }, 300);
               }
           });


        Button btn=(Button)findViewById(R.id.btn_home_overlay_gotit);

        final Activity that=this;

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!checkBlueTooth()){
                    Toast.makeText(that, "Bluetooth not enabled",Toast.LENGTH_LONG).show();
                    Toast.makeText(that, "Please enable",Toast.LENGTH_LONG).show();
                   // return;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RelativeLayout overlay =(RelativeLayout)findViewById(R.id.layout_home_overlay);
                        AlphaAnimation alphaAnimation= new AlphaAnimation(1,0);
                        alphaAnimation.setDuration(400);
                        overlay.startAnimation(alphaAnimation);
                        overlay.setVisibility(View.GONE);


                    }
                },300);

            }
        });





    }

    public boolean checkBlueTooth(){


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return false;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                return  false;

            }
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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
    protected void onResume(){

        super.onResume();

        View decorView = getWindow().getDecorView();

// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        AppState.getInstance().bShowNotifications=false;

    }


    @Override
    protected void onPause(){

        super.onPause();

        //force shop mode on
        AppState.getInstance().bShopMode=true;
        AppState.getInstance().bShowNotifications=true;


    }




    @Override
    protected void onStop() {

        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //remove al notifications if the app closes
        NotificationManager notificationManager =
                (NotificationManager) AppState.getInstance().AppContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();

        Log.d("TAG","DESTrzoY....");

        AppState.getInstance().BLE.onStop();
        AppState.getInstance().BLE.onDestroy();

    }



    @Override
    public void onChangeRegion() {

        Log.d("TAG","ZONE CHANGE "+AppState.getInstance().CurrentZone);

        if(AppState.getInstance().CurrentZone==null)
            return;




    }

    @Override
    public  void onChangeLocation(){


    }

    @Override
    protected  void onStart(){
        super.onStart();
        if(AppState.getInstance().BLE!=null)
            AppState.getInstance().BLE.onStart();
    }

    public void onClickDead(View view){

    }
}
