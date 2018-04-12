package com.fa.google.shopassist.cards;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.fa.google.shopassist.ListFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.VideoPlayerActivity;
import com.fa.google.shopassist.cards.Lists.ListViewFragment;
import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 3/4/15.
 */
public class VideoCard extends  BaseCardLayout {

    public VideoCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        findViewById(R.id.btn_img_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to list

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse("https://www.youtube.com/watch?v=FvoAMerBN3c"));
//                        intent.putExtra("force_fullscreen",true);
//
//                        ((MainActivity)getContext()).startActivity(intent);

                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                                //intent.setData(Uri.parse("https://www.youtube.com/watch?v=FvoAMerBN3c"));
                                //intent.putExtra("force_fullscreen", true);
                                intent.putExtra("url","http://fademos-3.s3.amazonaws.com/google/nexus6.mp4");
                                (getContext()).startActivity(intent);

                            }
                        }, 300);

                    }
                }, 300);

            }
        });

    }
}
