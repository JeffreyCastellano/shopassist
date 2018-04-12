package com.fa.google.shopassist.globals;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.estimote.sdk.Beacon;

import com.estimote.sdk.eddystone.Eddystone;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.SplashActivity;
import com.fa.google.shopassist.ble.BLEManager;
import com.fa.google.shopassist.models.ListCategoryModel;
import com.fa.google.shopassist.models.BLELocation;
import com.fa.google.shopassist.models.ListItemRowModel;
import com.fa.google.shopassist.models.ListModel;
import com.fa.google.shopassist.models.NFCTag;
import com.fa.google.shopassist.models.Product;
import com.fa.google.shopassist.models.Cart;
import com.fa.google.shopassist.models.Store;
import com.fa.google.shopassist.models.Zone;
import com.fa.google.shopassist.nfc.NFCManager;
import com.fa.google.shopassist.setup.HomeScreen;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by stevensanborn on 12/1/14.
 */
public class AppState {



    private static String TAG="GSA - Appstate";

    private static AppState ourInstance = new AppState();

    public BLELocation CurrentLocation;

    public Zone CurrentZone=null;

    public String strLockZoneId=null;

    public Context AppContext;

    public static AppState getInstance() {
        return ourInstance;
    }

    public ArrayList<Store> arrStores=new ArrayList<Store>();
    public ArrayList<Zone> arrZones=new ArrayList<Zone>();
    public ArrayList<Product> arrProducts=new ArrayList<Product>();
    public ArrayList<NFCTag> arrTags=new ArrayList<NFCTag>();

    public BLEManager BLE;
    public NFCManager NFC;

    public GoogleApiClient apiClient;

    public ListCategoryModel myList;
    public ListCategoryModel followedList;
    public ListCategoryModel giftsTim;
    public ListCategoryModel recommendedList;
    public ListCategoryModel techForTravel;
    public ListCategoryModel popularLists;

    public Cart Cart;
    public Cart ExpressCart;
    public Product CurrentProduct;
    public ListModel CurrentList;
    public int iCurrentEvent=0;

    public ListCategoryModel CurrentListCategory;
    public ArrayList<ListModel> arrLists= new ArrayList<>();
    public ArrayList<String> arrEvents= new ArrayList<>();
    public String strUserName;

    public boolean bCompareShown=false;

    public boolean bDialogFragment=false;

    public boolean bShopMode=false;

    public boolean bShowNotifications=true;

    public boolean bInitialized=false;

    public boolean bAnimateSplash=true;

    public boolean bLockIntoStore=false;

    public Location currentLocation;
    private long bestLocationTime;

    private AppState() {

    }


    public void init(Activity activity){


        this.AppContext=activity.getApplicationContext();

        //Load stores
        this.loadStores();

        //Load product data
        this.loadProducts();
//
//        //Load NFC data
        this.loadNFCData();
//
//        //Load Zone data
        this.loadZoneData();

        //load events
        this.loadEvents();

        this.BLE= new BLEManager(this.AppContext);

        this.NFC= new NFCManager(this.AppContext);

        this.myList= new ListCategoryModel();
        this.followedList= new ListCategoryModel();
        this.giftsTim= new ListCategoryModel();
        this.techForTravel= new ListCategoryModel();
        this.recommendedList= new ListCategoryModel();
        this.popularLists= new ListCategoryModel();
        this.loadLists();

        this.Cart = new Cart();

        this.ExpressCart = new Cart();

        bInitialized=true;


        if(AppState.getInstance().strLockZoneId!=null)
            AppState.getInstance().CurrentZone=getZoneById(AppState.getInstance().strLockZoneId);


    }

    public void loadStores(){

        try {

            JSONObject obj = new JSONObject(AppState.getAssetJSONFile("data/stores.json", this.AppContext));
            JSONArray m_storeArry = obj.getJSONArray("stores");

            for (int i = 0; i < m_storeArry.length(); i++) {
                JSONObject storeJson = m_storeArry.getJSONObject(i);
                this.arrStores.add(new Store(storeJson));
            }

        }catch (JSONException e){

            Log.e(TAG,"JSON Exception "+e.getMessage());
        }
    }

    public void loadNFCData(){
        try {

            JSONObject obj = new JSONObject(AppState.getAssetJSONFile("data/nfc.json", this.AppContext));
            JSONArray m_nfcArry = obj.getJSONArray("nfc");

            for (int i = 0; i < m_nfcArry.length(); i++) {
                JSONObject nfcJson = m_nfcArry.getJSONObject(i);
                this.arrTags.add(new NFCTag(nfcJson));
            }

        }catch (JSONException e){

            Log.e(TAG,"JSON Exception "+e.getMessage());
        }

    }


    public void loadLists(){

        //load all the list data

        try {

            JSONObject obj = new JSONObject(AppState.getAssetJSONFile("data/lists.json", AppContext));

            JSONArray mlistJsonArr=obj.getJSONArray("lists");

            for (int a = 0; a < mlistJsonArr.length(); a++) {

                JSONObject objList=mlistJsonArr.getJSONObject(a);

                ListModel LM= new ListModel();

                LM.strId = objList.getString("id");

                LM.strMainAsset = objList.getString("image_main");
                if(objList.has("image_thumb"))
                    LM.strRowThImage = objList.getString("image_thumb");
                if(objList.has("private"))
                    LM.bPrivate=objList.getBoolean("private");

                LM.strRowImage = objList.getString("image_content");
                LM.strListName= objList.getString("name");
                LM.iFollowers= objList.getInt("followers");
                if(objList.has("follow"))
                    LM.strFollow=objList.getString("follow");

                JSONArray m_listItemsArry = objList.getJSONArray("items");

                for (int i = 0; i < m_listItemsArry.length(); i++) {
                    JSONObject itemJson = m_listItemsArry.getJSONObject(i);
                    ListItemRowModel listItemModel;
                    if(itemJson.getString("productId")!=null)
                    {
                        listItemModel = new ListItemRowModel(itemJson.getString("productId"));
                    }
                    else {
                        listItemModel = new ListItemRowModel(
                                itemJson.getString("image_name"),
                                itemJson.getString("image_thumb")
                        );
                    }

                    LM.items.add(listItemModel);

                }


                this.arrLists.add(LM);
            }

        }catch (JSONException e){
            Log.e("JSON", "JSON Exception " + e.getMessage());
        }

        //load list categories
        myList.loadFromJSON("data/my_lists.json",this.AppContext);
        followedList.loadFromJSON("data/followed_list.json",this.AppContext);
        giftsTim.loadFromJSON("data/gifts_tim.json",this.AppContext);
        techForTravel.loadFromJSON("data/tech_travel.json",this.AppContext);
        recommendedList.loadFromJSON("data/recommended.json",this.AppContext);
        popularLists.loadFromJSON("data/popular_lists.json",this.AppContext);

    }

    public void loadEvents(){

        arrEvents.add("images/events/event_gamers_meetup.png");
        arrEvents.add("images/events/event_shot_on_android.png");
        arrEvents.add("images/events/event_tips_tricks.png");

    }

    public ListModel getListById(String strId){


        for (ListModel LM:arrLists){

            if (LM.strId.equals(strId))
                return LM;
        }

        return null;

    }

    public boolean isFollowing(ListModel lm){

        for (ListModel LM:followedList.items){

            if(LM.strId.equals(lm.strId))
                return true;
        }
        return false;
    }


    public void loadProducts(){

        try {

            JSONObject obj = new JSONObject(AppState.getAssetJSONFile("data/products.json", this.AppContext));
            JSONArray m_productsArry = obj.getJSONArray("products");

            for (int i = 0; i < m_productsArry.length(); i++) {
                JSONObject pJson = m_productsArry.getJSONObject(i);
                this.arrProducts.add(new Product(pJson));
            }

        }catch (JSONException e){

            Log.e(TAG,"JSON Exception "+e.getMessage());
        }
    }

    public void loadZoneData(){

        this.arrZones.clear();

        try {

            JSONObject obj = new JSONObject(AppState.getAssetJSONFile("data/zones.json", this.AppContext));

            JSONArray m_zoneArry = obj.getJSONArray("zones");

            for (int i = 0; i < m_zoneArry.length(); i++) {
                JSONObject zoneJson = m_zoneArry.getJSONObject(i);
                this.arrZones.add(new Zone(zoneJson));
            }



        }catch (JSONException e){

            Log.e(TAG,"JSON Exception "+e.getMessage());
        }

    }

    public Zone getZoneById(String id){


        for (Zone Z: arrZones) {

            if(Z.strId.equals(id))
                return Z;

        }

        Log.e(TAG, "no zone found" + id);

        return null;
    }

//    public BLELocation getLocationBasedOnMajorAndMinor(int major , int minor){
//
//        for (Zone Z: arrZones) {
//
//            for (BLELocation LM : Z.arrLocations) {
//
//
//                if (LM.iBeaconMinorId == minor && LM.iBeaconMajorId == major) {
//
//
////                    Log.d(TAG,"hit "+LM);
//                    return LM;
//                }
//            }
//
//        }
//        return null;
//    }

    public BLELocation getLocationBasedOnMajorAndMinor(String major , String minor) {

        for (Zone Z: arrZones) {

            for (BLELocation LM : Z.arrLocations) {


                if (LM.strBeaconMinorId.equals( minor) && LM.strBeaconMajorId.equals( major)) {
                    return LM;
                }
            }

        }

        return null;
    }

    //just checks if we are in the phone section
    public boolean isLocationPhoneSection(){

            return AppState.getInstance().CurrentLocation!=null &&
                    //estimote beacon
                    ( (AppState.getInstance().CurrentLocation.strBeaconMajorId.equals("edd1ebeac04e5defa017") &&
                    AppState.getInstance().CurrentLocation.strBeaconMinorId.equals("ffd68736f20c")) ||

                    //overhead
                    ( AppState.getInstance().CurrentLocation.strBeaconMajorId.equals("e6919021ec0e8e31fd66") &&
                            AppState.getInstance().CurrentLocation.strBeaconMinorId.equals("41503740da25")) ) ;

    }

    //just checks if we are in the meat section
    public boolean isLocationMeatSection(){



        return AppState.getInstance().CurrentLocation!=null &&(
//                (AppState.getInstance().CurrentLocation.strBeaconMajorId.equals("edd1ebeac04e5defa017") &&
//                AppState.getInstance().CurrentLocation.strBeaconMinorId.equals("cb19fc72d7ba")) )||

                (AppState.getInstance().CurrentLocation.strBeaconMajorId.equals("e6919021ec0e8e31fd66") &&
                        AppState.getInstance().CurrentLocation.strBeaconMinorId.equals("c6eeecc53b78")));

    }
    public BLELocation getLocationBasedOnEddyBeacon(Eddystone B){

        if(B.namespace==null || B.instance==null ){
            Log.d(TAG,"NULL BEACON");
            return null;
        }

        return  this.getLocationBasedOnMajorAndMinor(B.namespace, B.instance);
    }


    public static String getAssetJSONFile (String filename, Context context) {

        String ret = "";

        try {
            AssetManager manager = context.getAssets();
            InputStream file = manager.open(filename);


            InputStream inputStream =file;

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;


    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }





    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor)
    {
        return new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_activated},
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor,
                                pressedColor,
                                pressedColor,
                                normalColor
                        }
        );
    }

    public static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor)
    {
        return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor), getColorDrawableFromColor(normalColor), null);
    }

    public static ColorDrawable getColorDrawableFromColor(int color)
    {
        return new ColorDrawable(color);
    }



    public Product getProductById(String id){

        for(Product P : this.arrProducts){
            if(P.strId.equals(id))
                return P;
        }
        return null;
    }
/*
    public void sendNotification(String title, String text) {
        Context context = this.AppContext;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setColor(Color.argb(1, 96, 84, 224))
                        .setContentTitle(title)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentText(text);

        Intent intent = new Intent(context, SplashActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra("SkipIntro",true);

        //Vibration
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        //sound
        if(AppState.getInstance().bShowNotifications)
            mBuilder.setSound(Uri.parse("android.resource://" + AppState.getInstance().AppContext.getPackageName() + "/" + R.raw.notification));
        mBuilder.setContentIntent(pi);

        NotificationManager notificationManager =
                (NotificationManager) AppState.getInstance().AppContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification=mBuilder.build();


        if(AppState.getInstance().bShowNotifications )
            notificationManager.notify(0, notification);
    }*/


    public void sendPlassoNotification(String title, String text) {
        Context context = this.AppContext;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_plasso)
                        .setColor(Color.argb(255, 80, 80, 80))
                        .setContentTitle(title)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentText(text);


//        Intent intent = new Intent(context, SplashActivity.class);

//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
//        intent.putExtra("SkipIntro",true);

        //Vibration
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        //sound
        mBuilder.setSound(Uri.parse("android.resource://" + AppState.getInstance().AppContext.getPackageName() + "/" + R.raw.notification));
        mBuilder.setContentIntent(pi);

        NotificationManager notificationManager =
                (NotificationManager) AppState.getInstance().AppContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification=mBuilder.build();

        notificationManager.notify(0, notification);
    }


    public void setNewLocation(Location location){

        //check if this is better location
        float accuracy = location.getAccuracy();
        long time = location.getTime();


//        if(this.currentLocation==null)
//        {
            this.currentLocation=location;
            this.bestLocationTime=time;
            return;
//        }
//
//        if ((time > bestLocationTime && accuracy <= currentLocation.getAccuracy())) {
//            this.currentLocation=location;
//            this.bestLocationTime=time;
//            Log.d("TAG","location better "+accuracy);
//
//        }
//        else if (time - currentLocation.getTime() > 20000){
//            this.currentLocation=location;
//            this.bestLocationTime=time;
//            Log.d("TAG"," higher accuracy "+accuracy);
//
//        }





    }

}
