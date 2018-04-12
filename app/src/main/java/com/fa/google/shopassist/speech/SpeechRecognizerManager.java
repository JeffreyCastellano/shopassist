package com.fa.google.shopassist.speech;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fa.google.shopassist.ActivityListener;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by stevensanborn on 2/6/15.
 */
public class SpeechRecognizerManager implements ActivityListener {

    private static String TAG =SpeechRecognizerManager.class.getSimpleName();

    private static final int REQUEST_CODE_VOICE = 10321;

    private ImageButton btnVoice;
    private TextView txtLiveSpeech;

    private RelativeLayout RLSpeechOverlay;

    ImageButton btnMicrophone;

    public SpeechRecognizer SR;

    public MainActivity main;

    private  boolean bRecording =false;


    public interface SpeechRecognizerManagerListener{

        public void  onCompleteSpeech(String stResult);


    };

    public SpeechRecognizerManagerListener listener=null;

    public SpeechRecognizerManager(MainActivity activity){

        this.main=activity;
        this.main.activityListeners.add(this);

    }

    public void onResult(int requestCode, int resultCode, Intent data){




        if (requestCode == REQUEST_CODE_VOICE && resultCode == Activity.RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            //ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //editSearch.setText(matches.get(0));


//            String confidenceExtra = RecognizerIntent.EXTRA_CONFIDENCE_SCORES;
//            float[] confidence = results.getFloatArrayExtra(confidenceExtra);




        }
    }

    public void onDestroy(){

        this.SR.destroy();

    }

    public void onStart(){}

    public void onStop(){}

    public void onCreate(Bundle savedInstanceState){

        this.createSpeech();

        btnMicrophone = (ImageButton)this.main.findViewById(R.id.btn_microphone);
        btnMicrophone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addRLSpeechOverlay();

            }

        });
    }


    public void createSpeech(){
        SR = android.speech.SpeechRecognizer.createSpeechRecognizer(this.main);
        SR.setRecognitionListener(new SpeechRecognizerListener(this));
    }

    public void resetRecording(){
        this.SR.stopListening();
        this.SR.cancel();
        this.SR.destroy();


        createSpeech();
        bRecording=false;
        btnVoice.clearAnimation();
    }

    public void addRLSpeechOverlay(){


        final ViewGroup VG= (ViewGroup)main.findViewById(android.R.id.content);
        main.getLayoutInflater().inflate(R.layout.speech_overlay, VG);

        RelativeLayout rlSpeechOverlayHolder=(RelativeLayout)VG.findViewById(R.id.layout_speakresults_holder);
        rlSpeechOverlayHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSpeechLayer();
            }
        });
        final RelativeLayout rlSpeechOverlay=(RelativeLayout)VG.findViewById(R.id.layout_speakresults);
        ImageButton btnClose=(ImageButton)rlSpeechOverlay.findViewById(R.id.btn_close_speak);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSpeechLayer();
            }
        });
       // VG.addView(rlSpeechOverlay);

        RLSpeechOverlay=rlSpeechOverlay;
        RLSpeechOverlay.setElevation(20);//give it some elevation
        RLSpeechOverlay.setZ(10);

        //animate in
        Animator anim = ViewAnimationUtils.createCircularReveal(RLSpeechOverlay.findViewById(R.id.layout_speakresults), btnMicrophone.getLeft() + btnMicrophone.getWidth() / 2, btnMicrophone.getTop() + btnMicrophone.getHeight() / 2 - 600, 0, 1776);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
        anim.start();


        txtLiveSpeech=(TextView)RLSpeechOverlay.findViewById(R.id.text_speakresults);
        txtLiveSpeech.setText("Initiating...");

        //add voice button
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);

        btnVoice= new ImageButton(this.main);
        btnVoice.setImageResource(R.drawable.ic_google_voice);
        btnVoice.setLayoutParams(lp);
        btnVoice.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30, this.main.getResources().getDisplayMetrics()));
        btnVoice.setTranslationZ(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50, this.main.getResources().getDisplayMetrics()));
        btnVoice.setBackgroundColor(main.getResources().getColor(R.color.transparent));
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bRecording)
                    closeSpeechLayer();
                else
                    startListening();
            }
        });

        RLSpeechOverlay.addView(btnVoice);
        this.resetRecording();

        AnimationSet animationSet = new AnimationSet(false);

        btnVoice.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int iNewLeft=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,740.0f/3, this.main.getResources().getDisplayMetrics());
        final int iNewTop=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,110.0f/3, this.main.getResources().getDisplayMetrics());
        final int iNewRight=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30.0f/3, this.main.getResources().getDisplayMetrics());

        lp.topMargin=iNewTop;
//        lp.leftMargin=iNewLeft;
        lp.rightMargin=iNewRight;
//        TranslateAnimation transAnim= new TranslateAnimation(btnMicrophone.getLeft(),iNewLeft,btnMicrophone.getTop(),iNewTop);
//        transAnim.setDuration(400);
//        transAnim.setStartOffset(300);
//        transAnim.setFillEnabled(true);
//        transAnim.setInterpolator(new AccelerateDecelerateInterpolator());
//        btnVoice.startAnimation(transAnim);

        AlphaAnimation animAlpha= new AlphaAnimation(0,1);
        animAlpha.setDuration(250);
        animationSet.addAnimation(animAlpha);
        animAlpha.setStartOffset(300);
      //  animationSet.addAnimation(transAnim);
        btnVoice.startAnimation(animationSet);
        btnVoice.setLayoutParams(lp);

//        btnVoice.setTranslationX(iNewLeft);
//        btnVoice.setTranslationY(iNewTop);

            animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

//                RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)btnVoice.getLayoutParams();
//                lp.topMargin=iNewTop;
//                lp.leftMargin=iNewLeft;
//                btnVoice.setLayoutParams(lp);
                startListening();;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //animate btnIn
    }


    public void closeSpeechLayer(){

        this.resetRecording();

        //fade btn
        AlphaAnimation animAlpha= new AlphaAnimation(1,0);
        animAlpha.setDuration(250);
        animAlpha.setStartOffset(300);

        final ViewGroup VG= (ViewGroup)main.findViewById(android.R.id.content);

        Animator anim = ViewAnimationUtils.createCircularReveal(RLSpeechOverlay.findViewById(R.id.layout_speakresults),(int) btnVoice.getLeft() + btnVoice.getWidth() / 2, (int)btnVoice.getTop() + btnVoice.getHeight() / 2 , 1776, 0);
        anim.setDuration(300);
        Log.d(TAG,"btn "+btnVoice.getLeft());
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                VG.removeView((View)RLSpeechOverlay.getParent());
                btnVoice=null;
                RLSpeechOverlay=null;
            }
        });


        anim.start();
        btnVoice.startAnimation(animAlpha);

        this.resetRecording();



    }

    public void startListening(){
        main.hideKeyboard();
        btnVoice.setImageDrawable(this.main.getResources().getDrawable(R.drawable.ic_google_voice));
        //Start Voice Recording
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());

        if(intent.hasExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE))
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.main.getApplication().getPackageName());

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS,true);
        intent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES, true);

        SR.startListening(intent);
//        startActivityForResult(intent, 0);
    }

    public void setReady(){

        txtLiveSpeech.setText("Speak Now");
        btnVoice.startAnimation(AnimationUtils.loadAnimation(this.main,R.anim.pulse));
    }

    public void setRecording(){

        Log.d(TAG,"voice recording...");

        if(bRecording==false) {
            txtLiveSpeech.setText("Listening");

        }


        this.animatePulse();

        //mark as recording
        bRecording=true;
    }

    public void animatePulse(){
        //add ring animation

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(btnVoice.getWidth(),btnVoice.getHeight());
        lp.setMargins(btnVoice.getLeft(),btnVoice.getTop(),0,0);
        final ImageView img= new ImageView(this.main);
        img.setBackground(this.main.getResources().getDrawable(R.drawable.ring));
        img.setLayoutParams(lp);
        img.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, this.main.getResources().getDisplayMetrics()));
        img.setTranslationZ(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50, this.main.getResources().getDisplayMetrics()));

        this.RLSpeechOverlay.addView(img);

        //animate ring

        ScaleAnimation scaleAnimation= new ScaleAnimation(1,1.6f,1,1.6f,ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setInterpolator(new DecelerateInterpolator(2));

        AlphaAnimation animAlpha= new AlphaAnimation(1,0);
//        animAlpha.setStartOffset(300);
        animAlpha.setDuration(500);
        animAlpha.setInterpolator(new DecelerateInterpolator());

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(animAlpha);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Handler handler= new Handler();
                Runnable R=new Runnable() {
                    @Override
                    public void run() {
                        RLSpeechOverlay.removeView(img);

                    }
                };
                handler.postDelayed(R,0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img.startAnimation(animationSet);
    }

    public void setRecordingEnded(final String strResults){
        this.txtLiveSpeech.setText(strResults);

        this.resetRecording();

        //animate close
        final ViewGroup VG= (ViewGroup)main.findViewById(android.R.id.content);

        Animator anim = ViewAnimationUtils.createCircularReveal(RLSpeechOverlay.findViewById(R.id.layout_speakresults), btnMicrophone.getLeft() + btnMicrophone.getWidth() / 2+50, btnMicrophone.getTop() + btnMicrophone.getHeight() / 2+20 ,1776,0);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                VG.removeView((View)RLSpeechOverlay.getParent());
                RLSpeechOverlay=null;
            }
        });

        anim.start();


        AnimationSet animationSet = new AnimationSet(false);

      //  TranslateAnimation transAnim= new TranslateAnimation(0,btnMicrophone.getLeft()-btnVoice.getLeft()-40,0,-btnVoice.getTop()-50);
//        transAnim.setDuration(500);
//        transAnim.setFillAfter(true);
//        transAnim.setFillBefore(false);
//        transAnim.setInterpolator(new DecelerateInterpolator(2));

        ScaleAnimation scaleAnimation= new ScaleAnimation(1,.7f,1,.7f,ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setInterpolator(new DecelerateInterpolator(2));
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setFillBefore(false);

        AlphaAnimation animAlpha= new AlphaAnimation(1,0);
        animAlpha.setStartOffset(200);
        animAlpha.setDuration(300);
        animAlpha.setInterpolator(new DecelerateInterpolator(2));
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(animAlpha);
//        animationSet.addAnimation(transAnim);

        animationSet.setFillAfter(true);
        animationSet.setFillBefore(false);
        btnVoice.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(listener!=null)
                    listener.onCompleteSpeech(strResults);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public void setRecordingPartial(String strResults){
        this.txtLiveSpeech.setText(strResults);
    }

    public void timeout(){
        this.resetRecording();
        txtLiveSpeech.setText("Could not understand, tap microphone.");
        if(btnVoice!=null)
            btnVoice.setImageResource(R.drawable.ic_google_voice_off);
    }

    public void nomatch(){
        this.resetRecording();
        txtLiveSpeech.setText("Speech timed out, tap microphone.");
        if(btnVoice!=null)
            btnVoice.setImageResource(R.drawable.ic_google_voice_off);
    }

    public void recordingError(){
        this.resetRecording();
        txtLiveSpeech.setText("Voice Error.");
        if(btnVoice!=null)
            btnVoice.setImageResource(R.drawable.ic_google_voice_off);
    }


    public void onResume(){}
    public void onPause(){}
    public void onNewIntent(Intent intent){}

}
