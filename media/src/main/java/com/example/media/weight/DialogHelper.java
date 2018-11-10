package com.example.media.weight;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.example.media.R;

/**
 * 项  目 :  MediaSelector
 * 包  名 :  com.example.media.weight
 * 类  名 :  ${CLASS_NAME}
 * 作  者 :  胡庆岭
 * 时  间 : 2018/11/10
 * 描  述 :  ${TODO}
 *
 * @author ：
 */
public class DialogHelper {
    private DialogHelper() {
    }

    public static DialogHelper with() {
        return new DialogHelper();
    }

    public AlertDialog createDialog(@NonNull Context context, @NonNull String title, @NonNull String message,
                                  @NonNull DialogInterface.OnClickListener onNegativeListener, @NonNull DialogInterface.OnClickListener onPositiveListener) {
        return new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setNegativeButton(R.string.cancel, onNegativeListener).setPositiveButton(R.string.confirm, onPositiveListener)
                .create();
    }
}
