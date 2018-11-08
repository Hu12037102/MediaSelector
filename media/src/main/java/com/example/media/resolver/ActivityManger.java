package com.example.media.resolver;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ActivityManger {
    private static ActivityManger mManger;
    private Map<String, SoftReference<Activity>> mMap;

    private ActivityManger() {
        mMap = new HashMap<>();
    }

    public static ActivityManger get() {
        synchronized (ActivityManger.class) {
            if (mManger == null) {
                synchronized (ActivityManger.class) {
                    mManger = new ActivityManger();
                }
            }
        }
        return mManger;
    }

    public void addActivity(@NonNull Activity activity) {
            mMap.put(activity.getClass().getSimpleName(), new SoftReference<>(activity));
    }

    public void removeActivity(@NonNull String key) {
        if (mMap.size() > 0) {
            SoftReference<Activity> softReference = mMap.get(key);
            if (softReference != null && softReference.get() != null && !softReference.get().isFinishing()) {
                softReference.get().finish();
            }
            mMap.remove(key);
        }
    }

    public void clearActivity() {
        if (mMap.size() > 0) {
            Set<Map.Entry<String, SoftReference<Activity>>> entrySet = mMap.entrySet();
            Iterator<Map.Entry<String, SoftReference<Activity>>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, SoftReference<Activity>> next = iterator.next();
                if (next.getValue() != null && next.getValue().get() != null) {
                    next.getValue().get().finish();
                }
                iterator.remove();
            }
        }
    }

}
