package com.tuuzed.backupapk.util;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tuuzed.backupapk.entity.ApkEntity;

/**
 * @author TuuZed
 */
public class ApkUtils {
    private final static String TAG = "ApkUtils";

    /**
     * 查询手机内非系统应用
     *
     * @param activity:Activity
     * @return
     */
    public static List<ApkEntity> getAllApps(Activity activity) {
        List<ApkEntity> data = new ArrayList<>();
        PackageManager manager = activity.getPackageManager();
        //获取手机内所有应用
        for (PackageInfo pak : manager.getInstalledPackages(0)) {
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                ApkEntity entity = new ApkEntity();
                entity.setIcon(pak.applicationInfo.loadIcon(manager));
                entity.setApkName(manager.getApplicationLabel(pak.applicationInfo).toString());
                entity.setPackageName(pak.applicationInfo.packageName);
                entity.setVersionCode(pak.versionCode);
                entity.setVersionName(pak.versionName);
                data.add(entity);
            }
        }
        return data;
    }

    /**
     * 备份APK
     *
     * @param packageName 包名
     * @param path        备份APK路径
     * @param fileName    备份APK名字
     */
    public static void backupApp(String packageName, String path, String fileName) {
        String toFilePath = path + File.separator + fileName;
        if (TextUtils.isEmpty(packageName) || TextUtils.isEmpty(fileName)) {
            Log.e(TAG, "参数不合法");
            return;
        }
        File fromFile;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fromFile = new File("/data/app/" + packageName + "-1/base.apk");
            if (!fromFile.exists()) {
                fromFile = new File("/data/app/" + packageName + "-2/base.apk");
            }
        } else {
            fromFile = new File("/data/app/" + packageName + "-1.apk");
            if (!fromFile.exists()) {
                fromFile = new File("/data/app/" + packageName + "-2.apk");
            }
        }
        if (fromFile.exists()) {
            backup(fromFile, toFilePath);
        }
    }

    private static void backup(File fromFile, String toFilePath) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(fromFile);
            int i = toFilePath.lastIndexOf('/');
            if (i != -1) {
                File dirs = new File(toFilePath.substring(0, i));
                dirs.mkdirs();
            }
            byte[] c = new byte[1024];
            int len;
            out = new FileOutputStream(toFilePath);
            while ((len = in.read(c, 0, c.length)) != -1) {
                out.write(c, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            quietClose(in);
            quietClose(out);
        }
    }

    private static void quietClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
