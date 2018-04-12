package com.fa.google.shopassist.models;

import android.content.Context;
import android.util.Log;

import com.fa.google.shopassist.globals.AppState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by stevensanborn on 2/27/15.
 */
public class ListCategoryModel {


    public String strTitle;

    public ArrayList<ListModel> items= new ArrayList<>();


    public void loadFromJSON(String strFile,Context context){


        try {

            JSONObject obj = new JSONObject(AppState.getAssetJSONFile(strFile, context));

            this.strTitle=obj.getString("name");

            JSONArray mlistJsonArr=obj.getJSONArray("lists");

            for (int a = 0; a < mlistJsonArr.length(); a++) {

                ListModel LM= AppState.getInstance().getListById(mlistJsonArr.getString(a));

                if (LM==null)
                    Log.e("JSON","Error null list "+strFile);

                this.items.add(LM);
            }

        }catch (JSONException e){

            Log.e("JSON", "JSON Exception " + e.getMessage());
        }
    }

}
