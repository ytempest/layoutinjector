package com.ytempest.layoutinjector.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.ytempest.layoutinjector.annotation.InjectLayout;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.ytempest.layoutinjector.compiler.Configuration.FRAME_URL;
import static com.ytempest.layoutinjector.compiler.Configuration.INJECTOR_NAME;
import static com.ytempest.layoutinjector.compiler.Configuration.LAYOUT_ID_MAP_CLASS_NAME;
import static com.ytempest.layoutinjector.compiler.Configuration.MAP_FIELD_NAME;
import static com.ytempest.layoutinjector.compiler.Configuration.PACKAGE_NAME;


/**
 * @author ytempest
 * @since 2020/7/1
 */
@AutoService(Processor.class)
public class LayoutInjectorProcess extends AbstractProcessor {

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(InjectLayout.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> destElementSet = roundEnv.getElementsAnnotatedWith(InjectLayout.class);
        List<TypeElement> typeElementList = Utils.filterTypeElement(destElementSet);
        if (typeElementList.isEmpty()) {
            return false;
        }

        // 初始化类构建器
        TypeSpec.Builder classBuilder = ClassGenerateHelper.createClassBuilder();

        // 添加类成员变量
        FieldSpec mapField = ClassGenerateHelper.createMapField(typeElementList.size());
        classBuilder.addField(mapField);

        // 添加静态代码块
        CodeBlock.Builder staticBuilder = CodeBlock.builder();
        for (TypeElement typeElement : typeElementList) {
            String typeClassPath = typeElement.getQualifiedName().toString();

            InjectLayout injectLayout = typeElement.getAnnotation(InjectLayout.class);
            int layoutId = injectLayout.value();
            staticBuilder.add("$L.put($L.class, $L);\n", MAP_FIELD_NAME, typeClassPath, layoutId);
        }
        classBuilder.addStaticBlock(staticBuilder.build());

        // 添加get方法
        MethodSpec getMethod = ClassGenerateHelper.createGetMethod();
        classBuilder.addMethod(getMethod);

        // 生成映射表
        try {
            JavaFile.builder(PACKAGE_NAME, classBuilder.build())
                    .addFileComment("The class generate by $L ($L)", INJECTOR_NAME, FRAME_URL)
                    .build()
                    .writeTo(mFiler);
        } catch (IOException e) {
            System.err.println(String.format("%s fail to generate the class : %s", INJECTOR_NAME, LAYOUT_ID_MAP_CLASS_NAME));
            e.printStackTrace();
        }
        return false;
    }
}
