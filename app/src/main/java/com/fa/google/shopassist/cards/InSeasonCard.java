package com.fa.google.shopassist.cards;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.fa.google.shopassist.ListFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.OverlayListViewFragment;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.cards.Lists.ListViewFragment;
import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 4/7/15.
 */
public class InSeasonCard extends BaseCardLayout {

    public InSeasonCard (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void  onFinishInflate() {

        super.onFinishInflate();
//
        findViewById(R.id.layout_teriyaki).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to list

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ((MainActivity)getContext()).setFragment(OverlayListViewFragment.newInstance(R.array.recipe_teriyaki, "Teriyaki Tofu"),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);
                    }
                },300);

            }
        });



    }


}

