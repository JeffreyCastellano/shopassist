package com.fa.google.shopassist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by stevensanborn on 3/16/15.
 */
public class FadeInLinearLayout extends LinearLayout {

    public FadeInLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void onFinishInflate(){

        super.onFinishInflate();

        if(isInEditMode())return;

        for (int i=0; i<this.getChildCount(); i++){

            View V=this.getChildAt(i);
            V.setAlpha(0f);
        }

        animateIn();

    }

    public void animateIn(){


        for (int i=0; i<this.getChildCount(); i++) {
            View V = this.getChildAt(i);
            V.animate().alpha(1).setDuration(500).setStartDelay(350+i*100).setInterpolator(new DecelerateInterpolator());
        }
    }
}
