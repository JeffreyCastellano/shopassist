package com.fa.google.shopassist;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stevensanborn on 3/11/15.
 */
public class OverlayListViewFragment extends ListFragment{


    public static OverlayListViewFragment newInstance(int resourceId, String title) {

        OverlayListViewFragment fragment = new OverlayListViewFragment();

        Bundle args = new Bundle();
        args.putInt(ListFragment.ARG_PARAM1, resourceId);
        args.putString(ListFragment.ARG_PARAM2, title);

        fragment.setArguments(args);
        return fragment;
    }


    public OverlayListViewFragment(){

        super();

        bOverlay=true;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup VG =(ViewGroup) super.onCreateView(inflater,container,savedInstanceState);

        if(recyclerView!=null) {
            recyclerView.setPadding(0, 0, 0, 0);
            recyclerView.setBackgroundColor(getResources().getColor(R.color.gsa_light_gray_blue));
            recyclerView.setOnScrollListener(null);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    bScrolled=true;
                }
            });
        }
        return VG;
    }


    public void goToMoreEvents(View V){

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getActivity()).setListFragment(R.array.more_events, "More Events");
                    }
                },300);

            }

}
