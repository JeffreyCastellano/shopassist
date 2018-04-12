package com.fa.google.shopassist;

import android.content.Context;

/**
 * Created by mjoyce on 2/10/15.
 */
public class CardInfo {
    protected int resourceId;
    protected String resourceType;
    public boolean bAnimated=false;
    public int iIndex=0;

    public CardInfo(int resourceId, String resourceType) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;

    }

}