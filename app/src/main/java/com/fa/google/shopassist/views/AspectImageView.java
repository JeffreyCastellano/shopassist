package com.fa.google.shopassist.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class AspectImageView extends ImageView {


    public AspectImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.getDrawable();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


            int width = getMeasuredWidth();

            Drawable d = this.getDrawable(); //grab the drawable

            float fAspectRatio = (float) d.getIntrinsicWidth() / (float) d.getIntrinsicHeight();

            float fHeight = (float) width / fAspectRatio;//get new height

            setMeasuredDimension(width, (int) fHeight);

    }

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public AspectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}