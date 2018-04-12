package com.fa.google.shopassist;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.Cart;
import com.fa.google.shopassist.models.CartItem;
import com.fa.google.shopassist.models.Product;

import java.text.NumberFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private float fVel=0;
    private int iLastScrollY=0;
    private OnFragmentInteractionListener mListener;
    private boolean bExpress=false;
    private ViewTreeObserver.OnScrollChangedListener mlistener;
    ScrollView scrollView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2, boolean express) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ListFragment.ARG_PARAM1, param1);
        args.putString(ListFragment.ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.bExpress=express;
        return fragment;
    }

    public CartFragment() {

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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        Cart cart;

        if(bExpress) {
            cart = AppState.getInstance().ExpressCart;

        }
        else
            cart= AppState.getInstance().Cart;

        ViewGroup cartList = (ViewGroup) view.findViewById(R.id.cart_list);

        if(cart.size() == 0) {
            view.findViewById(R.id.cart_scroll).setVisibility(View.GONE);
            view.findViewById(R.id.cart_empty).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.cart_scroll).setVisibility(View.VISIBLE);
            float fPrice = 0;

            for (CartItem item : cart.getItems()) {
                final String productId = item.getProductId();

                int resourceId = getResources().getIdentifier(item.getAssetUrl(), "drawable", getActivity().getPackageName());

                ImageView img = new ImageView(getActivity().getApplicationContext());

                img.setImageResource(resourceId);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById(productId);
                        ProductListFragment LF= ProductListFragment.newInstance();
                        ((MainActivity)getActivity()).setFragment(LF, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, false);
                    }
                });

                cartList.addView(img);

                LinearLayout ll = new LinearLayout(getActivity().getApplicationContext());
                ll.setBackgroundColor(getResources().getColor(R.color.gsa_gray_dark));
                ll.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                ));
                cartList.addView(ll);
            }
            String strSubTotal = NumberFormat.getCurrencyInstance().format(cart.getSubTotal()) ;
            ((TextView)view.findViewById(R.id.txt_subtotal)).setText(strSubTotal);
        }

//        setScrollListener(view);

        scrollView=(ScrollView)view.findViewById(R.id.cart_scroll);

        if(bExpress)
        {
            view.findViewById(R.id.layout_cart_express_buy).setVisibility(View.VISIBLE);
            view.findViewById(R.id.layout_cart_buy).setVisibility(View.GONE);
        }
        else
        {
            view.findViewById(R.id.layout_cart_express_buy).setVisibility(View.GONE);
            view.findViewById(R.id.layout_cart_buy).setVisibility(View.VISIBLE);
        }

        ImageButton btnExpressBuy=(ImageButton)view.findViewById(R.id.btn_cart_express_buy);
        btnExpressBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).expressBuy();
            }
        });

        return view;
    }


    @Override
    public void onPause()
    {

        super.onPause();

        if (mlistener!=null)
            scrollView.getViewTreeObserver().removeOnScrollChangedListener(mlistener);

    }

    @Override
    public void onResume(){
        super.onResume();
        this.setScrollListener();
    }


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

        mlistener= new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {


                int scrollY=scrollView.getScrollY();

                Toolbar actionBar=((MainActivity)getActivity()).actionBar;


                int dy=-(iLastScrollY-scrollY);

                fVel=.9f*fVel+(0.1f*((float)dy));


                actionBar.clearAnimation();

                actionBar.setTranslationY(actionBar.getTranslationY()-dy);

                if(actionBar.getTranslationY() < -actionBar.getHeight()) {
                    actionBar.setTranslationY(-actionBar.getHeight());

                }
                if(actionBar.getTranslationY() > 0)
                    actionBar.setTranslationY(0);

                if(dy>0 && actionBar.getTranslationY() < -24){

                    if((getActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)==0)
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }
                else if(dy<0 && actionBar.getTranslationY() > -24){

                    if((getActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)!=0)
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }


                iLastScrollY=scrollY;


            }

        };

        scrollView.getViewTreeObserver().addOnScrollChangedListener( mlistener);

    }

}
