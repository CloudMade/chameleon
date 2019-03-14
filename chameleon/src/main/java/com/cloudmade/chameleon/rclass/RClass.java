package com.cloudmade.chameleon.rclass;

import com.cloudmade.chameleon.ResourceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

public class RClass {

    private Map<String, RInnerClass> rClass = new HashMap<>();

    RClass(TypeElement rClassElement) {
        processRInnerClasses(rClassElement);
    }

    public RInnerClass get(ResourceType resourceType) {
        return rClass.get(resourceType.defType);
    }

    private List<TypeElement> extractRInnerTypeElements(TypeElement rClassElement) {
        List<? extends Element> rEnclosedElements = rClassElement.getEnclosedElements();
        return ElementFilter.typesIn(rEnclosedElements);
    }

    private void processRInnerClasses(TypeElement rClassElement) {
        List<TypeElement> rInnerTypeElements = extractRInnerTypeElements(rClassElement);

        for (TypeElement rInnerTypeElement : rInnerTypeElements) {
            RInnerClass rInnerClass = new RInnerClass(rInnerTypeElement);
            rClass.put(rInnerTypeElement.getSimpleName().toString(), rInnerClass);
        }
    }
}
