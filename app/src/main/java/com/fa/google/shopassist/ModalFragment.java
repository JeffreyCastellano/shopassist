package com.fa.google.shopassist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
// ...

public class ModalFragment extends DialogFragment {

    private ImageView imageView;

    private boolean bFullscreen=false;

    public int resourceId;

    public ModalDismissListener dismissListener=null;

    public ModalFragment() {
        // Empty constructor required for DialogFragment
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments().getBoolean("fullscreen"))
            setStyle(STYLE_NO_FRAME, R.style.AppTheme);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view ;

        if(getArguments().getBoolean("fullscreen")) {
            view = inflater.inflate(R.layout.fragment_modal_fullscreen, container);
        }
        else
            view= inflater.inflate(R.layout.fragment_modal, container);


        imageView = (ImageView)view.findViewById(R.id.img_modal);

        imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                ModalFragment.this.dismiss();
             }
         });

        imageView.setImageResource(getArguments().getInt("resourceId"));

        this.resourceId=getArguments().getInt("resourceId");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if(getArguments().getBoolean("fullscreen"))
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }



    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        if(dismissListener!=null)
            dismissListener.OnDismissModalFragment(this);
    }
}