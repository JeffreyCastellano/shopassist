package com.fa.google.shopassist.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fa.google.shopassist.R;

import java.io.InputStream;

/**
 * Created by stevensanborn on 2/25/15.
 */
public class MapImageView extends View {

  //  public Rect r= new Rect(0,0 ,500,500);


    Drawable _map;

    public MapImageView(Context context) {
        super(context);
    }

    public MapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        InputStream mapInput = getResources().openRawResource(
                R.raw.img_map_store);
        _map = Drawable.createFromStream(mapInput, "transit_map");

        //_map.setBounds(0, 0, 540,890);

    }

    public MapImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    public void onDraw(Canvas canvas)
    {



    //    Drawable drawable = getDrawable();

//        int w = drawable.getIntrinsicHeight(),
//                h = drawable.getIntrinsicWidth();
//
//        Bitmap bmp = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
//        Canvas tmpCanvas = new Canvas(bmp);
//
//        // We're going to apply this paint eventually using a porter-duff xfer mode.
//        // This will allow us to only overwrite certain pixels. RED is arbitrary. This
//        // could be any color that was fully opaque (alpha = 255)
//        Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        xferPaint.setColor(Color.WHITE);
//
//        // We're just reusing xferPaint to paint a normal looking rounded box, the 20.f
//        // is the amount we're rounding by.
//        tmpCanvas.drawRect(this.r, xferPaint);

        // Now we apply the 'magic sauce' to the paint
       // xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

//        drawable.setBounds(0,0,600,500);

//        super.onDraw(canvas);

        _map.setBounds(-600, -600, _map.getIntrinsicWidth()+600, _map.getIntrinsicHeight()+600);
        _map.draw(canvas);

        //canvas.drawBitmap(bmp, 0, 0, xferPaint);
    }
}

