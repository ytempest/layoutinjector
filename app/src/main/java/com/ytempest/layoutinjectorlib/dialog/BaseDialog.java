package com.ytempest.layoutinjectorlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ytempest.layoutinjector.LayoutInjector;

/**
 * @author heqidu
 * @since 2020/7/1
 */
public class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context);
        LayoutInjector.inject(this);
    }
}
