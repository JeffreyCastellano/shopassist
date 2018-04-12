package com.fa.google.shopassist.cards;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fa.google.shopassist.CompareFragment;
import com.fa.google.shopassist.ConfigureFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.cards.Lists.ListAddFragment;
import com.fa.google.shopassist.compare.CompareModalFragment;
import com.fa.google.shopassist.compare.CompareOverlayLayout;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.Product;

import java.io.IOException;
import java.text.NumberFormat;

/**
 * Created by stevensanborn on 3/2/15.
 */
public class ProductCard extends BaseCardLayout  {

    private static final String TAG = ProductCard.class.getSimpleName();

    public ProductCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private ImageView imgMain;
    private TextView textTitle;
    private TextView textPrice;
    private TextView textPrice2Year;
    private TextView textExcerpt;
        private TextView textMore;
    private ImageView imgExpand;
    private ImageView imgChevron;
    private View lessDivider;
    private View moreDivider;

    private Button btnBuy;

    private ImageButton btnAddToList;
    private ImageButton btnShare;
    private ImageButton btnCompare;

    private LinearLayout layoutExpand;
    private Product product;

    RecyclerView recycleView;

    private boolean bExpanded=false;

    @Override
    public void  onFinishInflate() {

        this.resourceIdCard=R.id.card;

        super.onFinishInflate();

        product = AppState.getInstance().CurrentProduct;


        if(!isInEditMode()) {
            //TODO
            if(product==null) {
                product = AppState.getInstance().arrProducts.get(0);
            }

            //set correct product image
            this.resourceIdCard = R.id.card_home_nexus6;
            this.imgMain=(ImageView)findViewById(R.id.img_product_main);
            this.btnBuy=(Button)findViewById(R.id.btn_product_buy);
            this.textTitle=(TextView)findViewById(R.id.text_product_title);
            this.textPrice=(TextView)findViewById(R.id.text_product_price);
            this.textPrice2Year=(TextView)findViewById(R.id.text_product_price_2year);
            this.textExcerpt=(TextView)findViewById(R.id.text_product_excerpt);
            this.btnAddToList=(ImageButton)findViewById(R.id.btn_product_add_to_list);
            this.btnCompare=(ImageButton)findViewById(R.id.btn_product_compare);
            this.btnShare=(ImageButton)findViewById(R.id.btn_product_share);
            this.imgExpand=(ImageView)findViewById(R.id.img_product_expanded);
            this.layoutExpand = (LinearLayout)findViewById(R.id.layout_product_expand);
            this.imgChevron=(ImageView)findViewById(R.id.img_product_chevron);
            this.textMore=(TextView) findViewById(R.id.text_product_more);
            this.lessDivider=findViewById(R.id.less_divider);
            this.moreDivider=findViewById(R.id.more_divider);

            this.setCurrentProduct();

            if(product.strId.equals("nexus6")) {
                btnBuy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Runnable R= new Runnable() {
                            @Override
                            public void run() {
                                ((MainActivity)getContext()).setConfigureFragment("Configure your Nexus 6");
                            }
                        };
                        (new Handler()).postDelayed(R,300);
                    }
                });
            } else {
                btnBuy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Runnable R= new Runnable() {
                            @Override
                            public void run() {
                                ((MainActivity)getContext()).addToCart(null);
                            }
                        };
                        (new Handler()).postDelayed(R,300);
                    }
                });
            }

            btnCompare.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(AppState.getInstance().CurrentProduct.strId.equals("nexus6") ||
                            AppState.getInstance().CurrentProduct.strId.equals("galaxys5"))
                    {

                        CompareModalFragment Compare = new CompareModalFragment();
                        Compare.strInitProduct = "nexus6";
                        Compare.show(((MainActivity) getContext()).getFragmentManager(), "Compare");
                        Compare.setRetainInstance(false);
                    }

                }
            });


            btnAddToList.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    ListAddFragment AddToListModal= new ListAddFragment();
                    AddToListModal.show(((MainActivity)getContext()).getFragmentManager(), "Add to List");

                }
            });

            btnShare=(ImageButton)findViewById(R.id.btn_product_share);
            btnShare.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, AppState.getInstance().CurrentProduct.strName + " : "+AppState.getInstance().CurrentProduct.strBlurb+" [" + NumberFormat.getCurrencyInstance().format(AppState.getInstance().CurrentProduct.fPrice)+"]");
                    sendIntent.setType("text/plain");
                    getContext().startActivity(Intent.createChooser(sendIntent, "Shop Assist " + AppState.getInstance().CurrentProduct.strName));
                }
            });

            this.layoutExpand.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    bExpanded=!bExpanded;

                    final LinearLayout ll=(LinearLayout)findViewById(R.id.layout_details);
                   // ll.measure(0,0);
                    float fInitHeight=ll.getHeight()+20;
                    float fFinalHeight;
                    if(bExpanded) {
                        textMore.setText("Show Less");
                        imgChevron.setRotation(180);
                        imgExpand.setVisibility(View.INVISIBLE);
                        lessDivider.setVisibility(View.VISIBLE);
                        moreDivider.setVisibility(View.GONE);
                        recycleView=(RecyclerView)getParent();

                        if (recycleView!=null)
                            recycleView.smoothScrollBy(0,getTop()+layoutExpand.getTop());

                        imgExpand.measure(MeasureSpec.makeMeasureSpec(ll.getWidth(),MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));

                        fFinalHeight=fInitHeight+imgExpand.getMeasuredHeight()-18;

                    }
                    else {
                        imgChevron.setRotation(0);
                        textMore.setText("More about "+product.strName);
                        imgExpand.setVisibility(View.GONE);
                        lessDivider.setVisibility(View.GONE);
                        moreDivider.setVisibility(View.VISIBLE);

                        if (recycleView!=null)
                            recycleView.smoothScrollBy(0,getTop());

                        ll.measure(MeasureSpec.makeMeasureSpec(cardView.getWidth(),MeasureSpec.EXACTLY),0);
                        fFinalHeight=ll.getMeasuredHeight();

                    }

                    //animate height
                    ValueAnimator va = ValueAnimator.ofFloat(fInitHeight, fFinalHeight);
                    va.setDuration(400);
                    va.setInterpolator(new AccelerateInterpolator());
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            ll.getLayoutParams().height = (int) value;
                            ll.requestLayout();

                        }
                    });
                    va.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(bExpanded) {
                                imgExpand.setVisibility(View.VISIBLE);
                                imgExpand.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_in));
                            }else{


                            }
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
            });

        }

    }

    public void setCurrentProduct(){


        if(product.strLongName!=null)
            this.textTitle.setText(product.strLongName);
        else
            this.textTitle.setText(product.strName);
        this.textPrice.setText("$"+(int)product.fPrice);

        if(product.strType.equals("phone")) {

            this.textPrice2Year.setText("$" + (int) product.fPrice2Year);

        }
        else{
            findViewById(R.id.layout_2year).setVisibility(View.GONE);
            findViewById(R.id.text_unlocked_product).setVisibility(GONE);
        }

        this.textExcerpt.setText(product.strBlurb);
        this.textMore.setText("More about "+product.strName);

        try
        {
            Bitmap bmp = BitmapFactory.decodeStream(getContext().getAssets().open(product.strImage));
            this.imgMain.setImageBitmap(bmp);



            if(product.strExpanded!=null) {
                Bitmap bmpExpand = BitmapFactory.decodeStream(getContext().getAssets().open(product.strExpanded));
                this.imgExpand.setImageBitmap(bmpExpand);
            }
            else{
                layoutExpand.setVisibility(View.GONE);
            }
        }
        catch (IOException e){
            Log.e(TAG," IO "+e.getLocalizedMessage());
        }
        catch (OutOfMemoryError e) {
            Log.e(TAG," MEM "+e.getLocalizedMessage());

        }
    }

}
