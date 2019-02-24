package com.cloudmade.chameleon.rclass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

public class RInnerClass {

    private Set<String> idQualifiedNames = new HashSet<>();

    RInnerClass(TypeElement rInnerTypeElement) {
        List<? extends Element> idEnclosedElements = rInnerTypeElement.getEnclosedElements();
        List<VariableElement> idFields = ElementFilter.fieldsIn(idEnclosedElements);
        for (VariableElement idField : idFields) {
            TypeKind fieldType = idField.asType().getKind();
            if (fieldType == TypeKind.INT) {
                idQualifiedNames.add(idField.getSimpleName().toString());
            }
        }
    }

    public Set<String> getIdQualifiedNames() {
        return idQualifiedNames;
    }
}