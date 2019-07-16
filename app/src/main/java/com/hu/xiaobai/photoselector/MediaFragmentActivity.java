package com.hu.xiaobai.photoselector;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

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
