package com.cloudmade.chameleon;

public enum ResourceType {

    DRAWABLE("drawable"),
    COLOR("color");

    public final String defType;

    ResourceType(String defType) {
        this.defType = defType;
    }
}
