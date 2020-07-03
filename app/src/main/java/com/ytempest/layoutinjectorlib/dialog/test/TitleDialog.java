package com.ytempest.layoutinjectorlib.dialog.test;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.layoutinjectorlib.R;
import com.ytempest.layoutinjectorlib.dialog.BaseDialog;

/**
 * @author heqidu
 * @since 2020/7/1
 */
@InjectLayout(R.layout.dialog_title)
public class TitleDialog extends BaseDialog {

    public TitleDialog(@NonNull Context context) {
        super(context);
    }
}
