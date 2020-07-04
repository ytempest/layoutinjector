package com.ytempest.layoutinjectorlib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ytempest.layoutinjector.LayoutInjector;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInjector.inject(this);
    }
}
