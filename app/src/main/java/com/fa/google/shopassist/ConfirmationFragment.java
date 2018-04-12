package com.fa.google.shopassist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.fa.google.shopassist.R;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fa.google.shopassist.cards.Lists.ListViewFragment;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.map.StoreMapModalFragment;
import com.fa.google.shopassist.models.Cart;
import com.fa.google.shopassist.models.CartItem;
import com.fa.google.shopassist.models.Product;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfirmationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmationFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String REGULAR = "regular";
    public static final String EXPRESS = "express";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private View mainView;
    private Button btnContinue;
    private ImageView imgCheck;
    private TextView txtHeadline;
    private TextView txtDetail;
    private ImageView imgPreview;
    private TextView txtReceipt;

    public boolean bExpress;
    public boolean bPlasso=false;

//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmationFragment newInstance(String param1, String param2 , boolean express) {
        ConfirmationFragment fragment = new ConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        fragment.bExpress=express;

        return fragment;
    }

    public ConfirmationFragment() {
        // Required empty public constructor
       bOverlay=true;
        this.setFragmentTransactionValues(R.animator.none, R.animator.fadeout, R.animator.none, R.animator.fadeout);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_confirmation, container, false);

        btnContinue=(Button)view.findViewById(R.id.btn_confirm_continue);
        imgCheck=(ImageView)view.findViewById(R.id.img_confirm_check);
        txtHeadline=(TextView)view.findViewById(R.id.confirmation_headline);
        txtDetail=(TextView)view.findViewById(R.id.confirmation_detail);
        txtReceipt=(TextView)view.findViewById(R.id.confirmation_receipt);
        imgPreview=(ImageView)view.findViewById(R.id.img_checkout_mini_map);

        if (bExpress)
            imgPreview.setVisibility(View.GONE);

//        if(mParam1.equals(ConfirmationFragment.REGULAR)) {
            Cart cart;

               if(bExpress)
                   cart=AppState.getInstance().ExpressCart;
                else
                   cart= AppState.getInstance().Cart;

            view.findViewById(R.id.confirmation_receipt).setVisibility(View.INVISIBLE);
            ((TextView)view.findViewById(R.id.confirmation_headline)).setText("You're all set!");

            String strDetails = "";

            if(cart.size() > 1 && !bExpress) {
                strDetails = "Your products are waiting for you at Checkout.";
            } else {

                CartItem item = cart.getItems().get(0);

                Product product = AppState.getInstance().getProductById(item.getProductId());
                //String strVerb = product.strName.endsWith("s") ? "are" : "is";
                if(product.strId.equals("nexus6"))
                    strDetails="Your new Nexus 6 is waiting for you at Checkout.";
                else if(AppState.getInstance().CurrentZone!=null)
                    strDetails="Thank you for shopping\n at " + AppState.getInstance().CurrentZone.strZoneTitle+".";
                else
                    strDetails="Thank you for shopping.";

            }
            ((TextView)view.findViewById(R.id.confirmation_detail)).setText(strDetails);
       // }

        btnContinue.setVisibility(View.INVISIBLE);
        txtHeadline.setVisibility(View.INVISIBLE);
        imgCheck.setVisibility(View.INVISIBLE);
        imgPreview.setVisibility(View.INVISIBLE);
        txtReceipt.setVisibility(View.INVISIBLE);
        txtDetail.setVisibility(View.INVISIBLE);

        Runnable Run= new Runnable() {
            @Override
            public void run() {
                animateIn();
            }
        };
        android.os.Handler handler= new android.os.Handler();
        handler.postDelayed(Run,200);


        view.setVisibility(View.INVISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AppState.getInstance().CurrentProduct!=null
                        && AppState.getInstance().CurrentProduct.strId.equals("nexus6")
                        && !bPlasso)
                {

                   view.findViewById(R.id.layout_confirm_check).setVisibility(View.GONE);
                   Animation animFade= AnimationUtils.loadAnimation(getActivity(),R.anim.abc_fade_in);
                   view.findViewById(R.id.layout_confirm_plasso).startAnimation(animFade);
                   view.findViewById(R.id.layout_confirm_plasso).setVisibility(View.VISIBLE);
                   imgCheck.setVisibility(View.GONE);
                   imgPreview.setImageDrawable(getResources().getDrawable(R.drawable.img_confirm_nexus));
                   bPlasso=true;
                    //remove click on map
                    imgPreview.setOnClickListener(null);

                }
                else{


                    android.os.Handler handler= new android.os.Handler();


                    if(bPlasso )
                    {

                        AppState.getInstance().sendPlassoNotification("Plaso Confirmation", "Thank you, your plaso payment was successful.");

                    }

                    ((MainActivity)getActivity()).continueShopping(null);

                }


            }
        });

        imgPreview.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.os.Handler handler= new android.os.Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if (AppState.getInstance().CurrentProduct!=null ){


                            if(AppState.getInstance().CurrentProduct.strId.equals("nexus6"))
                            {
//                                StoreMapModalFragment storeMapModalFragment = new StoreMapModalFragment();
//                                Bundle bundle = new Bundle();
//                                bundle.putInt("resourceId", com.fa.google.shopassist.R.layout.layout_overlay_map);
//                                bundle.putString("title", "Checkout");
//                                bundle.putInt(StoreMapModalFragment.IMG_MAP_ARG, com.fa.google.shopassist.R.drawable.img_map_store_checkout);
//                                storeMapModalFragment.setArguments(bundle);
//                                storeMapModalFragment.show(getFragmentManager(), "Map");

                                ((MainActivity)getActivity()).showMap("Checkout", StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.CHECKOUT);
                            }
                            else if(AppState.getInstance().CurrentProduct.strId.equals("jacket"))
                            {


                                ((MainActivity)getActivity()).showModalLayoutFragment(R.layout.layout_jacket_receipt,"Receipt");

                            }

                        }

                    }
                },200);

            }
        });
        if(bExpress) {
            if(AppState.getInstance().CurrentProduct!=null) {

                if(AppState.getInstance().CurrentProduct.strId.equals("jacket")) {
                    imgPreview.setImageDrawable(getResources().getDrawable(R.drawable.img_confirm_jacket));

                }
                else if (AppState.getInstance().CurrentProduct.strId.equals("teriyaki")) {
                    imgPreview.setImageDrawable(getResources().getDrawable(R.drawable.img_confirm_teriyaki));
                }
                else if (AppState.getInstance().CurrentProduct.strId.equals("nexus6_case")) {
                    imgPreview.setImageDrawable(getResources().getDrawable(R.drawable.img_confirm_folio));
                }

            }
        }

        mainView=view;
        return view;
    }

    public void animateIn(){

        float centerX=this.mainView.getWidth()/2;
        float centerY=this.mainView.getHeight()/2;

        Animator anim = ViewAnimationUtils.createCircularReveal(mainView, (int)centerX, (int)centerY, 0, this.mainView.getHeight());

        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                super.onAnimationEnd(animation);

                AnimationSet animScale= (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.intro_scale_in);
                animScale.setInterpolator(new OvershootInterpolator(2));
                Animation animFade= AnimationUtils.loadAnimation(getActivity(),R.anim.intro_slide_up_in);
                Animation animFade2= AnimationUtils.loadAnimation(getActivity(),R.anim.intro_slide_up_in);
                Animation animFadeSlow= new AlphaAnimation(0,1);
                animFadeSlow.setDuration(800);

                animFade.setStartOffset(500);
                animFade2.setStartOffset(700);
                animFadeSlow.setStartOffset(1000);

                animScale.setInterpolator(new AnticipateOvershootInterpolator(.85f));
                animFade.setInterpolator(new DecelerateInterpolator(2));
                animFade2.setInterpolator(new DecelerateInterpolator(2));
                animFadeSlow.setInterpolator(new DecelerateInterpolator(2));

                imgCheck.startAnimation(animScale);
                txtHeadline.startAnimation(animFade);
                txtDetail.startAnimation(animFade2);
                txtReceipt.startAnimation(animFadeSlow);
                imgPreview.startAnimation(animFadeSlow);
                btnContinue.startAnimation(animFadeSlow);


                imgCheck.setVisibility(View.VISIBLE);
                txtHeadline.setVisibility(View.VISIBLE);
                txtDetail.setVisibility(View.VISIBLE);

                //dont show receipt on Nexus 6 because we will use Plasso:
                if(AppState.getInstance().CurrentProduct!=null
                        &&
                        (AppState.getInstance().CurrentProduct.strId.equals("nexus6")
//                        || AppState.getInstance().CurrentProduct.strId.equals("teriyaki")
//                        || AppState.getInstance().CurrentProduct.strId.equals("jacket")
//                        || AppState.getInstance().CurrentProduct.strId.equals("nexus6_case")
                        )

                        && !bPlasso)
                {
                    txtReceipt.clearAnimation();
                    txtReceipt.setVisibility(View.GONE);
                }
                else
                    txtReceipt.setVisibility(View.VISIBLE);

                //if(!bExpress)
                imgPreview.setVisibility(View.VISIBLE);
                btnContinue.setVisibility(View.VISIBLE);

            }
        });
        anim.start();
        mainView.setVisibility(View.VISIBLE);


    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        //getActivity().findViewById(R.id.action_bar).setVisibility(View.GONE);
//        mListener = null;
//    }

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

}
