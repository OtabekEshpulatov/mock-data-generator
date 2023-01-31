package org.otash;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern validFileNamePatten = Pattern.compile("^([a-zA-Z_]+\\d*)$");
    private static final List<FieldType> fields = new ArrayList<FieldType>();
    private static final int MAX_ROW_COUNT = 1_000_000_000;

    static {
        fields.addAll(Arrays.asList(FieldType.values()));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        var fakeDataGeneratorService = new FakeDataGeneratorService();
        var builder = Request.builder();
        builder.fileName(getValidFileName());
        builder.fileType(getValidFileType());
        builder.count(getValidCount());


        Set<Field> fieldSet = new TreeSet<>();


        String choice = "";

        while ( !choice.startsWith("q") ) {

            System.out.print("Enter Field Name: ");
            String fieldName = scanner.nextLine();
            showFieldTypes();
            FieldType fieldType = fields.get(getValidFieldId());
            Field field = new Field(fieldName , fieldType);
            fieldSet.add(field);

            System.out.println(" \"Quit\" -> q ");
            System.out.println(" \"Continue\" -> c ");
            choice = scanner.nextLine();

        }

        builder.fields(fieldSet);


        String path = fakeDataGeneratorService.processRequest(builder.build());

        System.out.print("File path=> " + Path.of(path));


    }

    private static int getValidFieldId() {
        int size = fields.size();
        System.out.printf("Enter field TYPE ID [1-%d] : " , size);

        try {
            int id = Integer.parseInt(scanner.nextLine()) - 1;
            if ( id < 0 || id >= size ) throw new NumberFormatException();
            return id;
        } catch ( NumberFormatException ex ) {
            System.out.println("Invalid Number entered! Try again.");
            showFieldTypes();
            return getValidFieldId();
        }
    }

    private static void showFieldTypes() {

        for ( int i = 0 ; i < fields.size() ; i++ ) {

            System.out.printf("%2d.%-20s" , i + 1 , fields.get(i).name());

            if ( i % 2 == 0 ) System.out.println();

        }
    }

    private static int getValidCount() {
        System.out.print("Rows Count: ");
        try {
            int count = Integer.parseInt(scanner.nextLine());
            if ( count < 1 || count > MAX_ROW_COUNT ) throw new NumberFormatException();
            return count;
        } catch ( NumberFormatException e ) {
            System.out.println("Invalid number entered!");
            return getValidCount();
        }

    }

    private static FileType getValidFileType() {

        FileType[] fileTypes = FileType.values();
        System.out.printf("Enter File Types %s " , Arrays.toString(fileTypes));
        String fileType = scanner.nextLine();

        FileType type = FileType.getType(fileType);

        if ( type == null ) {
            System.out.println("Invalid file type! Try again");
            getValidFileType();
        }
        return type;
    }

    private static String getValidFileName() {
        System.out.print("Enter File Name: ");
        String fileName = scanner.nextLine();

        Matcher matcher = validFileNamePatten.matcher(fileName);
        if ( !matcher.matches() ) {
            System.out.println("File name is invalid! Try again.");
            getValidFileName();
        }
        return fileName;


    }
}
