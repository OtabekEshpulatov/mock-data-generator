package org.otash;

import com.github.javafaker.*;
import com.github.javafaker.service.RandomService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;

public class FakeDataGeneratorService {


    private static final AtomicLong id = new AtomicLong(1);
    private static final DecimalFormat df = new DecimalFormat("#".repeat(10) + "." + "##");
    private static final Scanner scanner = new Scanner(System.in);
    private static final Faker faker = new Faker();
    private static final RandomService randomService = faker.random();

    private static final Random random = new Random();
    private static final Name name = faker.name();
    private static final PhoneNumber phoneNumber = faker.phoneNumber();
    private static final Country country = faker.country();
    private static final Address address = faker.address();
    private static final Lorem lorem = faker.lorem();
    private static final BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);
    private static final int THRESHOLD = 10000;


    public static Map<FieldType, Supplier<Object>> functions = new HashMap<>() {{
        put(FieldType.ID , id::getAndIncrement);
        put(FieldType.UUID , UUID::randomUUID);
        put(FieldType.FIRSTNAME , name::firstName);
        put(FieldType.LASTNAME , name::lastName);
        put(FieldType.USERNAME , name::username);
        put(FieldType.FULL_NAME , name::fullName);
        put(FieldType.BLOOD_GROUP , name::bloodGroup);
        put(FieldType.EMAIL , () -> name.username() + "@" + ( random.nextBoolean() ? "gmail.com" : "mail.ru" ));
        put(FieldType.GENDER , () -> random.nextBoolean() ? "MALE" : "FEMALE");
        put(FieldType.PHONE , phoneNumber::cellPhone);
        put(FieldType.COUNTRY_CODE , country::countryCode3);
        put(FieldType.COUNTRY_ZIP_CODE , address::zipCode);
        put(FieldType.CAPITAL , country::capital);
        put(FieldType.WORD , lorem::word);

    }};


    public String processRequest(Request request) throws ExecutionException, InterruptedException {


        FileType fileType = request.getFileType();
        String fileName = request.getFileName() + "." + fileType.name().toLowerCase();
        int count = request.getCount();
        Set<Field> fields = request.getFields();
        var rowActions = rowActionsFunction.apply(fields);


        return switch ( fileType ) {
            case SQL -> generateDataAsSql(fileName , count , rowActions);
            case CSV -> generateDataAsCsv(fileName , count , rowActions);
            case JSON -> generateDateAsJson(fileName , count , rowActions);
        };

    }


    private String writeToFile(String fileName , String result) {

        try {

            Path path = Path.of(FakeDataGeneratorService.class.getClassLoader().getResource("").getFile()).resolve(fileName).toAbsolutePath();

            if ( Files.notExists(path) ) {
                path = Files.createFile(path);
            }

            Files.writeString(path , result , StandardOpenOption.TRUNCATE_EXISTING);

            return path.toString();

        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }


    }

    private String generateDateAsJson(String fileName , int rowCount , List<RowAction> rowActions) throws InterruptedException, ExecutionException {


        List<Callable<String>> tasks = new ArrayList<>();

//        ExecutorService creatorThread = Executors.newSingleThreadExecutor();
        ExecutorService writerThreads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        class MyThread extends Thread {
            StringJoiner result = new StringJoiner("," , "[" , "]");
            int counter = 0;
            int putTime = 0;

            @Override
            public void run() {

                for ( int i = 0 ; i < rowCount ; i++ ) {
                    StringJoiner row = new StringJoiner("," , "{" , "}");
                    for ( RowAction rowAction : rowActions ) {
                        FieldType fieldType = rowAction.getFieldType();
                        row.add(fieldType.toJson(rowAction.getFieldName() , rowAction.getFunc().get().toString()));
                    }
                    counter++;
                    result.add(row.toString());
                    if ( counter == THRESHOLD ) {
                        queue.add(result.toString());
                        result = new StringJoiner("," , "[" , "]");
                        counter = 0;
                        putTime++;
                    }
                }

                queue.add(result.toString());
                putTime++;

            }
        }


        MyThread myThread = new MyThread();
        myThread.start();


        while ( myThread.isAlive() || !queue.isEmpty() ) {
            Callable<String> writerCallable = () -> writeToFile(fileName , queue.take());
            tasks.add(writerCallable);
        }

        Future<String> submit = null;
        for ( Callable<String> task : tasks ) {
            submit = writerThreads.submit(task);
        }

        writerThreads.shutdown();
        if ( writerThreads.awaitTermination(30 , TimeUnit.MINUTES) ) {
            writerThreads.shutdownNow();
        }
        return submit.get();

    }


    private String generateDataAsCsv(String fileName , int rowCount , List<RowAction> rowActions) {
        throw new RuntimeException("CSV format is NOT SUPPORTED");
    }

    private String generateDataAsSql(String fileName , int rowCount , List<RowAction> rowActions) {
        throw new RuntimeException("SQL format is NOT SUPPORTED");
    }

    private final Function<Set<Field>, List<RowAction>> rowActionsFunction = fields -> {

        final Map<FieldType, Supplier<Object>> functions = FakeDataGeneratorService.functions;

        return fields.stream().map(field -> {

            FieldType fieldType = field.getFieldType();

            switch ( fieldType ) {
                case RANDOM_DOUBLE -> {

                    System.out.printf("Min range of %s (inclusive) : " , field.getFieldName());
                    double min = scanner.nextDouble();
                    System.out.printf("Max range of %s (inclusive) : " , field.getFieldName());
                    double max = scanner.nextDouble();

                    functions.putIfAbsent(fieldType , () -> {
//                    return df.format(random.nextDouble(min , max));
                        return df.format(Math.random() * ( max - min + 1 ) + min);

                    });
                }
                case AGE , PARAGRAPHS , RANDOM_INT , LETTERS , WORDS -> {

                    System.out.printf("Min range of %s (inclusive) : " , field.getFieldName());
                    int min = scanner.nextInt();
                    System.out.printf("Max range of %s (inclusive) : " , field.getFieldName());
                    int max = scanner.nextInt();

                    switch ( fieldType ) {
                        case AGE , RANDOM_INT -> functions.putIfAbsent(fieldType , () -> {
//                    return random.nextInt(min , max);
                            return randomService.nextInt(min , max);
                        });

                        case PARAGRAPHS ->
                                functions.putIfAbsent(fieldType , () -> lorem.paragraphs(randomService.nextInt(min , max)));
                        case LETTERS -> functions.putIfAbsent(fieldType , () -> lorem.characters(min , max));
                        case WORDS ->
                                functions.putIfAbsent(fieldType , () -> lorem.words(randomService.nextInt(min , max)));

                    }

                    return new RowAction(field);
                }
            }

            return new RowAction(field);

        }).toList();
    };

}

;

