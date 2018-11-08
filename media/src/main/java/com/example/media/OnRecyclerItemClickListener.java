package com.example.media;

import android.support.annotation.NonNull;
import android.view.View;

public interface OnRecyclerItemClickListener {
    void itemClick(@NonNull View view, int position);
}
