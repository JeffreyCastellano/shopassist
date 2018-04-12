package com.fa.google.shopassist.models;

import com.fa.google.shopassist.globals.AppState;

/**
 * Created by mjoyce on 3/5/15.
 */
public class CartItem {

    private String productId;
    private String configuration;

    public CartItem(String productId, String configuration){
        this.productId = productId;
        this.configuration = configuration;
    }

    public String getProductId() {
        return this.productId;
    }

    public String getConfiguration() {
        return this.configuration;
    }

    public String getAssetUrl() {
        if(this.configuration != null) {
            return "cart_" + this.productId + "_" + this.configuration;
        } else {
            return "cart_" + this.productId;
        }
    }
}
