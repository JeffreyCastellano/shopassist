package com.fa.google.shopassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by stevensanborn on 2/6/15.
 */
public interface ActivityListener {

    public void onResult(int requestCode, int resultCode, Intent data);

    public void onDestroy();

    public void onStart();

    public void onStop();

    public void onPause();

    public void onResume();

    public void onNewIntent(Intent intent);

    public void onCreate(Bundle savedInstanceState);

}
