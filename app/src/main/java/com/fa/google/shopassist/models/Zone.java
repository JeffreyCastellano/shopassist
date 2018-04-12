package com.fa.google.shopassist.models;

import android.util.Log;

import com.fa.google.shopassist.models.BLELocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by stevensanborn on 12/1/14.
 */

public class Zone {

    public String strId="";

    public String strZoneTitle="";

    public String strDetails="";

    public String strNotificationTitle="";
    public String strNotificationSubtitle="";

    public ArrayList<BLELocation> arrLocations;


    public int iColorPrimary=0x333333;
    public int iColorPrimaryDark=0x000000;

    public Zone(JSONObject json){

        this.loadFromJSON(json);
    }

    public Zone(String id, String zone){

        this.strId=id;

        this.strZoneTitle=zone;

    }

    public void loadFromJSON(JSONObject json ){

        this.arrLocations= new ArrayList<BLELocation>();


        try {

            this.strId = json.getString("id");

            this.strDetails = json.getString("details");

            this.strZoneTitle = json.getString("title");

            this.strNotificationTitle=json.getString("notification_title");
            this.strNotificationSubtitle=json.getString("notification_subtitle");


            this.iColorPrimary=(int) Long.parseLong(json.getString("color_primary"),16);

            this.iColorPrimaryDark= (int)Long.parseLong(json.getString("color_primary_dark"),16);


            JSONArray arrLocationsJSON =  json.getJSONArray("locations");

            for (int i = 0; i < arrLocationsJSON.length(); i++) {

                JSONObject jsonLocation=arrLocationsJSON.getJSONObject(i);


                BLELocation L= new BLELocation(jsonLocation);
                L.Z=this;
                L.strZoneId=this.strId;
                Log.d("TAG"," L "+L.strBeaconMajorId+ " "+L.strBeaconMinorId);

                this.arrLocations.add(L);
            }


        }
        catch (JSONException e){

            Log.e("GSA ZONE Model","JSON exception "+e.getMessage());

        }



    }
}
