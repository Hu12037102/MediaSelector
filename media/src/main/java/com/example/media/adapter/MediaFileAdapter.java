package com.example.media.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.item.util.ScreenUtils;
import com.example.media.MediaSelector;
import com.example.media.OnRecyclerItemClickListener;
import com.example.media.R;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.resolver.Contast;
import com.example.media.utils.DateUtils;
import com.example.media.utils.GlideUtils;

import java.util.List;

public class MediaFileAdapter extends RecyclerView.Adapter<MediaFileAdapter.ViewHolder> {
    private List<MediaSelectorFile> mData;
    private Context mContext;
    private MediaSelector.MediaOptions mOptions;

    public void setOnCheckMediaListener(OnCheckMediaListener onCheckMediaListener) {
        this.onCheckMediaListener = onCheckMediaListener;
    }

    private OnCheckMediaListener onCheckMediaListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public MediaFileAdapter(@NonNull Context context, @NonNull List<MediaSelectorFile> data, @NonNull MediaSelector.MediaOptions options) {
        this.mContext = context;
        this.mData = data;
        this.mOptions = options;
    }


    @NonNull
    @Override
    public MediaFileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_media_file_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MediaFileAdapter.ViewHolder viewHolder, final int i) {
        ViewGroup.LayoutParams layoutParams = viewHolder.mIvData.getLayoutParams();
        if (mData.get(i).isShowCamera) {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            GlideUtils.loadImage(mContext, R.mipmap.icon_camera, viewHolder.mIvData);
            viewHolder.mIvCheck.setVisibility(View.GONE);
            viewHolder.mViewLay.setVisibility(View.GONE);
            viewHolder.mRlVideo.setVisibility(View.GONE);
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            viewHolder.mIvCheck.setVisibility(mOptions.maxChooseMedia > 1 ? View.VISIBLE : View.GONE);
            GlideUtils.loadImage(mContext, mData.get(i).filePath, viewHolder.mIvData);
            viewHolder.mIvCheck.setImageResource(mData.get(i).isCheck ? R.mipmap.icon_image_checked : R.mipmap.icon_image_unchecked);
            viewHolder.mViewLay.setVisibility(mData.get(i).isCheck ? View.VISIBLE : View.GONE);

            if (mData.get(i).isVideo) {
                viewHolder.mRlVideo.setVisibility(View.VISIBLE);
                viewHolder.mTvDuration.setText(DateUtils.videoDuration(mData.get(i).videoDuration));
            } else {
                viewHolder.mRlVideo.setVisibility(View.GONE);
            }


        }
         viewHolder.mIvData.setLayoutParams(layoutParams);

        viewHolder.mIvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckMediaListener != null) {
                    onCheckMediaListener.onChecked(mData.get(i).isCheck, i);
                }

            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        private ImageView mIvData;
        private ImageView mIvCheck;
        private View mViewLay;
        private RelativeLayout mRlVideo;
        private TextView mTvDuration;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            ViewGroup mRootGroup = itemView.findViewById(R.id.rl_root);
            mIvData = itemView.findViewById(R.id.iv_data);
            mIvCheck = itemView.findViewById(R.id.iv_check);
            mViewLay = itemView.findViewById(R.id.view_lay);
            mRlVideo = itemView.findViewById(R.id.rl_video);
            mTvDuration = itemView.findViewById(R.id.tv_duration);
            MediaFileAdapter.setRootGroupParams(mRootGroup);
        }
    }


    private static void setRootGroupParams(@NonNull ViewGroup viewGroup) {
        ViewGroup.LayoutParams mGroupParams = viewGroup.getLayoutParams();
        mGroupParams.width = viewGroup.getContext().getResources().getDisplayMetrics().widthPixels / 4;
        mGroupParams.height = viewGroup.getContext().getResources().getDisplayMetrics().widthPixels / 4;
        viewGroup.setLayoutParams(mGroupParams);
        viewGroup.setPadding(ScreenUtils.dp2px(viewGroup.getContext(), 1.5f), ScreenUtils.dp2px(viewGroup.getContext(), 1.5f),
                ScreenUtils.dp2px(viewGroup.getContext(), 1.5f), ScreenUtils.dp2px(viewGroup.getContext(), 1.5f));
    }

    public interface OnCheckMediaListener {
        void onChecked(boolean isCheck, int position);
    }

}
