package com.ytempest.layoutinjector.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.ytempest.layoutinjector.annotation.InjectLayout;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.ytempest.layoutinjector.compiler.Configuration.FRAME_URL;
import static com.ytempest.layoutinjector.compiler.Configuration.INJECTOR_NAME;
import static com.ytempest.layoutinjector.compiler.Configuration.MAP_FIELD_NAME;
import static com.ytempest.layoutinjector.compiler.Configuration.PACKAGE_NAME;

/**
 * @author heqidu
 * @since 2020/7/3
 */
class Coder {
    private Filer mFiler;
    private Trees mTrees;

    Coder(Filer filer, Trees trees) {
        mFiler = filer;
        mTrees = trees;
    }

    void code(List<TypeElement> typeElementList) throws IOException {
        // 初始化类构建器
        TypeSpec.Builder classBuilder = ClassGenerateHelper.createClassBuilder();

        // 添加类成员变量
        FieldSpec mapField = ClassGenerateHelper.createMapField(typeElementList.size());
        classBuilder.addField(mapField);

        // 添加静态代码块
        CodeBlock.Builder staticBuilder = CodeBlock.builder();
        for (TypeElement typeElement : typeElementList) {
            // Class路径，如：com.aaa.bbb.MainActivity.class
            CodeBlock typeClassCode = typeElementToClass(typeElement);

            // Layout路径，如：com.aaa.bbb.R.layout.activity_main
            int layoutId = typeElement.getAnnotation(InjectLayout.class).value();
            Id id = elementToId(typeElement, InjectLayout.class, layoutId);
            CodeBlock layoutCode = id.code();

            staticBuilder.add("$L.put($L, $L);\n", MAP_FIELD_NAME, typeClassCode, layoutCode);
        }
        classBuilder.addStaticBlock(staticBuilder.build());

        // 添加get方法
        MethodSpec getMethod = ClassGenerateHelper.createGetMethod();
        classBuilder.addMethod(getMethod);

        // 生成映射表
        JavaFile.builder(PACKAGE_NAME, classBuilder.build())
                .addFileComment("The class generate by $L ($L)", INJECTOR_NAME, FRAME_URL)
                .build()
                .writeTo(mFiler);
    }

    private static CodeBlock typeElementToClass(Element typeElement) {
        ClassName className = ClassName.get(typeElement.getEnclosingElement().toString(),
                typeElement.getSimpleName().toString());
        return CodeBlock.of("$T.class", className);
    }

    /*Layout Id Scan*/

    private final RScanner rScanner = new RScanner();

    private Id elementToId(Element element, Class<? extends Annotation> annotation, int value) {
        JCTree tree = (JCTree) mTrees.getTree(element, geAnnotationMirror(element, annotation));
        if (tree != null) { // tree can be null if the references are compiled types and not source
            rScanner.reset();
            tree.accept(rScanner);
            Id id = rScanner.getId();
            if (id != null) {
                return id;
            }
        }
        return new Id(value);
    }

    @Nullable
    private static AnnotationMirror geAnnotationMirror(Element element, Class<? extends Annotation> annotation) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotation.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }
}
