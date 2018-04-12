package com.fa.google.shopassist.models;

import com.fa.google.shopassist.globals.AppState;

import java.util.ArrayList;

/**
 * Created by mjoyce on 3/5/15.
 */
public class Cart {
    private ArrayList<CartItem> items = new ArrayList<CartItem>();

    public Cart(){}

    public void add(CartItem item) {
        this.items.add(item);
    }

    public void clear() {
        this.items.clear();
    }

    public int size() {
        return this.items.size();
    }

    public ArrayList<CartItem> getItems() {
        return this.items;
    }

    public float getSubTotal() {
        float fSubTotal = 0;
        Product product;
        for(CartItem item : this.items) {
            product = AppState.getInstance().getProductById(item.getProductId());
            fSubTotal = fSubTotal + product.getPrice(item.getConfiguration());
        }
        return fSubTotal;
    }

}
