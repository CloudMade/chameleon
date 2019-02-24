package com.cloudmade.chameleon.rclass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class RClassFinder {

    private ProcessingEnvironment processingEnvironment;

    public RClassFinder(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public RClass find(String packageName) throws IllegalStateException {
        Elements elementUtils = processingEnvironment.getElementUtils();
        String rClassPath = packageName + "." + "R";
        TypeElement rType = elementUtils.getTypeElement(rClassPath);

        if (rType == null) {
            throw new RuntimeException("The generated " + rClassPath + " class cannot be found");
        }

        return new RClass(rType);
    }
}
