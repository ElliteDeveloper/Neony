package com.ptnst.neon.neon;

import androidx.multidex.MultiDexApplication;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;


@ReportsCrashes(
        formKey = "",
        mailTo = "sokchea1099@gmail.com")
public class NeonApplication extends MultiDexApplication {
    @Override
    public void onCreate(){
        super.onCreate();
        ACRA.init(this);
    }
}
