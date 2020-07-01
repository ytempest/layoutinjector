package com.ytempest.layoutinjector.annotation;


import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ytempest
 * @since 2020/7/1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface InjectLayout {
    @LayoutRes int value();
}
