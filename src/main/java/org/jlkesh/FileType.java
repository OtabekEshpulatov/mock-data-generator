package org.jlkesh;


import java.io.File;
import java.rmi.NoSuchObjectException;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public enum FileType {
    /**
     * JSON TYPE FOR ****
     */
    JSON,
    CSV, SQL;

    public static FileType findByName(String name) {
        String aa = "123";
        Supplier<Integer> c = aa :: length;
        for ( FileType fileType : values() ) {
            if ( fileType.name().equalsIgnoreCase(name) )
                return fileType;
        }
        return FileType.JSON;
    }

}
