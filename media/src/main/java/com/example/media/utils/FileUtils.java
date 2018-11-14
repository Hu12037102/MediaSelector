package com.example.media.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.example.media.weight.MediaScanner;

import java.io.File;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtils {

    public static final String FILE_DIRECTOR_NAME = "MediaSelector";

    /**
     * 获取父文件夹名字
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getParentFileName(@NonNull String filePath) {
        return getParentFile(filePath).getName();
    }

    /**
     * 获取父文件夹绝对路径
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getParentFilePath(@NonNull String filePath) {
        return getParentFile(filePath).getAbsolutePath();
    }

    private static File getParentFile(@NonNull String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.getParentFile();
        }
        throw new NullPointerException("file must exists or isFile");
    }

    public static boolean existsFile(@NonNull String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile())
            return true;
        return false;
    }

    public static File outCameraFileDirectory(Context context) {
        String storageState = Environment.getExternalStorageState();
        File rootFile = storageState.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : context.getCacheDir();
        rootFile = new File(rootFile.getAbsolutePath(), FILE_DIRECTOR_NAME.concat("/Camera"));
        if (!rootFile.exists() || !rootFile.isDirectory()) {
            rootFile.mkdirs();
        }
        return rootFile;
    }


    public static File outFileDirectory(Context context, String folderName) {
        String storageState = Environment.getExternalStorageState();
        File rootFile = storageState.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : context.getCacheDir();
        rootFile = new File(rootFile.getAbsolutePath(), FILE_DIRECTOR_NAME.concat("/").concat(folderName));
        if (!rootFile.exists() || !rootFile.isDirectory()) {
            rootFile.mkdirs();
        }
        return rootFile;
    }

    public static File resultImageFile(Context context) {
        return new File(outCameraFileDirectory(context).getAbsolutePath(), "hxb" + System.currentTimeMillis() + ".jpg");
    }

    public static File resultImageFile(Context context, String folderName) {
        return new File(outFileDirectory(context, folderName).getAbsolutePath(), "crop" + System.currentTimeMillis() + ".jpg");
    }

    public static Uri fileToUri(@NonNull Context context, @NonNull File file, @NonNull Intent intent) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = context.getPackageName() + ".provider";
            uri = FileProvider.getUriForFile(context, authority, file);
            List<ResolveInfo> resolveInfoData = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfoData != null && resolveInfoData.size() > 0)
                for (ResolveInfo resolveInfo : resolveInfoData) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static void scanImage(@NonNull Context context, @NonNull File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaScanner ms = new MediaScanner(context, file);
            ms.refresh();
        } else {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            context.sendBroadcast(intent);
        }
    }

    public static int getFileWidth(@NonNull String path) {
        return getBitmapOptions(path).outWidth;
    }

    public static int getFileHeight(@NonNull String path) {
        return getBitmapOptions(path).outHeight;
    }

    private static BitmapFactory.Options getBitmapOptions(@NonNull String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return options;
    }
}
