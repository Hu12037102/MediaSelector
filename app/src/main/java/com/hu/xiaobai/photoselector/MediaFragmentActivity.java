package com.hu.xiaobai.photoselector;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MediaFragmentActivity  extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_media);
        initData();
    }

    private void initData() {
        MediaFragment mediaFragment = new MediaFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.root_view, mediaFragment);
        fragmentTransaction.commitNowAllowingStateLoss();
    }
}
