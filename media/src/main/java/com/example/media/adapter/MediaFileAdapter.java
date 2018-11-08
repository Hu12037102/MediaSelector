package com.example.media.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.item.util.ScreenUtils;
import com.example.media.MediaSelector;
import com.example.media.OnRecyclerItemClickListener;
import com.example.media.R;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.utils.GlideUtils;

import java.util.List;

public class MediaFileAdapter extends RecyclerView.Adapter<MediaFileAdapter.ViewHolder> {
    private List<MediaSelectorFile> mData;
    private Context mContext;

    public void setOnCheckMediaListener(OnCheckMediaListener onCheckMediaListener) {
        this.onCheckMediaListener = onCheckMediaListener;
    }

    private OnCheckMediaListener onCheckMediaListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public MediaFileAdapter(@NonNull Context context, List<MediaSelectorFile> data) {
        this.mContext = context;
        this.mData = data;
    }

    public void notifyData(List<MediaSelectorFile> data) {
        if (data == null || data.size() == 0)
            return;
        if (mData == null) {
            mData = data;
        } else {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_media_file, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        GlideUtils.loadImage(mContext, mData.get(i).filePath, viewHolder.mIvData);
        viewHolder.mIvCheck.setImageResource(mData.get(i).isCheck ? R.mipmap.icon_image_checked : R.mipmap.icon_image_unchecked);
        viewHolder.mViewLay.setVisibility(mData.get(i).isCheck ? View.VISIBLE : View.GONE);
        viewHolder.mIvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*mData.get(i).isCheck = !mData.get(i).isCheck;
                notifyItemChanged(i);*/
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            ViewGroup mRootGroup = itemView.findViewById(R.id.rl_root);
            mIvData = itemView.findViewById(R.id.iv_data);
            mIvCheck = itemView.findViewById(R.id.iv_check);
            mViewLay = itemView.findViewById(R.id.view_lay);
            ViewGroup.LayoutParams mGroupParams = mRootGroup.getLayoutParams();
            mGroupParams.width = itemView.getContext().getResources().getDisplayMetrics().widthPixels / 4;
            mGroupParams.height = itemView.getContext().getResources().getDisplayMetrics().widthPixels / 4;
            mRootGroup.setLayoutParams(mGroupParams);
            mRootGroup.setPadding(ScreenUtils.dp2px(itemView.getContext(), 1.5f), ScreenUtils.dp2px(itemView.getContext(), 1.5f),
                    ScreenUtils.dp2px(itemView.getContext(), 1.5f), ScreenUtils.dp2px(itemView.getContext(), 1.5f));
        }
    }

    public interface OnCheckMediaListener {
        void onChecked(boolean isCheck, int position);
    }
}
