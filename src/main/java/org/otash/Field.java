package org.otash;

import lombok.*;

import java.util.function.Supplier;


@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode( of = {"fieldName" , "fieldType"} )
public class Field {
    private final String fieldName;
    private final FieldType fieldType;
    private final Supplier<Object> func;

    public Field(String fieldName , FieldType fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.func = FakerApplicationGeneratorService.functions.get(fieldType);
    }

    public String getPatternAsJson() {
        return fieldType.getRowAsJson(fieldName , func.get());
    }

    @Override
    public String toString() {
        return "\033[1;92m%s : %s \033[0m\n".formatted(fieldName , fieldType.name());
    }
}
