package com.ytempest.layoutinjector.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * @author heqidu
 * @since 2020/7/3
 */
class Utils {
    static List<TypeElement> filterTypeElement(Set<? extends Element> elements) {
        List<TypeElement> list = new ArrayList<>();
        if (elements != null) {
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS) {
                    list.add((TypeElement) element);
                }
            }
        }
        return list;
    }
}
