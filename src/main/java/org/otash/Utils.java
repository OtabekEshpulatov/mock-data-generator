package org.otash;

import com.github.javafaker.*;
import com.github.javafaker.service.RandomService;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

public interface Utils {

    DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#".repeat(10) + "." + "##");
    Scanner SCANNER_STR = new Scanner(System.in);
    Scanner SCANNER_INT = new Scanner(System.in);
    Faker FAKER = new Faker();
    RandomService RANDOM_SERVICE = FAKER.random();

    Random RANDOM = new Random();
    Name NAME = FAKER.name();
    PhoneNumber PHONE_NUMBER = FAKER.phoneNumber();
    Country COUNTRY = FAKER.country();
    Address ADDRESS = FAKER.address();
    Lorem LOREM = FAKER.lorem();


    static String writeToFile(String fileName , String content , boolean append) {

        try {

            Path path = Path.of(Utils.class.getClassLoader().getResource("").getFile()).resolve(fileName).toAbsolutePath();

            if ( Files.notExists(path) ) {
                path = Files.createFile(path);
            }

            Files.writeString(path , content , !append ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.APPEND);

            return path.toString();

        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }


    }
}
