package com.example.media.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.media.R;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.utils.GlideUtils;
import com.example.media.utils.ScreenUtils;
import com.example.media.weight.Toasts;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class PreviewAdapter extends PagerAdapter {

    private int mChildCount;
    private List<MediaSelectorFile> mData;
    public CheckBox mCbPlay;

    public void setOnPreviewViewClickListener(OnPreviewViewClickListener onPreviewViewClickListener) {
        this.onPreviewViewClickListener = onPreviewViewClickListener;
    }

    private OnPreviewViewClickListener onPreviewViewClickListener;

    public void setOnPreviewVideoClickListener(OnPreviewVideoClickListener onPreviewVideoClickListener) {
        this.onPreviewVideoClickListener = onPreviewVideoClickListener;
    }

    private OnPreviewVideoClickListener onPreviewVideoClickListener;

    public PreviewAdapter(List<MediaSelectorFile> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return PagerAdapter.POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        if (mData.get(position).isVideo) {
            View inflate = LayoutInflater.from(container.getContext()).inflate(R.layout.item_video_play_view, container, false);
            container.addView(inflate);

            final PhotoView pTData = inflate.findViewById(R.id.pt_data);
            mCbPlay = inflate.findViewById(R.id.cb_play);
            ViewGroup.LayoutParams layoutParams = pTData.getLayoutParams();
            layoutParams.width = ScreenUtils.screenWidth(container.getContext());
            layoutParams.height = ScreenUtils.screenHeight(container.getContext());
            pTData.setLayoutParams(layoutParams);
            GlideUtils.loadImage(container.getContext(), mData.get(position).filePath, pTData,false);
            mCbPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && onPreviewVideoClickListener !=null) {
                        onPreviewVideoClickListener.onClickVideo(mCbPlay,position);

                    }
                }
            });
            clickPhotoView(pTData);

            return inflate;
        } else {
            PhotoView photoView = new PhotoView(container.getContext());
            container.addView(photoView);
            ViewGroup.LayoutParams layoutParams = photoView.getLayoutParams();
            layoutParams.width = ScreenUtils.screenWidth(container.getContext());
            layoutParams.height = ScreenUtils.screenHeight(container.getContext());
            photoView.setLayoutParams(layoutParams);
            GlideUtils.loadImage(container.getContext(), mData.get(position).filePath, photoView,false);
            clickPhotoView(photoView);
            return photoView;
        }

    }

    private void clickPhotoView(@NonNull PhotoView photoView) {
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (onPreviewViewClickListener != null) {
                    onPreviewViewClickListener.onPreviewView(view);
                }
            }
        });
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public static class PreviewPageTransformer implements ViewPager.PageTransformer {
        private ViewPager viewPager;

        @Override
        public void transformPage(@NonNull View view, float position) {
            if (viewPager == null) {
                viewPager = (ViewPager) view.getParent();
            }
            int leftInScreen = view.getLeft() - viewPager.getScrollX();
            float offsetRate = (float) leftInScreen * 0.08f / viewPager.getMeasuredWidth();
            float scaleFactor = 1 - Math.abs(offsetRate);
            if (scaleFactor > 0) {
                view.setScaleX(scaleFactor);
            }
        }
    }

    public interface OnPreviewViewClickListener {
        void onPreviewView(View view);
    }

    public interface OnPreviewVideoClickListener {
        void onClickVideo(View view, int position);
    }
}
