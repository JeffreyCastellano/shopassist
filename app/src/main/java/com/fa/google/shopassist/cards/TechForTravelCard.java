package com.fa.google.shopassist.cards;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.fa.google.shopassist.ListFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.cards.Lists.ListViewFragment;
import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 3/3/15.
 */
public class TechForTravelCard extends BaseCardLayout {

    public TechForTravelCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void  onFinishInflate() {

        super.onFinishInflate();

        findViewById(R.id.btn_img_card1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to list

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AppState.getInstance().CurrentListCategory= AppState.getInstance().techForTravel;
                        AppState.getInstance().CurrentList=AppState.getInstance().techForTravel.items.get(0);
                        handleClickGoList();

                    }
                },300);

            }
        });

        findViewById(R.id.btn_img_card2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to list

                AppState.getInstance().CurrentListCategory= AppState.getInstance().techForTravel;
                AppState.getInstance().CurrentList=AppState.getInstance().techForTravel.items.get(1);
                handleClickGoList();


            }
        });
        findViewById(R.id.btn_img_card3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to list

                AppState.getInstance().CurrentListCategory= AppState.getInstance().techForTravel;
                AppState.getInstance().CurrentList=AppState.getInstance().techForTravel.items.get(2);
                handleClickGoList();


            }
        });

        findViewById(R.id.btn_img_card4).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to list

                AppState.getInstance().CurrentListCategory= AppState.getInstance().techForTravel;
                AppState.getInstance().CurrentList=AppState.getInstance().techForTravel.items.get(3);
                handleClickGoList();


            }
        });

    }


    public void handleClickGoList(){


        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {


                ListViewFragment listViewFragment= new ListViewFragment();

                Bundle args = new Bundle();
                args.putInt(ListFragment.ARG_PARAM1, R.layout.layout_list);
                args.putString(ListFragment.ARG_PARAM2, AppState.getInstance().CurrentList.strListName);
                listViewFragment.setArguments(args);

                ((MainActivity)getContext()).setFragment(listViewFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);

            }
        },300);

    }
}
