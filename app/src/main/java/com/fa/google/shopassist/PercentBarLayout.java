package com.fa.google.shopassist;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by stevensanborn on 5/11/15.
 */
public class PercentBarLayout extends RelativeLayout {

    private Context mContext;

    private ImageView imgPercent;
    private ImageView imgFull;
    private ImageView imgTh;
    private View viewDivider;

    private TextView txtTitle;
    private TextView txtValue;



    private String strTh;
    private long DURATION=500;
    private float fTotal;
    private int iValue=0;
    private boolean bHideLine=false;

    private int iDelay;
    private String strTitle;
    private String strEnding;


    public PercentBarLayout(Context context, AttributeSet attrs) {

        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PercentBarLayout,
                0, 0);



            iDelay = a.getInteger(R.styleable.PercentBarLayout_delay, 0);
            iValue = a.getInteger(R.styleable.PercentBarLayout_value, 0);
            fTotal= a.getFloat(R.styleable.PercentBarLayout_total, 1);
            strTh = a.getString(R.styleable.PercentBarLayout_thumb);
            strTitle=a.getString(R.styleable.PercentBarLayout_product);
            strEnding=a.getString(R.styleable.PercentBarLayout_ending);
            bHideLine=a.getBoolean(R.styleable.PercentBarLayout_hide_line,false);

    }
    @Override
    public void onFinishInflate(){

        if(isInEditMode())return;

        this.setBackgroundColor(getResources().getColor(R.color.white));
        this.imgTh=(ImageView)findViewById(R.id.img_progress_th);
        this.imgPercent=(ImageView)findViewById(R.id.imgPercent);
        this.txtTitle= (TextView)findViewById(R.id.text_progress_title);
        this.txtValue=(TextView)findViewById(R.id.text_value);
        this.imgFull=(ImageView)findViewById(R.id.imgFull);
        this.viewDivider=findViewById(R.id.progress_loader_divider);

        String file="file:///android_asset/"+strTh;

        Picasso.with(getContext()).load(file).into(imgTh);

        this.txtTitle.setText(this.strTitle);
//        this.imgTh.setVisibility(View.INVISIBLE);

        this.txtValue.setText(this.iValue+" "+this.strEnding);

        if(bHideLine)
            viewDivider.setVisibility(GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animatePercentageWidth();
            }
        },this.iDelay+300);

    }




    public void animatePercentageWidth(){

        final float fPercent=(float)iValue/fTotal;
        final float fWidthFinal=imgFull.getMeasuredWidth() *fPercent;

//        this.txtValue.setText(this.iValue+" "+this.strEnding);
        ValueAnimator anim = ValueAnimator.ofInt(0, (int)fWidthFinal );

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = imgPercent.getLayoutParams();
                layoutParams.width = val;
                imgPercent.setLayoutParams(layoutParams);
                int iAnimValue=Math.round(val/fWidthFinal* iValue);
//                txtValue.setText(iAnimValue+" "+strEnding);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator(2));
        anim.setDuration(DURATION);
        anim.start();

//        Animation animFade=AnimationUtils.loadAnimation(getContext(),R.anim.abc_fade_in);
//        animFade.setDuration(DURATION);
//        this.imgTh.startAnimation(animFade);
        this.imgTh.setVisibility(VISIBLE);
    }

}
