package com.fa.google.shopassist.cards;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fa.google.shopassist.ListFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.ProductListFragment;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.compare.CompareModalFragment;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.Product;

import java.io.IOException;

/**
 * Created by stevensanborn on 2/19/15.
 */
public class NexusHomeCard  extends BaseCardLayout  implements ViewTreeObserver.OnGlobalLayoutListener{

    private ImageView btnImage;


    float fAspect;

    public NexusHomeCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void animateTransitionCard(){

        ViewTreeObserver vto = getViewTreeObserver();
        vto.removeOnGlobalLayoutListener(this);


        //hide the current card content
        ImageView imgView=(ImageView)cardView.findViewById(R.id.img_nexus_home_bg);
        ImageView imgViewMore=(ImageView)cardView.findViewById(R.id.img_nexus_more);
        imgView.animate().alpha(0).setDuration(300);
        imgViewMore.animate().alpha(0).setDuration(300);

        imgView.setVisibility(View.INVISIBLE);
        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        //get new Card to animate to
        final ProductCard CardLayout=(ProductCard) LayoutInflater.from(getContext()).inflate(R.layout.layout_product_card,null);


        CardLayout.cardView.measure(View.MeasureSpec.makeMeasureSpec(this.cardView.getWidth(), View.MeasureSpec.EXACTLY), 0);

        //hide the buy button
        final Button btnBuy=(Button)CardLayout.cardView.findViewById(R.id.btn_product_buy);
        CardLayout.cardView.setVisibility(View.VISIBLE);

        btnBuy.setVisibility(View.INVISIBLE);


        //animate w/h of the card
        ValueAnimator va = ValueAnimator.ofFloat(this.getHeight(), CardLayout.cardView.getMeasuredHeight());
        va.setDuration(300);
//        va.setInterpolator(new DecelerateInterpolator());
        final float transitionX=btnImage.getLeft();


        final float dpWidth = cardView.getWidth();

        final float fInitW=btnImage.getWidth();
        final float fInitH=btnImage.getHeight();

        final  float fDiffW=dpWidth-btnImage.getWidth();

        float fStaticHeight=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics());

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


                ScaleAnimation scaleAnimation= new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.5f);
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
                        ((MainActivity) getContext()).setFragment(LF,FragmentTransaction.TRANSIT_FRAGMENT_FADE,true);
                        invalidate();


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
            final NexusHomeCard that = this;


            super.onFinishInflate();

            try
            {

                this.measure(0,0);

                //set the thumb image
                Bitmap bmp = BitmapFactory.decodeStream(getContext().getAssets().open(AppState.getInstance().getProductById("nexus6").strImage));

                btnImage=(ImageView)this.findViewById(R.id.img_nexus6_home);

                btnImage.setImageBitmap(bmp);

                fAspect=(float)bmp.getHeight()/bmp.getWidth();

                
                ViewTreeObserver vto = getViewTreeObserver();

                vto.addOnGlobalLayoutListener(this);


            }
            catch (IOException e){
                Log.e("TAG"," IO "+e.getLocalizedMessage());
            }

        this.cardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AppState.getInstance().CurrentProduct = AppState.getInstance().getProductById("nexus6");
                parentFragment.get().animateOutWithLayout(that, R.array.nexus);

            }
        });

        }


    }

    @Override
    public void onGlobalLayout() {

        ImageView imgBG=(ImageView)this.findViewById(R.id.img_nexus_home_bg);

        ImageView imgThumb=(ImageView)findViewById(R.id.img_nexus6_home);

        imgBG.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), View.MeasureSpec.EXACTLY), 0);

        int newWidth=((int)(1.0f/fAspect* imgBG.getHeight()));
        int newHeight=(int)(newWidth*fAspect);
        if(btnImage.getLayoutParams().width!=newWidth || btnImage.getLayoutParams().height!=newHeight) {
            btnImage.setLayoutParams(new FrameLayout.LayoutParams(newWidth, newHeight));
        }
        if(((FrameLayout.LayoutParams) imgThumb.getLayoutParams()).leftMargin !=getWidth()-newWidth)
            ((FrameLayout.LayoutParams) imgThumb.getLayoutParams()).leftMargin = getWidth()-newWidth;

    }

@Override
protected void onLayout(boolean changed, int left, int top, int right, int bottom)
{
    // Android coordinate system starts from the top-left
    super.onLayout(changed,left,top,right,bottom);

    //imgThumb.invalidate();

}




}
