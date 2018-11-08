package com.example.media.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.media.bean.MediaSelectorFile;
import com.example.media.utils.GlideUtils;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class PreviewAdapter extends PagerAdapter {

    private int mChildCount;
    private List<MediaSelectorFile> mData;

    public void setOnPreviewViewClickListener(OnPreviewViewClickListener onPreviewViewClickListener) {
        this.onPreviewViewClickListener = onPreviewViewClickListener;
    }

    private OnPreviewViewClickListener onPreviewViewClickListener;

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
    public PhotoView instantiateItem(@NonNull final ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        container.addView(photoView);
        ViewGroup.LayoutParams layoutParams = photoView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        photoView.setLayoutParams(layoutParams);
        GlideUtils.loadImage(container.getContext(), mData.get(position).filePath, photoView);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (onPreviewViewClickListener != null) {
                    onPreviewViewClickListener.onPreviewView(view);
                }
            }
        });

        return photoView;
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
}
