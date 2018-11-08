package com.example.media.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.baixiaohu.permission.imp.OnPermissionsResult;
import com.bumptech.glide.Glide;
import com.example.item.weight.TitleView;
import com.example.media.MediaSelector;
import com.example.media.OnRecyclerItemClickListener;
import com.example.media.R;
import com.example.media.adapter.MediaFileAdapter;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.bean.MediaSelectorFolder;
import com.example.media.resolver.Contast;
import com.example.media.resolver.ILoadMediaResult;
import com.example.media.resolver.MediaHelper;
import com.example.media.weight.FolderWindow;
import com.example.media.weight.Toasts;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MediaActivity extends BaseActivity {

    private TitleView mTvTop;
    private TitleView mTvBottom;
    private RecyclerView mRecyclerView;
    private MediaFileAdapter mMediaFileAdapter;
    private List<MediaSelectorFile> mMediaFileData;
    private List<MediaSelectorFolder> mMediaFolderData;
    private FolderWindow mFolderWindow;
    private List<MediaSelectorFile> mCheckMediaFileData;
    private MediaSelector.MediaOptions mOptions;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_media;
    }

    @Override
    protected void initPermission() {
        requestPermission(new OnPermissionsResult() {
            @Override
            public void onAllow(List<String> list) {
                MediaActivity.super.initPermission();
            }

            @Override
            public void onNoAllow(List<String> list) {

            }

            @Override
            public void onForbid(List<String> list) {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Glide.with(this).onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();

        Glide.with(this).onStop();
    }

    @Override
    protected void onDestroy() {
        unRegisterEventBus();
        super.onDestroy();
//        Glide.with(this).onDestroy();
    }

    @Override
    protected void initView() {
        registerEventBus();
        mTvTop = findViewById(R.id.ctv_top);
        mTvBottom = findViewById(R.id.ctv_bottom);
        mRecyclerView = findViewById(R.id.ry_data);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
    }

    @Override
    protected void initData() {

        initIntent();

        MediaHelper mediaHelper = new MediaHelper(this);
        mCheckMediaFileData = new ArrayList<>();

        if (mMediaFileAdapter == null) {
            mMediaFileData = new ArrayList<>();
            mMediaFileAdapter = new MediaFileAdapter(this, mMediaFileData);
            mRecyclerView.setAdapter(mMediaFileAdapter);
        }
        mediaHelper.loadMedia(new ILoadMediaResult() {
            @Override
            public void mediaResult(List<MediaSelectorFolder> data) {
                if (data != null && data.size() > 0) {
                    mMediaFileData.addAll(data.get(0).fileData);
                    if (mMediaFolderData == null) {
                        mMediaFolderData = data;
                    } else {
                        mMediaFolderData.addAll(data);
                    }
                    mMediaFileAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void initIntent() {
        Intent intent = getIntent();
        mOptions = intent.getParcelableExtra(Contast.KEY_OPEN_MEDIA);
        if (mOptions == null) {
            mOptions = MediaSelector.getDefaultOptions();
        } else {
            if (mOptions.maxChooseMedia <= 0) {
                mOptions.maxChooseMedia = 1;
            }
        }
    }

    private void resultMediaData() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Contast.KEY_REQUEST_MEDIA_DATA, (ArrayList<? extends Parcelable>) mCheckMediaFileData);
        setResult(Contast.CODE_RESULT_MEDIA, intent);
    }

    @Override
    protected void initEvent() {
        mTvTop.setOnSureViewClickListener(new TitleView.OnSureViewClickListener() {
            @Override
            public void onSureClick(@NonNull View view) {
                resultMediaData();
                finish();
            }
        });
        mTvBottom.setOnTitleViewClickListener(new TitleView.OnTitleViewClickListener() {
            @Override
            public void onBackClick(@NonNull View view) {
                showMediaFolderWindows(view);
            }

            @Override
            public void onSureClick(@NonNull View view) {
                if (mCheckMediaFileData.size() > 0) {
                    toPreviewActivity(0, mCheckMediaFileData);
                }

            }
        });
        mMediaFileAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void itemClick(@NonNull View view, int position) {
                toPreviewActivity(position, mMediaFileData);
            }
        });


        mMediaFileAdapter.setOnCheckMediaListener(new MediaFileAdapter.OnCheckMediaListener() {
            @Override
            public void onChecked(boolean isCheck, int position) {
                if (isCheck) {
                    mMediaFileData.get(position).isCheck = false;
                    mCheckMediaFileData.remove(mMediaFileData.get(position));
                } else {
                    if (mCheckMediaFileData.size() < mOptions.maxChooseMedia) {
                        mMediaFileData.get(position).isCheck = true;
                        mCheckMediaFileData.add(mMediaFileData.get(position));
                    } else {
                        Toasts.with().showToast(MediaActivity.this, getString(R.string.max_choose_media, String.valueOf(mOptions.maxChooseMedia)));
                    }
                }
                setSureStatus();
                mMediaFileAdapter.notifyItemChanged(position);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    Glide.with(MediaActivity.this).resumeRequests();
                } else {
                    Glide.with(MediaActivity.this).pauseRequests();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void toPreviewActivity(int position, @NonNull List<MediaSelectorFile> data) {
        Intent intent = new Intent(MediaActivity.this, PreviewActivity.class);
        intent.putParcelableArrayListExtra(Contast.KEY_PREVIEW_MEDIA, (ArrayList<? extends Parcelable>) data);
        intent.putExtra(Contast.KEY_PREVIEW_POSITION, position);
        startActivityForResult(intent, Contast.REQUEST_CODE_MEDIA_TO_PREVIEW);
    }

    private void showMediaFolderWindows(View view) {

        if (mFolderWindow == null) {
            mFolderWindow = new FolderWindow(this, mMediaFolderData);
            mFolderWindow.setOnPopupItemClickListener(new FolderWindow.OnPopupItemClickListener() {
                @Override
                public void onItemClick(@NonNull View view, int position) {
                    clickCheckFolder(position);
                }
            });
            mFolderWindow.showWindows(view);
        } else if (mFolderWindow.getFolderWindow().isShowing()) {
            mFolderWindow.getFolderWindow().dismiss();
        } else {
            mFolderWindow.showWindows(view);
        }


    }

    private void setSureStatus() {
        if (mCheckMediaFileData.size() > 0) {
            mTvTop.mTvSure.setClickable(true);
            mTvTop.mTvSure.setTextColor(ContextCompat.getColor(MediaActivity.this, R.color.colorTextSelector));
            mTvTop.mTvSure.setText(getString(R.string.complete_count, String.valueOf(mCheckMediaFileData.size()), String.valueOf(mOptions.maxChooseMedia)));
        } else {
            mTvTop.mTvSure.setClickable(false);
            mTvTop.mTvSure.setTextColor(ContextCompat.getColor(MediaActivity.this, R.color.colorTextUnSelector));
            mTvTop.mTvSure.setText(R.string.sure);

        }
    }

    private void clickCheckFolder(int position) {
        mTvBottom.mTvBack.setText(mMediaFolderData.get(position).folderName);
        mMediaFileData.clear();
        mMediaFileData.addAll(mMediaFolderData.get(position).fileData);
        mMediaFileAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (mFolderWindow != null && mFolderWindow.getFolderWindow().isShowing()) {
            mFolderWindow.getFolderWindow().dismiss();
        } else {
            super.onBackPressed();
        }
    }


    @Subscribe(sticky = true)
    public void previewMediaResult(@NonNull MediaSelectorFile mediaSelectorFile) {
        Log.w("previewMediaResult--", mediaSelectorFile.isCheck + "--");
        if (mediaSelectorFile.isCheck) {
            //首先先判断选择的媒体库
            if (!mCheckMediaFileData.contains(mediaSelectorFile)) {
                mCheckMediaFileData.add(mediaSelectorFile);
            }

        } else {
            if (mCheckMediaFileData.contains(mediaSelectorFile)) {
                mCheckMediaFileData.remove(mediaSelectorFile);
            }
        }
        for (int i = 0; i < mMediaFolderData.size(); i++) {
            if (mMediaFolderData.get(i).fileData.contains(mediaSelectorFile)) {
                mMediaFolderData.get(i).fileData.get(mMediaFolderData.get(i).fileData.indexOf(mediaSelectorFile)).isCheck = mediaSelectorFile.isCheck;
            }
        }
        setSureStatus();
        mMediaFileAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Activity.RESULT_OK:
                if (requestCode == Contast.REQUEST_CODE_MEDIA_TO_PREVIEW){
                    resultMediaData();
                    finish();
                }
                break;
        }
    }
}
