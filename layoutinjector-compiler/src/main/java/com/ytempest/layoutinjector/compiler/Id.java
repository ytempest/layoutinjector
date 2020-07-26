package com.ytempest.layoutinjector.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.sun.tools.javac.code.Symbol;

import javax.annotation.Nullable;

/**
 * @author heqidu
 * @since 2020/7/3
 */
class Id {
    private static final ClassName ANDROID_R = ClassName.get("android", "R");
    private static final String R = "R";

    private int value;
    private final CodeBlock code;

    Id(int value) {
        this(value, null);
    }

    Id(int value, @Nullable Symbol symbol) {
        this.value = value;

        if (symbol == null) {
            code = CodeBlock.of("$L", value);

        } else {
            String modulePkgName = symbol.packge().getQualifiedName().toString(); // 如：com.aaa.bbb
            String layout = symbol.enclClass().name.toString(); // 如：layout
            String resourceName = symbol.name.toString(); // 如：activity_main

            ClassName className = ClassName.get(modulePkgName, R, layout);
            code = className.topLevelClassName().equals(ANDROID_R)
                    ? CodeBlock.of("$L.$N", className, resourceName)
                    : CodeBlock.of("$T.$N", className, resourceName);
        }
    }

    int getValue() {
        return value;
    }

    CodeBlock code() {
        return code;
    }
}
