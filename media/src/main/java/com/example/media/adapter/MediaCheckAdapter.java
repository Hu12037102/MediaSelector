package com.example.media.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.media.OnRecyclerItemClickListener;
import com.example.media.R;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.utils.GlideUtils;

import java.util.List;

public class MediaCheckAdapter extends RecyclerView.Adapter<MediaCheckAdapter.ViewHolder> {
    private List<MediaSelectorFile> mData;
    private Context mContext;
    private MediaSelectorFile mPreviewMedia;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public MediaCheckAdapter(@NonNull Context context, @NonNull List<MediaSelectorFile> data) {
        this.mContext = context;
        this.mData = data;
    }

    public void notifyCheckData(MediaSelectorFile previewMedia) {
        mPreviewMedia = previewMedia;
        this.notifyDataSetChanged();
    }

    public void removeItemNotifyData(int position) {
        mData.remove(position);
        this.notifyDataSetChanged();
    }

    public void addItemNotifyData(@NonNull MediaSelectorFile previewMedia) {
        mData.add(previewMedia);
        this.notifyItemRemoved(mData.indexOf(previewMedia));
    }

    @NonNull
    @Override
    public MediaCheckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_check_media_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.mIvMediaType.setVisibility(mData.get(i).isVideo ? View.VISIBLE : View.GONE);
        GlideUtils.loadImage(mContext, mData.get(i).filePath, viewHolder.mIvItem);
        viewHolder.mIvItem.setBackgroundResource(mPreviewMedia.filePath.equals(mData.get(i).filePath) ? R.drawable.shape_media_check : R.drawable.shape_media_uncheck);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onRecyclerItemClickListener != null) {
                    onRecyclerItemClickListener.itemClick(v, i);
                }
              /*  if (!mPreviewMedia.filePath.equals(mData.get(i).filePath)) {
                    if (onRecyclerItemClickListener != null) {
                        onRecyclerItemClickListener.itemClick(v, i);
                    }
                    notifyDataSetChanged();
                }*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvItem;
        private ImageView mIvMediaType;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            mIvItem = itemView.findViewById(R.id.iv_item);
            mIvMediaType = itemView.findViewById(R.id.iv_media_type);
        }
    }
}
