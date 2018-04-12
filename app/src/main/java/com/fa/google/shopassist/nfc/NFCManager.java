package com.fa.google.shopassist.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.fa.google.shopassist.ActivityListener;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.ble.BLEManager;
import com.fa.google.shopassist.models.NFCTag;

import java.util.ArrayList;


/**
 * Created by stevensanborn on 11/22/14.
 */
public class NFCManager implements NfcAdapter.ReaderCallback,ActivityListener {

    Context context;

    Activity activity;

    private NfcAdapter mNfcAdapter;

    public static final String MIME_TEXT_PLAIN = "text/plain";

    public static final String TAG = "GSA-NFCMANAGER";

    public ArrayList<NFCListener> listeners;

    MediaPlayer mp;

    public NFCManager(Context c){
        this.context=c;

    }

    public void initializeNFC(Activity  activity){

        this.activity=activity;

        this.listeners= new ArrayList<NFCListener>();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this.context);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(context, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
             return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(context, "Device NFC not enabled", Toast.LENGTH_LONG).show();

        } else {
            Log.d(TAG,"NFC Device enabled");
            //Toast.makeText(this.activity, "Device NFC is enabled", Toast.LENGTH_LONG).show();
        }


        handleIntent(this.activity.getIntent());

    }

    public void onCreate(Bundle savedInstanceState){

    }

    public void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                Log.d("TAG","NFC TAG");
                new NdefReaderTask((MainActivity)context).execute(tag);


            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask(this.activity).execute(tag);
                    break;
                }
            }
        }
    }


    public void onStart(){
        handleIntent(this.activity.getIntent());
    }

    public void onStop(){

    }

    public void onPause(){
        if(this.activity!=null && mNfcAdapter!=null)
        mNfcAdapter.disableReaderMode(this.activity);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);

    }

    public void onResume(){

        setUpReader(this.activity, mNfcAdapter);
    }

    public void setUpReader(final Activity activity, NfcAdapter adapter){

        if(adapter!=null)
        adapter.enableReaderMode(activity, this, NfcAdapter.FLAG_READER_NFC_A  | NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, null);

    }

    public void onTagDiscovered(Tag tag) {

        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator)this.context.getSystemService(Context.VIBRATOR_SERVICE);
//        Vibrator v = this.context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        v.vibrate(300);

        this.mp =  MediaPlayer.create(context, R.raw.nfc_success);
        mp.start();
        new NdefReaderTask(context).execute(tag);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }
     //   final MainActivity GSA=(MainActivity)MainActivity.context;

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);

        }

    public void onNFCTag(NFCTag tag){

        Log.d("TAG","ON NFC "+tag.strId+" num lisyneer"+listeners.size());

        for(NFCListener listener:this.listeners){
            listener.onNFCTag(tag);
        }
    }

    public void onNewIntent(Intent intent){
        handleIntent(intent);
    }

    public void onResult(int requestCode, int resultCode, Intent data){

    }

    public void onDestroy(){

    }
}

