package com.example;

import com.squareup.javapoet.ClassName;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/5-下午6:05
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class TypeUtil {
    public static final ClassName FINDER = ClassName.get("com.gxz.bindview_api.finder", "Finder");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName INJECTOR = ClassName.get("com.gxz.bindview_api", "Injector");
}
