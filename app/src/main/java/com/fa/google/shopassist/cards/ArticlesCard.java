package com.fa.google.shopassist.cards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;

/**
 * Created by stevensanborn on 3/4/15.
 */
public class ArticlesCard extends BaseCardLayout {

    public ArticlesCard(Context context, AttributeSet attrs) {
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
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://techcrunch.com/2014/11/12/nexus-6-review-a-big-beautiful-cumbersome-beast"));
                        ((MainActivity)getContext()).startActivity(intent);


                    }
                }, 300);

            }
        });

    }
}
