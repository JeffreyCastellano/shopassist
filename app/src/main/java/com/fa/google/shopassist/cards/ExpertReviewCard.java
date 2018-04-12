package com.fa.google.shopassist.cards;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;

/**
 * Created by stevensanborn on 3/4/15.
 */
public class ExpertReviewCard extends BaseCardLayout {


    public ExpertReviewCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void  onFinishInflate() {

        super.onFinishInflate();


//        findViewById(R.id.btn_img_card).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //go to list
//
//                (new Handler()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//
////                        ((MainActivity) getContext()).setFragment(listViewFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//
//                    }
//                }, 300);
//
//            }
//        });

    }

}
