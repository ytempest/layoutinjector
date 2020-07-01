package com.ytempest.layoutinjector;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.layoutinjector.config.Configuration;

/**
 * @author ytempest
 * @since 2020/7/1
 */
public final class LayoutInjector {

    static {
        try {
            Class.forName(Configuration.PACKAGE_NAME + "." + Configuration.VIEW_ID_MAP_NAME);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Not found real map of layout id, please check your LayoutInjector configuration; see more: " + Configuration.FRAME_URL);
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
