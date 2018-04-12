package com.fa.google.shopassist.cards;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import com.fa.google.shopassist.R;

/**
 * Created by mjoyce on 3/16/15.
 */
public class ExpandableLayout extends LinearLayout {

    private View content;
    private View btnExpand;
    private View btnCollapse;

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        content = this.findViewById(R.id.expandable_layout_content);
        btnExpand = this.findViewById(R.id.expandable_layout_expand);
        btnExpand.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand();
                    }
                },200);
            }
        });

        try {
            btnCollapse = this.findViewById(R.id.expandable_layout_collapse);
            btnCollapse.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            collapse();
                        }
                    }, 200);
                }
            });
        } catch(Exception e) {}
    }

    private void expand(){
        btnExpand.setVisibility(View.GONE);
        content.setVisibility(View.INVISIBLE);
        content.setAlpha(0);

        float fInitHeight = this.getHeight();
        this.measure(MeasureSpec.makeMeasureSpec(getWidth(),MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        float fFinalHeight=this.getMeasuredHeight();

        ValueAnimator va = ValueAnimator.ofFloat(fInitHeight, fFinalHeight);
        va.setDuration(300);
        va.setInterpolator(new AccelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ExpandableLayout.this.getLayoutParams().height = (int) value;
                ExpandableLayout.this.requestLayout();

            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                content.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(content, "alpha", 0f, 1f);
                anim.setDuration(300);
                anim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }

    private void collapse(){
        float fInitHeight=this.getHeight();
        content.setVisibility(View.GONE);
        btnExpand.setVisibility(View.INVISIBLE);
        Log.d("TAG","GET HEIGHT" +this.getHeight());
        this.measure(MeasureSpec.makeMeasureSpec(getWidth(),MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        float fFinalHeight=this.getMeasuredHeight();
        Log.d("TAG","GET MHEIGHT" +this.getMeasuredHeight());

        ValueAnimator va = ValueAnimator.ofFloat(fInitHeight, fFinalHeight);
        va.setDuration(200);
        va.setInterpolator(new AccelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ExpandableLayout.this.getLayoutParams().height = (int) value;
                ExpandableLayout.this.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnExpand.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }

}
