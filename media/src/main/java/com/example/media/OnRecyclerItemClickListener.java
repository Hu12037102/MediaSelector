package com.example.media;

import androidx.annotation.NonNull;
import android.view.View;

public interface OnRecyclerItemClickListener {
    void itemClick(@NonNull View view, int position);
}
