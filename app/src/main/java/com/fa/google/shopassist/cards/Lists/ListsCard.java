package com.fa.google.shopassist.cards.Lists;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fa.google.shopassist.ListFragment;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.cards.BaseCardLayout;
import com.fa.google.shopassist.cards.FeelingLuckyCard;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.ListCategoryModel;
import com.fa.google.shopassist.models.ListItemRowModel;
import com.fa.google.shopassist.models.ListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevensanborn on 2/27/15.
 */
public class ListsCard extends BaseCardLayout {

    private TextView textViewTitle;

    private ListView listView;

    private ListCategoryModel ListCat;

    private String strData;

    private ListItemArrayAdapter  arrayAdapter;

    private final  static String TAG=FeelingLuckyCard.class.getSimpleName();

    public ListsCard(Context context, AttributeSet attrs) {

        super(context, attrs);


        if(attrs!=null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ListsCard,
                    0, 0);

            CharSequence charData=a.getText(R.styleable.ListsCard_list_data).toString();

            if(charData!=null){
                strData=charData.toString();

                //load list from file
                if(strData.equals("mylists")){
                    ListCat=AppState.getInstance().myList;
                }
                else if(charData.toString().equals("followedlists")){
                    ListCat=AppState.getInstance().followedList;
                }

                else if(charData.toString().equals("recommended")){
                    ListCat=AppState.getInstance().recommendedList;
                }

                ArrayList<ListItemRowModel> items= new ArrayList<ListItemRowModel>(ListCat.items);

                arrayAdapter= new ListItemArrayAdapter(getContext(),0,items);

            }

        }


    }



    @Override

    public void onFinishInflate() {

        super.onFinishInflate();

        textViewTitle=(TextView)findViewById(R.id.txt_list_title);

        textViewTitle.setText(ListCat.strTitle);

        listView=(ListView) findViewById(R.id.list_list_content);

        listView.setAdapter(this.arrayAdapter);

        listView.setDividerHeight(0);

        DisplayMetrics metrics = new DisplayMetrics();
        ((MainActivity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);



        //get the row height

        float fHeightRow=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95, getResources().getDisplayMetrics());
//        Log.d(TAG,"H "+fHeightRow);
        fHeightRow+=50;//*((float)metrics.widthPixels/1080.0f-1);

        fHeightRow=285*metrics.widthPixels/1080;

        listView.getLayoutParams().height=(int)(this.arrayAdapter.items.size()*fHeightRow);//expand this card

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final View imgThumb=view.findViewById(R.id.img_row_img);

                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {

                          //go to list
                          AppState.getInstance().CurrentListCategory= ListCat;
                          AppState.getInstance().CurrentList=(ListModel)arrayAdapter.items.get(position);

                          ListViewFragment listViewFragment= new ListViewFragment();

                          Bundle args = new Bundle();
                          args.putInt(ListFragment.ARG_PARAM1, R.layout.layout_list);
                          args.putString(ListFragment.ARG_PARAM2, AppState.getInstance().CurrentList.strListName);
                          listViewFragment.setArguments(args);

//                          parentFragment.strSharedElement="headerimage";
                          imgThumb.setTransitionName("headerimage");
                          imgThumb.setId(generateViewId());
//                          parentFragment.sharedElementView=imgThumb;
                          ((MainActivity)getContext()).setFragment(listViewFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);

                      }
                  },300);
            }
        });




    }






}
