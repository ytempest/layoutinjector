package com.ytempest.layoutinjector.compiler;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
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
import javax.tools.Diagnostic;

import static com.ytempest.layoutinjector.compiler.Configuration.INJECTOR_NAME;
import static com.ytempest.layoutinjector.compiler.Configuration.LAYOUT_ID_MAP_CLASS_NAME;


/**
 * @author ytempest
 * @since 2020/7/1
 */
@AutoService(Processor.class)
public class LayoutInjectorProcess extends AbstractProcessor {

    private Coder mCoder;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Filer filer = processingEnv.getFiler();
        Trees trees = Trees.instance(processingEnv);
        mCoder = new Coder(filer, trees);
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

        try {
            mCoder.code(typeElementList);
        } catch (IOException e) {
            String msg = String.format("%s fail to generate the class : %s", INJECTOR_NAME, LAYOUT_ID_MAP_CLASS_NAME);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
        }
        return false;
    }
}
