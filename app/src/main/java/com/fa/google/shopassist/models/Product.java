package com.fa.google.shopassist.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by stevensanborn on 12/1/14.
 */
public class Product {

    public String strId;
    public String strName;
    public String strLongName=null;
    public float fPrice;
    public float fPrice2Year;
    public String strDescription;
    public String strListRow;
    public String strBlurb;
    public String strImage;
    public String strThumb;
    public String strExpanded;
    public float fStars;
    public String strType;
    public String strCardsResource;
    public JSONObject configurations;


    public Product(JSONObject json){

        try {

            this.strId = json.getString("id");
            this.strName = json.getString("title");
            if(json.has("longtitle"))
                this.strLongName = json.getString("title");
            this.fPrice= (float)json.getDouble("price");
            if(json.has("2yearprice"))
                this.fPrice2Year= (float)json.getDouble("2yearprice");
            this.fStars= (float)json.getDouble("stars");
            this.strBlurb = json.getString("blurb");
            this.strImage= json.getString("img");
            this.strThumb= json.getString("thumb");
            this.strType=json.getString("type");
            this.strListRow=json.getString("list");
            if(json.has("expanded"))
                this.strExpanded=json.getString("expanded");

            this.strCardsResource=json.getString("cards");
            if(json.has("configurations"))
                this.configurations = json.getJSONObject("configurations");
        }
        catch (JSONException e){

            Log.e("GSA Product Model", "JSON exception " + e.getMessage());

        }

    }


    public float getScore(){


        if(this.strId.equals("p001"))
            return 6.5f;
        else
            return 4.5f;
    }

    public String getQuestionResultAdjective(){

        if(getScore()>5.0)
            return "a very good";
        else
            return "a good";

    }

    public float getPrice(String strConfiguration) {
        float price = 0;

        if(this.configurations != null) {
            try {
                price = (float)this.configurations.getDouble(strConfiguration);
            } catch (JSONException e) {
                Log.e("GSA Product Model", "JSON exception " + e.getMessage());
            }
        } else {
            price = this.fPrice;
        }

        return price;
    }

}
