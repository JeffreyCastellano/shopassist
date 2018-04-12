package com.fa.google.shopassist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by stevensanborn on 4/29/15.
 */
public class ModalLayoutFragment  extends ModalFragment  {

    private ImageView imageView;

    private boolean bFullscreen=false;

    public int resourceId;

    public ModalDismissListener dismissListener=null;

    public ModalLayoutFragment() {
        // Empty constructor required for DialogFragment
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_FRAME, R.style.AppTheme);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view ;

        int iLayout=getArguments().getInt("layoutid");

        String strTitle=getArguments().getString("title");

        view = (ViewGroup)inflater.inflate(R.layout.fragment_modal_layout, container);

        TextView txt=(TextView)view.findViewById(R.id.txt_overlay_compare_title);
        txt.setText(strTitle);

        RelativeLayout layoutContent=(RelativeLayout)view.findViewById(R.id.layout_modal_content);
        inflater.inflate(iLayout, layoutContent, true);


        ImageButton btnGotIt=(ImageButton)view.findViewById(R.id.btn_close_modal);
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                },300);


            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }



    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        if(dismissListener!=null)
            dismissListener.OnDismissModalFragment(this);
    }
}
