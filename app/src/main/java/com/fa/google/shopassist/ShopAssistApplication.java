package com.fa.google.shopassist;

import android.app.Application;
import android.util.Log;

import com.estimote.sdk.EstimoteSDK;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;


@ReportsCrashes(
        formUri = "https://yeahdixon.cloudant.com/acra-shop-assist/_design/acra-storage/_update/report",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        formUriBasicAuthLogin = "maysessettedgemanderaver",
        formUriBasicAuthPassword = "1r8vKO2InnmQQSDFMJ0XG8qq",
        formKey = "", // This is required for backward compatibility but not used
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.toast_crash
)
/**
 * Created by stevensanborn on 5/27/15.
 */
public class ShopAssistApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // The following line triggers the initialization of ACRA
        //ACRA.init(this);
        Log.d("APP","shop app");

    }
}
