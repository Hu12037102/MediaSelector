package com.example.media.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.media.OnRecyclerItemClickListener;
import com.example.media.R;
import com.example.media.bean.MediaSelectorFolder;
import com.example.media.utils.GlideUtils;

import java.util.List;

public class MediaFolderAdapter extends RecyclerView.Adapter<MediaFolderAdapter.ViewHolder> {
    private List<MediaSelectorFolder> mData;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public MediaFolderAdapter(@Nullable List<MediaSelectorFolder> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_media_folder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        if (mData.get(i).firstFilePath != null) {
            GlideUtils.loadImage(viewHolder.itemView.getContext(), mData.get(i).firstFilePath, viewHolder.mIvLeft);
        }

        viewHolder.mTvCount.setText(viewHolder.itemView.getContext().getString(R.string.how_match_open, String.valueOf(mData.get(i).fileData.size())));
        viewHolder.mTvTitle.setText(mData.get(i).folderName);
        viewHolder.mIvCheck.setImageResource(mData.get(i).isCheck ? R.mipmap.icon_folder_check : R.mipmap.icon_folder_uncheck);
        viewHolder.mIvVideoStype.setVisibility(mData.get(i).isAllVideo ? View.VISIBLE : View.GONE);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCheckSoleData(mData, i);
                if (onRecyclerItemClickListener != null) {
                    onRecyclerItemClickListener.itemClick(v, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvLeft;
        private TextView mTvTitle;
        private TextView mTvCount;
        private ImageView mIvCheck;
        private ImageView mIvVideoStype;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            mIvLeft = itemView.findViewById(R.id.iv_left);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvCount = itemView.findViewById(R.id.tv_count);
            mIvCheck = itemView.findViewById(R.id.iv_check);
            mIvVideoStype = itemView.findViewById(R.id.iv_video_type);

        }
    }

    private void clickCheckSoleData(List<MediaSelectorFolder> data, int position) {
        if (data != null && data.size() > position) {
            if (!data.get(position).isCheck) {
                for (int i = 0; i < data.size(); i++) {
                    if (i == position) {
                        mData.get(position).isCheck = true;
                    } else if (mData.get(i).isCheck) {
                        mData.get(i).isCheck = false;
                    }
                }
                notifyDataSetChanged();
            }
        }
    }
}
