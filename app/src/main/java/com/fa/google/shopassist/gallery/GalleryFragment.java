package com.fa.google.shopassist.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fa.google.shopassist.R;

/**
 * Created by mjoyce on 3/18/15.
 */
public class GalleryFragment extends Fragment {

    public static final String RESOURCE_ID = "RESOURCE_ID";

    public static final GalleryFragment newInstance(int resourceId) {
        GalleryFragment f = new GalleryFragment();
        Bundle bdl = new Bundle(1);
        bdl.putInt(RESOURCE_ID, resourceId);
        f.setArguments(bdl);
        return f;
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int resourceId = getArguments().getInt(RESOURCE_ID);
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        ImageView img = (ImageView) v.findViewById(R.id.img_gallery);
        img.setImageResource(resourceId);
        return v;
    }

}

