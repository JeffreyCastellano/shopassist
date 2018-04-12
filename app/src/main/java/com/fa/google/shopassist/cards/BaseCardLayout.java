package com.fa.google.shopassist.cards;

import android.animation.Animator;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fa.google.shopassist.BaseFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;

import java.lang.ref.WeakReference;

/**
 * Created by stevensanborn on 2/19/15.
 */
public class BaseCardLayout extends RelativeLayout {

    public CardView cardView;
    protected ProgressBar progressBar;
    public int resourceIdCard=R.id.card;
    public int resourceIdProgress= R.id.progress_loader;
    public int iStartDelay=50;
    public WeakReference<BaseFragment> parentFragment;
  //  private Context mContext;



    public BaseCardLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
    //    mContext = context;

    }

    public void animateIn(){


        if(this.cardView!=null) {

            this.cardView.setTranslationY(300);
            cardView.setHasTransientState(true);


            cardView.animate().alpha(1).setDuration(350).setStartDelay(iStartDelay+500).translationY(0).setInterpolator(new DecelerateInterpolator(2)).withEndAction(new Runnable() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void run() {
                                                                                                                                                                            cardView.setHasTransientState(false);
                                                                                                                                                                            cardView.setAlpha(1);
                                                                                                                                                                        }
                                                                                                                                                                                                                                        }
            );
            cardView.setVisibility(VISIBLE);
        }
    }

    public void show(){

        this.cardView.clearAnimation();
        if(this.cardView.hasTransientState())
            this.cardView.setHasTransientState(false);
        this.cardView.setAlpha(1);
        this.cardView.setVisibility(VISIBLE);

    }

    @Override
    public void onFinishInflate() {

        super.onFinishInflate();

        if (isInEditMode())
            return;


        this.cardView=(CardView)this.findViewById(this.resourceIdCard);

        //hide the card view at first
       // this.cardView.setAlpha(0);
        this.cardView.setVisibility(INVISIBLE);
        this.cardView.setAlpha(0);

    }


    public void animateTransitionCard(){

        for(int i=0; i<this.cardView.getChildCount(); i++){
            View V= this.cardView.getChildAt(i);
            V.animate().alpha(0).setDuration(350);
            V.setVisibility(INVISIBLE);
        }
    }






}
