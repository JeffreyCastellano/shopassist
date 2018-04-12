package com.fa.google.shopassist.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

/**
 * Created by stevensanborn on 5/20/15.
 */
public class AspectImageButton extends ImageButton {

    private int iSource;


    public AspectImageButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.getDrawable();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();

        Drawable d=this.getDrawable(); //grab the drawable

        float fAspectRatio=(float)d.getIntrinsicWidth()/(float)d.getIntrinsicHeight();

        float fHeight=(float)width/fAspectRatio;//get new height

        setMeasuredDimension(width, (int)fHeight);
    }

    public AspectImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.iSource= attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);


        // TODO Auto-generated constructor stub
    }

    public AspectImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.iSource= attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);

        // TODO Auto-generated constructor stub
    }
}
