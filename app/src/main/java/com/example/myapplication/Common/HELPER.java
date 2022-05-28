package com.example.myapplication.Common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class HELPER {

    public static void print(String tag, String message) {
        Log.e(tag, message);
    }

    public static void printData(String tag, String message) {
        print("Tag:", tag);
        print("Response:", message);
    }

    public static void WHATSAPP_REDIRECTION(Activity act, String mobileNumber) {
        try {
            String BrandContact = "\nRegistered Number: ";
            //String text = "Hello *BrandMania* ,  \n" + "This is request to add  *Frame* For BrandName:" + businessName + BrandContact + mobileNumber;
            String toNumber = "91" + mobileNumber;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + toNumber));
            act.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
