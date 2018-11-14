package com.example.media;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.media.activity.MediaActivity;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.resolver.Contast;

import java.lang.ref.SoftReference;
import java.util.List;

public class MediaSelector {
    private MediaOptions mMediaOptions = MediaSelector.getDefaultOptions();
    private SoftReference<Activity> mSoftActivity;
    private SoftReference<Fragment> mSoftFragment;

    private MediaSelector(Activity activity) {
        mSoftActivity = new SoftReference<>(activity);
    }

    private MediaSelector(Fragment fragment) {
        mSoftFragment = new SoftReference<>(fragment);
    }

    public static MediaSelector with(Activity activity) {
        return new MediaSelector(activity);
    }

    public static MediaSelector with(Fragment fragment) {
        return new MediaSelector(fragment);
    }

    public MediaSelector setMediaOptions(@NonNull MediaOptions options) {
        this.mMediaOptions = options;
        return this;
    }

    public void openMediaActivity() {
        if (mSoftActivity != null && mSoftActivity.get() != null) {
            Activity activity = mSoftActivity.get();
            Intent intent = new Intent(activity, MediaActivity.class);
            intent.putExtra(Contast.KEY_OPEN_MEDIA, mMediaOptions);
            activity.startActivityForResult(intent, Contast.CODE_REQUEST_MEDIA);
        } else if (mSoftFragment != null && mSoftFragment.get() != null) {
            Fragment fragment = mSoftFragment.get();
            Intent intent = new Intent(fragment.getContext(), MediaActivity.class);
            intent.putExtra(Contast.KEY_OPEN_MEDIA, mMediaOptions);
            fragment.startActivityForResult(intent, Contast.CODE_REQUEST_MEDIA);
        }
    }

    public static List<MediaSelectorFile> resultMediaFile(Intent data) {
        if (data == null)
            return null;
        return data.getParcelableArrayListExtra(Contast.KEY_REQUEST_MEDIA_DATA);
    }


    public static class MediaOptions implements Parcelable {
        public MediaOptions() {
        }

        public int maxChooseMedia = Contast.MAX_CHOOSE_MEDIA;
        public boolean isCompress;
        public boolean isShowCamera;
        public boolean isShowVideo;
        public @ColorRes
        int themeColor = R.color.colorTheme;
        public boolean isCrop;
        public int scaleX = 1;
        public int scaleY = 1;
        public int cropWidth = 720;
        public int cropHeight = 720;


        protected MediaOptions(Parcel in) {
            maxChooseMedia = in.readInt();
            isCompress = in.readByte() != 0;
            isShowCamera = in.readByte() != 0;
            isShowVideo = in.readByte() != 0;
            themeColor = in.readInt();
            isCrop = in.readByte() != 0;
            scaleX = in.readInt();
            scaleY = in.readInt();
            cropWidth = in.readInt();
            cropHeight = in.readInt();
        }

        public static final Creator<MediaOptions> CREATOR = new Creator<MediaOptions>() {
            @Override
            public MediaOptions createFromParcel(Parcel in) {
                return new MediaOptions(in);
            }

            @Override
            public MediaOptions[] newArray(int size) {
                return new MediaOptions[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(maxChooseMedia);
            dest.writeByte((byte) (isCompress ? 1 : 0));
            dest.writeByte((byte) (isShowCamera ? 1 : 0));
            dest.writeByte((byte) (isShowVideo ? 1 : 0));
            dest.writeInt(themeColor);
            dest.writeByte((byte) (isCrop ? 1 : 0));
            dest.writeInt(scaleX);
            dest.writeInt(scaleY);
            dest.writeInt(cropWidth);
            dest.writeInt(cropHeight);
        }
    }

    public static MediaOptions getDefaultOptions() {
        return new MediaOptions();
    }
}
