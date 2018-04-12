package com.fa.google.shopassist.cards;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.ProductListFragment;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.map.StoreMapModalFragment;

/**
 * Created by stevensanborn on 3/12/15.
 */
public class WhereIsProduct extends BaseCardLayout {

    public WhereIsProduct(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void  onFinishInflate() {

        super.onFinishInflate();

        findViewById(R.id.btn_img_card1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getContext()).showMap(AppState.getInstance().CurrentProduct.strName, StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.PHONE);
                /*
                StoreMapModalFragment storeMapModalFragment= new StoreMapModalFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title",AppState.getInstance().CurrentProduct.strName);
                bundle.putInt("resourceId",  com.fa.google.shopassist.R.layout.layout_overlay_map);
                bundle.putInt(StoreMapModalFragment.IMG_MAP_ARG,  R.drawable.img_map_store_phones);
                storeMapModalFragment.setArguments(bundle);
                storeMapModalFragment.show(((MainActivity)getContext()).getFragmentManager(), "Map");*/

            }

        });

        findViewById(R.id.layout_more).setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById("nexus6");
                        ProductListFragment LF= ProductListFragment.newInstance();
                        ((MainActivity) getContext()).setFragment(LF, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, false);

                    }
                },300);

            }
        });


    }

}
