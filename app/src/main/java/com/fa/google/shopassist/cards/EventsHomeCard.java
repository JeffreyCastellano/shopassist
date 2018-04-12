package com.fa.google.shopassist.cards;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.fa.google.shopassist.EventFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 3/3/15.
 */
public class EventsHomeCard  extends BaseCardLayout {

    public EventsHomeCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void  onFinishInflate() {

        super.onFinishInflate();

        findViewById(R.id.btn_img_card1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getContext()).setFragment(EventFragment.newInstance(AppState.getInstance().arrEvents.get(AppState.getInstance().iCurrentEvent))
                        , FragmentTransaction.TRANSIT_FRAGMENT_OPEN, false);


            }

        });

        findViewById(R.id.layout_more).setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getContext()).setListFragment(R.array.more_events, "More Events");
                    }
                },300);

            }
        });


    }

}
