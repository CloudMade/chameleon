package com.cloudmade.chameleon.generating;

public enum ResourceType {

    DRAWABLE("drawable"),
    COLOR("color");

    public final String defType;

    ResourceType(String defType) {
        this.defType = defType;
    }
}
