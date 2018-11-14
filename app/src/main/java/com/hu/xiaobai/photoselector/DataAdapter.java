package com.hu.xiaobai.photoselector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.media.OnRecyclerItemClickListener;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.resolver.Contast;
import com.example.media.utils.GlideUtils;
import com.example.media.utils.ScreenUtils;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context mContext;
    private List<MediaSelectorFile> mData;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public DataAdapter( Context context, @NonNull List<MediaSelectorFile> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_data_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        if (!mData.get(i).isCheck) {
            viewHolder.mIvMedia.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_picture_image));
        } else {
            GlideUtils.loadImage(mContext, mData.get(i).filePath, viewHolder.mIvMedia);
        }

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
        return mData == null ? 0 : (mData.size() > Contast.MAX_CHOOSE_MEDIA) ? Contast.MAX_CHOOSE_MEDIA : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvMedia;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            mIvMedia = itemView.findViewById(R.id.iv_media);
            LinearLayout viewRoot = itemView.findViewById(R.id.view_root);
            ViewGroup.LayoutParams layoutParams = viewRoot.getLayoutParams();
            layoutParams.height = ScreenUtils.screenWidth(itemView.getContext()) / 3;
            layoutParams.width = ScreenUtils.screenWidth(itemView.getContext()) / 3;
            viewRoot.setLayoutParams(layoutParams);
        }
    }
}
