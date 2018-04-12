package com.fa.google.shopassist.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by mjoyce on 3/18/15.
 */
class GalleryAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public GalleryAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override

    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }


    @Override

    public int getCount() {
        return this.fragments.size();
    }

}
