package com.fa.google.shopassist;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fa.google.shopassist.ble.BLEListener;
import com.fa.google.shopassist.cards.Lists.ListViewFragment;
import com.fa.google.shopassist.compare.CompareModalFragment;
import com.fa.google.shopassist.gallery.GalleryActivity;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.map.StoreMapModalFragment;
import com.fa.google.shopassist.models.Cart;
import com.fa.google.shopassist.models.CartItem;
import com.fa.google.shopassist.models.NFCTag;
import com.fa.google.shopassist.models.Product;
import com.fa.google.shopassist.models.User;
import com.fa.google.shopassist.nfc.NFCListener;
import com.fa.google.shopassist.speech.SpeechRecognizerManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.fa.google.zxing.integration.android.IntentIntegrator;
import com.fa.google.zxing.integration.android.IntentResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import java.util.ArrayList;



public class MainActivity extends BaseNavActivity
    implements ListFragment.OnFragmentInteractionListener,
        SimpleFragment.OnFragmentInteractionListener,
        CartFragment.OnFragmentInteractionListener,
        ConfigureFragment.OnFragmentInteractionListener,
        ConfirmationFragment.OnFragmentInteractionListener,
        ExpressFragment.OnFragmentInteractionListener,
        FragmentManager.OnBackStackChangedListener,
        BLEListener,
        NFCListener,
        ModalDismissListener,
        SpeechRecognizerManager.SpeechRecognizerManagerListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final  static  String TAG=MainActivity.class.getSimpleName(); //for logging
    private final  static  String BACK_TAG = null;
    // Activity Listeners
    public ArrayList<ActivityListener> activityListeners = new ArrayList<>();

    private ModalFragment modalFragment;

    private SpeechRecognizerManager SpeechManager;
    private User user;

    public final static int CODE_LIST_REDIRECT=898989;

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    public MainActivity(){

        super();

        this.SpeechManager= new SpeechRecognizerManager(this);
        this.SpeechManager.listener=this;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Log.d(TAG,"APP "+AppState.getInstance().CurrentZone);

        Log.d(TAG,"REQURST PERMISSION");





        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);

        if(AppState.getInstance().BLE!=null)
            AppState.getInstance().BLE.onCreate(savedInstanceState);





        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);



        setContentView(R.layout.activity_main);

        initUser();
        initDrawerLayout();
        initActionBar();
        initTxtSearch();

        if(AppState.getInstance().BLE!=null)
            AppState.getInstance().BLE.listeners.add(this);//pass along activity callbacks

        //init NFC
        if(AppState.getInstance().NFC!=null) {
            AppState.getInstance().NFC.initializeNFC(this);
            AppState.getInstance().NFC.listeners.add(this);
            this.activityListeners.add(AppState.getInstance().NFC); //pass along activity callbacks
        }
        for(ActivityListener listener : this.activityListeners)
            listener.onCreate(savedInstanceState);

//        ListFragment LF= ListFragment.newInstance(R.array.home,null);
//
//        LF.resourceAnimEnter=LF.resourceAnimExit=0;
//
//        setFragment(LF,FragmentTransaction.TRANSIT_NONE,true);

        this.goHome();

        setUpAutoComplete();

        getFragmentManager().addOnBackStackChangedListener(this);
        buildGoogleApiClient();

        btnShoppingCart=(ImageButton)findViewById(R.id.btn_cart);

        btnShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCartFragment("Cart",true);
            }
        });



    }





    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        }
        else
        {
            //only ditch out if we are home
            if(onHome())
                super.onBackPressed();
            else
               this.goHome();
        }
    }

    public boolean onHome(){

        Fragment activeFragment =  getFragmentManager().findFragmentById(R.id.container);
        if (activeFragment!=null) {
            String fragmentTitle = activeFragment.getArguments().getString("title");

            if (fragmentTitle == null ||  fragmentTitle.equals("Shop Assist"))
                return true;
            else
                return false;
        }
        return false;

    }

    @Override
    public void onBackStackChanged() {
        Fragment activeFragment =  getFragmentManager().findFragmentById(R.id.container);
        String fragmentTitle = activeFragment.getArguments().getString("title");


        if((activeFragment instanceof  BaseFragment) &&
                ((BaseFragment)activeFragment).bOverlay) {
          //  ((RelativeLayout.LayoutParams) findViewById(R.id.container).getLayoutParams()).removeRule(RelativeLayout.BELOW);
            actionBar.setElevation(-1);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
        else {
           // ((RelativeLayout.LayoutParams) findViewById(R.id.container).getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.action_bar);
            actionBar.setElevation(9);
            animateHeaderIn();//after navigation show header
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        //check if fragment has a title to determine the ActionBar state
        if(fragmentTitle != null) {
            setTitleBar(fragmentTitle);

            if(activeFragment instanceof BaseFragment && ((BaseFragment)activeFragment).bShowDrawer)
                setNavDrawer();
            else
                setNavBackArrow();

        } else {
            txtTitle.setText("Shop Assist");
            setNavDrawer();
        }
    }

    private void initUser()
    {

        SharedPreferences sharedPref  = getSharedPreferences(getString(R.string.gsa_prefs), 0);
        String firstName = sharedPref.getString("first_name", "Marc");
        String lastName = sharedPref.getString("last_name", "Vandelberghe");
        String email = sharedPref.getString("email", "marc.vandelberghe@gmail.com");
        user = new User(firstName, lastName,email);

    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        TextView txtName = (TextView)findViewById(R.id.txt_name);
        txtName.setText(user.getFullName());
        TextView txtEmail = (TextView)findViewById(R.id.txt_email);
        txtEmail.setText(user.getEmail());
    }

    @Override
    public void doSearch() {
        String strQuery = txtSearch.getText().toString().toLowerCase();
        int resourceId = 0;
        String title = "";

        if(strQuery.equals("buy") || strQuery.equals("by") || strQuery.equals("buy this") || strQuery.equals("by this") || (strQuery.equals("express by") || strQuery.contains("express buy")))
        {
            this.dispatchTakePictureIntent();
//            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
//            scanIntegrator.initiateScan();
            return;
        }
        else if( strQuery.contains("camera")) {
            resourceId = R.array.srp_best_camera;
            title = strQuery;
        } else if(strQuery.contains("screen")) {
            resourceId = R.array.srp_best_screen;
            title = strQuery;
        }

        else if(strQuery.contains("friends") ) {
            resourceId = R.array.srp_friends_like;
            title = "Which do my friends like?";
        }
        else if ((strQuery.contains("phone") && strQuery.contains("buy") && strQuery.contains("should"))
                    || (strQuery.contains("get") && strQuery.contains("should")))
        {

            resourceId = R.array.srp_which_phone;
            title = strQuery;

        }

        else if(strQuery.contains("where")) {
            if(strQuery.contains("nexus")) {
//                showMap(R.drawable.img_map_store_phones, "Nexus 6");
                showMap( "Nexus 6" , StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.PHONE);

                return;
            }
            else if(strQuery.contains("samsung") || strQuery.contains("s6")) {
//                showMap(R.drawable.img_map_store_phones, "Samsung S6");
                showMap( "Samsung S6" , StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.PHONE);
                return;
            }
            else if(strQuery.contains("store")) {
                resourceId = R.array.srp_where_is_store;
                title = strQuery;
            }
        } else if (strQuery.contains("associate") || strQuery.contains("associates")) {
            resourceId = R.array.associate_list;
            title = "Available associates";
        }  else if (strQuery.contains("expert") && strQuery.contains("speak") || strQuery.contains("talk") ) {
            resourceId = R.array.associate_list;
            title = "Available associates";
        }
        else if (strQuery.contains("thor")) {
            resourceId = R.array.srp_thor;
            title = strQuery;
        }
        else if(strQuery.contains("when")) {
            if(strQuery.contains("store")) {
                resourceId = R.array.srp_store_hours;
                title = strQuery;
            }
        }
         else if(strQuery.contains("how much")) {
            if(strQuery.contains("nexus")) {
                resourceId = R.array.srp_how_much_is_nexus;
                title = strQuery;
            }
        } else if(strQuery.contains("nexus")) {
            showProduct("nexus6", true);
            return;
        }
        else if(strQuery.contains("samsung") || strQuery.contains("galaxy") || strQuery.contains("s5") ) {
            showProduct("galaxys5",true);
            return;
        } else if (strQuery.contains("moto")) {
            showProduct("moto360",true);
            return;
        }
        else if (strQuery.contains("seidio")) {
            showProduct("seidio_case",true);
            return;
        }

        else if (strQuery.equals("compare")){
            CompareModalFragment Compare= new CompareModalFragment();
            Compare.show(getFragmentManager(), "Compare");
            return;
        }
        else if(strQuery.contains("explore")){
            resourceId = R.array.explore_store;
            title = "Explore Store";
        }
        else if(strQuery.contains("store") && (strQuery.contains("time") || strQuery.contains("hours"))){
            resourceId = R.array.explore_store;
            title = "Explore Store";
        }
        else if(strQuery.contains("recipe") || strQuery.contains("dinner")|| strQuery.contains("tonight")) {

            //check if we happen to be in the meat section

            if(AppState.getInstance().isLocationMeatSection())
                resourceId = R.array.srp_recipe_dinner_meat;
            else
                resourceId = R.array.srp_recipe_dinner_seafood;

            title = strQuery;
        }
        else if(strQuery.contains("jacket") || strQuery.contains("last week")) {
            resourceId = R.array.srp_jacket;
            title = strQuery;
        }

        else if (strQuery.contains("buy") || strQuery.contains("by")) {
            if (strQuery.contains("folio")) {
                AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById("nexus6_case");
                Cart cart = AppState.getInstance().ExpressCart;
                cart.add(new CartItem("nexus6_case", null));
                setCartCount();
                ExpressFragment LF = ExpressFragment.newInstance(null, "Express Checkout");
                setFragment(LF, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, false);
                return;
            } else if (strQuery.contains("nexus")) {
                ;
                AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById("nexus6");
                setConfigureFragment("Configure your Nexus 6");
                return;
            }
        }
        else if (strQuery.contains("stand") || strQuery.contains("folio")) {
            showProduct("nexus6_case",true);
            return;
        }


        for(Product P:AppState.getInstance().arrProducts){
            if (P.strName.toLowerCase().equals(strQuery)){
                showProduct(P.strId,true);
                return;
            }
        }

        if(resourceId == 0) {
            resourceId = R.array.srp_queries;
            title = "Available Queries";
        }

//        setListFragment(resourceId, title);
        setFragment(ListFragment.newInstance(resourceId, title),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,true);
    }

    public void onSideNavItemClicked(View v) {

        drawerLayout.closeDrawers();

        final int iId=v.getId();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch(iId) {
                    case R.id.side_nav_home:

                            goHome();

                        break;
                    case R.id.side_nav_explore:

                        if(AppState.getInstance().CurrentZone!=null && AppState.getInstance().CurrentZone.strId.equals("target"))
                            setFragment(ListFragment.newInstance(R.array.explore_store_target, "Explore Store"),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,true);
                        else if(AppState.getInstance().CurrentZone!=null && AppState.getInstance().CurrentZone.strId.equals("wholefoods"))
                            setFragment(ListFragment.newInstance(R.array.explore_store_wholefoods, "Explore Store"),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,true);
                        else
                            setFragment(ListFragment.newInstance(R.array.explore_store, "Explore Store"),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,true);

                    break;

                    case R.id.side_nav_lists:
                        if(AppState.getInstance().CurrentZone==null || AppState.getInstance().CurrentZone.strId.equals("googleplay"))
                            setListFragment(R.array.lists, "Lists",true);
                        break;
                    case R.id.side_nav_history:
                        setListFragment(R.array.history, "Shopping History",true);
                        break;
                    case R.id.side_nav_associates:
                        if(AppState.getInstance().CurrentZone==null || AppState.getInstance().CurrentZone.strId.equals("googleplay"))
                            setListFragment(R.array.associate_list, "Associates",true);
                        break;
                    default:
                        break;
                }
            }
        },350);
    }


    public void setFragment(Fragment fragment, int transit, boolean landing) {

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction;

        boolean bOverlay=false;

        //check if we set up a Custom Frag animation
        if(fragment instanceof  BaseFragment) {
            BaseFragment BF=(BaseFragment)fragment;
            bOverlay=BF.bOverlay;
            if(landing)
                BF.bShowDrawer=true;
        }


        if(fragmentManager.getBackStackEntryCount() == 0 ) {

            transaction = fragmentManager.beginTransaction();

            transaction.add(R.id.container, fragment,"home");

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        } else {

            Fragment activeFragment = getFragmentManager().findFragmentById(R.id.container);

            if(activeFragment instanceof  BaseFragment){

                ((BaseFragment) activeFragment).customFragmentTransistion(fragment);

                transaction = fragmentManager.beginTransaction();

                ((BaseFragment) activeFragment).customTransaction(transaction);

                if(fragment instanceof  BaseFragment)
                    ((BaseFragment) fragment).customTransaction(transaction);

            }
            else{

                transaction = fragmentManager.beginTransaction();

                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            }

            if(landing) {

                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                transaction.add(R.id.container, fragment);

            }
            else
                transaction.replace(R.id.container, fragment);


        }

        transaction.addToBackStack(null).commit();

    }

    public void setCartFragment(String title, boolean landing) {
        setFragment(CartFragment.newInstance(null, title,false),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,landing);
    }

    public  void setSimpleFragment(int resourceId, String title) {
        setFragment(SimpleFragment.newInstance(resourceId, title),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);
    }

    public void setConfigureFragment(String title) {
        setFragment(ConfigureFragment.newInstance(null, title),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);
    }

    public  void setListFragment(int resourceId, String title) {
        setFragment(ListFragment.newInstance(resourceId, title),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);
    }

    public  void setListFragment(int resourceId, String title,boolean landing) {
        setFragment(ListFragment.newInstance(resourceId, title),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,landing);
    }

    public  void setListFragment(int resourceId, String title,int transit,boolean landing) {
        setFragment(ListFragment.newInstance(resourceId, title),transit, landing);
    }

    private void setModalFragment(int resourceId) {
       this.setModalFragment(resourceId,false,"Compare");
    }

    public void setModalFragment(int resourceId, boolean fullscreen,String title) {
        if(modalFragment == null) {
            modalFragment = new ModalFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("resourceId", resourceId);
        bundle.putBoolean("fullscreen",fullscreen);
        modalFragment.setArguments(bundle);
        modalFragment.show(getSupportFragmentManager(), title);
        modalFragment.dismissListener=this;

    }

    public void onFragmentInteraction(Uri uri) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }


    public void showModalLayoutFragment(int layoutid,String title){

        if(modalFragment == null) {
            modalFragment = new ModalLayoutFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("layoutid", layoutid);
        bundle.putString("title", title);
        modalFragment.setArguments(bundle);
        modalFragment.show(getSupportFragmentManager(), title);
        modalFragment.dismissListener=this;

    }


    @Override
    protected  void onStart(){
        super.onStart();

        Log.d(TAG,"on Start");

        if(com.estimote.sdk.SystemRequirementsChecker.checkWithDefaultDialogs(this)) {

            Log.d(TAG,"DEFAULT CHECK");;

            AppState.getInstance().BLE.onStart();

            for (ActivityListener listener : this.activityListeners)
                listener.onStart();

            onChangeRegion();

            AppState.getInstance().apiClient.connect();
        }
    }

    @Override
    protected  void onPause(){

        Log.d(TAG,"on pause");

        super.onPause();

        for(ActivityListener listener : this.activityListeners)
            listener.onPause();

        //force shop mode on
        AppState.getInstance().bShopMode=true;



        AppState.getInstance().bShowNotifications=true;//resume showing notifications if this goes into the background
    }

    @Override
    protected  void onResume(){

        super.onResume();

        if(!AppState.getInstance().BLE.listeners.contains(this)) {
            AppState.getInstance().BLE.listeners.add(this);

        }

        for(ActivityListener listener : this.activityListeners)
            listener.onResume();

         AppState.getInstance().bShowNotifications=false; //don't show notifications if we are browsing content in store

    }


    @Override
    protected  void onStop() {

        Log.d(TAG,"on stop");

        super.onStop();
        for(ActivityListener listener : this.activityListeners)
            listener.onStop();

        if (AppState.getInstance().apiClient.isConnected()) {
            AppState.getInstance().apiClient.disconnect();
        }

        AppState.getInstance().BLE.listeners.remove(this);
    }

    //Activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG,"on result");

        for(ActivityListener listener : this.activityListeners)
            listener.onResult(requestCode, resultCode, data);

        if(resultCode==CODE_LIST_REDIRECT){
            String title=data.getExtras().getString("title");
            int resource=data.getExtras().getInt("resource");

            setListFragment(resource,title);
        }

        //Bar code Scans

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();

            Product P= AppState.getInstance().getProductById(scanContent);
            if(P!=null) {
                AppState.getInstance().CurrentProduct = P;
                Cart cart = AppState.getInstance().ExpressCart;
                cart.clear();
                cart.add(new CartItem(P.strId, null));
                setCartCount();
                ExpressFragment LF = ExpressFragment.newInstance(null, "Express Checkout");
                setFragment(LF, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, true);
            }

//            Toast.makeText(this, "Barcode scan "+scanContent, Toast.LENGTH_LONG).show();
        }



        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();


//            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.camera);
//            mediaPlayer.start(); // no need to call prepare(); create() does that for you


            String strProductId;
            if(AppState.getInstance().CurrentZone!=null && AppState.getInstance().CurrentZone.strId.equals("target")){
                strProductId="jacket";
            }
            else if(AppState.getInstance().CurrentZone!=null && AppState.getInstance().CurrentZone.strId.equals("wholefoods")) {
                strProductId = "teriyaki";
            }
            else{
                strProductId="nexus6_case";
            }

            AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById(strProductId);

            Cart cart = AppState.getInstance().ExpressCart;
            cart.clear();
            cart.add(new CartItem(strProductId, null));
            setCartCount();
            ExpressFragment LF = ExpressFragment.newInstance(null, "Express Checkout");
            setFragment(LF, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, true);



            AppState.getInstance().bShopMode=true;


        }



    }

    @Override
    protected void onNewIntent(Intent intent) {
        for(ActivityListener listener : this.activityListeners)
            listener.onNewIntent(intent);
        super.onDestroy();

    }

    @Override
    public void onDestroy(){
        for(ActivityListener listener : this.activityListeners)
            listener.onDestroy();
        super.onDestroy();

    }

    //When voice has been entered
    @Override
    public void onCompleteSpeech(String strResults){

        String strSearchResults=strResults;
        //filter

        if(strSearchResults.equals("by") || strSearchResults.equals("bias"))
            strSearchResults="buy";

        if (strSearchResults.contains("by "))
            strSearchResults=  strSearchResults.replace("by ","buy ");

        ((TextView)actionBar.findViewById(R.id.txt_search)).setText(strSearchResults);

        doSearch();
    }

    public void OnDismissModalFragment(ModalFragment modalFragment){
        if(modalFragment.resourceId==R.drawable.cart_confirmation){
            FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStackImmediate(1,0);
        }
    }

    public void onCardClicked(View v) {

        int iWhich;
        if(v.getTag()!=null){
            iWhich=(int)v.getTag();
        }
        else {
           iWhich=v.getId();
        }


        switch(iWhich) {
            case R.drawable.buy_configure_nexus:
                setListFragment(R.array.buy_cart, "Cart");
                break;
            case R.drawable.buy_cart:
                setModalFragment(R.drawable.cart_confirmation, true, "Buy");
                break;
            case R.drawable.event:
                setListFragment(R.array.event_confirm, "Confirm Event");
                break;
            case R.id.more_events_1:
                showEvent(0);
                break;
            case R.id.more_events_2:
                showEvent(1);
                break;
            case R.id.more_events_3:
                showEvent(2);
                break;
            case R.layout.layout_card_home_nexus6:
                setListFragment(R.array.nexus, "Nexus 6");
                break;
            case R.drawable.home_3:
                setListFragment(R.array.prod_list, "Jet Set Tech");
                break;
            case R.drawable.moto_1:
                setModalFragment(R.drawable.moto_add_to_list_modal);
                break;
            case R.id.btn_find_in_store:
//                showMap(R.drawable.img_map_store_phones, "Phones");
                showMap( "Phones" , StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.PHONE);

                break;
            case R.id.btn_find_jacket_in_store:
//                showMap(R.drawable.img_map_store_jacket, "Men’s Colorblocked Jacket");
                showMap("Men’s Colorblocked Jacket", StoreMapModalFragment.MAP_ENUM.TARGET, StoreMapModalFragment.MARKER_ENUM.JACKET);
                break;

            case R.drawable.nexus_2a:
                setListFragment(R.array.nexus_compatibility_config,"Compatibility Config");
                break;
            case R.drawable.nexus_article:
                showArticle("http://techcrunch.com/2014/11/12/nexus-6-review-a-big-beautiful-cumbersome-beast");
                break;
            case R.drawable.prod_list_1:
                setListFragment(R.array.moto, "Moto 360");
                break;
            case R.id.img_best_video:
                showVideo("http://fademos-3.s3.amazonaws.com/google/best_camera.mp4");
                break;
            case R.drawable.srp_camera_3:
                showArticle("http://www.tomsguide.com/us/nexus-6-comparison,news-19774.html");
                break;
            case R.id.associates_jordan_2:
//                showMap(R.drawable.img_map_jordan, "Jordan");
                showMap("Jordan", StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.JORDAN);
                break;
            case R.drawable.associates_matthew_2:
//                showMap(R.drawable.img_map_store_wear, "Matthew");
                showMap("Matthew", StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.WEAR);
                break;

            case R.id.img_history_1_target:
                setListFragment(R.array.history_target, "Shopping History");
            break;

            case R.id.img_history_1_whole_foods:
                setListFragment(R.array.history_wholefoods, "Shopping History");
            break;

            case R.id.img_history_1_google:
                setListFragment(R.array.history_google, "Shopping History");
                break;
        }
    }

    public void onCardSectionClicked(View v) {


        int resourceId;
        if(v.getTag()!=null){
            String strResource = v.getTag().toString();
            resourceId = getResources().getIdentifier(strResource, "array", getPackageName());
        }
        else {
            resourceId=v.getId();
        }


        switch(resourceId) {
            case R.array.associate_list:
                setListFragment(R.array.associate_list, "Associates");
                break;
            case R.array.associate_jordan:

//                setListFragment(R.array.associate_jordan, "Jordan Castellano");

                setFragment(OverlayListViewFragment.newInstance(R.array.associate_jordan, "Jordan Castellano"),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);

            break;
            //jordans map
            case R.id.associates_jordan_2:
                showMap("Jordan", StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.JORDAN);
                break;
            case R.array.associate_matthew:
//                setListFragment(R.array.associate_matthew, "Mathew Robertson");

                setFragment(OverlayListViewFragment.newInstance(R.array.associate_matthew, "Jordan Castellano"),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);

                break;
            case R.array.compatibility_phones:
                setListFragment(R.array.compatibility_phones, "Phones by Compatibility");
                break;
            case R.array.compatibility_settings:
                setListFragment(R.array.compatibility_settings, "Compatibility Settings");
                break;
            case R.array.nexus:
                showProduct("nexus6",false);
                break;
            case R.array.prod_list:
                setListFragment(R.array.prod_list, "Jet Set Tech");
                break;
            case R.id.home_whole_available_show_map:
//                showMap(R.drawable.img_map_whole_foods_avail, "Available here",R.drawable.img_avocados_ad);
                showMap( "Available here", StoreMapModalFragment.MAP_ENUM.WHOLE_FOODS,StoreMapModalFragment.MARKER_ENUM.AVAILABLE_HERE_WF, R.drawable.img_avocados_ad);
                break;
            case R.id.home_google_available_show_map:
//                showMap(R.drawable.img_map_store_headphones, "Recommended nearby",R.drawable.img_headphones_ad);
                showMap( "Recommended nearby",StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.AVAILABLE_HERE_GOOGLE);
                break;
            case R.array.event:
                setListFragment(R.array.event, "Upcoming Events");
                break;
//            case R.array.history_more:
//                setListFragment(R.array.history_more, "Shopping History");
//                break;
            case R.id.img_galaxy_link:
                showProduct("galaxys5",false);
            break;
            case R.id.img_moto360_link:
                showProduct("moto360",false);
            break;
            case R.id.img_sony_earbuds_link:
                showProduct("sony_earbuds",false);
            break;
            case R.id.img_chromecast_link:
                showProduct("chromecast",false);
            break;
            case R.id.img_nexus6_link:
                showProduct("nexus6",false);
            break;
            case R.id.btn_store_map:
                if(AppState.getInstance().CurrentZone==null || AppState.getInstance().CurrentZone.strId.equals("googleplay"))
//                    showMap(R.drawable.img_map_store_google, "Store Map");
                    showMap("Store Map", StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.NONE);
                else if(AppState.getInstance().CurrentZone.strId.equals("wholefoods"))
//                    showMap(R.drawable.img_map_whole_foods, "Store Map");
                    showMap( "Store Map", StoreMapModalFragment.MAP_ENUM.WHOLE_FOODS, StoreMapModalFragment.MARKER_ENUM.NONE);
                else if(AppState.getInstance().CurrentZone.strId.equals("target"))
//                        showMap(R.drawable.img_map_target, "Store Map");
                        showMap( "Store Map", StoreMapModalFragment.MAP_ENUM.TARGET, StoreMapModalFragment.MARKER_ENUM.NONE);
                break;
            case R.id.btn_photo_gallery:
                Intent intent = new Intent(this, GalleryActivity.class);
                startActivity(intent);
            break;

//            case R.id.btn_lists_all:
//                setListFragment(R.array.lists, "Lists",true);
//                break;
//            case R.id.btn_more_events:
//                setListFragment(R.array.more_events, "More Events");
//                break;
        }
    }

    public void onChangeRegion(){

        //dont zone change welcome
        if(AppState.getInstance().CurrentZone!=null && AppState.getInstance().CurrentZone.strId.equals("welcome"))
            return;

        if(!this.onHome())
            return;

        this.goHome();



    }



    public void goHome(){

        if( AppState.getInstance().CurrentZone==null){
            setListFragment(R.array.home, null,true);
            this.animateHeaderIn();
        }
        else if(AppState.getInstance().CurrentZone.strId.equals("wholefoods")){

            setListFragment(R.array.home_whole_foods, null,true);
            this.animateHeaderIn();
        }

        else if(AppState.getInstance().CurrentZone.strId.equals("target")){

            setListFragment(R.array.home_target, null,true);
            this.animateHeaderIn();
        }
        else{

            setListFragment(R.array.home, null ,true);
            this.animateHeaderIn();
        }
    }

    public void onChangeLocation(){

        Log.d(TAG,"LOCATION CHANIGN IN MAIN");

        //check if we are in the phone section and whether to toggle it on or off

        if (AppState.getInstance().CurrentZone!=null && AppState.getInstance().CurrentZone.strId.equals("googleplay")) {

            View viewRecommenedNearby=findViewById(R.id.id_recommended_nearby);

            Fragment activeFragment =  getFragmentManager().findFragmentById(R.id.container);


            if(activeFragment instanceof ListFragment){

                //get the title of the page
                String strTitle=activeFragment.getArguments().getString(ListFragment.ARG_PARAM2);


                if(strTitle==null) { //check that we are on the home page Title==null

                    ListFragment lf = (ListFragment) activeFragment;
                    CardAdapter cardAdapter = (CardAdapter) lf.recyclerView.getAdapter();


                    int indexOfNearby = -1;
                    //find position of recommend card
                    for (int i = 0; i < cardAdapter.cardList.size(); i++) {
                        CardInfo card = cardAdapter.cardList.get(i);
                        Log.d(TAG, "Card " + card.resourceType);
                        if (card.resourceId == R.layout.layout_home_google_nearby)
                            indexOfNearby = i;

                    }


                    //if we are in the phone section then show the phones nearby

                    if (AppState.getInstance().isLocationPhoneSection()) {

                        //if there is no card add it to the adapter
                        if (indexOfNearby == -1) {

                            CardInfo card = new CardInfo(R.layout.layout_home_google_nearby, "layout");
                            //find out where to add the card
                            int position = lf.recyclerView.getChildAdapterPosition(lf.recyclerView.getChildAt(0)) + 1;

                            if (position >= cardAdapter.cardList.size())
                                position = cardAdapter.cardList.size() - 1;
                            cardAdapter.cardList.add(position, card);

                            cardAdapter.updateCardIndices();
                            cardAdapter.notifyItemChanged(position);
                            lf.recyclerView.smoothScrollToPosition(position);


                        }

                    } else {


                        if (indexOfNearby > -1) {
                            Log.d(TAG, "removing....");
                            cardAdapter.cardList.remove(indexOfNearby);
                            cardAdapter.updateCardIndices();
                            cardAdapter.notifyItemRemoved(indexOfNearby);

                        }
                    }
                }

            }

            }



    }


    public void onNFCTag(NFCTag tag){

        Fragment F= getFragmentManager().findFragmentById(R.id.container);

        if(AppState.getInstance().bCompareShown) {
            return;
        }


        Fragment activeDialogFragment =  getFragmentManager().findFragmentByTag("Map");
        if (activeDialogFragment!=null)
        {
            ((DialogFragment)activeDialogFragment).dismiss();
        }

        Log.d("NFC","nfc main act received" );

        //grab current fragment
        String strFragTitle="";
        if(F instanceof ListFragment){
            strFragTitle=F.getArguments().getString(ListFragment.ARG_PARAM2);
            if(strFragTitle==null)
                strFragTitle="";
        }

//        Log.d(TAG,"--"+strFragTitle);

        switch(tag.strId) {

            case "nexus6":
                showProduct("nexus6",true);
            break;
            case "moto":
                showProduct("moto360",true);
                break;
            case "samsungs5":
                showProduct("galaxys5",true);
            break;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG,"build api client");
        AppState.getInstance().apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.d(TAG,"AVAIL "+GooglePlayServicesUtil.getErrorString(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)));
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Connected to GoogleApiClient");

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                AppState.getInstance().apiClient);
        if (mLastLocation != null) {
           Log.d(TAG,"Last Lattitude "+String.valueOf(mLastLocation.getLatitude()));
           Log.d(TAG,"Last Longitude "+mLastLocation.getLongitude());
           AppState.getInstance().setNewLocation(mLastLocation);
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.d(TAG, "Connection suspended");
        AppState.getInstance().apiClient.connect();
    }

    public void addToCart(View v) {

        Cart cart = AppState.getInstance().Cart;

        Product product = AppState.getInstance().CurrentProduct;

        String strConfiguration = null;

        if(product.strId.equals("nexus6")) {
            strConfiguration = "";
            View white = findViewById(R.id.configure_white);
            View blue = findViewById(R.id.configure_blue);
            View gb32 = findViewById(R.id.configure_32);
            View gb64 = findViewById(R.id.configure_64);
            View simfree = findViewById(R.id.configure_simfree);

            if (blue.getTag() != null && (boolean)blue.getTag() == true) {
                strConfiguration = "blue";
            }
            if (white.getTag() != null && (boolean)white.getTag() == true) {
                strConfiguration = "white";
            }
            if (gb32.getTag() != null && (boolean)gb32.getTag() == true) {
                strConfiguration += "_32";
            }
            if (gb64.getTag() != null && (boolean)gb64.getTag() == true) {
                strConfiguration += "_64";
            }
            if (simfree.getTag() != null && (boolean)simfree.getTag() == true) {
                strConfiguration += "_unlocked";
            }
        }

        cart.add(new CartItem(product.strId, strConfiguration));
        setCartCount();

        setCartFragment("Cart",true);
    }


    public void addToExpressCart(View v) {

        Cart cart = AppState.getInstance().ExpressCart;

        Product product = AppState.getInstance().CurrentProduct;

        String strConfiguration = null;

        if(product.strId.equals("nexus6")) {
            strConfiguration = "";
            View white = findViewById(R.id.configure_white);
            View blue = findViewById(R.id.configure_blue);
            View gb32 = findViewById(R.id.configure_32);
            View gb64 = findViewById(R.id.configure_64);
            View simfree = findViewById(R.id.configure_simfree);

            if (blue.getTag() != null && (boolean)blue.getTag() == true) {
                strConfiguration = "blue";
            }
            if (white.getTag() != null && (boolean)white.getTag() == true) {
                strConfiguration = "white";
            }
            if (gb32.getTag() != null && (boolean)gb32.getTag() == true) {
                strConfiguration += "_32";
            }
            if (gb64.getTag() != null && (boolean)gb64.getTag() == true) {
                strConfiguration += "_64";
            }
            if (simfree.getTag() != null && (boolean)simfree.getTag() == true) {
                strConfiguration += "_unlocked";
            }
        }
        cart.clear();
        cart.add(new CartItem(product.strId, strConfiguration));

        setFragment(CartFragment.newInstance(null, "Express Cart",true),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,true);
    }


    public void pickupInStore(View v) {
        animateHeaderOut();
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setFragment(ConfirmationFragment.newInstance(ConfirmationFragment.REGULAR, null,false), FragmentTransaction.TRANSIT_NONE, false);

            }},300);

    }

    public void expressBuy(){

        animateHeaderOut();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setFragment(ConfirmationFragment.newInstance(ConfirmationFragment.REGULAR, null,true), FragmentTransaction.TRANSIT_NONE, false);

            }},300);
    }

    public void expressCheckout(View v) {
        animateHeaderOut();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setFragment(ConfirmationFragment.newInstance(ConfirmationFragment.REGULAR, null,true), FragmentTransaction.TRANSIT_NONE, false);

            }},300);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                AppState.getInstance().sendNotification("Thank you for your purchase.", "Your receipt has been emailed to you.");
//            }
//        }, 10000);

    }

    public void setCartCount() {
        TextView txtCartCount = (TextView)findViewById(R.id.cart_count);
        int size = AppState.getInstance().Cart.size();

        if(size == 0) {
            txtCartCount.setText("");
        } else {
            txtCartCount.setText(Integer.toString(size));
            animateCartIncrement();
        }




    }

    public void continueShopping(View v) {
        Cart cart = AppState.getInstance().Cart;
        /*
        //GOTO LAST ITEM IN CART
        CartItem lastItem = cart.getItems().get(cart.size()-1);
        AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById(lastItem.getProductId());
        setFragment(ProductListFragment.newInstance(),FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);
        */
        cart.clear();
        setCartCount();
        this.goHome();;
    }

    public void onCartClicked(View v){
        setCartFragment("Cart",true);
    }

    public void animateHeaderOut(){

        if(actionBar.getTranslationY()==actionBar.getHeight())
            return;

//        TranslateAnimation translateAnimationHeader= new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,-actionBar.getHeight());
//        translateAnimationHeader.setDuration(200);
//        translateAnimationHeader.setFillAfter(true);
//        translateAnimationHeader.setInterpolator(new AccelerateInterpolator());
        actionBar.clearAnimation();
//        actionBar.startAnimation(translateAnimationHeader);

        ValueAnimator va=ValueAnimator.ofFloat(actionBar.getTranslationY(),-actionBar.getHeight());
        va.setDuration(200);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                actionBar.setTranslationY((float)animation.getAnimatedValue());
            }
        });

        va.start();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void animateHeaderIn(){

        if(actionBar.getTranslationY()==0)
            return;

        ValueAnimator va=ValueAnimator.ofFloat(actionBar.getTranslationY(),0);
        va.setDuration(200);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                actionBar.setTranslationY((float) animation.getAnimatedValue());
            }
        });

        va.start();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void onExpertReviewClick(View v) {
        String url = v.getTag().toString();
        showArticle(url);
    }

    public void dummy(View v) {}

    public void mapNexus6(View v) {
        showMap( "Nexus 6", StoreMapModalFragment.MAP_ENUM.GOOGLE, StoreMapModalFragment.MARKER_ENUM.PHONE);
    }

    public void onProductClicked(View v) {
        String strProductId = v.getTag().toString();
        showProduct(strProductId,false);
    }

    public void onPopularListClicked(View v) {
        int listIndex = Integer.parseInt(v.getTag().toString());
        AppState.getInstance().CurrentListCategory= AppState.getInstance().popularLists;
        AppState.getInstance().CurrentList=AppState.getInstance().popularLists.items.get(listIndex);
        ListViewFragment listViewFragment= new ListViewFragment();
        Bundle args = new Bundle();
        args.putInt(ListFragment.ARG_PARAM1, R.layout.layout_list);
        args.putString(ListFragment.ARG_PARAM2, AppState.getInstance().CurrentList.strListName);
        listViewFragment.setArguments(args);
        setFragment(listViewFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);
    }

    public void onEventClicked(View v) {
        int eventId = Integer.parseInt(v.getTag().toString());
        showEvent(eventId);
    }

    public void showMap(String title,StoreMapModalFragment.MAP_ENUM eMap, StoreMapModalFragment.MARKER_ENUM eMarker) {
       this.showMap(title,eMap,eMarker,0);
    }

    private void showMap( String title,StoreMapModalFragment.MAP_ENUM eMap, StoreMapModalFragment.MARKER_ENUM eMarker, int ad ) {

        StoreMapModalFragment storeMapModalFragment= new StoreMapModalFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
//        bundle.putInt("resourceId", R.layout.layout_overlay_map);
//        bundle.putInt(StoreMapModalFragment.IMG_MAP_ARG, resourceId);
        bundle.putSerializable("map", eMap);
        bundle.putSerializable("marker",eMarker);

        storeMapModalFragment.setArguments(bundle);

        if(ad>0) {
            storeMapModalFragment.bShowAd = true;
            storeMapModalFragment.iAdResource = ad;
        }
        else
            storeMapModalFragment.bShowAd = false;


        storeMapModalFragment.show(getFragmentManager(), "Map");
    }

     private void showProduct(String productId, boolean landing) {
        AppState.getInstance().CurrentProduct=AppState.getInstance().getProductById(productId);
        ProductListFragment LF= ProductListFragment.newInstance();
        setFragment(LF,FragmentTransaction.TRANSIT_FRAGMENT_OPEN,landing);
    }

    private void showArticle(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void showVideo(String url) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void showEvent(int eventId) {
        AppState.getInstance().iCurrentEvent = eventId;
        setFragment(EventFragment.newInstance(AppState.getInstance().arrEvents.get(AppState.getInstance().iCurrentEvent))
                , FragmentTransaction.TRANSIT_FRAGMENT_OPEN, false);
    }


    public void onClickRecipeLocation(View v){

        showMap("Whole Foods",StoreMapModalFragment.MAP_ENUM.WHOLE_FOODS, StoreMapModalFragment.MARKER_ENUM.RECIPE);

    }

}
