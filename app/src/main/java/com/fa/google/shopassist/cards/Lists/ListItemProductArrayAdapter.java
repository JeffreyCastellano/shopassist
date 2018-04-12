package com.fa.google.shopassist.cards.Lists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fa.google.shopassist.R;
import com.fa.google.shopassist.models.ListItemRowModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevensanborn on 3/2/15.
 */
public class ListItemProductArrayAdapter extends ArrayAdapter<ListItemRowModel> {

    private final Context context;

    public ArrayList<ListItemRowModel> items = new ArrayList<ListItemRowModel>();


    public ListItemProductArrayAdapter(Context context, int textViewResourceId,
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView;

        if (convertView == null) {
            rowView = inflater.inflate(R.layout.layout_list_product_row, parent, false);

            final ImageView imageView = (ImageView) rowView.findViewById(R.id.img_row);

            final ListItemRowModel rowModel = this.items.get(position);


            BmpDecodeTask task = new BmpDecodeTask();
            if (imageView.getDrawable() == null)
                task.execute(imageView, rowModel.P.strListRow, getContext());

        } else
            rowView = convertView;
//        Log.d("TAG","oi "+position+" "+this.items.size());

        rowView.setBackgroundColor(context.getResources().getColor(R.color.white));

        return rowView;
    }


    // The definition of our task class
    private class BmpDecodeTask extends AsyncTask<Object, String, Bitmap> {

        public ImageView img;

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
//            Log.d("TAG","BACK");
            img = (ImageView) params[0];

            String uri = (String) params[1];

            Context context1 = (Context) params[2];

            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(context1.getAssets().open(uri));
            } catch (IOException e) {

                Log.e("IO error", "e " + e.getLocalizedMessage());
            }
            return bmp;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
//            Log.d("TAG", "POSTEXE" + img);

            if (img != null && result != null) {
                img.setImageBitmap(result);
                img.setAlpha(0f);
                img.animate().alpha(1).setDuration(300);
            }

        }
    }

}