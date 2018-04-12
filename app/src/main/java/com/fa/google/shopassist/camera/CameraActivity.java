/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fa.google.shopassist.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fa.google.shopassist.MainActivity;
import com.fa.google.shopassist.R;

public class CameraActivity extends Activity {

    public static boolean active = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }


    }

    @Override
    public void onStart(){
        super.onStart();
        CameraActivity.active=true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();;
        CameraActivity.active=false;
    }
}
