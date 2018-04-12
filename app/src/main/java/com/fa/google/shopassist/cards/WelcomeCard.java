package com.fa.google.shopassist.cards;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fa.google.shopassist.ListFragment;
import com.fa.google.shopassist.OverlayListViewFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.cards.Lists.ListViewFragment;
import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 3/3/15.
 */
public class WelcomeCard extends BaseCardLayout {

    public TextView textViewName;

    public WelcomeCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) return;

        ImageView img=(ImageView) findViewById(R.id.img_store_welcome);
        ImageView imgDetails=(ImageView) findViewById(R.id.img_store_welcome_details);

        if(AppState.getInstance().CurrentZone==null) {
            img.setImageDrawable(getResources().getDrawable(R.drawable.img_welcome_google_store));
            imgDetails.setImageDrawable(getResources().getDrawable(R.drawable.img_welcome_google_details));
        }
        else if (AppState.getInstance().CurrentZone.strId.equals("target")) {
            img.setImageDrawable(getResources().getDrawable(R.drawable.img_welcome_target_store));
            imgDetails.setImageDrawable(getResources().getDrawable(R.drawable.img_welcome_target_details));
        }
        else if (AppState.getInstance().CurrentZone.strId.equals("wholefoods")){
            img.setImageDrawable(getResources().getDrawable(R.drawable.img_welcome_wf_store));
            imgDetails.setImageDrawable(getResources().getDrawable(R.drawable.img_welcome_wf_details));
        }
        else{
            img.setImageDrawable(getResources().getDrawable(R.drawable.img_welcome_google_store));
            imgDetails.setImageDrawable(getResources().getDrawable(R.drawable.img_welcome_google_details));
        }

        textViewName=(TextView)findViewById(R.id.txt_welcome);

        OnClickListener clickListener=new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        ImageButton btnStore=(ImageButton)findViewById(R.id.btn_store_map);
        ImageButton btnPhotos=(ImageButton)findViewById(R.id.btn_photo_gallery);
        if(AppState.getInstance().CurrentZone==null || AppState.getInstance().CurrentZone.strId.equals("googleplay")) {
            btnStore.setImageDrawable(getResources().getDrawable(R.drawable.home_store_map));
            btnPhotos.setImageDrawable(getResources().getDrawable(R.drawable.home_photo_gallery));
        }
        else if (AppState.getInstance().CurrentZone.strId.equals("target")) {
            btnStore.setImageDrawable(getResources().getDrawable(R.drawable.home_store_map_target));
            btnPhotos.setImageDrawable(getResources().getDrawable(R.drawable.home_photo_gallery_target));
            btnPhotos.setOnClickListener(clickListener);
//            btnStore.setOnClickListener(clickListener);
        }
        else if (AppState.getInstance().CurrentZone.strId.equals("wholefoods")) {
            btnStore.setImageDrawable(getResources().getDrawable(R.drawable.home_store_map_wholefoods));
            btnPhotos.setImageDrawable(getResources().getDrawable(R.drawable.home_photo_gallery_wholefoods));
            btnPhotos.setOnClickListener(clickListener);
//            btnPhotos.setOnClickListener(clickListener);
        }
        SharedPreferences sharedPref = ((MainActivity)getContext()).getSharedPreferences(((MainActivity)getContext()).getString(R.string.gsa_prefs), 0);
        textViewName.setText("Welcome, " + sharedPref.getString("first_name", "Marc") );



        findViewById(R.id.btn_explore_store).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AppState.getInstance().CurrentZone!=null && AppState.getInstance().CurrentZone.strId.equals("target"))
                    ((MainActivity) getContext()).setFragment(ListFragment.newInstance(R.array.explore_store_target, "Explore Store"),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,true);
                else if(AppState.getInstance().CurrentZone!=null && AppState.getInstance().CurrentZone.strId.equals("wholefoods"))
                    ((MainActivity) getContext()).setFragment(ListFragment.newInstance(R.array.explore_store_wholefoods, "Explore Store"),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,true);
                else
                    ((MainActivity) getContext()).setFragment(ListFragment.newInstance(R.array.explore_store, "Explore Store"),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,true);


            }
        });

    }
}
