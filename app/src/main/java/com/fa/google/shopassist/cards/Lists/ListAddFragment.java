package com.fa.google.shopassist.cards.Lists;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fa.google.shopassist.ListFragment;
import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;
import com.fa.google.shopassist.compare.CompareOverlayLayout;
import com.fa.google.shopassist.globals.AppState;
import com.fa.google.shopassist.models.ListItemRowModel;
import com.fa.google.shopassist.models.ListModel;

import java.io.IOException;

/**
 * Created by stevensanborn on 3/2/15.
 */
public class ListAddFragment  extends DialogFragment {


    public LinearLayout layoutListsContainer;
    public LinearLayout layoutListsComplete;
    public LinearLayout layoutListCreate;

    public LinearLayout layoutLists;
    public LinearLayout layoutAddNewList;

    public Button btnClose;

    public int iListClicked;

    public ListAddFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);

//            AppState.getInstance().bCompareShown=true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View V = (ViewGroup) inflater.inflate(getResources().getLayout(R.layout.layout_add_to_list), container);

        layoutListsComplete=(LinearLayout)V.findViewById(R.id.layout_add_list_complete);
        layoutListsContainer=(LinearLayout)V.findViewById(R.id.layout_add_list_container);
        layoutListCreate=(LinearLayout)V.findViewById(R.id.layout_add_list_create);
        layoutAddNewList=(LinearLayout)V.findViewById(R.id.layout_add_new_list);


        layoutListsComplete.setVisibility(View.GONE);
        layoutListCreate.setVisibility(View.GONE);

        btnClose = (Button) V.findViewById(R.id.btn_cancel);

        View.OnClickListener clickDismmissListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
        btnClose.setOnClickListener(clickDismmissListener);


        layoutLists=(LinearLayout)V.findViewById(R.id.layout_existing_lists);

        int index=0;

        for(final ListModel LM: AppState.getInstance().myList.items){

            View VRow=inflater.inflate(getResources().getLayout(R.layout.layout_add_list_row),layoutLists,false);

            TextView tv=(TextView)VRow.findViewById(R.id.text_list_name);
            tv.setText(LM.strListName);


            Bitmap bmp=null;
            try {
                bmp= BitmapFactory.decodeStream(getActivity().getAssets().open(LM.strRowThImage));
            }catch (IOException e){
                Log.e("IO error", "e " + e.getLocalizedMessage());
            }

            ImageView imgth=(ImageView)VRow.findViewById(R.id.img_add_list_row);
            imgth.setImageBitmap(bmp);

            layoutLists.addView(VRow);


            final int iFinalIndex=index;

            VRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layoutListsContainer.setVisibility(View.GONE);
                    layoutListsComplete.setVisibility(View.VISIBLE);
                    layoutListsComplete.setAlpha(0f);
                    layoutListsComplete.animate().alpha(1).setDuration(300);
                    ListItemRowModel LIM=new ListItemRowModel(AppState.getInstance().CurrentProduct.strId);

                    String strListName=LIM.strListName;


                    LM.items.add(LIM);

                    iListClicked=iFinalIndex;
                }
            });


            index++;
        }

        V.findViewById(R.id.btn_ok).setOnClickListener(clickDismmissListener);
        V.findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                AppState.getInstance().CurrentListCategory= AppState.getInstance().myList;
                AppState.getInstance().CurrentList=AppState.getInstance().myList.items.get(iListClicked);


                ListViewFragment listViewFragment= new ListViewFragment();

                Bundle args = new Bundle();
                args.putInt(ListFragment.ARG_PARAM1, R.layout.layout_list);
                args.putString(ListFragment.ARG_PARAM2, "list");
                listViewFragment.setArguments(args);

                ((MainActivity)getActivity()).setFragment(listViewFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);
//
            }
        });


        layoutAddNewList.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutListsContainer.setVisibility(View.GONE);
                layoutListCreate.setVisibility(View.VISIBLE);
                layoutListCreate.setAlpha(0f);
                layoutListCreate.animate().alpha(1).setDuration(300);
            }
        });

        V.findViewById(R.id.btn_cancel_new_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutListsContainer.setVisibility(View.VISIBLE);
                layoutListCreate.setVisibility(View.GONE);
                layoutListsContainer.setAlpha(0f);
                layoutListsContainer.animate().alpha(1).setDuration(300);
            }
        });
        V.findViewById(R.id.btn_create_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create the new list model

                final ListModel LM = new ListModel();
                LM.strMainAsset="images/custom/custom_main.png";
                LM.strListName=((EditText)V.findViewById(R.id.edit_list_create_name)).getText().toString();
                LM.strRowThImage="images/custom/custom_th.png";
                LM.strRowImage=null;
                
//                LM.strRowBGImage="images/custom/custom_content.png";

                ListItemRowModel LIM=new ListItemRowModel(AppState.getInstance().CurrentProduct.strId);

                LM.items.add(LIM);

                //add it to my lists
                AppState.getInstance().myList.items.add(LM);

                //set the title
                ((TextView)V.findViewById(R.id.text_list_add_create)).setText("List Created!");



                //show the created layout
                LinearLayout layoutCreated=(LinearLayout)V.findViewById(R.id.layout_add_list_created);
                layoutCreated.setVisibility(View.VISIBLE);
                layoutCreated.setAlpha(0f);
                layoutCreated.animate().alpha(1).setDuration(300);


                //hide the form
                V.findViewById(R.id.layout_create_form).setVisibility(View.GONE);

                //set the propper title
                ((TextView)V.findViewById(R.id.text_list_created)).setText(LM.strListName);

                //set public or not
                if(!((CheckBox)V.findViewById(R.id.checkbox_add_list_create)).isChecked())
                    ((TextView) V.findViewById(R.id.text_list_created_public)).setText("This list is public");
                else
                    ((TextView) V.findViewById(R.id.text_list_created_public)).setText("This list is private");


                V.findViewById(R.id.btn_view_created).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();

                        AppState.getInstance().CurrentList=LM;
                        ListViewFragment listViewFragment= new ListViewFragment();

                        Bundle args = new Bundle();
                        args.putInt(ListFragment.ARG_PARAM1, R.layout.layout_list);
                        args.putString(ListFragment.ARG_PARAM2, "list");
                        listViewFragment.setArguments(args);

                        ((MainActivity)getActivity()).setFragment(listViewFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN,false);
                    }
                });


            }
        });

        V.findViewById(R.id.btn_ok_created).setOnClickListener(clickDismmissListener);

        // set color transpartent
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);

        V.setOnClickListener(clickDismmissListener);
        return V;
    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
//        getDialog().getWindow()
//                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

}
