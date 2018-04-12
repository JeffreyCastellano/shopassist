package com.fa.google.shopassist;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 3/18/15.
 */
public class ExploreStoreLayout extends RelativeLayout {


    public ExploreStoreLayout (Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    @Override
    public void onFinishInflate() {

        if (isInEditMode())return;


//        ImageView imgExplore=(ImageView)findViewById(R.id.img_explore_store_1);

        ImageView img=(ImageView)findViewById(R.id.img_sound_spectrum);

        img.setBackgroundResource(R.drawable.anim_audio_spectrum);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();



        // Start the animation (looped playback by default).
        frameAnimation.start();

    }
}
