package org.otash;

public enum FieldType {
    UUID("\""),
    ID(""),
    FIRSTNAME("\""),
    LASTNAME("\""),
    USERNAME("\""),
    FULL_NAME("\""),
    BLOOD_GROUP("\""),
    EMAIL("\""),
    GENDER("\""),
    PHONE("\""),
    AGE(""),
    COUNTRY_CODE("\""),
    CAPITAL("\""),
    COUNTRY_ZIP_CODE("\""),
    WORD("\""),
    WORDS("\""),
    PARAGRAPH("\""),
    PARAGRAPHS("\""),
    LETTERS("\""),
    RANDOM_INT(""),
    RANDOM_DOUBLE(""),
    ;

    private String wrapper;

    FieldType(String wrapper) {
        this.wrapper = wrapper;
    }


    public String toJson(String fieldName , String value) {
        return "\"%s\":%s%s%s".formatted(fieldName , wrapper , value , wrapper);
    }
}
