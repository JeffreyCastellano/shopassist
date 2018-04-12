package com.fa.google.shopassist.cards.Lists;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fa.google.shopassist.BaseFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.ProductListFragment;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.compare.CompareOverlayLayout;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.Product;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.io.IOException;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;


/**
 * Created by stevensanborn on 2/27/15.
 */
public class ListViewFragment extends BaseFragment implements ObservableScrollViewCallbacks {

    ViewGroup VG;


    RelativeLayout rlFollowers;
    ImageView imgHeader;
    ImageButton btnBack;

    ObservableListView listView;
    ToggleButton btnToggle;
    TextView textTitleView;
    TextView textSubTitleView;

    private View mOverlayView;
    private View mListBackgroundView;

    LinearLayout llNav;

    int mFlexibleSpaceImageHeight ;

    public ImageView imgClicked;


    public ListViewFragment()

    {
        super();

        bOverlay=true;

        this.setFragmentTransactionValues(R.animator.slide_frag_in, R.animator.slide_frag_out, R.animator.slide_frag_back_in, R.animator.slide_frag_back_out);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mFlexibleSpaceImageHeight=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());

        this.VG =(ViewGroup) inflater.inflate(getArguments().getInt("resourceId"), null);

        btnBack=(ImageButton)this.VG.findViewById(R.id.btn_list_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).onBackPressed();
                    }
                }, 300);
            }
        });

        textTitleView=(TextView)this.VG.findViewById(R.id.text_list_view_title);

        textSubTitleView=(TextView)this.VG.findViewById(R.id.text_list_view_subtitle);

        textTitleView.setText(AppState.getInstance().CurrentList.strListName);

        if(AppState.getInstance().CurrentList.bPrivate)
            textSubTitleView.setText("Private List");
        else
            textSubTitleView.setText(""+AppState.getInstance().CurrentList.iFollowers +" followers");

        imgHeader=(ImageView)this.VG.findViewById(R.id.img_list_view_main_header);

        imgHeader.setTransitionName("headerimage");

        try
        {

            imgHeader.setImageBitmap(BitmapFactory.decodeStream(getActivity().getAssets().open(AppState.getInstance().CurrentList.strMainAsset)));

            if(AppState.getInstance().CurrentList.strFollow!=null) {
                ImageView imgFollowers=(ImageView) VG.findViewById(R.id.img_followers);
                imgFollowers.setImageBitmap(BitmapFactory.decodeStream(getActivity().getAssets().open(AppState.getInstance().CurrentList.strFollow)));
            }

        }
        catch (IOException e){
            Log.e("IO Exception", "e " + e.getLocalizedMessage());
        }

        mOverlayView = VG.findViewById(R.id.overlay);
        listView=(ObservableListView)this.VG.findViewById(R.id.list_listview);
        listView.setScrollViewCallbacks(this);

        View paddingView = new View(this.getActivity());
        AbsListView.LayoutParams lp;

        if(AppState.getInstance().CurrentListCategory==AppState.getInstance().myList){
            VG.findViewById(R.id.layout_list_follow).setVisibility(View.GONE);
            lp= new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,mFlexibleSpaceImageHeight);
        }
        else{
            textSubTitleView.setVisibility(View.GONE);
            lp= new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,mFlexibleSpaceImageHeight+264);
        }

        paddingView.setLayoutParams(lp);


        // This is required to disable header's list selector effect
        paddingView.setClickable(true);

        listView.addHeaderView(paddingView);

        final ListItemProductArrayAdapter arrayAdapter= new ListItemProductArrayAdapter(getActivity(),0, AppState.getInstance().CurrentList.items);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // arrayAdapter.items.get(position-1);
                Product P=AppState.getInstance().getProductById(arrayAdapter.items.get(position-1).strProductId);

                if(P.strType.equals("app"))return;

                AppState.getInstance().CurrentProduct=P;
                ProductListFragment LF= ProductListFragment.newInstance();
                LF.resourceAnimEnter=LF.resourceAnimExit=0;
                imgClicked=(ImageView)view.findViewById(R.id.img_row_img);
                ((MainActivity) getActivity()).setFragment(LF, FragmentTransaction.TRANSIT_NONE,false);

                //finish();
            }
        });

        llNav=(LinearLayout)VG.findViewById(R.id.ll_list_nav);

        mListBackgroundView = VG.findViewById(R.id.list_background);
        final View contentView = VG;
        contentView.post(new Runnable() {
            @Override
            public void run() {
                // mListBackgroundView's should fill its parent vertically
                // but the height of the content view is 0 on 'onCreate'.
                // So we should get it with post().
                mListBackgroundView.getLayoutParams().height = contentView.getHeight();
            }
        });


        rlFollowers=(RelativeLayout)VG.findViewById(R.id.layout_list_follow);


        btnToggle=(ToggleButton)VG.findViewById(R.id.toggle);
        if(AppState.getInstance().isFollowing(AppState.getInstance().CurrentList))
            btnToggle.setChecked(true);
        else
            btnToggle.setChecked(false);

        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    AppState.getInstance().followedList.items.add(AppState.getInstance().CurrentList);
                }
                else{

                    AppState.getInstance().followedList.items.remove(AppState.getInstance().CurrentList);
                }
            }
        });

        this.animateIn();

        return VG;
    }

    public void animateIn(){


        imgHeader.setAlpha(0f);
        imgHeader.animate().alpha(1).setDuration(500).setStartDelay(300).setInterpolator(new DecelerateInterpolator(2));

        textTitleView.setVisibility(View.INVISIBLE);
        textSubTitleView.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);

        textTitleView.setAlpha(0f);
        AlphaAnimation alphaAnimation= new AlphaAnimation(0,1);
        alphaAnimation.setDuration(500);
        alphaAnimation.setStartOffset(600);
        textTitleView.startAnimation(alphaAnimation);
        textTitleView.setVisibility(View.VISIBLE);


        if(AppState.getInstance().CurrentListCategory==AppState.getInstance().myList ) {
            textSubTitleView.startAnimation(alphaAnimation);
            textSubTitleView.setVisibility(View.VISIBLE);
        }


        AlphaAnimation alphaAnimation1= new AlphaAnimation(0,1);
        alphaAnimation1.setDuration(500);
        alphaAnimation1.setStartOffset(800);
        listView.setVisibility(View.VISIBLE);
        listView.setAnimation(alphaAnimation1);
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        float fSmallSliver = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140.0f/3, getResources().getDisplayMetrics());;
        float fLargeSliver =mFlexibleSpaceImageHeight-fSmallSliver;



        float fPercentPart1=ScrollUtils.getFloat((float) (scrollY - fLargeSliver ) / fSmallSliver, 0, 1);
        float fPercentPart2=ScrollUtils.getFloat((float) (scrollY) / fLargeSliver, 0, 1);
        float fPercentPart3=ScrollUtils.getFloat((float) (scrollY) / (fLargeSliver/1.5f), 0, 1);

        ViewHelper.setTranslationY(mOverlayView,-fPercentPart2*(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        ViewHelper.setTranslationY(imgHeader, -fPercentPart2*(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(fSmallSliver, -scrollY + mFlexibleSpaceImageHeight));

        ViewHelper.setTranslationY(rlFollowers, mFlexibleSpaceImageHeight-scrollY);

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, fPercentPart2);

        float oneMinus2=(1-fPercentPart2);

        float fPosMaxTitleY=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400.0f/3, getResources().getDisplayMetrics());

        if(textSubTitleView.getVisibility()!=View.VISIBLE)
            fPosMaxTitleY+=textSubTitleView.getHeight();

        ViewHelper.setTranslationY(textTitleView, oneMinus2*fPosMaxTitleY - fPercentPart2 *fSmallSliver);

        float fSubTitleViewSpacer=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230.0f/3, getResources().getDisplayMetrics());

        ViewHelper.setTranslationY(textSubTitleView, oneMinus2*fPosMaxTitleY+fSubTitleViewSpacer - fPercentPart2 *fSmallSliver);

        float fllNavSpacer=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());


        ViewHelper.setTranslationY(llNav,(1-fPercentPart2)*fllNavSpacer - fPercentPart1 *fSmallSliver);


        if(fPercentPart2>.15)
            ViewHelper.setAlpha(textSubTitleView, 0);
        else
            ViewHelper.setAlpha(textSubTitleView, 1);


        ViewHelper.setAlpha(textTitleView,1f-fPercentPart3);

    }


    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }




  }
