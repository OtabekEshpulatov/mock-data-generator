package org.otash;


import lombok.*;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
public class Field implements Comparable<Field> {

    private String fieldName;
    private FieldType fieldType;

    @Override
    public int compareTo(@NotNull Field o) {
        return -1*this.fieldType.ordinal() - o.fieldType.ordinal();
    }
}
