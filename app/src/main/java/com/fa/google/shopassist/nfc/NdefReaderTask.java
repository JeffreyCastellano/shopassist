package com.fa.google.shopassist.nfc;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.NFCTag;

import java.io.UnsupportedEncodingException;

/**
 * Created by stevensanborn on 11/19/14.
 */
public class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    private Context context;


    public static final String TAG = NdefReaderTask.class.getSimpleName();

    public NdefReaderTask(Context context){
            super();
//            this.GSA=activity;
        this.context=context;

    }
    @Override
    protected String doInBackground(Tag... params) {
        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
        if(ndefMessage!=null) {

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {

//                Log.d(TAG,"NFC : TNF "+ndefRecord.getTnf());
//                Log.d(TAG,"NFC : TYPE "+ndefRecord.getType());

                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN) {
                    try {


                        String strRecord=readText(ndefRecord);
                        Log.d(TAG,"NFC : "+strRecord);

                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }
        }
        return null;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(String result) {


        if (result != null) {


            for(NFCTag N:AppState.getInstance().arrTags){

                if(N.strId.equals(result)){

                    //found nfc tag
//                    Log.d(TAG,"NFC : FOUND");
                    AppState.getInstance().NFC.onNFCTag(N);

                }

            }

        }
        else{

            Log.d(TAG,"NFC POST NULL RESULT");

        }
    }
}