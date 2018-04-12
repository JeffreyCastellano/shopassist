package com.fa.google.shopassist.models;

import java.util.ArrayList;

/**
 * Created by stevensanborn on 2/27/15.
 */
public class ListModel extends  ListItemRowModel{

    public String strId;

    public String strMainAsset;

    public int iFollowers;

    public String strFollow;

    public boolean bPrivate=false;

    public String strBlurb;

    public ArrayList<ListItemRowModel> items= new ArrayList<ListItemRowModel>();

    public ListModel(){}


}
