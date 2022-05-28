package com.example.myapplication.Common;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.accessibility.AccessibilityEvent;

import java.net.URLEncoder;

public class MySMService extends AccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }


    public static void handleActionWhatsapp(Activity act, String message, String count, String[] mobile_number) {
        Intent intent = null;
        try {

            PackageManager packageManager = act.getPackageManager();

            if (mobile_number.length != 0) {

                for (int j = 0; j < mobile_number.length; j++) {

                    for (int i = 0; i < Integer.parseInt(count.toString()); i++) {
                        String number = mobile_number[j];
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(message, "UTF-8")));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.whatsapp");

                        if (intent.resolveActivity(packageManager) != null) {
                            act.startActivity(intent);
                            Thread.sleep(10000);
                            act.sendOrderedBroadcast(intent, "Result: " + number);
                        } else {
                            act.sendOrderedBroadcast(intent, "Whatsapp Not Installed");
                        }
                    }
                }
            }
        } catch (Exception e) {
            act.sendOrderedBroadcast(intent, "Result: " + e.toString());
        }
    }

}
