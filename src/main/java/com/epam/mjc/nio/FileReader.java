package com.epam.mjc.nio;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReader {

    private static final String NAME_KEY = "name";
    private static final String AGE_KEY = "age";
    private static final String EMAIL_KEY = "email";
    private static final String PHONE_KEY = "phone";
    private static final Profile DEFAULT_PROFILE = new Profile("Unknown", 0, "unknown@example.com", 0L);
    private static final Logger LOGGER = Logger.getLogger(com.epam.mjc.nio.FileReader.class.getName());

    public Profile getDataFromFile(File file) {

        Path filePath = file.toPath();
        Map<String, String> profileMap = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String profileLine;
            while ((profileLine = reader.readLine()) != null) {
                String[] keyValue = profileLine.split(":", 2);
                if (keyValue.length == 2) {
                    profileMap.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
                }
            }
            return createProfileFromMap(profileMap);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Ошибка при чтении файла %s", file.getName()), e);
            return DEFAULT_PROFILE;
        }
    }

    private Profile createProfileFromMap (Map<String, String> profileMap) {
        try {
            String name = profileMap.getOrDefault(NAME_KEY, DEFAULT_PROFILE.getName());
            Integer age = Integer.valueOf(profileMap.getOrDefault(AGE_KEY, "0"));
            String email = profileMap.getOrDefault(EMAIL_KEY,DEFAULT_PROFILE.getEmail());
            Long phone = Long.valueOf(profileMap.getOrDefault(PHONE_KEY, "0"));
            return new Profile(name, age, email, phone);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Ошибка при преобразовании данных в профиле", e);
            return DEFAULT_PROFILE;
        }
    }
}