package com.ytempest.layoutinjector.annotation;


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
    /**
     * setting your layout, such as R.layout.activity_main
     */
    int value();
}
