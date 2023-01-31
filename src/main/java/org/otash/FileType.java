package org.otash;

public enum FileType {
    JSON,CSV,SQL;

    public static FileType getType(String fileType) {

        for ( FileType value : FileType.values() ) {
            if(value.name().equalsIgnoreCase(fileType))
                return value;
        }

        return null;
    }
}
