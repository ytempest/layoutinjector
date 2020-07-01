package com.ytempest.layoutinjectorlib.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.layoutinjectorlib.R;


@InjectLayout(R.layout.activity_first)
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }
}
