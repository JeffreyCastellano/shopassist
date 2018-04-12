package com.fa.google.shopassist.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stevensanborn on 4/3/15.
 */
public class Store {

    public String strId;
    public String strName;


    public Store(JSONObject json){

        try {

            this.strId = json.getString("id");

            this.strName = json.getString("name");

        }catch(JSONException e){

            Log.e("GSA NFCTag ", "JSON Exception " + e.getMessage());

        }
    }
}
