package com.fa.google.shopassist;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigureFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private float fVel=0;
    private int iLastScrollY=0;

    private ImageView imgHeader;

    private ScrollView scrollView;

    ImageView vHeader ;
    ImageButton vCurrent;
    ImageButton vBlue ;
    ImageButton vWhite;
    ImageButton v32 ;
    ImageButton v64 ;

    ImageButton vAtt;
    ImageButton vSprint;
    ImageButton vVerizon;
    ImageButton vSimFree;
    ImageButton btnContinue;


    private boolean bSelectedColor=false;
    private boolean bSelectedStorage=false;
    private boolean bSelectedPlan=false;


    //  private OnFragmentInteractionListener mListener;

    private ViewTreeObserver.OnScrollChangedListener mlistener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigureFragment newInstance(String param1, String param2) {
        ConfigureFragment fragment = new ConfigureFragment();
        Bundle args = new Bundle();
        args.putString(ListFragment.ARG_PARAM1, param1);
        args.putString(ListFragment.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ConfigureFragment() {
        this.setFragmentTransactionValues(R.animator.slide_frag_in, R.animator.slide_frag_out, R.animator.slide_frag_back_in, R.animator.slide_frag_back_out);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(mParam2 != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setTitleBar(mParam2);
        }
        View v=inflater.inflate(R.layout.fragment_configure, container, false);
        scrollView=(ScrollView)v.findViewById(R.id.config_scroll);
        imgHeader=(ImageView)v.findViewById(R.id.configure_header);
        Toolbar actionBar=((MainActivity)getActivity()).actionBar;
        imgHeader.setTranslationY(actionBar.getHeight());
        this.setScrollListener();




        vHeader =(ImageView) v.findViewById(R.id.configure_header);
//
        vBlue =(ImageButton) v.findViewById(R.id.configure_blue);
        vWhite =(ImageButton)v.findViewById(R.id.configure_white);
        v32 = (ImageButton)v.findViewById(R.id.configure_32);
        v64 = (ImageButton)v.findViewById(R.id.configure_64);

        vAtt = (ImageButton)v.findViewById(R.id.configure_att);
        vSprint = (ImageButton)v.findViewById(R.id.configure_sprint);
        vVerizon = (ImageButton)v.findViewById(R.id.configure_verizon);
        vSimFree = (ImageButton)v.findViewById(R.id.configure_simfree);
        btnContinue=(ImageButton)v.findViewById(R.id.configure_continue);
        btnContinue.setEnabled(false);
        btnContinue.setAlpha(.4f);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.getViewTreeObserver().removeOnScrollChangedListener(mlistener);

                ((MainActivity)getActivity()).addToCart(v);
            }
        });

        View.OnClickListener listener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleConfigure(v);
            }
        };

        vBlue.setOnClickListener(listener);
        vWhite.setOnClickListener(listener);
        v32.setOnClickListener(listener);
        v64.setOnClickListener(listener);
        vSimFree.setOnClickListener(listener);


        return v;
    }

    public void toggleConfigure(View v) {

        ImageButton vCurrent = (ImageButton)v;

        boolean bSelected = false;

        if(v.getTag() != null) {
            bSelected = (boolean) v.getTag();
        }

        if(bSelected) {
            return;
        }

        boolean bColorSelected = false;
        boolean bStorageSelected = false;
        boolean bBlue = false;
        boolean bWhite = false;
        boolean b32 = false;
        boolean b64 = false;
        boolean bSimFree = false;



        if(vBlue.getTag() != null) {
            bBlue = (boolean) vBlue.getTag();
            this.bSelectedColor=true;
        }
        if(vWhite.getTag() != null) {
            bWhite = (boolean) vWhite.getTag();
            this.bSelectedColor=true;
        }
        if(v32.getTag() != null) {
            b32 = (boolean) v32.getTag();
            this.bSelectedStorage=true;
        }
        if(v64.getTag() != null) {
            b64 = (boolean) v64.getTag();
            this.bSelectedStorage=true;
        }
        if(vSimFree.getTag() != null) {
            bSimFree = (boolean) vSimFree.getTag();
            this.bSelectedPlan=true;
        }

        bColorSelected = bBlue || bWhite;
        bStorageSelected = b32 || b64;

        switch(v.getId()) {
            case R.id.configure_blue:
                vCurrent.setImageResource(R.drawable.cart_configure_nexus_blue);
                vCurrent.setTag(true);
                vWhite.setImageResource(R.drawable.cart_configure_nexus_white_off);
                vWhite.setTag(false);
                bBlue = true;
                bWhite = false;
                this.bSelectedColor=true;
                break;
            case R.id.configure_white:
                vCurrent.setImageResource(R.drawable.cart_configure_nexus_white);
                vCurrent.setTag(true);
                vBlue.setImageResource(R.drawable.cart_configure_nexus_blue_off);
                vBlue.setTag(false);
                bWhite = true;
                bBlue = false;
                this.bSelectedColor=true;
                break;
            case R.id.configure_32:
                if(!bColorSelected) {
                    return;
                }
                vCurrent.setImageResource(R.drawable.cart_configure_nexus_32);
                vCurrent.setTag(true);
                v64.setImageResource(R.drawable.cart_configure_nexus_64_off);
                v64.setTag(false);
                b32 = true;
                b64 = false;
                this.bSelectedStorage=true;
                break;
            case R.id.configure_64:
                if(!bColorSelected) {
                    return;
                }
                vCurrent.setImageResource(R.drawable.cart_configure_nexus_64);
                vCurrent.setTag(true);
                v32.setImageResource(R.drawable.cart_configure_nexus_32_off);
                v32.setTag(false);
                b64 = true;
                b32 = false;
                this.bSelectedStorage=true;
                break;
            case R.id.configure_simfree:
                if(!bStorageSelected) {
                    return;
                }
                vCurrent.setImageResource(R.drawable.cart_configure_nexus_simfree);
                vCurrent.setTag(true);
                bSimFree = true;
                this.bSelectedPlan=true;


                break;
        }

        String strResource = "cart_nexus_configure";

        if(bBlue) {
            strResource += "_blue";
        }
        if(bWhite) {
            strResource += "_white";
        }
        if(b32) {
            strResource += "_32";
        }
        if(b64) {
            strResource += "_64";
        }
        if(bSimFree) {
            strResource += "_unlocked";
        }

        int resourceId = getResources().getIdentifier(strResource, "drawable", getActivity().getPackageName());
        vHeader.setImageResource(resourceId);

        if(this.bSelectedColor && this.bSelectedStorage && this.bSelectedPlan) {
           btnContinue.setAlpha(1f);
           btnContinue.setEnabled(true);
        }
        else btnContinue.setAlpha(.4f);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
       //     mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onPause(){

        if (mlistener!=null)
        scrollView.getViewTreeObserver().removeOnScrollChangedListener(mlistener);


        super.onPause();;


    }

    @Override
    public void onResume(){
        super.onResume();
        this.setScrollListener();
    }
    @Override
    public void onDetach() {
        super.onDetach();


        //  mListener = null;
    }

    @Override
    public void onDestroy(){

        super.onDestroy();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    public void setScrollListener(){

        if(mlistener!=null)
            scrollView.getViewTreeObserver().removeOnScrollChangedListener(mlistener);


        mlistener=new ViewTreeObserver.OnScrollChangedListener() {



            @Override
            public void onScrollChanged() {

                int scrollY=scrollView.getScrollY();

                if(scrollView.getPaddingTop() != (int)(imgHeader.getHeight()+ TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,getResources().getDisplayMetrics())))

                    scrollView.setPadding(scrollView.getPaddingLeft(),(int)(imgHeader.getHeight()+ TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,getResources().getDisplayMetrics())),scrollView.getPaddingRight(),scrollView.getPaddingBottom());

                if(getActivity()==null){
                    scrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
                    return;
                }


                Toolbar actionBar=((MainActivity)getActivity()).actionBar;


                if(actionBar!=null) {
                    int dy = -(iLastScrollY - scrollY);

                    fVel = .9f * fVel + (0.1f * ((float) dy));

                    actionBar.clearAnimation();

                    actionBar.setTranslationY(actionBar.getTranslationY()-dy);

                    if(actionBar.getTranslationY() < -actionBar.getHeight()) {
                        actionBar.setTranslationY(-actionBar.getHeight());

                    }
                    if(actionBar.getTranslationY() > 0)
                        actionBar.setTranslationY(0);

                    int iHeighhCheck=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72, getResources().getDisplayMetrics());

                    if(dy>0 && actionBar.getTranslationY() < -iHeighhCheck){

                        if((getActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)==0)
                            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    }
                    else if(dy<0 && actionBar.getTranslationY() > -iHeighhCheck){

                        if((getActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)!=0)
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }

                    imgHeader.clearAnimation();
                    imgHeader.setTranslationY(actionBar.getTranslationY()+actionBar.getHeight());

                    iLastScrollY = scrollY;

                }



            }
        };

        scrollView.getViewTreeObserver().addOnScrollChangedListener( mlistener);



    }




}
