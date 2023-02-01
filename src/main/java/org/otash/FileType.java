package org.otash;


public enum FileType {
    /**
     * JSON TYPE FOR ****
     */
    JSON,
    CSV, SQL;

    public static FileType findByName(String name) {

        for ( FileType fileType : values() ) {
            if ( fileType.name().equalsIgnoreCase(name) )
                return fileType;
        }
        return FileType.JSON;
    }

}
