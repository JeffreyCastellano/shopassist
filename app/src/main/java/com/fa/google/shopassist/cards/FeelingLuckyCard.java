package com.fa.google.shopassist.cards;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.ProductListFragment;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 2/24/15.
 */
public class FeelingLuckyCard extends BaseCardLayout {

    private final  static String TAG=FeelingLuckyCard.class.getSimpleName();

    public FeelingLuckyCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean bExapanded=false;

    RelativeLayout RL;

    ImageView imgHome;

    RecyclerView recycleView;

    LinearLayout layoutExpand;

    int iIndex=0;

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        final ImageView imgHome=(ImageView)findViewById(R.id.home_lucky);

        RL=(RelativeLayout)findViewById(R.id.home_lucky_content_layout);

        layoutExpand=(LinearLayout)findViewById(R.id.layout_lucky_expanded);

        final ImageView imgNewImage=(ImageView)findViewById(R.id.img_lucky_expand);


        ImageButton btnShowAnother= (ImageButton)findViewById(R.id.btn_lucky_show_another);


        imgNewImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (iIndex) {
                    case 0:
                        AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById("moto360");

                    break;
                    case 1:
                        AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById("chromecast");
                    break;
                    case 2:
                        AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById("sony_earbuds");
                    break;
                    case 3:
                        AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById("bose");
                        break;
                }
                ProductListFragment LF= ProductListFragment.newInstance();
                LF.resourceAnimEnter=LF.resourceAnimExit=0;
                ((MainActivity) getContext()).setFragment(LF, FragmentTransaction.TRANSIT_NONE,false);
            }
        });
        btnShowAnother.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iIndex=(iIndex+1)%4;

                switch (iIndex){

                    case 0:
                        imgNewImage.setImageDrawable(getResources().getDrawable(R.drawable.home_lucky_expand_1));
                    break;

                    case 1:
                        imgNewImage.setImageDrawable(getResources().getDrawable(R.drawable.home_lucky_expand_2));
                    break;

                    case 2:
                        imgNewImage.setImageDrawable(getResources().getDrawable(R.drawable.home_lucky_expand_3));
                    break;

                    case 3:
                        imgNewImage.setImageDrawable(getResources().getDrawable(R.drawable.home_lucky_expand_4));
                    break;


                }
                imgNewImage.setAlpha(0f);
                imgNewImage.animate().alpha(1).setDuration(500);


            }
        });


        if(AppState.getInstance().CurrentZone==null || AppState.getInstance().CurrentZone.strId.equals("googleplay"))

            imgHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                recycleView=(RecyclerView)getParent();

                Log.d(TAG," "+recycleView);

//                imgHome.animate().alpha(.99).setDuration(300).withEndAction();


                imgNewImage.setVisibility(View.INVISIBLE);

                layoutExpand.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recycleView.smoothScrollBy(0,getTop());
                        layoutExpand.setVisibility(View.VISIBLE);
                        imgNewImage.setVisibility(View.VISIBLE);
                        imgHome.animate().alpha(0).setDuration(400);
                        layoutExpand.animate().alpha(1).setDuration(500).setStartDelay(1400);
                        imgHome.setVisibility(View.GONE);

                    }
                },300);


                final float fWidthInit=RL.getWidth();
                final float fHeightInit=RL.getHeight();

                RL.measure(0, 0);
                RL.requestLayout();
                recycleView.requestLayout();

            }
        });
    }




}
