package com.fa.google.shopassist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.fa.google.shopassist.cards.BaseCardLayout;

/**
 * Created by stevensanborn on 2/19/15.
 */
public class BaseFragment extends Fragment{

    public int resourceAnimEnter=0;
    public int resourceAnimExit=0;
    public int resourceAnimPopEnter=0;
    public int resourceAnimPopExit=0;

    public boolean bOverlay=false;
    public boolean bShowDrawer=false;


    public View sharedElementView=null;
    public String strSharedElement="";

    public void setFragmentTransactionValues(int enter, int exit, int popenter, int popexit){

        this.resourceAnimEnter=enter;
        this.resourceAnimExit=exit;
        this.resourceAnimPopEnter=popenter;
        this.resourceAnimPopExit=popexit;

    }

    public void setTransactionCustomAnimation(FragmentTransaction transaction){
//            transaction.setCustomAnimations(this.resourceAnimEnter,this.resourceAnimExit,this.resourceAnimPopEnter,this.resourceAnimPopExit);
    }

    public void customFragmentTransistion(Fragment fragment){

        if(sharedElementView!=null) {

            Log.d("TAG", "SHARED " + sharedElementView);

            sharedElementView.setTransitionName(strSharedElement);
            fragment.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.tranform_image_share));
            fragment.setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.tranform_image_share));
            fragment.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));


        }
    }

    public void customTransaction(FragmentTransaction transaction){

    this.transaction(transaction);
    }


    public void transaction(FragmentTransaction transaction) {


        if(sharedElementView!=null){

            transaction.addSharedElement(sharedElementView, strSharedElement);
        }
        else
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

    }


    public void animateOutWithLayout(BaseCardLayout layout,int resourceId){}


}
