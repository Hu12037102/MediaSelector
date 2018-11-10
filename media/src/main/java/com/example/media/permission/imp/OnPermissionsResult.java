package com.example.media.permission.imp;

import java.util.List;

/**
 * 项  目 :  PermissionUtils
 * 包  名 :  com.baixiaohu.permission.imp
 * 类  名 :  OnPermissionsResult
 * 作  者 :  胡庆岭
 * 时  间 :  2018/1/11 0011 下午 3:07
 * 描  述 :  ${TODO}
 *
 * @author ：
 */

public interface OnPermissionsResult {
    void onAllow(List<String> allowPermissions);

    void onNoAllow(List<String> noAllowPermissions);

    void onForbid(List<String> noForbidPermissions);

}
