package com.ytempest.layoutinjector.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.ytempest.layoutinjector.config.Configuration;

import java.util.HashMap;

import javax.lang.model.element.Modifier;

/**
 * @author ytempest
 * @since 2020/7/1
 */
class ClassGenerateHelper {
    static TypeSpec.Builder createClassBuilder() {
        return TypeSpec.classBuilder(Configuration.VIEW_ID_MAP_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    static FieldSpec createMapField(int mapSize) {
        ClassName hashMap = ClassName.get(HashMap.class);
        ClassName mapKey = ClassName.get(Class.class);
        ClassName mapValue = ClassName.get(Integer.class);
        // 为HashMap添加泛型
        TypeName fieldType = ParameterizedTypeName.get(hashMap, mapKey, mapValue);
        return FieldSpec.builder(fieldType, Configuration.MAP_NAME)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new HashMap<>($L)", mapSize)
                .build();
    }

    static MethodSpec createGetMethod() {
        return MethodSpec.methodBuilder("get")
                .addAnnotation(ClassName.get("android.support.annotation", "LayoutRes"))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.INT)
                .addParameter(ClassName.get(Object.class), "obj")
                .addCode("Class clazz = obj.getClass();\n")
                .addCode("Integer id = $L.get(clazz);\n", Configuration.MAP_NAME)
                .addCode("if (id == null || id <= 0) {\n")
                .addCode("    throw new IllegalStateException(\"No found layout id by \" + clazz.getCanonicalName());\n")
                .addCode("}\n")
                .addCode("return id;\n")
                .build();
    }
}
