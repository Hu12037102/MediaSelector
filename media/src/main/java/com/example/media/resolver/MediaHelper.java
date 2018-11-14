package com.example.media.resolver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.media.bean.MediaSelectorFile;
import com.example.media.bean.MediaSelectorFolder;
import com.example.media.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class MediaHelper {
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    //查询的内容
    @SuppressLint("InlinedApi")
    private static final String[] IMAGE_PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.WIDTH,
            MediaStore.Files.FileColumns.HEIGHT,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DISPLAY_NAME};
    @SuppressLint("InlinedApi")
    private static final String[] ALL_PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.WIDTH,
            MediaStore.Files.FileColumns.HEIGHT,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION};
    private static final String IMAGE_SELECTION_TYPE = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String ALL_SELECTION_TYPE = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" +
            " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] IMAGE_WHERE_TYPE = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};
    private static final String[] ALL_WHERE_TYPE = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
    private static final String SORT_ORDER = MediaStore.Files.FileColumns.DATE_MODIFIED + " desc";
    private Activity mActivity;

    public MediaHelper(@NonNull Activity activity) {
        this.mActivity = activity;
    }


    public void loadMedia(boolean isShowCamera, boolean isShowVideo, @Nullable ILoadMediaResult onResult) {

        Cursor cursor = mActivity.getContentResolver().query(MediaHelper.QUERY_URI, isShowVideo ? ALL_PROJECTION : MediaHelper.IMAGE_PROJECTION, isShowVideo ? ALL_SELECTION_TYPE : MediaHelper.IMAGE_SELECTION_TYPE, isShowVideo ? ALL_WHERE_TYPE : MediaHelper.IMAGE_WHERE_TYPE, SORT_ORDER);
        if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
            //所有的图片
            List<MediaSelectorFile> mAllFileData = new ArrayList<>();

            //所有文件夹
            List<MediaSelectorFolder> folderData = new ArrayList<>();
            List<MediaSelectorFile> mVideoFileData = new ArrayList<>();

            while (cursor.moveToNext()) {
                MediaSelectorFile mediaFile = new MediaSelectorFile();
                mediaFile.fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));

                mediaFile.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                if (TextUtils.isEmpty(mediaFile.fileName) || TextUtils.isEmpty(mediaFile.filePath)
                        || TextUtils.getTrimmedLength(mediaFile.fileName) == 0 || TextUtils.getTrimmedLength(mediaFile.filePath) == 0 || mediaFile.fileName.endsWith(".gif")) {
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
                mediaFile.isVideo = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
                if (mediaFile.isVideo) {
                    mediaFile.videoDuration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    if (mediaFile.videoDuration >= 60 * 60 * 1000 || mediaFile.videoDuration < 1000) {
                        continue;
                    }
                    mVideoFileData.add(mediaFile);
                }

                MediaSelectorFolder mediaFolder = new MediaSelectorFolder();
                mediaFolder.folderPath = mediaFile.folderPath;
                //首先判断该文件的父文件夹有没有在集合中？有的话直接把文件加入对应的文件夹：没有就新建一个文件夹再添加进去
                if (folderData.size() > 0 && folderData.contains(mediaFolder) && folderData.indexOf(mediaFolder) >= 0) {
                    folderData.get(folderData.indexOf(mediaFolder)).fileData.add(mediaFile);
                } else {
                    mediaFolder.folderName = mediaFile.folderName;
                    mediaFolder.fileData.add(mediaFile);
                    mediaFolder.firstFilePath = mediaFile.filePath;
                    folderData.add(mediaFolder);
                }
                mAllFileData.add(mediaFile);

            }
            cursor.close();
            if (mAllFileData.size() > 0) {
                if (isShowCamera) {
                    MediaSelectorFile cameraMediaFile = new MediaSelectorFile();
                    cameraMediaFile.isShowCamera = true;
                    mAllFileData.add(0, cameraMediaFile);
                }

                MediaSelectorFolder allMediaFolder = new MediaSelectorFolder();
                allMediaFolder.folderPath = Contast.ALL_FILE;
                allMediaFolder.folderName = Contast.ALL_FILE;
                allMediaFolder.firstFilePath = isShowCamera ? mAllFileData.get(1).filePath : mAllFileData.get(0).filePath;
                allMediaFolder.fileData.addAll(mAllFileData);
                allMediaFolder.isCheck = true;
                folderData.add(0, allMediaFolder);
                //增加视频目录
                if (mVideoFileData.size() > 0) {
                    MediaSelectorFolder videoMediaFolder = new MediaSelectorFolder();
                    videoMediaFolder.folderPath = Contast.ALL_VIDEO;
                    videoMediaFolder.folderName = Contast.ALL_VIDEO;
                    videoMediaFolder.firstFilePath = mVideoFileData.get(0).filePath;
                    videoMediaFolder.fileData.addAll(mVideoFileData);
                    videoMediaFolder.isAllVideo = true;
                    folderData.add(folderData.indexOf(allMediaFolder) + 1, videoMediaFolder);
                }
                if (onResult != null && folderData.size() > 0) {
                    onResult.mediaResult(folderData);
                }
            }

        } else {
            Toast.makeText(mActivity, "没有文件", Toast.LENGTH_SHORT).show();
        }

    }


}
