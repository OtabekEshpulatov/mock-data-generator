package org.jlkesh;

import com.github.javafaker.*;
import com.github.javafaker.service.RandomService;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

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
}
