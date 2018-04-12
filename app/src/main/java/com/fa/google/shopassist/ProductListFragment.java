package com.fa.google.shopassist;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;

import com.fa.google.shopassist.cards.BaseCardLayout;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevensanborn on 3/3/15.
 */
public class ProductListFragment extends ListFragment {


    // TODO: Rename and change types of parameters


    LinearLayoutManager llm;
    List<CardInfo> cardInfoList;

    CardAdapter cardAdapter;
    TypedArray resources;

    private String localProduct=null;


    public  static final String TAG = ProductListFragment.class.getSimpleName();


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *


     * @return A new instance of fragment DummyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance() {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, AppState.getInstance().CurrentProduct.strName);
        fragment.setArguments(args);


        fragment.setFragmentTransactionValues(R.animator.slide_frag_in, R.animator.slide_frag_out, R.animator.slide_frag_back_in, R.animator.slide_frag_back_out);

        return fragment;
    }

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


        if(savedInstanceState!=null){

            //check if we want to load a saved product

            if (savedInstanceState.containsKey("product_id")){
                localProduct=savedInstanceState.getString("product_id");
            }

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //check if there was a product associated with this page
        if(localProduct!=null) {

            //check if there was Product associated with this fragment

            AppState.getInstance().CurrentProduct = AppState.getInstance().getProductById(localProduct);

        }

        if( AppState.getInstance().CurrentProduct!=null){

            localProduct=AppState.getInstance().CurrentProduct.strId;
        }

        final View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.card_list);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        cardInfoList = new ArrayList<CardInfo>();

        String resourceType;

        //first add the main card


        //add product card
        CardInfo CIProd = new CardInfo(R.layout.layout_product_card, getResources().getResourceTypeName(R.layout.layout_product_card));
        CIProd.iIndex = 0;
        cardInfoList.add(CIProd);

        //look up card resources
        String strCard=AppState.getInstance().CurrentProduct.strCardsResource;

        if(AppState.getInstance().CurrentProduct.strCardsResource!=null) {


            int resourceId = getResources().getIdentifier(strCard, "array", getActivity().getPackageName());


            if (resourceId != 0) {

                resources = getResources().obtainTypedArray(resourceId);

                for (int i = 0; i < resources.length(); i++) {
                    resourceType = getResources().getResourceTypeName(resources.getResourceId(i, 0));
                    CardInfo CI = new CardInfo(resources.getResourceId(i, 0), resourceType);
                    CI.iIndex = i + 1;
                    cardInfoList.add(CI);
                }
            }

        }
        else{

            Log.d("TAG","Product "+AppState.getInstance().CurrentProduct.strId+" has a null card resource");
        }
        cardAdapter = new CardAdapter(cardInfoList, getActivity(),this);
        recyclerView.setAdapter(cardAdapter);

        recyclerView.setOnScrollListener( new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setTitleBar(AppState.getInstance().CurrentProduct.strName);


            this.setScrollListener();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the product we are using
        outState.putString("product_id",localProduct);
    }
}
