package com.hznge.nlscollect.Service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import com.hznge.nlscollect.NotificationMonitor;

import java.util.List;

public class KeepInTouchService extends Service {
    private static final String TAG = "KeepInTouchService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        ensureServiceRunning();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void ensureServiceRunning() {
        ComponentName collectorComponent = new ComponentName(this, NotificationMonitor.class);
        Log.v(TAG, "ensure CollectRunning component: " + collectorComponent);
        
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        boolean collectorRunning = false;

        List<ActivityManager.RunningServiceInfo> runningService = manager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo service:
             runningService) {
            if (service.service.equals(collectorComponent)) {
                Log.w(TAG, "ensureServiceRunning service - pid" + service.pid + ", currentPID" + Process.myPid()
                    + ", clientPackage" + service.clientPackage + ", clientLabel: " 
                        + ((service.clientLabel == 0) ? "0" : "(" + getResources().getString(service.clientLabel) + ")")
                );
                
                if (service.pid == Process.myPid()) {
                    collectorRunning = true;
                }
            }
            
        }
        
        if (collectorRunning) {
            Log.d(TAG, "ensureServiceRunning: cp  ");
        }
    }

    public KeepInTouchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
