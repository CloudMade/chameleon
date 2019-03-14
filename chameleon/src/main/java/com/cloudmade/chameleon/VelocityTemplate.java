package com.cloudmade.chameleon;

public enum VelocityTemplate {

    THEME_ELEMENT_PROVIDE_METHOD("templates/theme_element_provide_method_template.vm"),
    THEME_ELEMENT_PROVIDER_INITIALIZER("templates/theme_element_provider_initializer_template.vm"),
    THEME_ELEMENT_PROVIDER("templates/theme_element_provider_template.vm"),
    THEME_ELEMENT_RESOURCE("templates/theme_element_resource_template.vm"),
    THEME_SUFFIXES_INITIALIZER("templates/theme_suffixes_initializer_template.vm"),
    CHAMELEON_THEMES("templates/themes_template.vm"),
    CHAMELEON_THEME("templates/theme_template.vm"),
    JAVA_DOC_INFO("templates/java_doc_info_template.vm");

    public final String templatePath;

    VelocityTemplate(String templatePath) {
        this.templatePath = templatePath;
    }
}
