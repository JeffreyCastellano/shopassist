package com.fa.google.shopassist.cards;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.ProductListFragment;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.globals.AppState;

import java.io.IOException;

/**
 * Created by stevensanborn on 3/3/15.
 */
public class SeidoCardHome extends BaseCardLayout  {

    private ImageView btnImage;

    public SeidoCardHome(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public void animateTransitionCard(){

        //hide the current card content
        ImageView imgView=(ImageView)cardView.findViewById(R.id.img_nexus_home_bg);
        imgView.animate().alpha(0).setDuration(300);
        imgView.setVisibility(View.INVISIBLE);



        //get new Card to animate to
        final ProductCard CardLayout=(ProductCard) LayoutInflater.from(getContext()).inflate(R.layout.layout_product_card,null);


        Log.d("TAG", "BASE " + CardLayout.cardView);
        CardLayout.cardView.measure(View.MeasureSpec.makeMeasureSpec(this.cardView.getWidth(), View.MeasureSpec.EXACTLY),0);
        CardLayout.cardView.setVisibility(View.VISIBLE);

        //hide the buy button
        final Button btnBuy=(Button)CardLayout.cardView.findViewById(R.id.btn_product_buy);
        btnBuy.setVisibility(View.INVISIBLE);


        //animate w/h of the card
        ValueAnimator va = ValueAnimator.ofFloat(this.getHeight(), CardLayout.cardView.getMeasuredHeight());
        va.setDuration(300);
        va.setInterpolator(new DecelerateInterpolator());
        final float transitionX=btnImage.getLeft();

//        ImageView imgBig=(ImageView)CardLayout.findViewById(R.id.img_product_main);
//        final Bitmap bfImageBig=((BitmapDrawable)imgBig.getDrawable()).getBitmap();
        final float dpWidth = cardView.getWidth();
        final float fInitW=btnImage.getWidth();
        final float fInitH=btnImage.getHeight();
        final  float fDiffW=dpWidth-btnImage.getWidth();
        float fStaticHeight= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics());

        final float fDiffH=fStaticHeight - btnImage.getHeight();

//        float DiffY=((FrameLayout.LayoutParams)btnImage.getLayoutParams()).height

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float time=animation.getAnimatedFraction();

                btnImage.setTranslationX(-time * transitionX);
//                ((FrameLayout.LayoutParams)btnImage.getLayoutParams()).width=btnImage.getWidth()+(int)((dpWidth-btnImage.getWidth())*time);
                ((FrameLayout.LayoutParams)btnImage.getLayoutParams()).width=(int)(animation.getAnimatedFraction()*fDiffW+fInitW);
//                ((FrameLayout.LayoutParams)btnImage.getLayoutParams()).height=btnImage.getHeight()+(int)((dpHeight-btnImage.getHeight()/3)*time);
                ((FrameLayout.LayoutParams)btnImage.getLayoutParams()).height=(int)(animation.getAnimatedFraction()*fDiffH+fInitH);
                btnImage.requestLayout();
                cardView.getLayoutParams().height = (int) value;
                cardView.getLayoutParams().width=(int)dpWidth;
                cardView.requestLayout();

            }
        });
        va.setStartDelay(300);
        va.start();

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                CardLayout.removeView(CardLayout.cardView);
                addView(CardLayout.cardView);
                CardLayout.cardView.setElevation(cardView.getElevation() + 1);
                CardLayout.cardView.setTranslationZ(cardView.getTranslationZ());
                CardLayout.cardView.setAlpha(0);
                CardLayout.cardView.animate().alpha(1).setDuration(300);


                ScaleAnimation scaleAnimation= new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.5f);
                scaleAnimation.setDuration(400);
                scaleAnimation.setInterpolator(new OvershootInterpolator());
                btnBuy.startAnimation(scaleAnimation);
                btnBuy.setVisibility(VISIBLE);
                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ProductListFragment LF= ProductListFragment.newInstance();
                        LF.resourceAnimEnter=LF.resourceAnimExit=0;
                        LF.bScrolled=true;//turn off animations
                        ((MainActivity) getContext()).setFragment(LF, FragmentTransaction.TRANSIT_NONE,false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });

    }



    @Override
    public void  onFinishInflate() {


        if(!isInEditMode()) {

            this.resourceIdCard = R.id.card_home_nexus6;
            final SeidoCardHome that = this;


            super.onFinishInflate();

            try
            {

                Bitmap bmp = BitmapFactory.decodeStream(getContext().getAssets().open(AppState.getInstance().getProductById("seidio_case").strImage));

                ((ImageView)this.findViewById(R.id.img_nexus6_home)).setImageBitmap(bmp);

            }
            catch (IOException e){
                Log.e("TAG"," IO "+e.getLocalizedMessage());
            }

            this.cardView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById("seidio_case");
                    parentFragment.get().animateOutWithLayout(that,R.array.nexus);

                }
            });

        }
        btnImage=(ImageView)this.findViewById(R.id.img_nexus6_home);


    }


}
