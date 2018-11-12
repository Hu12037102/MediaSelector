package com.example.media.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.media.R;

public class Toasts {


    private Toasts() {

    }

    private static Toasts mToasts = new Toasts();

    public static Toasts with() {
        return mToasts;
    }

    private Toast mToast;

    public void showToast(@NonNull Context context, @NonNull String text) {
        if (mToast == null) {
             mToast = Toast.makeText(context.getApplicationContext(),text, Toast.LENGTH_SHORT);

        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        Log.w("showToast--", "showToast");
        mToast.show();
    }

    public synchronized void showToast(@NonNull Context context, @StringRes int text, Object... object) {

        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), context.getString(text, object), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(context.getString(text, object));
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        Log.w("showToast--", "showToast");
        mToast.show();
    }
}
