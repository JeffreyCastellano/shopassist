package com.fa.google.shopassist.speech;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by stevensanborn on 2/6/15.
 */
public class SpeechRecognizerListener implements RecognitionListener  {

    private SpeechRecognizerManager SRM;

    private float fRunningVoiceVol=0;

    public  SpeechRecognizerListener(SpeechRecognizerManager sr){
        this.SRM=sr;
    }

    private static final String TAG = SpeechRecognizerListener.class.getSimpleName();

    @Override
    public void onReadyForSpeech(Bundle params) {
        this.SRM.setReady();
    }

    @Override
    public void onBeginningOfSpeech() {
        this.SRM.setRecording();
    }


    @Override
    public void onRmsChanged(float rmsdB) {
       // Log.d(TAG,"VOL "+fRunningVoiceVol);
            if(rmsdB-fRunningVoiceVol>4.5f)
            {
                SRM.animatePulse();

            }
        fRunningVoiceVol=rmsdB;//(fRunningVoiceVol*3.0f)/4.0f +(rmsdB/4);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "end of  speech....");
//        this.main.btnImageVoice.setPressed(false);

    }


    @Override
    public void onError(int error) {

        Log.d(TAG, "errr...." + error);

        if(error== SpeechRecognizer.ERROR_NO_MATCH){
            this.SRM.nomatch();

        }
        else if(error==SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
            this.SRM.timeout();

        }
        else {


            this.SRM.recordingError();
        }

    }


    @Override
    public void onResults(Bundle results) {

        ArrayList<String> arr=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        Log.d(TAG,"final results...."+arr);


        this.SRM.setRecordingEnded(arr.get(0));

        float[] scores=results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);

        for(float s : scores)
            Log.d(TAG,"score :"+s);


    }

    @Override
    public void onPartialResults(Bundle partialResults) {

        ArrayList<String> arr=partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        this.SRM.setRecordingPartial(arr.get(0));
    }


    @Override
    public void onEvent(int eventType, Bundle params) {

    }

}
