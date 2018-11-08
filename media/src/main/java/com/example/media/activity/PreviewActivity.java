package com.example.media.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.example.item.weight.TitleView;
import com.example.media.R;
import com.example.media.adapter.PreviewAdapter;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.resolver.ActivityManger;
import com.example.media.resolver.Contast;
import com.example.media.utils.ScreenUtils;
import com.example.media.weight.PreviewViewPager;
import com.example.media.weight.Toasts;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends BaseActivity {

    private PreviewViewPager mVpPreview;
    private TitleView mTvTop;
    private TitleView mTvBottom;
    private List<MediaSelectorFile> mMediaFileData;
    private PreviewAdapter mPreviewAdapter;
    private boolean isShowTitleView = true;
    private int mPreviewPosition;


    @Override
    protected void initUI() {


    }

    @Override
    protected void initView() {
        mVpPreview = findViewById(R.id.vp_preview);
        mTvTop = findViewById(R.id.ctv_top);
        mTvBottom = findViewById(R.id.ctv_bottom);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mMediaFileData = intent.getParcelableArrayListExtra(Contast.KEY_PREVIEW_MEDIA);
        mPreviewPosition = intent.getIntExtra(Contast.KEY_PREVIEW_POSITION, 0);
        if (mMediaFileData == null || mMediaFileData.size() == 0) {
            Toasts.with().showToast(this, "没有预览媒体库文件");
            finish();
            return;
        }
        mTvTop.mTvBack.setText(getString(R.string.count_sum_count, String.valueOf(mPreviewPosition + 1), String.valueOf(mMediaFileData.size())));

        mTvBottom.mTvSure.setCompoundDrawablesWithIntrinsicBounds(mMediaFileData.get(mPreviewPosition).isCheck ? R.mipmap.icon_preview_check : R.mipmap.icon_preview_uncheck, 0, 0, 0);
        mPreviewAdapter = new PreviewAdapter(mMediaFileData);
        mVpPreview.setAdapter(mPreviewAdapter);
        mVpPreview.setCurrentItem(mPreviewPosition, true);
        mVpPreview.setPageTransformer(true, new PreviewAdapter.PreviewPageTransformer());
    }

    @Override
    protected void initEvent() {
        mVpPreview.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mPreviewPosition = i;
                mTvTop.mTvBack.setText(getString(R.string.count_sum_count, String.valueOf(i + 1), String.valueOf(mMediaFileData.size())));
                mTvBottom.mTvSure.setCompoundDrawablesWithIntrinsicBounds(mMediaFileData.get(i).isCheck ? R.mipmap.icon_preview_check : R.mipmap.icon_preview_uncheck, 0, 0, 0);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mPreviewAdapter.setOnPreviewViewClickListener(new PreviewAdapter.OnPreviewViewClickListener() {
            @Override
            public void onPreviewView(View view) {
                topTileViewAnimation();
                isShowTitleView = !isShowTitleView;
            }
        });
        mTvBottom.setOnSureViewClickListener(new TitleView.OnSureViewClickListener() {
            @Override
            public void onSureClick(@NonNull View view) {
                mMediaFileData.get(mPreviewPosition).isCheck = !mMediaFileData.get(mPreviewPosition).isCheck;
                mTvBottom.mTvSure.setCompoundDrawablesWithIntrinsicBounds(mMediaFileData.get(mPreviewPosition).isCheck ? R.mipmap.icon_preview_check : R.mipmap.icon_preview_uncheck, 0, 0, 0);
                EventBus.getDefault().post(mMediaFileData.get(mPreviewPosition));
                // setResult(Activity.RESULT_OK);
               /* Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(Contast.KEY_PREVIEW_MEDIA, (ArrayList<? extends Parcelable>) mMediaFileData);
                    setResult(Activity.RESULT_OK, intent);*/
            }
        });
        mTvTop.setOnSureViewClickListener(new TitleView.OnSureViewClickListener() {
            @Override
            public void onSureClick(@NonNull View view) {
                setResult(Activity.RESULT_OK);
                finish();

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    private void topTileViewAnimation() {
        ObjectAnimator topAnimatorTranslation;
        ObjectAnimator bottomAnimatorTranslation;
        AnimatorSet animatorSet = new AnimatorSet();
        if (isShowTitleView) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            topAnimatorTranslation = ObjectAnimator.ofFloat(mTvTop, "translationY", 0, -(ScreenUtils.getStatuWindowsHeight(this) + mTvTop.getMeasuredHeight()));
            bottomAnimatorTranslation = ObjectAnimator.ofFloat(mTvBottom, "translationY", 0, (mTvBottom.getMeasuredHeight()));

        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            topAnimatorTranslation = ObjectAnimator.ofFloat(mTvTop, "translationY", -(ScreenUtils.getStatuWindowsHeight(this) + mTvTop.getMeasuredHeight()), 0);
            bottomAnimatorTranslation = ObjectAnimator.ofFloat(mTvBottom, "translationY", (mTvBottom.getMeasuredHeight()), 0);

        }
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.play(topAnimatorTranslation).with(bottomAnimatorTranslation);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            //    mPreviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


}
