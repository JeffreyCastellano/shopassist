package com.fa.google.shopassist.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

import com.fa.google.shopassist.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends FragmentActivity {

    GalleryAdapter adapter;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        List<Fragment> fragments = getFragments();
        adapter = new GalleryAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager)findViewById(R.id.view_pager);
        pager.setAdapter(adapter);
    }

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(GalleryFragment.newInstance(R.drawable.gallery_1));
        fList.add(GalleryFragment.newInstance(R.drawable.gallery_2));
        fList.add(GalleryFragment.newInstance(R.drawable.gallery_3));
        return fList;
    }


}
