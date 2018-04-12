package com.fa.google.shopassist;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fa.google.shopassist.cards.ExpandableLayout;

/**
 * Created by stevensanborn on 4/30/15.
 */
public class ShoppingHistoryLayout extends LinearLayout {

    public ImageView imgHistoryWholefoods;
    public ImageView imgHistoryTarget;
    public ImageView imgHistoryGoogle;

    LinearLayout layoutWholeFoods;
    LinearLayout layoutTarget;
    LinearLayout layoutGoogle;

    public LinearLayout CurrentLayout;

    public ShoppingHistoryLayout (Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    @Override
    public void onFinishInflate() {

        if (isInEditMode()) return;


        this.imgHistoryGoogle=(ImageView)findViewById(R.id.img_history_1_google);
        this.imgHistoryTarget=(ImageView)findViewById(R.id.img_history_1_target);
        this.imgHistoryWholefoods=(ImageView)findViewById(R.id.img_history_1_whole_foods);


//        layoutGoogle=(LinearLayout)findViewById(R.id.layout_content_google);
//        layoutWholeFoods=(LinearLayout)findViewById(R.id.layout_content_whole_foods);
//        layoutTarget=(LinearLayout)findViewById(R.id.layout_content_target);

        this.imgHistoryTarget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                collapseContent(layoutTarget);
            }
        });

        this.imgHistoryWholefoods.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseContent( layoutWholeFoods);
            }
        });

        this.imgHistoryGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseContent( layoutGoogle);
            }
        });
    }

    public void collapseContent(final LinearLayout layoutExpand){

        if(CurrentLayout==null)
        {
            if(layoutExpand==layoutGoogle)
                expandGoogle();
            else if(layoutExpand==layoutTarget)
                expandTarget();
            else if(layoutExpand==layoutWholeFoods)
                expandWholeFoods();
            CurrentLayout=layoutExpand;

            return;
        }

        ValueAnimator va = ValueAnimator.ofFloat(CurrentLayout.getHeight(), 0);
        va.setDuration(800);
        va.setInterpolator(new AccelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                CurrentLayout.getLayoutParams().height = (int) value;
//                CurrentLayout.requestLayout();
            }
        });

        va.addListener( new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                CurrentLayout=layoutExpand;

                CurrentLayout.removeAllViews();

                if(layoutExpand==layoutGoogle)
                    expandGoogle();
                else if(layoutExpand==layoutTarget)
                    expandTarget();
                else if(layoutExpand==layoutWholeFoods)
                    expandWholeFoods();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();



       RecyclerView recycleView=(RecyclerView)getParent().getParent();

        if (recycleView!=null)
            recycleView.smoothScrollBy(0,getTop());



    }


    public void expandGoogle(){
        Log.d("TAG", "expand google");
           LayoutInflater.from(getContext()).inflate(R.layout.layout_history_google,layoutGoogle,true);

        layoutGoogle.measure(MeasureSpec.makeMeasureSpec(layoutGoogle.getWidth(),MeasureSpec.EXACTLY),0);
        float fFinalHeight=layoutGoogle.getMeasuredHeight();

        ValueAnimator va = ValueAnimator.ofFloat(0, fFinalHeight);
        va.setDuration(400);
        va.setInterpolator(new AccelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
               float value = (float) animation.getAnimatedValue();
               layoutGoogle.getLayoutParams().height = (int) value;
               layoutGoogle.requestLayout();

            }
        });

        va.start();


    }

    public void expandTarget(){
        Log.d("TAG","expand target");
        LayoutInflater.from(getContext()).inflate(R.layout.layout_history_target,layoutTarget,true);

    }


    public void expandWholeFoods(){
        Log.d("TAG","expand wholes ");
        LayoutInflater.from(getContext()).inflate(R.layout.layout_history_wholefoods,layoutWholeFoods,true);
    }
}
