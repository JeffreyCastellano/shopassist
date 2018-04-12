package com.fa.google.shopassist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fa.google.shopassist.globals.AppState;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import java.io.IOException;

/**
 * Created by stevensanborn on 3/13/15.
 */
public class EventFragment  extends BaseFragment {

    ViewGroup VG;

    private Button btnContinue;
    private ImageView imgCheck;
    private TextView txtHeadline;
    private TextView txtDetail;
    private Button btnRSVP;

    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String title)
    {

        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ListFragment.ARG_PARAM2, title);
        fragment.setArguments(args);
        fragment.setFragmentTransactionValues(R.animator.slide_frag_in, R.animator.slide_frag_out, R.animator.slide_frag_back_in, R.animator.slide_frag_back_out);

        return fragment;
    }


    public EventFragment()

    {
        super();

        bOverlay = true;


        this.setFragmentTransactionValues(R.animator.slide_frag_in, R.animator.slide_frag_out, R.animator.slide_frag_back_in, R.animator.slide_frag_back_out);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        this.VG = (ViewGroup) inflater.inflate(R.layout.layout_event, null);


        ImageView imgMain=(ImageView)this.VG.findViewById(R.id.img_event_main);

        try
        {

            imgMain.setImageBitmap(BitmapFactory.decodeStream(getActivity().getAssets().open(AppState.getInstance().arrEvents.get(AppState.getInstance().iCurrentEvent))));

        }catch (IOException e){
            Log.e("IO Exception", "e " + e.getLocalizedMessage());
        }


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        imgMain.measure(View.MeasureSpec.makeMeasureSpec(size.x,View.MeasureSpec.EXACTLY),0);

        float fHeight=imgMain.getMeasuredWidth()/1.35f;

        btnContinue=(Button)VG.findViewById(R.id.btn_confirm_continue);


        imgCheck=(ImageView)VG.findViewById(R.id.img_confirm_check);
        txtHeadline=(TextView)VG.findViewById(R.id.confirmation_headline);
        txtDetail=(TextView)VG.findViewById(R.id.confirmation_detail);

        btnRSVP=(Button)VG.findViewById(R.id.btn_rsvp);

        btnRSVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animateInConfirmation(v);
            }
        });
        btnRSVP.setVisibility(View.INVISIBLE);

        btnRSVP.measure(0,0);

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(btnRSVP.getLayoutParams());
        params.topMargin=(int)fHeight-btnRSVP.getMeasuredHeight()/2;
        params.rightMargin=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());;
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnRSVP.setLayoutParams(params);


        btnContinue.setVisibility(View.INVISIBLE);
        txtHeadline.setVisibility(View.INVISIBLE);
        imgCheck.setVisibility(View.INVISIBLE);
        txtDetail.setVisibility(View.INVISIBLE);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Handler handler= new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).continueShopping(null);
                    }
                },200);
            }
        });


       ImageButton  btnBack=(ImageButton)this.VG.findViewById(R.id.btn_list_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).onBackPressed();
                    }
                }, 300);
            }
        });


        //animate in View

        ;
        VG.setVisibility(View.INVISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(500);
        alphaAnimation.setStartOffset(300);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        VG.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnRSVP.setVisibility(View.VISIBLE);
                ScaleAnimation scaleAnimation= new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.5f);
                scaleAnimation.setDuration(400);
                scaleAnimation.setInterpolator(new OvershootInterpolator());
                btnRSVP.startAnimation(scaleAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        VG.setVisibility(View.VISIBLE);




        return VG;

    }

    public void animateInConfirmation(View v){
        final FrameLayout flConfirm=(FrameLayout)VG.findViewById(R.id.layout_event_confirmation);

        v.animate().alpha(0).setDuration(300);
        v.setVisibility(View.INVISIBLE);
        //animate

        Animator anim = ViewAnimationUtils.createCircularReveal(flConfirm, v.getLeft()+v.getWidth()/2, v.getTop()+v.getHeight()/2, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 640, getResources().getDisplayMetrics()));
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());

        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);


                AnimationSet animScale= (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.intro_scale_in);
                animScale.setInterpolator(new OvershootInterpolator(2));
                Animation animFade= AnimationUtils.loadAnimation(getActivity(),R.anim.intro_slide_up_in);
                Animation animFade2= AnimationUtils.loadAnimation(getActivity(),R.anim.intro_slide_up_in);
                Animation animFadeSlow= new AlphaAnimation(0,1);
                animFadeSlow.setDuration(800);

                animFade.setStartOffset(500);
                animFade2.setStartOffset(700);
                animFadeSlow.setStartOffset(1000);

                animScale.setInterpolator(new AnticipateOvershootInterpolator(.85f));
                animFade.setInterpolator(new DecelerateInterpolator(2));
                animFade2.setInterpolator(new DecelerateInterpolator(2));
                animFadeSlow.setInterpolator(new DecelerateInterpolator(2));

                imgCheck.startAnimation(animScale);
                txtHeadline.startAnimation(animFade);
                txtDetail.startAnimation(animFade2);
                btnContinue.startAnimation(animFadeSlow);


                imgCheck.setVisibility(View.VISIBLE);
                txtHeadline.setVisibility(View.VISIBLE);
                txtDetail.setVisibility(View.VISIBLE);
                btnContinue.setVisibility(View.VISIBLE);

            }
        });
        anim.start();
        flConfirm.setVisibility(View.VISIBLE);

    }
}
