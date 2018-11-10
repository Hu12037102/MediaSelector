package com.example.media.resolver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.load.model.MediaStoreFileLoader;
import com.example.media.bean.MediaSelectorFile;
import com.example.media.bean.MediaSelectorFolder;
import com.example.media.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaHelper {
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    //查询的内容
    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.WIDTH,
            MediaStore.Files.FileColumns.HEIGHT,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DISPLAY_NAME};
    private static final String SELECTION_TYPE = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] WHERE_TYPE = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};
    private static final String SORT_ORDER = MediaStore.Files.FileColumns.DATE_MODIFIED + " desc";
    private Activity mActivity;

    public MediaHelper(@NonNull Activity activity) {
        this.mActivity = activity;
    }


    public void loadMedia(ILoadMediaResult onResult) {
        Cursor cursor = mActivity.getContentResolver().query(MediaHelper.QUERY_URI, MediaHelper.PROJECTION, MediaHelper.SELECTION_TYPE, MediaHelper.WHERE_TYPE, SORT_ORDER);
        if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
            //所有的图片
            List<MediaSelectorFile> mAllFileData = new ArrayList<>();
            //所有文件夹
            List<MediaSelectorFolder> mFolderData = new ArrayList<>();


            while (cursor.moveToNext()) {
                MediaSelectorFile mediaFile = new MediaSelectorFile();
                mediaFile.fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
                mediaFile.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                if (TextUtils.isEmpty(mediaFile.fileName) || TextUtils.isEmpty(mediaFile.filePath)
                        || TextUtils.getTrimmedLength(mediaFile.fileName) == 0 || TextUtils.getTrimmedLength(mediaFile.filePath) == 0) {
                    continue;
                }
                mediaFile.fileSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mediaFile.width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH));
                    mediaFile.height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT));
                }
                if (FileUtils.existsFile(mediaFile.filePath)) {
                    mediaFile.folderName = FileUtils.getParentFileName(mediaFile.filePath);
                    mediaFile.folderPath = FileUtils.getParentFilePath(mediaFile.filePath);
                } else {
                    continue;
                }
                MediaSelectorFolder mediaFolder = new MediaSelectorFolder();
                mediaFolder.folderPath = mediaFile.folderPath;
                //首先判断该文件的父文件夹有没有在集合中？有的话直接把文件加入对应的文件夹：没有就新建一个文件夹再添加进去
                if (mFolderData.size() > 0 && mFolderData.contains(mediaFolder) && mFolderData.indexOf(mediaFolder) >= 0) {
                    mFolderData.get(mFolderData.indexOf(mediaFolder)).fileData.add(mediaFile);
                } else {
                    mediaFolder.folderName = mediaFile.folderName;
                    mediaFolder.fileData.add(mediaFile);
                    mediaFolder.firstFilePath = mediaFile.filePath;
                    mFolderData.add(mediaFolder);
                }
                mAllFileData.add(mediaFile);
            }
            cursor.close();
            MediaSelectorFolder allMediaFolder = new MediaSelectorFolder();
            allMediaFolder.folderPath = Contast.ALL_FILE;
            allMediaFolder.folderName = Contast.ALL_FILE;
            allMediaFolder.firstFilePath = mAllFileData.get(0).filePath;
            allMediaFolder.fileData.addAll(mAllFileData);
            allMediaFolder.isCheck = true;
            mFolderData.add(0, allMediaFolder);
            if (onResult != null && mFolderData.size() > 0) {
                onResult.mediaResult(mFolderData);
            }
        } else {
            Toast.makeText(mActivity, "没有文件", Toast.LENGTH_SHORT).show();
        }

    }


}
