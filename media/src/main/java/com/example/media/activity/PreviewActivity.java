package com.example.media.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.example.item.weight.TitleView;
import com.example.media.MediaSelector;
import com.example.media.OnRecyclerItemClickListener;
import com.example.media.R;
import com.example.media.adapter.MediaCheckAdapter;
import com.example.media.adapter.PreviewAdapter;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.resolver.Contast;
import com.example.media.utils.ScreenUtils;
import com.example.media.weight.PreviewViewPager;
import com.example.media.weight.Toasts;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.bean.ImageConfig;
import utils.task.CompressImageTask;

public class PreviewActivity extends BaseActivity {

    private PreviewViewPager mVpPreview;
    private TitleView mTvTop;
    private TitleView mTvBottom;
    private List<MediaSelectorFile> mMediaFileData;
    private PreviewAdapter mPreviewAdapter;
    private boolean isShowTitleView = true;
    private int mPreviewPosition;
    private RecyclerView mRvCheckMedia;
    private View mLlBottom;
    private List<MediaSelectorFile> mCheckMediaData;
    private MediaCheckAdapter mCheckAdapter;
    private AnimatorSet mAnimatorSet;
    private MediaSelector.MediaOptions mOptions;


    @Override
    protected void initUI() {
    }

    @Override
    protected void initView() {
        mVpPreview = findViewById(R.id.vp_preview);
        ViewGroup.LayoutParams layoutParams = mVpPreview.getLayoutParams();
        layoutParams.width = ScreenUtils.screenWidth(this);
        layoutParams.height = ScreenUtils.screenHeight(this);
        mVpPreview.setLayoutParams(layoutParams);
        mTvTop = findViewById(R.id.ctv_top);
        mRvCheckMedia = findViewById(R.id.rv_check_media);
        mRvCheckMedia.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        mTvBottom = findViewById(R.id.ctv_bottom);
        mLlBottom = findViewById(R.id.ll_bottom);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mCheckMediaData = intent.getParcelableArrayListExtra(Contast.KEY_PREVIEW_CHECK_MEDIA);
        mMediaFileData = intent.getParcelableArrayListExtra(Contast.KEY_PREVIEW_DATA_MEDIA);
        mPreviewPosition = intent.getIntExtra(Contast.KEY_PREVIEW_POSITION, 0);
        mOptions = intent.getParcelableExtra(Contast.KEY_OPEN_MEDIA);
        if (mMediaFileData == null || mMediaFileData.size() == 0) {
            Toasts.with().showToast(this, "没有预览媒体库文件");
            finish();
            return;
        }
        if (mCheckMediaData != null && mCheckMediaData.size() > 0) {
            for (int i = 0; i < mCheckMediaData.size(); i++) {
                if (!mMediaFileData.contains(mCheckMediaData.get(i))) {
                    mMediaFileData.add(mCheckMediaData.get(i));
                }
            }
        }
        mTvTop.mTvBack.setText(getString(R.string.count_sum_count, String.valueOf(mPreviewPosition + 1), String.valueOf(mMediaFileData.size())));
        setTitleViewSureText();
        mTvBottom.mTvSure.setCompoundDrawablesWithIntrinsicBounds(mMediaFileData.get(mPreviewPosition).isCheck ? R.mipmap.icon_preview_check : R.mipmap.icon_preview_uncheck, 0, 0, 0);
        mPreviewAdapter = new PreviewAdapter(mMediaFileData);
        mVpPreview.setAdapter(mPreviewAdapter);
        mVpPreview.setCurrentItem(mPreviewPosition, true);
        mVpPreview.setPageTransformer(true, new PreviewAdapter.PreviewPageTransformer());

        mCheckAdapter = new MediaCheckAdapter(this, mCheckMediaData);
        mRvCheckMedia.setAdapter(mCheckAdapter);
        mCheckAdapter.notifyCheckData(mMediaFileData.get(mPreviewPosition));
        initAdapterEvent();
    }

    private void setTitleViewSureText() {
        if (mCheckMediaData.size() > 0) {
            mTvTop.mTvSure.setText(getString(R.string.complete_count, String.valueOf(mCheckMediaData.size()), String.valueOf(mOptions.maxChooseMedia)));
        } else {
            mTvTop.mTvSure.setText(R.string.sure);
        }
    }

    private void initAdapterEvent() {
        mVpPreview.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mPreviewPosition = i;
                mTvTop.mTvBack.setText(getString(R.string.count_sum_count, String.valueOf(i + 1), String.valueOf(mMediaFileData.size())));
                mTvBottom.mTvSure.setCompoundDrawablesWithIntrinsicBounds(mMediaFileData.get(i).isCheck ? R.mipmap.icon_preview_check : R.mipmap.icon_preview_uncheck, 0, 0, 0);
                mCheckAdapter.notifyCheckData(mMediaFileData.get(mPreviewPosition));
                if (mCheckMediaData.contains(mMediaFileData.get(mPreviewPosition))) {
                    mRvCheckMedia.scrollToPosition(mCheckMediaData.indexOf(mMediaFileData.get(mPreviewPosition)));
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mPreviewAdapter.setOnPreviewViewClickListener(new PreviewAdapter.OnPreviewViewClickListener() {
            @Override
            public void onPreviewView(View view) {
                if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
                    mAnimatorSet.end();
                }
                titleViewAnimation();
                isShowTitleView = !isShowTitleView;
            }
        });
        mCheckAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void itemClick(@NonNull View view, int position) {
                if (mMediaFileData.contains(mCheckMediaData.get(position))) {
                    mVpPreview.setCurrentItem(mMediaFileData.indexOf(mCheckMediaData.get(position)), true);
                    mCheckAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    protected void initEvent() {

        mTvBottom.setOnSureViewClickListener(new TitleView.OnSureViewClickListener() {
            @Override
            public void onSureClick(@NonNull View view) {
                if (mCheckMediaData.size() < mOptions.maxChooseMedia || (mCheckMediaData.size() == mOptions.maxChooseMedia && mMediaFileData.get(mPreviewPosition).isCheck)) {
                    mMediaFileData.get(mPreviewPosition).isCheck = !mMediaFileData.get(mPreviewPosition).isCheck;
                    mTvBottom.mTvSure.setCompoundDrawablesWithIntrinsicBounds(mMediaFileData.get(mPreviewPosition).isCheck ? R.mipmap.icon_preview_check : R.mipmap.icon_preview_uncheck, 0, 0, 0);
                    EventBus.getDefault().post(mMediaFileData.get(mPreviewPosition));
                    if (mCheckAdapter != null) {
                        if (mMediaFileData.get(mPreviewPosition).isCheck) {
                            mCheckAdapter.addItemNotifyData(mMediaFileData.get(mPreviewPosition));
                            mRvCheckMedia.scrollToPosition(mCheckMediaData.indexOf(mMediaFileData.get(mPreviewPosition)));

                        } else {
                            if (mCheckMediaData.contains(mMediaFileData.get(mPreviewPosition))) {
                                mCheckAdapter.removeItemNotifyData(mCheckMediaData.indexOf(mMediaFileData.get(mPreviewPosition)));
                                mRvCheckMedia.scrollToPosition(mCheckMediaData.size() - 1);
                            }
                        }
                    }
                    //设置完成的数量
                    setTitleViewSureText();
                } else {
                    Toasts.with().showToast(PreviewActivity.this, R.string.max_choose_media, String.valueOf(mOptions.maxChooseMedia));
                }


            }
        });
        mTvTop.setOnSureViewClickListener(new TitleView.OnSureViewClickListener() {
            @Override
            public void onSureClick(@NonNull View view) {
                if (mCheckMediaData.size() > 0) {
                    if (mOptions.isCompress) {
                        List<ImageConfig> configData = new ArrayList<>();
                        for (int i = 0; i < mCheckMediaData.size(); i++) {
                            configData.add(MediaSelectorFile.thisToDefaultImageConfig(mCheckMediaData.get(i)));
                        }
                        coompressImage(configData, new CompressImageTask.OnImagesResult() {
                            @Override
                            public void startCompress() {

                            }

                            @Override
                            public void resultFilesSucceed(List<File> list) {
                                mCheckMediaData.clear();
                                for (int i = 0; i < list.size(); i++) {
                                    mCheckMediaData.add(MediaSelectorFile.checkFileToThis(list.get(i)));
                                }
                                EventBus.getDefault().post(mCheckMediaData);
                                finish();
                            }

                            @Override
                            public void resultFilesError() {

                            }
                        });
                    } else {
                        EventBus.getDefault().post(mCheckMediaData);
                        finish();
                    }
                }


            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    private void titleViewAnimation() {
        ObjectAnimator topAnimatorTranslation;
        ObjectAnimator bottomAnimatorTranslation;
        if (mAnimatorSet == null) {
            mAnimatorSet = new AnimatorSet();
        }
        if (isShowTitleView) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            topAnimatorTranslation = ObjectAnimator.ofFloat(mTvTop, "translationY", 0, -(ScreenUtils.getStatuWindowsHeight(this) + mTvTop.getMeasuredHeight()));
            bottomAnimatorTranslation = ObjectAnimator.ofFloat(mLlBottom, "translationY", 0, (mLlBottom.getMeasuredHeight()));

        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            topAnimatorTranslation = ObjectAnimator.ofFloat(mTvTop, "translationY", -(ScreenUtils.getStatuWindowsHeight(this) + mTvTop.getMeasuredHeight()), 0);
            bottomAnimatorTranslation = ObjectAnimator.ofFloat(mLlBottom, "translationY", (mLlBottom.getMeasuredHeight()), 0);

        }
        mAnimatorSet.setDuration(500);
        mAnimatorSet.setInterpolator(new LinearInterpolator());
        mAnimatorSet.playTogether(topAnimatorTranslation, bottomAnimatorTranslation);
        mAnimatorSet.start();


    }


}
