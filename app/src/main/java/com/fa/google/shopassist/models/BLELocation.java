package com.fa.google.shopassist.models;

import android.util.Log;

import com.estimote.sdk.Beacon;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stevensanborn on 12/1/14.
 */
public class BLELocation {

    public String strId="";


    public String strTitle="";

//    public int iBeaconMinorId=0;
//    public int iBeaconMajorId=0;

    public String strBeaconMajorId="";
    public String strBeaconMinorId="";


    public Zone Z;

    public String strZoneId="";

    public BLELocation(JSONObject json){

        try {
            this.strBeaconMajorId = json.getString("major");
            this.strBeaconMinorId = json.getString("minor");
        }
        catch (JSONException e){

            Log.e("GSA BLELocation Model","JSON exception "+e.getMessage());

        }


    }


    public boolean isLocation(Beacon B){

        return (
            String.valueOf( B.getMinor()).equals(this.strBeaconMinorId) &&
            String.valueOf( B.getMajor()).equals(this.strBeaconMajorId)
        );

//        return (B.getMinor()==this.iBeaconMinorId && B.getMajor()==this.iBeaconMajorId);

    }


}
