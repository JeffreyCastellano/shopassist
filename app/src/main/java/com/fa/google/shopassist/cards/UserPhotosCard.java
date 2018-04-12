package com.fa.google.shopassist.cards;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;

/**
 * Created by stevensanborn on 3/4/15.
 */
public class UserPhotosCard extends BaseCardLayout {


    public UserPhotosCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void  onFinishInflate() {
        super.onFinishInflate();

        /*
        findViewById(R.id.layout_more).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getContext()).setListFragment(R.array.nexus_more_expert_reviews, "More Expert Reviews");
                    }
                }, 300);

            }
        });
        */

    }

}
