package com.fa.google.shopassist.ble;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.eddystone.Eddystone;
import com.fa.google.shopassist.ActivityListener;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.SplashActivity;
import com.fa.google.shopassist.camera.CameraActivity;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.BLELocation;
import com.fa.google.shopassist.models.Zone;
import com.fa.google.shopassist.setup.HomeScreen;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by stevensanborn on 11/22/14.
 */
public class BLEManager implements ActivityListener {

    public static final String TAG= "GSA";

    private static final int REQUEST_ENABLE_BT = 1234;

    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

    private NotificationManager notificationManager;

    private static final int NOTIFICATION_ID = 123;

    private final float DISTANCE_IN_A_ZONE=-78;

    private Context context;

    public BeaconManager beaconManager;

    public ArrayList<BLEListener> listeners= new ArrayList<BLEListener>();

    public  boolean bInit=false;

    private boolean bConnected=false;

    private Handler changeZoneHandler;

    private Runnable runnableZoneChange;

    private String scanId="";

    private int countInARow=0;

    private Zone LastZone=null;

    public BLEManager(Context c){

        this.context=c;

        this.listeners=new ArrayList<BLEListener>();

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }



    public void onCreate(Bundle savedInstanceState){

        if(!bInit)
            this.initializeBLE();

    }

    public void initializeBLE(){

        bInit=true;
//        EstimoteSDK.initialize(context, "stevensanborn-gmail-com-s--et7", "82c161290f00956ceeed2c04b9eff22c");
        EstimoteSDK.initialize(context, "free-association-s-google--18z", "f661352ec81764b4136ff2c4c5d02e6a");
        EstimoteSDK.enableDebugLogging(true);beaconManager = new BeaconManager(context);

        Log.d(TAG, "creating beacon manager");


        /*


        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {


                if (AppState.getInstance().strLockZoneId != null)
                    return;

//                Log.d(TAG,"BEAOCNS" + beacons.size());
                // LOCATION CHANGE
                if (beacons.size() > 0) {

                    Beacon closestBeacon = beacons.get(0);


                    BLELocation Local = AppState.getInstance().getLocationBasedOnBeacon(closestBeacon);
                    if (AppState.getInstance().CurrentLocation != Local) {
                        AppState.getInstance().CurrentLocation = Local;
                        notifyChangeLocation();

                    }


                }

                //REGION CHANGE
                if (beacons.size() > 1) {

                    Beacon closestBeacon = beacons.get(0);

                    Beacon nextClosestBeacon = beacons.get(1);

                    BLELocation Local = AppState.getInstance().getLocationBasedOnBeacon(closestBeacon);

                    BLELocation NextLocal = AppState.getInstance().getLocationBasedOnBeacon(nextClosestBeacon);


                    if (Local == null || NextLocal == null) {

                        //if both are null they may have left a zone
                        if (Local == null && NextLocal == null) {
                            if (AppState.getInstance().CurrentZone != null) {


                                //AppState.getInstance().CurrentZone = null;

                                Log.d(TAG, "LEFT ZONE both are null");

                                //notifyChangeRegion();
                            }
                        }

                        Log.d(TAG, "ONE ZONE IS null " + closestBeacon.getMajor() + " " + nextClosestBeacon.getMajor());

                        return;
                    }


                    //check for conflicting zone ids

                    if (!Local.strZoneId.equals(NextLocal.strZoneId)) {
                        return;
                    }

                    String strZoneId = "";

                    //first make sure we have a strong enough signal


                    if (closestBeacon.getRssi() > -98) {

                        //save temp zone
                        if (AppState.getInstance().CurrentZone != null)
                            strZoneId = AppState.getInstance().CurrentZone.strId;

                        // ZONE CHANGE

                        if (!strZoneId.equals(Local.Z.strId)) {

                            if (AppState.getInstance().bShopMode) {
                                AppState.getInstance().CurrentZone = Local.Z;
                                // Log.d(TAG,"ZONE REGION"+Local.Z.strZoneTitle);


                                notifyChangeRegion();
                            }


                        }


                    }


                } else if (beacons.size() == 0) {

                    if (AppState.getInstance().CurrentZone != null) {
                        AppState.getInstance().CurrentZone = null;
                        Log.d(TAG, "LEFT ZONE no beacons found");
                        notifyChangeRegion();
                    }
                }
            }

        });


        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {

                Log.d(TAG, "on enterred Region " + region.getIdentifier());

            }

            @Override
            public void onExitedRegion(Region region) {

                Log.d(TAG, "on exit Region " + region.getIdentifier());
            }
        });

        */

//        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
//            @Override public void onNearablesDiscovered(List<Nearable> nearables) {
//                Log.d(TAG, "Discovered nearables: " + nearables);
//            }
//        });


        /* Eddystones */

        beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> list) {

                Log.d(TAG, "Nearby Eddystone beacons: " + list);

//                Log.d(TAG, "Nearby Eddystone beacons!!!!: " + list.size());
                if(AppState.getInstance().strLockZoneId!=null && AppState.getInstance().strLockZoneId.length() >0){
                    return;
                }


                if(list.size()>0) {

                    //grab the closest beacon

                    Eddystone closestBeacon = null;
                    BLELocation Local=null;
                    //search for the closest beacon

                    for (Eddystone beacon:list) {

                        BLELocation Location = AppState.getInstance().getLocationBasedOnEddyBeacon(beacon);

                       // Log.d(TAG, "LOCATION "+beacon.namespace +" "+beacon.instance );

                        if(Location!=null && closestBeacon==null ){

                            closestBeacon=beacon;

                            Local=Location;

                            Log.d(TAG,Local.strBeaconMajorId+"____"+Local.strBeaconMinorId);

                        }

                        Log.d(TAG,beacon.instance+"____"+beacon.namespace);

                    }

                    if(Local!=null) {//verify a location exists and a zone

                        //check if we are in a new location and zone

                        if (Local.Z != null) {

                            if (AppState.getInstance().CurrentZone != Local.Z) { //check if we are in a new zone

                                    AppState.getInstance().CurrentZone = Local.Z; //update the current zone

                                    notifyChangeRegion();


                             //   Log.d(TAG, "CHANGED ZONE");


                            }

                        }

                        //check if we are in a new location
                        //
                        //
                        if (AppState.getInstance().CurrentLocation!=Local){

                            AppState.getInstance().CurrentLocation=Local;

                            notifyChangeLocation();

                            //Log.d(TAG, "CHANGED LOCATION");
//                            Toast.makeText(context, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
//                            if(Local.strBeaconMinorId.equals("c6eeecc53b78"))
//                             Toast.makeText(context, "*Meat*", Toast.LENGTH_LONG).show();

//                            Toast.makeText(context, " LOC:"+Local.strBeaconMajorId+","+Local.strBeaconMinorId+, Toast.LENGTH_LONG).show();

                        }

                    }
                    else{


//                        Log.d(TAG, "Beacon is null " + closestBeacon.namespace + " " + closestBeacon.instance);

                    }




                }


                /*
                for (Eddystone beacon:list
                     ) {

                    //check if the beacon is within range
                    if(beacon.rssi<-98) {

                        Log.d(TAG, "--" + beacon.namespace);
                        Log.d(TAG, "--" + beacon.instance);

                        //check if the beacon is defined in zones
                        BLELocation foundBeacon = AppState.getInstance().getLocationBasedOnEddyBeacon(beacon);

                        if(foundBeacon!=null) {

                            Log.d(TAG, "--- found " + foundBeacon);

                        }
                        else
                        {


                            Log.d(TAG, "--- not found " + beacon.namespace+" "+beacon.instance);

                        }



                    }

                }

                 */
            }
        });

    }

    public void onStart(){

        if(!bConnected) {

            // Check if device supports Bluetooth Low Energy.
//            if (!beaconManager.hasBluetooth()) {
//                Toast.makeText(context, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
//                return;
//            }

            // If Bluetooth is not enabled, let user enable it.
//            if (!beaconManager.isBluetoothEnabled()) {
//
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                Log.d(TAG, "Asking for BLE Permission");
//                ((Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//
//            } else {
                Log.d(TAG, "BLE is enabled connecting to service...");
                connectToService();
//            }

        }
    }
    public void onDestroy(){

        beaconManager.disconnect();
        bConnected=false;
        bInit=false;

    }

    public void startMonitoring(){

        try {

            beaconManager.startMonitoring(ALL_ESTIMOTE_BEACONS_REGION);
        }
        catch (Exception e){

            Log.e(TAG,"Remote Ex "+e.getMessage());

        }

    }

    public void onStop(){
//        try {

            Log.d(TAG,"STOP MONITORING");


            beaconManager.stopEddystoneScanning(scanId);

//            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
//
//            beaconManager.stopMonitoring(ALL_ESTIMOTE_BEACONS_REGION);

//        } catch (RemoteException e) {
//            Log.d(TAG, "Error while stopping ranging", e);
//        }

    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG,"BLE ACTIVITY RESULT OK");
                connectToService();
            } else {
                Toast.makeText(this.context, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
            }
        }

    }


    private void connectToService() {


//        Log.d(TAG,"connect to service");

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
//                try {
//                    Log.d(TAG, "start ranging..");
//                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);

                  //  beaconManager.setForegroundScanPeriod(1100, 0);
                    if(!scanId.equals(""))beaconManager.stopEddystoneScanning(scanId); //stop before starting

//                    scanId = beaconManager.startNearableDiscovery();
                    scanId=beaconManager.startEddystoneScanning();
                    Log.d(TAG,"Start Scannning Eddystone : id"+ scanId);

                    bConnected=true;
//                    startMonitoring();
//                } catch (Exception e) {
//                    Toast.makeText(context, "Cannot start ranging, something terrible happened",
//                            Toast.LENGTH_LONG).show();
//                    Log.e(TAG, "Cannot start ranging", e);
//                }
            }
        });
    }

    public void notifyChangeLocation(){

        for (int j = 0; j < listeners.size(); j++) {

            BLEListener listen = listeners.get(j);
            listen.onChangeLocation();
        }

    }

    public void notifyChangeRegion(){

        Log.d("TAG"," "+AppState.getInstance().bShopMode);



        for (int j = 0; j < listeners.size(); j++) {

            BLEListener listen = listeners.get(j);

            listen.onChangeRegion();

        }

        //Send notifications when you change a zone

        if(AppState.getInstance().bShowNotifications && AppState.getInstance().CurrentZone!=null){


            if(this.changeZoneHandler!=null){

                this.changeZoneHandler.removeCallbacks(runnableZoneChange);
            }

            final Context context=this.context;
            changeZoneHandler= new Handler();
            runnableZoneChange= new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(context, "--"+AppState.getInstance().CurrentZone.strZoneTitle,
//                            Toast.LENGTH_LONG).show();
                    if(!AppState.getInstance().CurrentZone.strId.equals("welcome")) {

                        //make sure that we are not using the camera
                        if(CameraActivity.active)
                            return;

                        AppState.getInstance().BLE.sendNotification(AppState.getInstance().CurrentZone.strNotificationTitle , AppState.getInstance().CurrentZone.strNotificationSubtitle);

                    }

                }
            };

            changeZoneHandler.postDelayed(runnableZoneChange,500);



        }
    }

    public void onPause(){}

    public void onResume(){}

    public void onNewIntent(Intent intent){}



    public void sendNotification(String title, String text) {

        Context context = AppState.getInstance().AppContext;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setColor(Color.argb(1, 96, 84, 224))
                        .setContentTitle(title)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentText(text);


        Intent intent ;

        intent= new Intent(context, SplashActivity.class);

        intent.putExtra("SkipIntro",true);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP
                );

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Vibration
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        //sound
        mBuilder.setSound(Uri.parse("android.resource://" + AppState.getInstance().AppContext.getPackageName() + "/" + R.raw.notification));

        mBuilder.setContentIntent(pi);


        NotificationManager notificationManager =  (NotificationManager) AppState.getInstance().AppContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();

        Notification notification=mBuilder.build();

        notificationManager.notify(112310, notification);

    }
}
