package com.fa.google.shopassist.compare;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.ModalFragment;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.VideoPlayerActivity;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.NFCTag;
import com.fa.google.shopassist.nfc.NFCListener;

import java.util.ArrayList;

/**
 * Created by stevensanborn on 2/16/15.
 */

public class CompareOverlayLayout extends RelativeLayout implements NFCListener{

    private ImageButton btnClose;

    private ScrollView scrollContent;
    ViewTreeObserver.OnScrollChangedListener mlistener;
    private LinearLayout scrollHeader;
    private ImageView imgPulseRepeat;

    private ImageView imgProduct1;
    private ImageView imgProduct2;
    private ImageView imgVersus;

    float fVel=0;
    int iLastScrollY=0;
    boolean bScrollHeader=false;
    private final  static  String TAG=CompareOverlayLayout.class.getSimpleName();


    private Context contxt;
    public ArrayList <String> arrProducts= new ArrayList<String>();

    public CompareOverlayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.contxt=context;

    }

    @Override
    public void onFinishInflate() {

        if(!isInEditMode())
        {


        final CompareOverlayLayout that=this;


        this.scrollContent=(ScrollView)findViewById(R.id.scroll_overlay_compare_results);
        this.scrollHeader=(LinearLayout)findViewById(R.id.layout_compare_scroll_header);

        this.imgProduct1=(ImageView)findViewById(R.id.img_tap_phone_1);

        if(AppState.getInstance().CurrentProduct!=null)
            setFirstProduct(AppState.getInstance().CurrentProduct.strId);


        this.imgProduct2=(ImageView)findViewById(R.id.img_tap_phone_2);

        this.imgPulseRepeat=(ImageView)findViewById(R.id.img_radar_2);

        this.scrollHeader.measure(0,0);
        this.scrollHeader.setTranslationY(-this.scrollHeader.getMeasuredHeight());

        ImageView imgArticle= (ImageView)findViewById(R.id.btn_img_compare_card_article);
        imgArticle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.phonearena.com/reviews/Samsung-Galaxy-S6-vs-Google-Nexus-6_id3982"));
                        contxt.startActivity(intent);


                    }
                }, 300);
            }
        });


        ImageView imgVideo=(ImageView)findViewById(R.id.btn_img_compare_card_video);

        imgVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                        intent.putExtra("url","http://fademos-3.s3.amazonaws.com/google/compare.mp4");
                        (getContext()).startActivity(intent);
                    }
                }, 300);
            }
        });


        scrollContent.setVisibility(View.GONE);

        scrollHeader.setVisibility(View.GONE);

            mlistener=new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {

                int scrollY = scrollContent.getScrollY(); //for verticalScrollView


                fVel=.9f*fVel+(0.1f*(iLastScrollY-scrollY));

                iLastScrollY=scrollY;

                Log.d(TAG,"SCOLLLR");

                if(scrollY>scrollHeader.getHeight() && fVel<0  && !bScrollHeader)
                    animateHeaderIn();
                else if(fVel>0 && bScrollHeader)
                    animateHeaderOut();
            }

        };


            //intro animation

            AnimationSet animInTap = getAnimInScale();
            animInTap.setStartOffset(500);

            animInTap.setAnimationListener( new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imgProduct1.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            this.imgVersus=(ImageView)findViewById(R.id.img_versus);
            this.imgVersus.setVisibility(View.VISIBLE);
            this.imgVersus.setAlpha(0f);

            ObjectAnimator animationY = ObjectAnimator.ofFloat(imgVersus, "rotationY", 180.0f, 360f);
            animationY.setStartDelay(700);
            animationY.setDuration(500);

            ObjectAnimator animationAlpha = ObjectAnimator.ofFloat(imgVersus, "alpha", 0.0f, 1f);
            animationAlpha.setStartDelay(700);
            animationAlpha.start();
            animationAlpha.setDuration(500);

            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imgVersus, "scaleX", 0.5f,Animation.RELATIVE_TO_SELF);
            scaleDownX.setDuration(500);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imgVersus, "scaleY", 0.5f,Animation.RELATIVE_TO_SELF);
            scaleDownY.setDuration(500);


            AnimatorSet animatorSet= new AnimatorSet();
            animatorSet.playTogether(animationY,animationAlpha,scaleDownX,scaleDownY);
            animatorSet.start();

          //  this.imgPulseRepeat.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.pulse));

            this.imgProduct1.startAnimation(animInTap);


            final AnimationSet animInTap2 = getAnimInScale();
            animInTap2.setStartOffset(900);
            animInTap2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    final ImageView imgNFC=(ImageView)findViewById(R.id.img_nfc);

                    imgNFC.setVisibility(VISIBLE);
                    imgNFC.setAlpha(0f);
                    imgNFC.setPivotX(imgNFC.getWidth()/2);
                    imgNFC.setPivotY(imgNFC.getHeight() / 2);
                    imgNFC.animate().alpha(1).setDuration(500).setInterpolator(new OvershootInterpolator(2)).withEndAction(new Runnable() {
                        @Override
                        public void run() {

                            AnimationSet animPulse = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.pulse);
                            animPulse.setDuration(650 * 2);

                            imgNFC.startAnimation(animPulse);
                            imgPulseRepeat.setPivotX(imgPulseRepeat.getWidth() / 2);
                            imgPulseRepeat.setPivotY(imgPulseRepeat.getHeight() / 2);
                            imgPulseRepeat.setScaleX(.75f);
                            imgPulseRepeat.setScaleY(.75f);
                            imgPulseRepeat.setVisibility(VISIBLE);
                            imgPulseRepeat.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pulse));


                        }
                    });


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.imgProduct2.startAnimation(animInTap2);
            this.imgProduct2.setVisibility(VISIBLE);



        }



    }

    public AnimationSet getAnimInScale(){

        AnimationSet animInTap = new AnimationSet(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(400);

        ScaleAnimation scaleAnimation = new ScaleAnimation(.5f,1,.5f,1,ScaleAnimation.RELATIVE_TO_SELF,.5f,ScaleAnimation.RELATIVE_TO_SELF,.5f);
        scaleAnimation.setDuration(300);


        animInTap.addAnimation(scaleAnimation);
        animInTap.addAnimation(alphaAnimation);

        animInTap.setInterpolator(new OvershootInterpolator(2));

        return  animInTap;
    }

    public void animateHeaderIn(){
        bScrollHeader=true;
        final LinearLayout layoutHeader=(LinearLayout)findViewById(R.id.layout_compare_header);
        TranslateAnimation translateAnimationHeader= new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,-layoutHeader.getHeight());
        translateAnimationHeader.setDuration(300);
        translateAnimationHeader.setFillAfter(true);
        translateAnimationHeader.setInterpolator(new AccelerateInterpolator());
        //translateAnimationHeader.setFillEnabled(true);
        layoutHeader.startAnimation(translateAnimationHeader);
        //

        TranslateAnimation translateAnimation= new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,-scrollHeader.getHeight(),Animation.ABSOLUTE,0);
        translateAnimation.setDuration(350);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        //translateAnimation.setFillEnabled(true);
        scrollHeader.startAnimation(translateAnimation);
        scrollHeader.setTranslationY(0);

    }

    public void animateHeaderOut(){
        bScrollHeader=false;
        LinearLayout layoutHeader=(LinearLayout)findViewById(R.id.layout_compare_header);
        TranslateAnimation translateAnimationHeader= new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,-layoutHeader.getHeight(),Animation.ABSOLUTE,0);
        translateAnimationHeader.setDuration(300);
        translateAnimationHeader.setInterpolator(new DecelerateInterpolator());
        layoutHeader.startAnimation(translateAnimationHeader);
        layoutHeader.setTranslationY(0);

        final int iHeight=scrollHeader.getHeight();
        TranslateAnimation translateAnimation= new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,-iHeight);
        translateAnimation.setDuration(350);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                scrollHeader.setTranslationY(-iHeight);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scrollHeader.startAnimation(translateAnimation);


    }
    public void setFirstProduct(String strId) {
        if(strId.equals("nexus6"))
            imgProduct1.setImageDrawable(getResources().getDrawable(R.drawable.img_compare_nexus));
        else
            imgProduct1.setImageDrawable(getResources().getDrawable(R.drawable.img_compare_samsung));

        this.arrProducts.add(strId);

    }
    public void addProduct(String strId){


        if (this.arrProducts.size() == 0) {
            imgProduct1.setImageDrawable(getResources().getDrawable(R.drawable.img_compare_nexus));
            imgProduct1.startAnimation(getAnimInScale());
            arrProducts.add(strId);
        }
        else if (this.arrProducts.size() == 1) {


            if(!this.arrProducts.get(0).equals("nexus6"))
                imgProduct2.setImageDrawable(getResources().getDrawable(R.drawable.img_compare_nexus));
            else
                imgProduct2.setImageDrawable(getResources().getDrawable(R.drawable.img_compare_samsung));


            //imgProduct2.setImageDrawable(getResources().getDrawable(R.drawable.img_compare_samsung));

            AnimationSet animSet=getAnimInScale();

            imgProduct2.startAnimation(animSet);


            findViewById( R.id.img_nfc).clearAnimation();
            findViewById( R.id.img_nfc).setVisibility(View.GONE);

            imgPulseRepeat.clearAnimation();
            imgPulseRepeat.setVisibility(View.GONE);
            

            animSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                    imgVersus.animate().alpha(0).scaleXBy(.15f).scaleYBy(.15f).setDuration(400).setInterpolator(new AnticipateOvershootInterpolator(2));


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {



//                            findViewById(R.id.layout_mode_tap).setVisibility(View.GONE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showResults();
                                }
                            },500);
                        }
                    },300);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.arrProducts.add(strId);

        } else if (this.arrProducts.size() == 2) {

            this.showResults();

        }


    }

    public void startPulse(){



    }

    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        if(isInEditMode())return;
        if(!AppState.getInstance().NFC.listeners.contains(this))
        AppState.getInstance().NFC.listeners.add(this);



    }

    public void cleanup(){

        scrollContent.getViewTreeObserver().removeOnScrollChangedListener(mlistener);

    }

    public void setUpListener(){
        scrollContent.getViewTreeObserver().addOnScrollChangedListener(mlistener);

    }

    @Override
    public void onDetachedFromWindow(){




        super.onDetachedFromWindow();
        if(AppState.getInstance().NFC.listeners.contains(this))
            AppState.getInstance().NFC.listeners.remove(this);

        if(imgPulseRepeat!=null)
            imgPulseRepeat.clearAnimation();



    }

    public void onNFCTag(NFCTag tag) {

        this.addProduct(tag.strId);

    }


/*
    public ImageView generatePulse(){

        final  RelativeLayout RL=(RelativeLayout)findViewById(R.id.layout_pulse_phone);

        final ImageView img = new ImageView(getContext());
        img.setBackground(getContext().getResources().getDrawable(R.drawable.ring_tap));
        RelativeLayout.LayoutParams RLParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        RLParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        RLParams.setMargins(0, 444, 0, 0);
        img.setLayoutParams(RLParams);
        RL.addView(img,0);
        return img;
    }

    public void animatePulse(ImageView img , int offset, final boolean repeat,float scale){

        final  RelativeLayout RL=(RelativeLayout)findViewById(R.id.layout_pulse_phone);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, scale, 1, scale, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(1300);
        scaleAnimation.setStartOffset(offset * 30);
        scaleAnimation.setInterpolator(new DecelerateInterpolator(2));

        AlphaAnimation animAlpha = new AlphaAnimation(.5f, 0);
//
        animAlpha.setDuration(1300);
        animAlpha.setStartOffset(offset * 50);
        animAlpha.setInterpolator(new DecelerateInterpolator());

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(animAlpha);
        final ImageView imgV=img;

        if(repeat) {
            animAlpha.setRepeatCount(Animation.INFINITE);
            animAlpha.setRepeatMode(Animation.RESTART);
            scaleAnimation.setRepeatCount(Animation.INFINITE);
            scaleAnimation.setRepeatMode(Animation.RESTART);
            animationSet.setRepeatCount(Animation.INFINITE);
            animationSet.setRepeatMode(Animation.RESTART);
        }

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(repeat) {
                    animation.start();
                    return;
                }
                Handler handler = new Handler();
                Runnable R = new Runnable() {
                    @Override
                    public void run() {
                        RL.removeView(imgV);
                    }
                };
                handler.postDelayed(R, 0);
            }

            @Overridemli
            public void onAnimationRepeat(Animation animation) {

            }
        });

        img.startAnimation(animationSet);

    }*/


    public void showResults(){


        ScrollView scrollView=(ScrollView)this.findViewById(R.id.scroll_overlay_compare_results);
        scrollView.setVisibility(View.VISIBLE);
        scrollHeader.setVisibility(View.VISIBLE);



        ImageView imgViewRight= (ImageView)findViewById(R.id.img_compare_1_right);
        ImageView imgViewLeft= (ImageView)findViewById(R.id.img_compare_1_left);
        ImageView imgViewStickyBar=(ImageView)findViewById(R.id.img_compare_sticky_bar);

        if(this.arrProducts.get(0).equals("nexus6"))
        {

            imgViewLeft.setImageDrawable(getResources().getDrawable(R.drawable.nexus_compare_results_b));
            imgViewRight.setImageDrawable(getResources().getDrawable(R.drawable.nexus_compare_results_a));
            imgViewStickyBar.setImageDrawable(getResources().getDrawable(R.drawable.nexus_compare_results_sticky_bar));

        }
        else{
            imgViewLeft.setImageDrawable(getResources().getDrawable(R.drawable.nexus_compare_results_a));
            imgViewRight.setImageDrawable(getResources().getDrawable(R.drawable.nexus_compare_results_b));
            imgViewStickyBar.setImageDrawable(getResources().getDrawable(R.drawable.nexus_compare_results_sticky_bar_flipped));
        }
        findViewById( R.id.layout_mode_tap).animate().alpha(0).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                findViewById( R.id.layout_mode_tap).setVisibility(View.GONE);
            }
        });

        for(int i= 0; i<((ViewGroup)scrollView.getChildAt(0)).getChildCount(); i++){

            View V= ((ViewGroup)scrollView.getChildAt(0)).getChildAt(i);
            V.setAlpha(0);

            V.animate().alpha(1).setDuration(i*300).setStartDelay(i*300).withEndAction(new Runnable() {
                @Override
                public void run() {

                }
            });


        }



        LinearLayout LLResults=(LinearLayout)findViewById(R.id.layout_overlay_compare_results);
        for(int i=0; i<LLResults.getChildCount(); i++){

            View v=LLResults.getChildAt(i);
            Animation alphaAnim=AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in);
            alphaAnim.setStartOffset(400*i);
            v.startAnimation(alphaAnim);


        }

        findViewById(R.id.progressBar).startAnimation(AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out));
        findViewById(R.id.progressBar).setVisibility(INVISIBLE);
    }


}
