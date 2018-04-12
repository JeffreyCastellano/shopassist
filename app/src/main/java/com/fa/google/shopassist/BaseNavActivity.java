package com.fa.google.shopassist;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fa.google.shopassist.camera.CameraActivity;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.Product;
import com.fa.google.shopassist.setup.SetupActivity;

import java.util.ArrayList;

/**
 * Created by stevensanborn on 3/5/15.
 */
public class BaseNavActivity  extends ActionBarActivity {

    private final  static  String TAG=MainActivity.class.getSimpleName(); //for logging


    public Toolbar actionBar;
    protected LinearLayout searchBar;
    protected LinearLayout titleBar;
    protected TextView txtTitle;
    protected ImageButton btnDrawer;
    protected ImageButton btnSearchBack;
    protected ImageButton btnSearchClear;

    protected ImageButton btnCamera;
    protected ImageButton btnMicrophone;


    protected DrawerLayout drawerLayout;
    protected DrawerArrowDrawable drawerArrowDrawable;
    protected AutoCompleteTextView txtSearch;

    protected ImageButton btnSearch;
    protected ImageButton  btnShoppingCart;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



    }

    protected void initActionBar() {
        actionBar = (Toolbar)findViewById(R.id.action_bar);
        searchBar = (LinearLayout)findViewById(R.id.search_bar);
        titleBar = (LinearLayout)findViewById(R.id.title_bar);
        txtTitle = (TextView)findViewById(R.id.txt_title);
        btnDrawer = (ImageButton)findViewById(R.id.btn_drawer);
        btnSearchBack = (ImageButton)findViewById(R.id.btn_search_back);
        btnSearchClear = (ImageButton)findViewById(R.id.btn_search_clear);
        btnMicrophone = (ImageButton)findViewById(R.id.btn_microphone);
        btnCamera = (ImageButton)findViewById(R.id.btn_camera);
        btnSearch= (ImageButton)findViewById(R.id.btn_action_search);


        setSupportActionBar(actionBar);

        drawerArrowDrawable = new DrawerArrowDrawable(getResources());
        drawerArrowDrawable.setStrokeColor(Color.parseColor("#787878"));
        btnSearchBack.setImageDrawable(drawerArrowDrawable);
        drawerArrowDrawable.setFlip(true);
        drawerArrowDrawable.setParameter(1);
        /*
        btnSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ValueAnimator valueAnimator;
                if(!drawerArrowDrawable.getFlip())
                    valueAnimator= ValueAnimator.ofFloat(0,1);
                else
                    valueAnimator= ValueAnimator.ofFloat(1,0);
                valueAnimator.setDuration(300);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        drawerArrowDrawable.setParameter((float) animation.getdValue());
                    }
                });
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(drawerArrowDrawable.getFlip())
                            onBtnSearchBackClicked(null);
                        eletCAe
                            onBtnSearchBackClicked(null);

                        drawerArrowDrawable.setFlip(!drawerArrowDrawable.getFlip());

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                valueAnimator.start();

            }
        });*/


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setSearchState();
                        txtSearch.setText("");
                        txtSearch.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txtSearch, InputMethodManager.SHOW_IMPLICIT);
                    }
                },300);

            }
        });

        btnSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideKeyboard();
                        titleBar.setVisibility(View.VISIBLE);
                        searchBar.setVisibility(View.GONE);
                    }
                },300);

            }
        });

        txtSearch=(AutoCompleteTextView)findViewById(R.id.txt_search);

        findViewById(R.id.layout_gray_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                titleBar.setVisibility(View.VISIBLE);
                searchBar.setVisibility(View.GONE);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }


    protected void setSearchState() {
        /*
        if (txtSearch.hasFocus()) {

            if(btnSearchBack.getVisibility()!=View.VISIBLE){

                btnSearchBack.setVisibility(View.VISIBLE);
                btnDrawer.setVisibility(View.GONE);
                drawerArrowDrawable.setFlip(true);
                drawerArrowDrawable.setParameter(1);

                ValueAnimator valueAnimator;
                valueAnimator = ValueAnimator.ofFloat(0, 1);

                valueAnimator.setDuration(300);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        drawerArrowDrawable.setParameter((float) animation.getAnimatedValue());
                    }
                });
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        drawerArrowDrawable.setFlip(false);

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                valueAnimator.start();
            }


        } else {
            btnSearchBack.setVisibility(View.GONE);
            btnDrawer.setVisibility(View.VISIBLE);
        }

        if (txtSearch.getText().length() > 0) {
            btnMicrophone.setVisibility(View.GONE);
            btnSearchClear.setVisibility(View.VISIBLE);
        } else {
            btnSearchClear.setVisibility(View.GONE);
            btnMicrophone.setVisibility(View.VISIBLE);
        }
        */
        searchBar.setVisibility(View.VISIBLE);
        titleBar.setVisibility(View.GONE);
        findViewById(R.id.layout_gray_search).setVisibility(View.VISIBLE);
    }

    public void setSearchBar() {
        txtSearch.setText("");
        txtTitle.setText("");
        titleBar.setVisibility(View.GONE);

        if (searchBar.getVisibility()!=View.VISIBLE) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(300);
            searchBar.startAnimation(alphaAnimation);
        }
        searchBar.setVisibility(View.VISIBLE);

    }

    public void setTitleBar(String title) {
        txtTitle.setText(title);
        titleBar.setVisibility(View.VISIBLE);
        if (searchBar.getVisibility()!=View.GONE) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);

            alphaAnimation.setDuration(300);
            searchBar.startAnimation(alphaAnimation);
            searchBar.setVisibility(View.GONE);
        }

    }



    public void onBtnDrawerClicked(View v) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

//    public void onBtnSearchBackClicked(View v) {
//        hideKeyboard();
//        new Handler().postDelayed(new Runnable() {"
//            @Override
//            public void run() {
//                txtSearch.clearFocus();
//                setSearchState();
//            }
//        },300);
//
//    }

    public void onBtnSearchClearClicked(View v) {
        txtSearch.setText("");
        setSearchState();
    }

//    public void onBtnTitleClearClicked(View v) {
//        (new Handler()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setSearchBar();
//            }
//        },300);
//
//    }

    public void onBtnTitleBackClicked(View v) {
        hideKeyboard();
        final MainActivity MA=((MainActivity)this);

        Runnable R= new Runnable() {
            @Override
            public void run() {
                MA.onBackPressed();
              }
        };

        (new Handler()).postDelayed(R, 250);

    }

    public void hideKeyboard() {
        findViewById(R.id.layout_gray_search).setVisibility(View.GONE);

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(getCurrentFocus()!=null)
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }



    protected void initTxtSearch() {
        txtSearch = (AutoCompleteTextView)findViewById(R.id.txt_search);

        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                setSearchState();

                if(!hasFocus ){
                    hideKeyboard();;
                }
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSearchState();
            }
        });

        txtSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                        doSearch();
                        hideKeyboard();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void setNavBackArrow(){

        findViewById(R.id.btn_title_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_drawer).setVisibility(View.GONE);

    }

    public void setNavDrawer(){

        findViewById(R.id.btn_title_back).setVisibility(View.GONE);
        findViewById(R.id.btn_drawer).setVisibility(View.VISIBLE);

    }

    public void doSearch(){}

    public void setUpAutoComplete(){


        //Uncomment for simple AutoComplete
        AutoCompleteTextView autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.txt_search);

        final ArrayList<String> arrayList= new ArrayList<String>();
        for(Product P: AppState.getInstance().arrProducts) {
            arrayList.add(P.strName);
            arrayList.add("Buy "+P.strName);
        }

        arrayList.add("Compare");
        arrayList.add("Thor");
        arrayList.add("Headphones");
        arrayList.add("Which phones do my friends like?");
        arrayList.add("Store");
        arrayList.add("Which phone has the best camera?");
        arrayList.add("Which phone has the best screen?");
        arrayList.add("Which phone should I buy?");
        arrayList.add("Which phones do my friends like?");
        arrayList.add("When does the Google Store close?");
        arrayList.add("Explore Store");
        arrayList.add("Associate");
        arrayList.add("Where is the Nexus 6?");
        arrayList.add("Where is the Samsung S6?");
        arrayList.add("When does the Google Store close?");
        arrayList.add("What should I make for dinner?");
        arrayList.add("I’m feeling lucky");
        arrayList.add("What was the jacket I tried on last week?");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.auto_complete_list_item, R.id.autoCompleteItem, arrayList);

        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                txtSearch.setText((String)parent.getItemAtPosition(position));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doSearch();
                    }
                },100);

                hideKeyboard();
            }
        });
        autoCompleteTextView.setBackground(getDrawable(R.drawable.search_bg));

        adapter.notifyDataSetChanged();
    }


    protected void animateCartIncrement(){

        final TextView txtCartCount = (TextView)findViewById(R.id.cart_count);

        txtCartCount.setAlpha(0);

        //Shopping cart Animation
        AnimationSet animationSet= new AnimationSet(false);

        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,-60,0);
        translateAnimation.setDuration(350);
        translateAnimation.setInterpolator(new OvershootInterpolator());
        animationSet.addAnimation(translateAnimation);

        AlphaAnimation alphaAnimation= new AlphaAnimation(0,1);
        alphaAnimation.setDuration(350);
        animationSet.addAnimation(alphaAnimation);

        txtCartCount.startAnimation(animationSet);
        animationSet.setStartOffset(550);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txtCartCount.setAlpha(1);

                txtCartCount.setPivotX(txtCartCount.getWidth()/2f);
                txtCartCount.setPivotY(txtCartCount.getHeight() / 1.8f);

                txtCartCount.setScaleX(1.45f);
                txtCartCount.setScaleY(1.45f);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtCartCount.setAlpha(1);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        btnShoppingCart.setPivotX(btnShoppingCart.getWidth()/2);
        btnShoppingCart.setPivotY(btnShoppingCart.getHeight()/6);

        btnShoppingCart.animate().scaleYBy(.30f).scaleXBy(.30f).setInterpolator(new DecelerateInterpolator(2)).setDuration(200).setStartDelay(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                btnShoppingCart.animate().scaleX(1).scaleY(1).setDuration(250).setStartDelay(500).setInterpolator(new DecelerateInterpolator());
                txtCartCount.animate().scaleX(1).scaleY(1).setDuration(250).setStartDelay(500).setInterpolator(new DecelerateInterpolator());;
            }
        });


    }


    protected void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
//        {
//
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//
//        }

        this.onBtnSearchClearClicked(null);

        final Activity that=this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(that, CameraActivity.class);

                startActivityForResult(intent, MainActivity.REQUEST_IMAGE_CAPTURE);
                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);

            }
        },300);


    }
}