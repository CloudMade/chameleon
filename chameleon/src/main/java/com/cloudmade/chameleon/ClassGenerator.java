package com.cloudmade.chameleon;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;

class ClassGenerator {

    static final String GENERATED_PACKAGE = "com.cloudmade.chameleon";

    private ProcessingEnvironment processingEnvironment;
    private VelocityEngine velocityEngine;

    ClassGenerator(ProcessingEnvironment processingEnvironment, VelocityEngine velocityEngine) {
        this.processingEnvironment = processingEnvironment;
        this.velocityEngine = velocityEngine;
    }

    void writeClass(VelocityContext velocityContext, VelocityTemplate velocityTemplate, String className) {
        Template template = velocityEngine.getTemplate(velocityTemplate.templatePath);

        try {
            JavaFileObject source = processingEnvironment.getFiler().createSourceFile(GENERATED_PACKAGE + "." + className);
            Writer writer = source.openWriter();
            template.merge(velocityContext, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed " + e.getMessage());
            e.printStackTrace();
        }
    }

    String mergeVelocityContext(VelocityContext velocityContext, VelocityTemplate velocityTemplate) {
        Template template = velocityEngine.getTemplate(velocityTemplate.templatePath);
        StringWriter sw = new StringWriter();
        template.merge(velocityContext, sw);
        return sw.toString();
    }
}
