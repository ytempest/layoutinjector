package com.ytempest.layoutinjector.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.ytempest.layoutinjector.annotation.InjectLayout;

import java.util.HashMap;

import javax.lang.model.element.Modifier;

import static com.ytempest.layoutinjector.compiler.Configuration.LAYOUT_ID_MAP_CLASS_NAME;
import static com.ytempest.layoutinjector.compiler.Configuration.MAP_FIELD_NAME;


/**
 * @author ytempest
 * @since 2020/7/1
 */
class ClassGenerateHelper {

    private static final ClassName LayoutRes = ClassName.get("android.support.annotation", "LayoutRes");
    private static final ClassName NonNull = ClassName.get("android.support.annotation", "NonNull");

    static TypeSpec.Builder createClassBuilder() {
        return TypeSpec.classBuilder(LAYOUT_ID_MAP_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    static FieldSpec createMapField(int mapSize) {
        ClassName hashMap = ClassName.get(HashMap.class);
        ClassName mapKey = ClassName.get(Class.class);
        ClassName mapValue = ClassName.get(Integer.class);
        // 为HashMap添加泛型
        TypeName fieldType = ParameterizedTypeName.get(hashMap, mapKey, mapValue);
        return FieldSpec.builder(fieldType, MAP_FIELD_NAME)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $L<>($L)", hashMap.simpleName(), mapSize)
                .build();
    }

    static MethodSpec createGetMethod() {
        return MethodSpec.methodBuilder("get")
                .addAnnotation(LayoutRes)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.INT)
                .addParameter(ParameterSpec.builder(ClassName.get(Object.class), "obj")
                        .addAnnotation(NonNull).build())
                .addCode("Class clazz = obj.getClass();\n")
                .addCode("Integer layoutId = $L.get(clazz);\n", MAP_FIELD_NAME)
                .addCode("if (layoutId == null || layoutId <= 0) {\n")
                .addCode("\tthrow new IllegalStateException(\"Please inject layout for \" + obj + \" by @$L\");\n",
                        InjectLayout.class.getSimpleName())
                .addCode("}\n")
                .addCode("return layoutId;\n")
                .build();
    }
}
