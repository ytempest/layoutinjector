package com.ytempest.layoutinjectorlib.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.layoutinjectorlib.Fragment.HomeFrag;
import com.ytempest.layoutinjectorlib.R;
import com.ytempest.layoutinjectorlib.dialog.TitleDialog;


@InjectLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrameLayout = findViewById(R.id.frameLayout);
    }

    public void onFragmentClick(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment homeFrag = manager.findFragmentByTag(HomeFrag.class.getCanonicalName());
        if (homeFrag == null) {
            homeFrag = new HomeFrag();
        }

        if (homeFrag.isAdded()) {
            transaction.show(homeFrag);
        } else {
            transaction.add(R.id.frameLayout, homeFrag, homeFrag.getClass().getCanonicalName());
        }
        transaction.commit();
    }

    public void onDialogClick(View view) {
        TitleDialog dialog = new TitleDialog(this);
        dialog.show();
    }
}
