package io.github.tuuzed.backupapk.entity;

import android.graphics.drawable.Drawable;

/**
 * @author TuuZed
 */
public class ApkEntity {
    // 是否选中
    private boolean checked = true;
    // 图标
    private Drawable icon;
    // 应用名称
    private String apkName;
    // 包名
    private String packageName;
    // 版本代码
    private int versionCode;
    // 版本名称
    private String versionName;

    public ApkEntity() {
    }

    public ApkEntity(Drawable icon, String apkName, String packageName, int versionCode, String versionName) {
        this.icon = icon;
        this.apkName = apkName;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
