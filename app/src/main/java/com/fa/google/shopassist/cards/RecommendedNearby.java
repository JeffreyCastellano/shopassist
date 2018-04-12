package com.fa.google.shopassist.cards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 9/3/15.
 */
public class RecommendedNearby extends BaseCardLayout {

    public RecommendedNearby(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onFinishInflate() {

        super.onFinishInflate();

        Log.d("TAG","Recommeneded");

        View viewRecommenedNearby=findViewById(R.id.id_recommended_nearby);


        if(AppState.getInstance().isLocationPhoneSection()){

            //viewRecommenedNearby.setVisibility(View.GONE);

        }

//        findViewById(R.id.btn_img_card).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //go to list
//
//                (new Handler()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse("http://techcrunch.com/2014/11/12/nexus-6-review-a-big-beautiful-cumbersome-beast"));
//                        ((MainActivity) getContext()).startActivity(intent);
//
//
//                    }
//                }, 300);
//
//            }
//        });

    }
}
