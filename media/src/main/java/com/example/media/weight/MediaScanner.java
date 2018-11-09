package com.example.media.weight;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.imagecompress
 * 类  名 :  utils.MediaScanner
 * 作  者 :  胡庆岭
 * 时  间 :  2018/1/31 0031 上午 11:03
 * 描  述 :  ${TODO}
 *
 * @author ：
 */

public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mMSC;
    private File mImageFile;

    @Override
    public void onMediaScannerConnected() {
        mMSC.scanFile(mImageFile.getAbsolutePath(), "image");
    }

    @Override
    public void onScanCompleted(String s, Uri uri) {
        mMSC.disconnect();
    }

    public MediaScanner(Context context, File file) {
        this.mImageFile = file;
        if (mMSC == null) {
            mMSC = new MediaScannerConnection(context, this);
        }

    }

    public void refresh() {
        if (mMSC != null && !mMSC.isConnected()) {
            mMSC.connect();
        }
    }

}
