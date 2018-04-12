package com.fa.google.shopassist;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.fa.google.shopassist.compare.CompareOverlayLayout;
import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 4/22/15.
 */
public class CompareFragment extends OverlayListViewFragment{

    /**
     * Created by stevensanborn on 2/16/15.
     */

    public static CompareFragment newInstance(String title)
    {

        CompareFragment fragment = new CompareFragment();
        Bundle args = new Bundle();
        args.putString(ListFragment.ARG_PARAM2, title);
        fragment.setArguments(args);
        fragment.setFragmentTransactionValues(R.animator.slide_frag_in, R.animator.slide_frag_out, R.animator.slide_frag_back_in, R.animator.slide_frag_back_out);

        return fragment;
    }


    public CompareFragment(){

            this.setFragmentTransactionValues(R.animator.slide_frag_in, R.animator.slide_frag_out, R.animator.slide_frag_back_in, R.animator.slide_frag_back_out);

        }

        public RelativeLayout VG;
        public String strInitProduct="";

        private ImageButton btnClose;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

//        this.VG =(ViewGroup) inflater.inflate(getArguments().getInt("resourceId"), container);
            this.VG=null;
            try{


                this.VG = (RelativeLayout)inflater.inflate(R.layout.layout_overlay_compare, null);
                Log.d("TAG",""+this.VG);


                btnClose=(ImageButton)VG.findViewById(R.id.btn_compare_overlay_close);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        CompareFragment.this.dismiss();
                        ( (MainActivity) getActivity()).onBackPressed();
                    }
                });
                //((CompareOverlayLayout)VG).addProduct("asd");


            }

            catch (InflateException e){

                Log.e("EXCEPTION", " inflate cause: " + e.getCause());
                Log.e("EXCEPTION"," inflate message: "+e.getMessage());

            }
            catch (OutOfMemoryError e){

                Log.e("EXCEPTION"," memory cause: "+e.getCause());
                Log.e("EXCEPTION"," memory message: "+e.getMessage());
            }
            return VG;
        }

/*
        @Override
        public void onActivityCreated(Bundle arg0) {
            super.onActivityCreated(arg0);
//            getDialog().getWindow()
//                    .getAttributes().windowAnimations = R.style.DialogAnimation;
        }
*/

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

        }

        @Override
        public void onDestroy(){
            super.onDestroy();

        }


        @Override
        public void transaction(FragmentTransaction transaction) {

            transaction.setCustomAnimations(R.animator.slide_up_in, 0, R.animator.slide_up_in, R.animator.slide_down_out);

        }


}
