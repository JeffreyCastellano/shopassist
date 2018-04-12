package com.fa.google.shopassist.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fa.google.shopassist.R;
import com.fa.google.shopassist.globals.AppState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by stevensanborn on 5/20/15.
 */
public class ShopMapFragment extends MapFragment implements OnMapReadyCallback{

    public  Marker markerUser;

    public StoreMapModalFragment.MARKER_ENUM eMarker;
    public StoreMapModalFragment.MAP_ENUM eMap;


    public ShopMapFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstance){


        super.onCreate(savedInstance);



    }

    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {

        View v = super.onCreateView(arg0, arg1, arg2);
        Log.d("TAG", "create shop map fragment");

        this.getMapAsync(this);
        return v;
    }

    private void initMap(){
        UiSettings settings = getMap().getUiSettings();
        //settings.setAllGesturesEnabled(false);
        settings.setMyLocationButtonEnabled(true);
//        getMap().setMyLocationEnabled(true);;
//        getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if(AppState.getInstance().currentLocation!=null) {
            LatLng pos = new LatLng(AppState.getInstance().currentLocation.getLatitude(), AppState.getInstance().currentLocation.getLongitude());

//            markerUser=getMap().addMarker(new MarkerOptions()
//                    .position(pos)
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_location)));
//            );

        }

        BitmapDescriptor resourceMap=null;

        LatLng position=null;


        if(this.eMap == StoreMapModalFragment.MAP_ENUM.GOOGLE){

            resourceMap=BitmapDescriptorFactory.fromResource(R.drawable.sq_map_google);

            position=new LatLng(37.418207, -122.083319);
        }

        else if(this.eMap == StoreMapModalFragment.MAP_ENUM.TARGET){

            resourceMap=BitmapDescriptorFactory.fromResource(R.drawable.sq_map_target);

            position=new LatLng(37.418221, -122.083319);

        }
        else if(this.eMap == StoreMapModalFragment.MAP_ENUM.WHOLE_FOODS){

            resourceMap=BitmapDescriptorFactory.fromResource(R.drawable.sq_map_wf);

            position=new LatLng(37.418221, -122.083319);

        }

        //add map
        GroundOverlayOptions floorplan = new GroundOverlayOptions()
                .image(resourceMap)
                .anchor(0, 0)
                .position(new LatLng(37.418693, -122.084162), 107.5f, 107.5f);

        getMap().addGroundOverlay(floorplan);


        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(position, 19.3f));


        // getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(mPosFija, 16));
        markerUser = getMap().addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_location)));
        markerUser.setAnchor(.5f, .5f);


//        getMap().addMarker(new MarkerOptions().position(new LatLng(37.418311, -122.083968)));


        if(this.eMarker== StoreMapModalFragment.MARKER_ENUM.PHONE){

            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418437, -122.083440)));

        }
        else if(this.eMarker== StoreMapModalFragment.MARKER_ENUM.JACKET){

            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418426, -122.083454)));

        }
        else if (this.eMarker == StoreMapModalFragment.MARKER_ENUM.AVAILABLE_HERE_GOOGLE) {

            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418161, -122.083386)));
            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418256, -122.083381)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_dot)));
            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418290, -122.083378)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_dot)));

        }
        // NOT USED
        else if (this.eMarker == StoreMapModalFragment.MARKER_ENUM.WEAR) {
            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418221, -122.083319)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_oval))).setAnchor(.5f, .5f);

        }
        else if (this.eMarker == StoreMapModalFragment.MARKER_ENUM.JORDAN) {
            GroundOverlayOptions box = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_rect_jordan))
                    .anchor(.5f, .5f)
                    .position(new LatLng(37.418405, -122.083467), 13.5f, 15.5f);

            getMap().addGroundOverlay(box);


        }

        else if (this.eMarker == StoreMapModalFragment.MARKER_ENUM.CHECKOUT) {


            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418582, -122.083322)));//.setAnchor(.5f, .5f);//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_oval))

            //adjust position to be closer to checkout
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.418525, -122.083333), 19.3f));


        }
        else if (this.eMarker == StoreMapModalFragment.MARKER_ENUM.AVAILABLE_HERE_WF) {

            getMap().addMarker(new MarkerOptions().position(new LatLng(37.417997, -122.083384)));
            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418205, -122.083147)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_dot)));
            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418582, -122.083062)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_dot)));

        }
        else if (this.eMarker == StoreMapModalFragment.MARKER_ENUM.RECIPE) {

            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418219, -122.083375)));
            getMap().addMarker(new MarkerOptions().position(new LatLng(37.417997, -122.083384)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_dot)));
            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418210, -122.083238)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_dot)));
            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418129, -122.083145)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_dot)));
            getMap().addMarker(new MarkerOptions().position(new LatLng(37.418598, -122.083317)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_dot)));

        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("TAG","MAP READY");
        initMap();
    }
}
