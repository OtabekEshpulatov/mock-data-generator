package org.jlkesh;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jlkesh.FakerApplicationService.BLACK_LIST;
import static org.jlkesh.FakerApplicationService.functions;
import static org.jlkesh.Utils.*;


public class FakerApplicationRunner {

    private static final Pattern validFieldNamePattern = Pattern.compile("^[a-zA-Z_]+\\d*");
    private static final Pattern validFileNamePattern = Pattern.compile("^([a-zA-Z_]+[0-9]*)$");
    private static final List<FieldType> fields = Collections.synchronizedList(new ArrayList<>());
    private static final int fieldsCount;

    static {
        fields.add(null);
        fields.addAll(Arrays.asList(FieldType.values()));
        fieldsCount = fields.size();
    }


    public static void main(String[] args) {
        var fakerDataGeneratorService = new FakerApplicationService();
        var builder = FakerApplicationGenerateRequest.builder();
        builder.fileName(getValidatedFileName());
        builder.fileType(getValidFileType());
        builder.count(getValidRowCount());
        Set<Field> dataFields = new HashSet<>();
        var choice = "";
        while ( !choice.startsWith("q") ) {
            String fieldName = getValidatedFieldName();
            showFieldTypes();
            FieldType fieldType = getValidatedFieldType();
            putValidSupplier(fieldName , fieldType);
            dataFields.add(new Field(fieldName , fieldType));

            System.out.println("\"q\" -> quit ");
            System.out.println("\"ANY KEYBOARD\" -> continue ");
            choice = SCANNER_STR.nextLine().toLowerCase();
        }
        builder.fields(dataFields);
        var response = fakerDataGeneratorService.processRequest(builder.build());
        System.out.println(response);
    }

    private static void putValidSupplier(String fieldName , FieldType fieldType) {
        switch ( fieldType ) {
            case RANDOM_DOUBLE -> {
                System.out.printf("Min range of %s (inclusive) : " , fieldName);
                double min = SCANNER_INT.nextDouble();
                System.out.printf("Max range of %s (inclusive) : " , fieldName);
                double max = SCANNER_INT.nextDouble();
                functions.putIfAbsent(fieldType , () -> DECIMAL_FORMAT.format(Math.random() * ( max - min + 1 ) + min));
            }

            case AGE , PARAGRAPHS , RANDOM_INT , LETTERS , WORDS -> {

                System.out.printf("Min range of %s (inclusive) : " , fieldName);
                int min = SCANNER_INT.nextInt();
                System.out.printf("Max range of %s (inclusive) : " , fieldName);
                int max = SCANNER_INT.nextInt();

                switch ( fieldType ) {

                    case AGE , RANDOM_INT -> functions.putIfAbsent(fieldType , () -> RANDOM_SERVICE.nextInt(min , max));
                    case PARAGRAPHS ->
                            functions.putIfAbsent(fieldType , () -> LOREM.paragraphs(RANDOM_SERVICE.nextInt(min , max)));
                    case LETTERS -> functions.putIfAbsent(fieldType , () -> LOREM.characters(min , max));
                    case WORDS ->
                            functions.putIfAbsent(fieldType , () -> LOREM.words(RANDOM_SERVICE.nextInt(min , max)));

                }
            }
        }
    }

    private static int getValidRowCount() {
        System.out.print("Enter Rows Count : ");
        try {
            int rowCount = Integer.parseInt(SCANNER_STR.nextLine());
            if ( rowCount < 0 || rowCount > 1_000_000 ) throw new InputMismatchException();
            return rowCount;
        } catch ( InputMismatchException e ) {
            System.out.println("Row Count is Invalid");
            return getValidRowCount();
        }
    }

    /**
     * @return if not match to any variant returns
     * {@link  dev.jlkesh.seeder.FileType#JSON};
     */
    private static FileType getValidFileType() {
        System.out.print("Enter File Type (JSON, CSV, SQL) : ");
        return FileType.findByName(SCANNER_STR.nextLine());
    }

    private static String getValidatedFileName() {
        System.out.print("Enter File Name : ");
        String nextLine = SCANNER_STR.nextLine();
        Matcher matcher = validFileNamePattern.matcher(nextLine);
        if ( !matcher.matches() ) {
            System.out.printf("File Name Is Not Valid : '%s'%n" , nextLine);
            return getValidatedFileName();
        }
        return nextLine;
    }

    private static FieldType getValidatedFieldType() {
        System.out.printf("\nEnter Field Type ID(1-%d) : " , fieldsCount);
        String nextLine = SCANNER_STR.nextLine();
        try {
            var fieldTypeIndex = Integer.parseInt(nextLine);
            if ( fieldTypeIndex < 1 || fieldTypeIndex > fieldsCount ) throw new InputMismatchException();
            return fields.get(fieldTypeIndex);
        } catch ( InputMismatchException e ) {
            System.out.printf("Input must be a number and must be between[1-%d%n]" , fieldsCount);
            return getValidatedFieldType();
        }
    }

    private static String getValidatedFieldName() {
        System.out.print("Enter Field Name : ");
        var fieldName = SCANNER_STR.nextLine();
        Matcher matcher = validFieldNamePattern.matcher(fieldName);
        if ( !matcher.matches() ) {
            System.out.println("Invalid Field Name");
            return getValidatedFieldName();
        }
        return fieldName;
    }

    private static void showFieldTypes() {
        int i = 1;
        for ( FieldType fieldType : FieldType.values() ) {
            System.out.printf("%2d.%-20s" , i , fieldType);
            if ( i % 2 == 0 ) System.out.println();
            i++;
        }
    }
}