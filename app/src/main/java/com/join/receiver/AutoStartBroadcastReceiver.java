package com.join.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.join.activity.HomePage;

/**
 * Created by Administrator on 2017/6/18 0018.
 */

public class AutoStartBroadcastReceiver extends BroadcastReceiver {
    String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)) {
            Intent sayHelloIntent = new Intent(context, HomePage.class);

            sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(sayHelloIntent);
        }
    }
}
