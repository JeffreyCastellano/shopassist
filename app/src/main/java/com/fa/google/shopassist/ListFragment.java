package com.fa.google.shopassist;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fa.google.shopassist.cards.BaseCardLayout;
import com.fa.google.shopassist.globals.AppState;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public  static final String ARG_PARAM1 = "resourceId";
    public  static final String ARG_PARAM2 = "title";
    public  static final String TAG = ListFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private int mResourceId;
    private String mTitle;

    protected RecyclerView recyclerView;
    LinearLayoutManager llm;
    List<CardInfo> cardInfoList;

    TypedArray resources;

    private int overallYScroll = 0;

    float fVel=0;

    int iLastScrollY=0;

    public  boolean bScrolled=false;



//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resourceId Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment DummyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(int resourceId, String title) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, resourceId);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);

        fragment.setFragmentTransactionValues(R.animator.slide_frag_in, R.animator.slide_frag_out, R.animator.slide_frag_back_in, R.animator.slide_frag_back_out);

        return fragment;
    }

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //bundle args
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResourceId = getArguments().getInt(ARG_PARAM1);
            mTitle = getArguments().getString(ARG_PARAM2);

        }



    }


        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.card_list);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        cardInfoList = new ArrayList<CardInfo>();

        String resourceType;

        resources = getResources().obtainTypedArray(mResourceId);
        for (int i = 0; i < resources.length(); i++) {
            resourceType = getResources().getResourceTypeName(resources.getResourceId(i, 0));
            CardInfo CI=new CardInfo(resources.getResourceId(i, 0), resourceType);
            CI.iIndex=i;
            cardInfoList.add(CI);
        }

        CardAdapter cardAdapter  = new CardAdapter(cardInfoList, getActivity(),this);

        recyclerView.setAdapter(cardAdapter);

        if(mTitle != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setTitleBar(mTitle);
        }

        this.setScrollListener();

            DefaultItemAnimator itemAnimator= new DefaultItemAnimator();
            itemAnimator.setAddDuration(350);
            itemAnimator.setRemoveDuration(350);

            recyclerView.setItemAnimator(itemAnimator);


            return view;
    }

    public void setScrollListener(){


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                if(newState ==RecyclerView.SCROLL_STATE_IDLE){
                    Toolbar actionBar=((MainActivity)getActivity()).actionBar;

                    if( -actionBar.getTranslationY() > actionBar.getHeight()/2)
                        ((MainActivity) getActivity()).animateHeaderOut();
                    else
                        ((MainActivity) getActivity()).animateHeaderIn();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                super.onScrolled(recyclerView, dx, dy);

                if(dy!=0)
                    bScrolled=true;

                fVel=.9f*fVel+(0.1f*((float)dy));

                Toolbar actionBar=((MainActivity)getActivity()).actionBar;

                overallYScroll = overallYScroll + dy;

                actionBar.clearAnimation();

                actionBar.setTranslationY(actionBar.getTranslationY()-dy);

                if(actionBar.getTranslationY() < -actionBar.getHeight())
                    actionBar.setTranslationY(-actionBar.getHeight());
                if(actionBar.getTranslationY() > 0)
                    actionBar.setTranslationY(0);

                int iHeighhCheck=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());


                if(dy>0 && actionBar.getTranslationY() < -iHeighhCheck){

                    if((getActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)==0)
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }
                else if(dy<0 && actionBar.getTranslationY() > -iHeighhCheck){

                    if((getActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)!=0)
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            }
        });



    }



    @Override
    public void animateOutWithLayout(BaseCardLayout layout,int resourceId){

        //animate out the others
        for(int i=0; i<recyclerView.getChildCount(); i++){
            View BCL=recyclerView.getChildAt(i);
            if(BCL != layout) {
                AlphaAnimation animAlpha=new AlphaAnimation(1, 0);
                animAlpha.setDuration(250);
                BCL.startAnimation(animAlpha);
                BCL.setVisibility(View.INVISIBLE);
            }
        }


        //animate this to the top
        ObjectAnimator moveY = ObjectAnimator.ofFloat(layout, "translationY",-layout.getTop()+recyclerView.getPaddingTop());
        moveY.setDuration(300);
        moveY.setInterpolator(new AccelerateDecelerateInterpolator());
        moveY.start();
        moveY.setStartDelay(250);

        moveY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        final BaseCardLayout BCL=layout;


        BCL.animateTransitionCard();


    }

/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
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
        public void onFragmentInteraction(Uri uri);
    }








}
