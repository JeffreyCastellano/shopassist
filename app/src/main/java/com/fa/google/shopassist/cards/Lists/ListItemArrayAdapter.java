package com.fa.google.shopassist.cards.Lists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fa.google.shopassist.R;
import com.fa.google.shopassist.models.ListItemRowModel;
import com.fa.google.shopassist.models.ListModel;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevensanborn on 2/27/15.
 */
public class ListItemArrayAdapter extends ArrayAdapter<ListItemRowModel> {

    private final Context context;

    public ArrayList<ListItemRowModel> items = new ArrayList<ListItemRowModel>();

    public boolean bCustom=false;

    public ListItemArrayAdapter(Context context, int textViewResourceId,
                                List<ListItemRowModel> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;

        //copy data
        for (ListItemRowModel L : objects) {
            this.items.add(L);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        if(convertView==null) {
            rowView = inflater.inflate(R.layout.layout_list_row_layout, parent, false);

            final ImageView imageViewMain = (ImageView) rowView.findViewById(R.id.img_row_img);

            final  ListItemRowModel rowModel = this.items.get(position);

            LinearLayout layoutCustom=(LinearLayout)rowView.findViewById(R.id.layout_custom);
            if(rowModel.strRowImage!=null){
                BmpDecodeTask task = new BmpDecodeTask();
                    task.execute(imageViewMain,rowModel.strRowImage,getContext());
                layoutCustom.setVisibility(View.GONE);
            }else{
                imageViewMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                imageViewMain.setTag("custom");
                BmpDecodeTask task = new BmpDecodeTask();

                task.execute(imageViewMain,rowModel.strRowThImage,getContext());

                ((TextView)rowView.findViewById(R.id.text_list_row_title)).setText(rowModel.strListName);
                ((TextView)rowView.findViewById(R.id.text_list_row_followers)).setText(((ListModel)rowModel).iFollowers+" Followers");
            }

        }
        else
            rowView=convertView;

        //rowView.setBackgroundColor(context.getResources().getColor(R.color.white));

        return rowView;
    }


    // The definition of our task class
    private class BmpDecodeTask extends AsyncTask<Object,String, Bitmap> {

        public ImageView img;

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Object...  params) {

            img= (ImageView) params[0];

            String uri = (String)params[1];

            Context context1=(Context)params[2];

            Bitmap bmp=null;
            try {
                bmp=BitmapFactory.decodeStream(context1.getAssets().open(uri));
            }catch (IOException e){

                Log.e("IO error","e "+e.getLocalizedMessage());
            }

            //float fHeightRow= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95, getResources().getDisplayMetrics());
            //fHeightRow=285*metrics.widthPixels/1080;


            return bmp;
        }



        @Override
        protected void onPostExecute(Bitmap result) {

            if(img!=null && result!=null) {
                LinearLayout LL=(LinearLayout)img.getParent();

                if(img.getTag()!=null && img.getTag().equals("custom")) {
                    Log.d("TAG", "-" + LL.getWidth());
                    float fAspect = (float) result.getHeight() / (float) result.getWidth();
                    float fWidth = (LL.getWidth() * .328f);
                    img.getLayoutParams().width = (int)fWidth;
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    img.getLayoutParams().height = (int) (fAspect * fWidth);
                }
                img.setImageBitmap(result);
                img.setAlpha(0f);
                img.animate().alpha(1).setDuration(300);

            }

        }
    }
}
