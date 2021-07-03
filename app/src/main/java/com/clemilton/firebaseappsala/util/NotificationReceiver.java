package com.clemilton.firebaseappsala.util;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.navigation.NavDeepLinkBuilder;

import com.clemilton.firebaseappsala.NavigationActivity;
import com.clemilton.firebaseappsala.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        String message = intent.getStringExtra("toast");
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();


       Intent intent1 = new Intent();
       intent1.setClassName("com.clemilton.firebaseappsala",
               "com.clemilton.firebaseappsala.NavigationActivity");
       intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       context.startActivity(intent1);


    }
}
