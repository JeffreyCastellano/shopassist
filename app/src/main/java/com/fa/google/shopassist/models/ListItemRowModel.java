package com.fa.google.shopassist.models;

import android.util.Log;

import com.fa.google.shopassist.globals.AppState;

import java.util.ArrayList;

/**
 * Created by stevensanborn on 2/27/15.
 */
public class ListItemRowModel {


    public  String strListName;

    public String strRowThImage;

    public String strRowImage;

    public Product P;

    public String strProductId;

    public ListItemRowModel(){}

    public ListItemRowModel(String productId){

        this.strProductId=productId;

        this.P=AppState.getInstance().getProductById(productId);

        if(P==null){

            Log.e("PRODUCT Id","ERROR NO PID "+productId);
        }
        this.strRowThImage=P.strThumb;
        this.strListName=P.strName;

    }

    public ListItemRowModel(String name, String rowThImage){

        this.strListName=name;

        this.strRowThImage=rowThImage;

    }
}
