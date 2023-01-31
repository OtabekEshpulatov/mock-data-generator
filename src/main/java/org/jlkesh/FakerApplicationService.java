package org.jlkesh;


import com.github.javafaker.*;
import com.github.javafaker.service.RandomService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.jlkesh.FieldType.*;

public class FakerApplicationService {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AtomicLong id = new AtomicLong(1);
    private static final Faker faker = new Faker();
    private static final Country country = faker.country();
    private static final Address address = faker.address();
    private static final Book book = faker.book();
    private static final Name name = faker.name();
    private static final Lorem lorem = faker.lorem();
    private static final RandomService random = faker.random();
    private static final PhoneNumber phoneNumber = faker.phoneNumber();
    public static final Map<FieldType, Supplier<Object>> functions = new HashMap<>() {{
        put(org.jlkesh.FieldType.ID , id::getAndIncrement);
        put(org.jlkesh.FieldType.UUID , java.util.UUID::randomUUID);
        put(org.jlkesh.FieldType.FIRSTNAME , name::firstName);
        put(org.jlkesh.FieldType.LASTNAME , name::lastName);
        put(org.jlkesh.FieldType.USERNAME , name::username);
        put(FULLNAME , name::fullName);
        put(org.jlkesh.FieldType.BLOOD_GROUP , name::bloodGroup);
        put(org.jlkesh.FieldType.EMAIL , () -> name.username() + "@" + ( random.nextBoolean() ? "gmail.com" : "mail.ru" ));
        put(org.jlkesh.FieldType.GENDER , () -> random.nextBoolean() ? "MALE" : "FEMALE");
        put(org.jlkesh.FieldType.PHONE , phoneNumber::cellPhone);
        put(org.jlkesh.FieldType.COUNTRY_CODE , country::countryCode3);
        put(org.jlkesh.FieldType.COUNTRY_ZIP_CODE , address::zipCode);
        put(org.jlkesh.FieldType.CAPITAL , country::capital);
        put(org.jlkesh.FieldType.WORD , lorem::word);
    }};

    public static final List<FieldType> BLACK_LIST = List.of(
            AGE , WORDS , PARAGRAPHS , RANDOM_INT , POST_TITLE , POST_BODY , LETTERS
    );

    public String processRequest(FakerApplicationGenerateRequest fakerApplicationGenerateRequest) {
        var fileType = fakerApplicationGenerateRequest.getFileType();
        var fileName = fakerApplicationGenerateRequest.getFileName() + "." + fileType.name().toLowerCase();
        var rowsCount = fakerApplicationGenerateRequest.getCount();
        var fields = fakerApplicationGenerateRequest.getFields();
        return switch ( fileType ) {
            case JSON -> generateDataAsJson(rowsCount , fileName , fields);
            case CSV -> "Not Supported";
            case SQL -> "Not Supported";
        };
    }


    private String generateDataAsJson(int rowsCount , String fileName , Set<Field> fields) {
        synchronized ( FakerApplicationService.class ) {
            var result = new StringJoiner(",\n" , "[" , "]");
            for ( int i = 0 ; i < rowsCount ; i++ ) {
                var row = new StringJoiner(", " , "{" , "}");
                for ( Field field : fields )
                    row.add(field.getPatternAsJson());
                result.add(row.toString());
            }
            Path path = Path.of(fileName);
            try {
                if ( Files.notExists(path) )
                    Files.createFile(path);
                Files.writeString(path , result.toString() , StandardOpenOption.TRUNCATE_EXISTING);
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            return path.toAbsolutePath().toString();
        }
    }

}
