package com.fa.google.shopassist.compare;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fa.google.shopassist.ModalFragment;
import com.fa.google.shopassist.R;

import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 2/16/15.
 */
public class CompareModalFragment extends DialogFragment {

    public CompareModalFragment(){}

    public ViewGroup VG;
    public String strInitProduct="";

    private ImageButton btnClose;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        this.VG =(ViewGroup) inflater.inflate(getArguments().getInt("resourceId"), container);
        this.VG=null;
        try{


        this.VG =(ViewGroup) inflater.inflate(R.layout.layout_overlay_compare, null);
//        if(!strInitProduct.equals(""))
//            ((CompareOverlayLayout)this.VG).setFirstProduct(strInitProduct);

        btnClose=(ImageButton)VG.findViewById(R.id.btn_compare_overlay_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CompareModalFragment.this.dismiss();

            }
        });
        //((CompareOverlayLayout)VG).addProduct("asd");


        }

        catch (InflateException e){

            Log.e("EXCEPTION"," inflate cause: "+e.getCause());
            Log.e("EXCEPTION"," inflate message: "+e.getMessage());

        }
        catch (OutOfMemoryError e){

            Log.e("EXCEPTION"," memory cause: "+e.getCause());
            Log.e("EXCEPTION"," memory message: "+e.getMessage());
        }
        return VG;
    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }


    @Override
    public void onPause(){
        super.onPause();
        AppState.getInstance().bCompareShown=false;


        ((CompareOverlayLayout)this.VG).cleanup();

    }

    @Override
    public void onResume(){

        super.onResume();
        AppState.getInstance().bCompareShown=true;
        ((CompareOverlayLayout)this.VG).setUpListener();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

}
