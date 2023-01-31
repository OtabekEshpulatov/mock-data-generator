package org.jlkesh;

public enum FieldType {
    ID(""),
    UUID("\""),
    BOOK_TITLE("\""),
    BOOT_AUTHOR("\""),
    POST_TITLE("\""),
    POST_BODY("\""),
    FIRSTNAME("\""),
    LASTNAME("\""),
    USERNAME("\""),
    FULLNAME("\""),
    BLOOD_GROUP("\""),
    EMAIL("\""),
    GENDER("\""),
    PHONE("\""),
    LOCAlDATE("\""),
    AGE(""),
    COUNTRY_CODE("\""),
    COUNTRY_ZIP_CODE("\""),
    CAPITAL("\""),
    WORD("\""),
    WORDS("\""),
    PARAGRAPH("\""),
    PARAGRAPHS("\""),
    LETTERS("\""),
    RANDOM_INT(""), RANDOM_DOUBLE("");

    private final String i;

    FieldType(String i) {
        this.i = i;
    }


    public String getRowAsJson(String fieldName , Object data) {
        return ( "\"" + fieldName + "\" : " + i + data + i );
    }
}
