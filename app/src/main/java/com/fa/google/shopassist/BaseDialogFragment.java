package com.fa.google.shopassist;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;

import com.fa.google.shopassist.globals.AppState;

/**
 * Created by stevensanborn on 5/6/15.
 */
public class BaseDialogFragment extends DialogFragment {


    @Override
    public void show(FragmentManager manager, String tag) {
        if (AppState.getInstance().bDialogFragment) return;

        super.show(manager, tag);
        AppState.getInstance().bDialogFragment=true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        AppState.getInstance().bDialogFragment=false;
        super.onDismiss(dialog);
    }

}
