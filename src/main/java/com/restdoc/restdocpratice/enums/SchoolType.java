package com.restdoc.restdocpratice.enums;

public enum SchoolType {
    PRIMARY("primary"),
    MIDDLE("middle"),
    HIGH("high"),
    COLLAGE("collage"),
    UNIVERSITY("university"),
    GRAD("grad")
    ;

    private final String type;

    SchoolType(String type) {
        this.type = type;
    }

    public String type(){
        return type;
    }

    public static SchoolType getSchoolType(String type){
        for (SchoolType schoolType : SchoolType.values()) {
            if (schoolType.type.equals(type)){
                return schoolType;
            }
        }

        return null;
    }
}
