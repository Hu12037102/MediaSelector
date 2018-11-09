package com.hu.xiaobai.photoselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.media.MediaSelector;
import com.example.media.OnRecyclerItemClickListener;
import com.example.media.bean.MediaSelectorFile;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FragmentMedia extends Fragment {
    private DataAdapter mDataAdapter;
    private RecyclerView mRyMedia;
    private List<MediaSelectorFile> mData;
    private View mInflateView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflateView = inflater.inflate(R.layout.fragment_media, container, false);
        return mInflateView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        mData = new ArrayList<>();
        MediaSelectorFile mediaSelectorFile = new MediaSelectorFile();
        mData.add(mediaSelectorFile);
        mDataAdapter = new DataAdapter(getActivity(), mData);
        mRyMedia.setAdapter(mDataAdapter);
    }

    private void initView() {
        mRyMedia = mInflateView.findViewById(R.id.rv_media);
        mRyMedia.setLayoutManager(new GridLayoutManager(getActivity(), 3));

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
                    MediaSelector.with(FragmentMedia.this).openMediaActivity();
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
    }
}
