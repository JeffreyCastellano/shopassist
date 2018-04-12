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
import com.fa.google.shopassist.models.ListModel;

/**
 * Created by stevensanborn on 3/3/15.
 */
public class TimsWishListCard  extends BaseCardLayout   {


    public TimsWishListCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void  onFinishInflate() {

        super.onFinishInflate();

        findViewById(R.id.home_4b).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to list
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AppState.getInstance().CurrentListCategory = AppState.getInstance().giftsTim;
                        AppState.getInstance().CurrentList = AppState.getInstance().giftsTim.items.get(0);
                        handleClickGoList();

                    }
                }, 300);

            }
        });

        findViewById(R.id.home_4c).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to list
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AppState.getInstance().CurrentListCategory = AppState.getInstance().giftsTim;
                        AppState.getInstance().CurrentList = AppState.getInstance().giftsTim.items.get(1);
                        handleClickGoList();

                    }
                }, 300);

            }
        });

        findViewById(R.id.home_4d).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to list
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AppState.getInstance().CurrentListCategory = AppState.getInstance().giftsTim;
                        AppState.getInstance().CurrentList = AppState.getInstance().giftsTim.items.get(2);
                        handleClickGoList();

                    }
                }, 300);

            }
        });

        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_more);
        ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getContext()).setListFragment(R.array.more_lists, "More  Lists");
                    }
                }, 300);

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
