package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SleepDataLoader {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private static final String DEFAULT_RESOURCE = "sleep_log.txt";

    public static List<SleepingSession> loadDefault() throws IOException {
        InputStream inputStream = SleepDataLoader.class.getClassLoader()
                .getResourceAsStream(DEFAULT_RESOURCE);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + DEFAULT_RESOURCE);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .map(line -> {
                        String[] parts = line.split(";");
                        if (parts.length != 3) {
                            throw new IllegalArgumentException("Invalid line format: " + line);
                        }
                        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), FORMATTER);
                        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), FORMATTER);
                        SleepQuality quality = SleepQuality.valueOf(parts[2].trim().toUpperCase());
                        return new SleepingSession(start, end, quality);
                    })
                    .collect(Collectors.toList());
        }
    }
}