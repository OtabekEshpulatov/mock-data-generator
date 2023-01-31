package org.otash;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
public class RowAction {

    private final String fieldName;
    private final FieldType fieldType;
    private Supplier<Object> func;

    public RowAction(Field field) {
        this.fieldName = field.getFieldName();
        this.fieldType = field.getFieldType();
        this.func = FakeDataGeneratorService.functions.get(fieldType);
    }
}
