package com.fa.google.shopassist.map;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.fa.google.shopassist.BaseDialogFragment;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.compare.CompareOverlayLayout;
import com.fa.google.shopassist.globals.AppState;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by stevensanborn on 2/23/15.
 */
public class StoreMapModalFragment extends BaseDialogFragment implements TabHost.TabContentFactory , SensorEventListener,LocationListener {


    public enum MAP_ENUM { GOOGLE, TARGET, WHOLE_FOODS };

    public enum MARKER_ENUM { PHONE,RECIPE,AVAILABLE_HERE_WF,AVAILABLE_HERE_GOOGLE,JORDAN, MATTHEW,JACKET,WEAR,CHECKOUT, NONE };

    public MAP_ENUM eMap;
    public MARKER_ENUM eMarker;


    private  SensorManager mSensorManager;
//    private LocationManager mLocationManager;

//    private ImageView imgLocation;
//    private ImageView imgMap;
//    private ImageView imgAd;

    private TextView textTitle;
    private RelativeLayout layoutCalibrate;

    public static final String IMG_MAP_ARG="BundleMAP";

    public boolean bShowAd=false;
    public float Heading = 0.f;

    protected final static String TAG=StoreMapModalFragment.class.getSimpleName();

    float[] mGravity;
    float[] mGeomagnetic;

    float fVelocityX=0;
    float fVelocityY=0;

    int accuracy=SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;
    public int iAdResource=0;


    boolean bUseGPS=false;

    Location centerLocation= new Location("center store");
    final double centerLong=37.418724; //physical center point of map
    final double centerLat=-122.082887; //physical center point of map
    final float GPSSpeed=50; //how fast to move map



    Location currentLocation;
    long bestTime;

    protected LocationRequest mLocationRequest;

    Timer timer;

    public StoreMapModalFragment(){


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args=getArguments();

        this.eMap= (StoreMapModalFragment.MAP_ENUM) args.getSerializable("map");
        this.eMarker= (StoreMapModalFragment.MARKER_ENUM) args.getSerializable("marker");

        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);


        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        centerLocation.setLongitude(centerLong);

        centerLocation.setLatitude(centerLat);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.layout_overlay_map,container, false);

        ImageButton  btnGotIt=(ImageButton)rootView.findViewById(R.id.btn_gotit);
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                },300);


            }
        });

        textTitle=(TextView)rootView.findViewById(R.id.txt_overlay_compare_title);
        textTitle.setText(getArguments().getString("title"));

        TabHost mTabHost = (TabHost)rootView.findViewById(android.R.id.tabhost);
//
        mTabHost.setup();

        TabHost.TabSpec tab1=mTabHost.newTabSpec("fragment1");
        tab1.setIndicator("Map");
        tab1.setContent(this);
        mTabHost.addTab(tab1);

        TabHost.TabSpec tab2=mTabHost.newTabSpec("fragment2");
        tab2.setIndicator("Augmented Reality");
        tab2.setContent(this);
        mTabHost.addTab(tab2);
//
//        TabHost.TabSpec tab3=mTabHost.newTabSpec("fragment2");
//        tab3.setIndicator("Map 3");
//        tab3.setContent(this);
//        mTabHost.addTab(tab3);


//        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//
//                Log.d("TAG","tab "+tabId);
//
//            }
//        });

        TabWidget widget = mTabHost.getTabWidget();
        widget.setEnabled(false);

        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            tv.setAllCaps(true);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            v.setBackgroundResource(R.drawable.tab_indicator_ab_example);
            //v.setBackgroundResource(R.color.primary);
        }

//        this.imgLocation=(ImageView)rootView.findViewById(R.id.img_your_location);
//        this.imgMap=(ImageView)rootView.findViewById(R.id.img_store_map);
//        this.imgMap.setImageDrawable(getResources().getDrawable(getArguments().getInt(StoreMapModalFragment.IMG_MAP_ARG)));
//        this.layoutCalibrate=(RelativeLayout)rootView.findViewById(R.id.layout_calibrate);



        this.createLocationRequest();

        return rootView;

    }



    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(1000);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(500);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);


        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }



    public View createTabContent(String tag) {


        if(tag.equals("fragment1")) {
            View V=LayoutInflater.from(getActivity()).inflate(R.layout.layout_overlay_map2d, null);

            ShopMapFragment SMF=(ShopMapFragment)getFragmentManager().findFragmentById(R.id.map);

            SMF.eMap=this.eMap;
            SMF.eMarker=this.eMarker;


            if(bShowAd) {
                V.findViewById(R.id.img_recipe_ad).setVisibility(View.VISIBLE);
                final ImageView imgViewAd= (ImageView)V.findViewById(R.id.img_recipe_ad);
                try {
                    imgViewAd.setImageDrawable(getResources().getDrawable(this.iAdResource));
                }catch (Resources.NotFoundException e){
                    Log.e("TAG","IMG RESOURCE MAP NOT FOUNd");
                }
            }
            return V;
        }
//        else if(tag.equals("fragment2")) {
//             View   V=(ViewGroup)LayoutInflater.from(getActivity()).inflate(R.layout.layout_augmented, null);
//            return V;
//        }
        else {

            View V=LayoutInflater.from(getActivity()).inflate(R.layout.layout_overlay_map2d, null);
            return V;
        }

    }

    @Override
    public void onDestroy(){
        timer.cancel();
           this.unRegister();

        MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();

        super.onDestroy();
    }

    public void unRegister(){
        mSensorManager.unregisterListener(this);
        if(AppState.getInstance().apiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(AppState.getInstance().apiClient, this);

//        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onPause(){

        if(timer!=null)
            timer.cancel();

        super.onPause();
        this.unRegister();
            AppState.getInstance().bCompareShown=false;




    }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI );
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_UI);

        if(AppState.getInstance().apiClient!=null && AppState.getInstance().apiClient.isConnected() )
            LocationServices.FusedLocationApi.requestLocationUpdates(AppState.getInstance().apiClient, mLocationRequest, this);



        //update roation
        timer = new Timer();
        TimerTask TT=new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

/*

                        float fDifference = Heading - imgLocation.getRotation();
                        if (fDifference > 180 + imgLocation.getRotation())
                            fDifference -= 360;
                        if (fDifference < -180)
                            fDifference += 360;

                        float fRotation = imgLocation.getRotation() + (fDifference) / 10;
                        if (fRotation < 0)
                            fRotation = fRotation + 360;
                        if (fRotation > 360)
                            fRotation -= 360;
                        imgLocation.setRotation(fRotation);

                        if (!bUseGPS) {

                            if (accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM ) {
                                 layoutCalibrate.setVisibility(View.VISIBLE);
                                return;
                            } else {
                                layoutCalibrate.setVisibility(View.GONE);

                            }

                            fVelocityX = 0;

                            float fRotatedVelocityX = (float) Math.sin(Math.toRadians(fRotation)) * fVelocityY;
                            float fRotatedVelocityY = (float) Math.cos(Math.toRadians(fRotation)) * fVelocityY;

                            float fTransY = imgMap.getTranslationY() + fRotatedVelocityY / 2;
                            float fTransX = imgMap.getTranslationX() - fRotatedVelocityX / 2;

                            if (fTransY > 615)
                                fTransY = 615;
                            if (fTransY < -450)
                                fTransY = -450;
                            if (fTransX > 400)
                                fTransX = 400;
                            if (fTransX < -118)
                                fTransX = -118;

//                            imgMap.setTranslationX(fTransX);
//                            imgMap.setTranslationY(fTransY);



                            fVelocityX *= .95f;
                            fVelocityY *= .95f;
                        } else {

                            //translate by center of the image
                            if(currentLocation==null)return;

                            double fMaxDiffLat = .000041;//y
                            double fMaxDiffLon = 0.000101;//x


                            double fTransY = -((centerLocation.getLatitude() - currentLocation.getLatitude())/ fMaxDiffLat *GPSSpeed);
                            double fTransX = -((centerLocation.getLongitude() - currentLocation.getLongitude())/ fMaxDiffLon *GPSSpeed);


//                            imgMap.setTranslationX((float)(imgMap.getTranslationX()+((fTransX-imgMap.getTranslationX())/10.0)));
//                            imgMap.setTranslationY((float)(imgMap.getTranslationY()+((fTransY-imgMap.getTranslationY())/10.0)));

                        }
                        */
                    }
                });
            }
        };



        timer.scheduleAtFixedRate(TT,0,1000/40);

    }








    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        Log.d(TAG,"accuracy "+accuracy);

        this.accuracy=accuracy;

//        if(accuracy==SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
//            Toast.makeText(getActivity(), "Compass Accuracy HIGH "+sensor.getName(),Toast.LENGTH_LONG).show();
//        else if(accuracy==SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM)
//            Toast.makeText(getActivity(), "Compass Accuracy Med "+sensor.getName(),Toast.LENGTH_LONG).show();
//        else if(accuracy==SensorManager.SENSOR_STATUS_ACCURACY_LOW)
//            Toast.makeText(getActivity(), "Compass Accuracy Low "+sensor.getName(),Toast.LENGTH_LONG).show();


    }

    public void onSensorChanged(SensorEvent event) {

        final ShopMapFragment  f= (ShopMapFragment) getFragmentManager().findFragmentById(R.id.map);

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = event.values;
                break;

            case Sensor.TYPE_LINEAR_ACCELERATION:

                float XChange=event.values[0]/2.0f;
                float YChange=event.values[1]/2.0f;

                if(Math.abs(XChange) >.3)
                    fVelocityX+=XChange;
                if(YChange >.1) {
                    fVelocityY += YChange;
                }
//                Log.d("TAG","CHANGE "+fVelocityY);

                break;

            case Sensor.TYPE_ROTATION_VECTOR:

                break;
            default:

                return;
        }

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {


                 /* Compensate device orientation */
                // http://android-developers.blogspot.de/2010/09/one-screen-turn-deserves-another.html
                float[] remappedRotationMatrix = new float[9];
                switch (getActivity().getWindowManager().getDefaultDisplay()
                        .getRotation()) {
                    case Surface.ROTATION_0:
                        SensorManager.remapCoordinateSystem(R,
                                SensorManager.AXIS_X, SensorManager.AXIS_Y,
                                remappedRotationMatrix);
                        break;
                    case Surface.ROTATION_90:
                        SensorManager.remapCoordinateSystem(R,
                                SensorManager.AXIS_Y,
                                SensorManager.AXIS_MINUS_X,
                                remappedRotationMatrix);
                        break;
                    case Surface.ROTATION_180:
                        SensorManager.remapCoordinateSystem(R,
                                SensorManager.AXIS_MINUS_X,
                                SensorManager.AXIS_MINUS_Y,
                                remappedRotationMatrix);
                        break;
                    case Surface.ROTATION_270:
                        SensorManager.remapCoordinateSystem(R,
                                SensorManager.AXIS_MINUS_Y,
                                SensorManager.AXIS_X, remappedRotationMatrix);
                        break;
                }

                /* Calculate Orientation */
                float results[] = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix,
                        results);

                /* Get measured value */
                float current_measured_bearing = (float) (results[0] * 180 / Math.PI);
                if (current_measured_bearing < 0) {
                    current_measured_bearing += 360;
                }

                /* Smooth values using a 'Low Pass Filter' */
                this.Heading=current_measured_bearing;

                if(Heading< 0) Heading+=360;
                if(Heading>360)Heading-=360;

//    Log.d(TAG,"HEADING "+orientation[0]);

                if (f != null)
                {
                  //  CameraPosition cpos=f.getMap().getCameraPosition();


                    if(f.markerUser!=null) {
                        f.markerUser.setRotation(f.markerUser.getRotation() + (Heading-f.markerUser.getRotation())/10);
//                        f.markerUser.setRotation(Heading);
                    }
//                    CameraPosition cameraPosition = new CameraPosition.Builder().target(cpos.target).bearing(this.Heading).zoom(cpos.zoom).build();
//                    f.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                    f.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 0,null);
                }

            }
        }

    }


    @Override
    public void onLocationChanged(Location location) {

        this.Heading = location.getBearing();

        this.currentLocation = location;


        AppState.getInstance().setNewLocation(location);

        /*
        //check if there is a map to update
        ShopMapFragment  f= (ShopMapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null)
        {
            LatLng pos=new LatLng(AppState.getInstance().currentLocation.getLatitude(),AppState.getInstance().currentLocation.getLongitude());
            if(pos!=null && f.markerUser!=null)
                f.markerUser.setPosition(pos);

//            CameraPosition cameraPosition = new CameraPosition.Builder().target(pos).bearing(this.Heading).zoom(22).build();
//
//            f.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500,null);
        }
        */
    }

//
//  73.95783638
       // this.currentLocation.setLongitude(-73.95773538);
    //    Toast.makeText(getActivity(), "LOCATION "+location.getLatitude()+" , "+location.getLongitude(),Toast.LENGTH_LONG).show();

//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        // TODO Auto-generated method stub
//        Toast.makeText(getActivity(), "Status " + provider+" "+status,
//                Toast.LENGTH_SHORT).show();
//
//    }

//    @Override
//    public void onProviderEnabled(String provider) {
//        Toast.makeText(getActivity(), "Enabled new provider " + provider,
//                Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Toast.makeText(getActivity(), "Disabled provider " + provider,
//                Toast.LENGTH_SHORT).show();
//    }



}
