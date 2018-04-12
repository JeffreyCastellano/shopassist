package com.fa.google.shopassist;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.Touch;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fa.google.shopassist.cards.BaseCardLayout;

import com.fa.google.shopassist.globals.AppState;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by mjoyce on 2/10/15.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context context;
    public List<CardInfo> cardList;
    private int lastPosition = -1;
    public boolean bAnimatedIn = true;
    private ListFragment LF;

    final static String TAG=CardAdapter.class.getSimpleName();

    public CardAdapter(List<CardInfo> cardList, Context context,ListFragment lf) {
        this.cardList = cardList;
        this.context = context;
        this.LF=lf;
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }



    @Override
    public void onBindViewHolder(CardViewHolder contactViewHolder, int i) {

        CardInfo cardInfo = cardList.get(i);

        if(cardInfo.resourceType.equals("layout") ) {

        } else {


//            contactViewHolder.img.setImageResource(cardInfo.resourceId);
            contactViewHolder.img.setImageDrawable(contactViewHolder.img.getResources().getDrawable( cardInfo.resourceId));
//               Resources resources=AppState.getInstance().AppContext.getResources();
////            String strURI=ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(cardInfo.resourceId) + '/' + resources.getResourceTypeName(cardInfo.resourceId) + '/' + resources.getResourceEntryName(cardInfo.resourceId);
//            String strURI="drawable-xxhdpi://"+cardInfo.resourceId;
//            Log.d("TAG","URI "+strURI);
//
//            ImageLoader.getInstance().displayImage(strURI,contactViewHolder.img);


//            Picasso.with(context).load(cardInfo.resourceId).into(contactViewHolder.img);

//            Uri uri = Uri.parse("http://frescolib.org/static/fresco-logo.png");
//            SimpleDraweeView draweeView = (SimpleDraweeView)contactViewHolder.img;
//            Log.d("TAG","contet "+draweeView);
//
//            draweeView.setImageURI(uri);


        }

        contactViewHolder.itemView.setTag(cardInfo.resourceId);

        if(contactViewHolder.itemView instanceof BaseCardLayout){
            if(cardInfo.bAnimated==false) {

                //sets the delay
                ((BaseCardLayout) contactViewHolder.itemView).iStartDelay=150*i;

                //int iOffset=cardInfo.iIndex-((LinearLayoutManager)LF.recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if(!LF.bScrolled)
                    ((BaseCardLayout) contactViewHolder.itemView).animateIn();
                else
                    ((BaseCardLayout) contactViewHolder.itemView).show();
                cardInfo.bAnimated=true;
            }
            else{
                ((BaseCardLayout) contactViewHolder.itemView).show();

            }
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        CardViewHolder CVH;

        Log.d(TAG,"on create ");


        if(i!=0) {
            viewGroup.getLayoutParams().width=-1;
            CVH =new  CardViewHolder( LayoutInflater.from(viewGroup.getContext()).inflate(i, null));
           //   Log.d(TAG,"inflated..." );

        }
        else {
           // Log.d(TAG,"inflate...");
            CVH = new CardViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false));
        }
        //pass reference to the holder fragment
        if(CVH.itemView instanceof BaseCardLayout)
            ((BaseCardLayout)CVH.itemView).parentFragment=new WeakReference<BaseFragment>(LF);

        return  CVH;
    }

    @Override
    public int getItemViewType(int position)
    {

        CardInfo cardInfo = cardList.get(position);

        if(cardInfo.resourceType.equals("layout")){
            return cardInfo.resourceId;
        }
        else
            return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        protected CardView card;
        protected ImageView img;

        public CardViewHolder(View v) {

            super(v);
            card = (CardView) v.findViewById(R.id.card);
            img = (ImageView) v.findViewById(R.id.btn_img_card);
        }

    }

    public void updateCardIndices(){
        int i=0;
        for (CardInfo ci:this.cardList) {
            ci.iIndex=i;
            i++;
        }

    }


}

