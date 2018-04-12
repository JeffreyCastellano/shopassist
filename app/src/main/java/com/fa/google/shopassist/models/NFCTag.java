package com.fa.google.shopassist.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stevensanborn on 12/1/14.
 */
public class NFCTag {

    public String strId="";

    public String strProductId="";



    public NFCTag (JSONObject json){


        try {

            this.strId = json.getString("id");

            this.strProductId = json.getString("product_id");

        }catch(JSONException e){

            Log.e("GSA NFCTag ","JSON Exception "+e.getMessage());

        }
    }
}
