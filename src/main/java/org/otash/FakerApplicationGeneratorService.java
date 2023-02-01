package org.otash;


import com.github.javafaker.*;
import com.github.javafaker.service.RandomService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static org.otash.FieldType.*;
import static org.otash.Utils.writeToFile;

public class FakerApplicationGeneratorService {


    private static final AtomicLong id = new AtomicLong(1);
    private static final Faker faker = new Faker();
    private static final Book book = faker.book();
    private static final Country country = faker.country();
    private static final Address address = faker.address();
    private static final Name name = faker.name();
    private static final Lorem lorem = faker.lorem();
    private static final RandomService random = faker.random();
    private static final PhoneNumber phoneNumber = faker.phoneNumber();
    public static final Map<FieldType, Supplier<Object>> functions = new HashMap<>() {{
        put(org.otash.FieldType.ID , id::getAndIncrement);
        put(org.otash.FieldType.UUID , java.util.UUID::randomUUID);
        put(org.otash.FieldType.FIRSTNAME , name::firstName);
        put(org.otash.FieldType.LASTNAME , name::lastName);
        put(org.otash.FieldType.USERNAME , name::username);
        put(FULLNAME , name::fullName);
        put(org.otash.FieldType.BLOOD_GROUP , name::bloodGroup);
        put(org.otash.FieldType.EMAIL , () -> name.username() + "@" + ( random.nextBoolean() ? "gmail.com" : "mail.ru" ));
        put(org.otash.FieldType.GENDER , () -> random.nextBoolean() ? "MALE" : "FEMALE");
        put(org.otash.FieldType.PHONE , phoneNumber::cellPhone);
        put(org.otash.FieldType.COUNTRY_CODE , country::countryCode3);
        put(org.otash.FieldType.COUNTRY_ZIP_CODE , address::zipCode);
        put(org.otash.FieldType.CAPITAL , country::capital);
        put(org.otash.FieldType.WORD , lorem::word);
        put(LOCAlDATE , () -> {
            int year = random.nextInt(1900 , Year.now().getValue() - 1);
            int month = random.nextInt(1 , 12);
            YearMonth yearMonth = YearMonth.of(year , month);
            int day = random.nextInt(1 , yearMonth.getMonth().length(yearMonth.isLeapYear()));
            return LocalDate.of(year , month , day);

        });
        put(BOOK_TITLE , book::title);
        put(BOOK_AUTHOR , book::author);
        put(POST_TITLE , () -> String.join(" " , lorem.words(random.nextInt(1 , 10))));
        put(POST_BODY , () -> String.join("" , lorem.paragraphs(random.nextInt(1 , 10))));
    }};


    public String processRequest(FakerApplicationGenerateRequest fakerApplicationGenerateRequest) {


        var fileType = fakerApplicationGenerateRequest.getFileType();

        return switch ( fileType ) {
            case JSON -> startActionWithJson(fakerApplicationGenerateRequest);
            case CSV -> "Not Supported";
            case SQL -> "Not Supported";
        };
    }

    private static String startActionWithJson(FakerApplicationGenerateRequest request) {

        String fileName = request.getFileName() + "." + request.getFileType().name().toLowerCase();
        Path path = Path.of(FakerApplicationRunner.class.getClassLoader().getResource("").getFile()).resolve(fileName).toAbsolutePath();
        String pathString = path.toFile().getPath();

        Utils.writeToFile(pathString , "[" , false);
        JsonDataWriter dataWriter = new JsonDataWriter(request , request.getCount() , pathString);

        ForkJoinPool joinPool = new ForkJoinPool();
        joinPool.invoke(dataWriter);
        writeToFile(fileName , "]" , true);
        return pathString;
    }


//    private String generateDataAsJson(int rowsCount , String fileName , Set<Field> fields) {
//        synchronized ( FakerApplicationService.class ) {
//            var result = new StringJoiner(",\n" , "[" , "]");
//            for ( int i = 0 ; i < rowsCount ; i++ ) {
//                var row = new StringJoiner(", " , "{" , "}");
//                for ( Field field : fields )
//                    row.add(field.getPatternAsJson());
//                result.add(row.toString());
//            }
//            Path path = Path.of(fileName);
//            try {
//                if ( Files.notExists(path) ) Files.createFile(path);
//                Files.writeString(path , result.toString() , StandardOpenOption.TRUNCATE_EXISTING);
//            } catch ( IOException e ) {
//                e.printStackTrace();
//            }
//            return path.toAbsolutePath().toString();
//        }
//    }

}
