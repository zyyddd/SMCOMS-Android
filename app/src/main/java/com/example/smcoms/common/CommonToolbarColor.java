package com.example.smcoms.common;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.example.smcoms.R;

public class CommonToolbarColor {
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void toolbarColorSet(Activity activity)
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.setStatusBarColor(activity.getColor(R.color.colorPrimary));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
