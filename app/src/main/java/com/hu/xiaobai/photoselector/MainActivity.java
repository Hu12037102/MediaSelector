package com.hu.xiaobai.photoselector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.baixiaohu.permission.PermissionActivity;
import com.baixiaohu.permission.imp.OnPermissionsResult;
import com.example.media.MediaSelector;
import com.example.media.OnRecyclerItemClickListener;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.bean.MediaSelectorFolder;
import com.example.media.resolver.ILoadMediaResult;
import com.example.media.resolver.MediaHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends PermissionActivity {


    private DataAdapter mDataAdapter;
    private RecyclerView mRyMedia;
    private List<MediaSelectorFile> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();

    }


    private void initData() {
        mData = new ArrayList<>();
        MediaSelectorFile mediaSelectorFile = new MediaSelectorFile();
        mData.add(mediaSelectorFile);
        mDataAdapter = new DataAdapter(this, mData);
        mRyMedia.setAdapter(mDataAdapter);
    }

    private void initView() {
        mRyMedia = findViewById(R.id.rv_media);
        mRyMedia.setLayoutManager(new GridLayoutManager(this, 3));

    }

    private void initEvent() {
        mDataAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void itemClick(@NonNull View view, int position) {
                if (!mData.get(position).isCheck) {
                    if (mData.size() > 1) {
                        ListIterator<MediaSelectorFile> iterator = mData.listIterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().isCheck) {
                                iterator.remove();
                            }
                        }
                        mDataAdapter.notifyDataSetChanged();
                    }
                    MediaSelector.with(MainActivity.this).openMediaActivity();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<MediaSelectorFile> mediaList = MediaSelector.resultMediaFile(data);

        if (mediaList != null && mediaList.size() > 0) {
            mData.addAll(0, mediaList);
            mDataAdapter.notifyDataSetChanged();

        }
        Log.w("onActivityResult--", mData.size()+ "--");
    }
}
