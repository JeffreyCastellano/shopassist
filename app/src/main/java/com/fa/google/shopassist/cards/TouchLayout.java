package com.fa.google.shopassist.cards;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;

import java.lang.ref.WeakReference;

/**
 * Created by mjoyce on 2/10/15.
 */
public class TouchLayout extends BaseCardLayout {

    WeakReference<ViewGroup> parent;

    float x1, x2, y1, y2, dx, dy, rawX, rawY, initLeft, offsetIndex;
    float alpha = 1;
    int width = 0;

    boolean bVertical = false;

    public TouchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        parent = new WeakReference<ViewGroup>((ViewGroup)getParent());
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent.requestDisallowInterceptTouchEvent(true);
                bVertical = false;
                initLeft = getLeft();
                x1 = event.getX();
                y1 = event.getY();
                rawX =  event.getRawX();
                rawY =  event.getRawY();
                width = getWidth();
                dx = rawX - x1;
                break;

            case MotionEvent.ACTION_MOVE:
                if(bVertical) {
                    return true;
                }

                x2 = event.getRawX();
                y2 = event.getRawY();

                dx = Math.abs(x2 - rawX);
                dy = Math.abs(y2 - rawY);

                if(!bVertical && (dy > 5f) && (dy > dx)) {
                    bVertical = true;
                    parent.requestDisallowInterceptTouchEvent(false);
                    return true;
                }

                dx = x2 - x1;
                offsetIndex = dx / (float)width;
                alpha = 1 - Math.abs(offsetIndex);
                setX(dx);
                setAlpha(alpha);
                break;

            case MotionEvent.ACTION_UP:
                ObjectAnimator animX;
                AnimatorSet anims = new AnimatorSet();
                anims.setInterpolator(new AccelerateInterpolator());

                parent.requestDisallowInterceptTouchEvent(false);
                bVertical = false;

                if(alpha < 0.4f) {

                    float finalX;

                    if(offsetIndex > 0f) {
                        finalX = dx + (float) width;
                    } else {
                        finalX = dx - (float) width;
                    }

                    animX = new ObjectAnimator().ofFloat(this, "x", dx, finalX).setDuration(100);
                    anims.play(animX);
                    anims.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            RecyclerView rv = (RecyclerView)parent;
                            CardAdapter ca = (CardAdapter) rv.getAdapter();
                            ca.notifyItemRemoved(rv.getChildPosition(TouchLayout.this));
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                } else {
                    if(Math.abs(dx - (float)initLeft) < 1f) {
                        callOnClick();
                        return true;
                    }
                    ObjectAnimator animAlpha = new ObjectAnimator().ofFloat(this, "alpha", getAlpha(), 1f).setDuration(300);
                    animX = new ObjectAnimator().ofFloat(this, "x", dx, (float) initLeft).setDuration(300);
                    anims.play(animX);
                    anims.play(animAlpha);
                }
                anims.start();
                break;

            default:
                break;
        }

        return true;


    }
    */

    @Override
    public void  onFinishInflate(){
        super.onFinishInflate();

        if(isInEditMode())return;;

        final TouchLayout that=this;


        cardView.setForeground(getSelectedItemDrawable());
        cardView.setClickable(true);
//        cardView.setVisibility(View.INVISIBLE);



        cardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                ((MainActivity)getContext()).onCardClicked(that);


//                ObjectAnimator oa = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.card_rise);
//                oa.setTarget(cardView);
//                oa.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        cardView.setElevation(6.0f);
//                        cardView.invalidate();
//
//                        ((MainActivity)getContext()).onCardClicked(that);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//                oa.start();

            }
        });



    }

    public Drawable getSelectedItemDrawable() {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta =(getContext()).obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }




}
