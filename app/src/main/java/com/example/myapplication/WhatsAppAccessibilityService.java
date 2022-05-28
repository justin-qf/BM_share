package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class WhatsAppAccessibilityService extends AccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (getRootInActiveWindow() == null) {
            return;
        }

        AccessibilityNodeInfoCompat rootNodeInfo = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
        List<AccessibilityNodeInfoCompat> messageNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
        if (messageNodeList == null || messageNodeList.isEmpty()) {
            return;
        }
        AccessibilityNodeInfoCompat messageField = messageNodeList.get(0);
        if (messageField == null || messageField.getText().length() == 0 || !messageField.getText().toString().endsWith("   "))
            return;


        List<AccessibilityNodeInfoCompat> sendMessageNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
        if (sendMessageNodeList == null || sendMessageNodeList.isEmpty()) {
            return;
        }


        AccessibilityNodeInfoCompat sendMessage = sendMessageNodeList.get(0);
        if (!sendMessage.isVisibleToUser()) {
            return;
        }

        sendMessage.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        //go back by clicking back buton

        try {
            Thread.sleep(2000);
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(GLOBAL_ACTION_BACK);
    }



    @Override
    public void onInterrupt() {

    }
}