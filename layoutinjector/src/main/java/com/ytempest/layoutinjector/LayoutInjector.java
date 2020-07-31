package com.ytempest.layoutinjector;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ytempest
 * @since 2020/7/1
 */
public final class LayoutInjector {

    static {
        try {
            // 检查映射表是否存在
            Class<LayoutMap> checkClass = LayoutMap.class;
        } catch (Throwable e) {
            throw new IllegalStateException("Not found real map of layout id, whether you have use the LayoutInjector correctly");
        }
    }

    public static int getLayoutId(Object from) {
        return LayoutMap.get(from);
    }

    public static void inject(Activity activity) {
        inject(activity, activity);
    }

    public static void inject(Object from, Activity activity) {
        int layoutId = getLayoutId(from);
        activity.setContentView(layoutId);
    }

    public static View inject(Object from, LayoutInflater inflater, ViewGroup container) {
        int layoutId = getLayoutId(from);
        return inflater.inflate(layoutId, container, false);
    }

    public static void inject(Dialog dialog) {
        inject(dialog, dialog);
    }

    public static void inject(Object from, Dialog dialog) {
        int layoutId = getLayoutId(from);
        dialog.setContentView(layoutId);
    }
}
