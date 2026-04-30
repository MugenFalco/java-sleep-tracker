package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SleepTrackerApp {

    public static void main(String[] args) {
        List<SleepingSession> sessions;
        try {
            sessions = SleepDataLoader.loadDefault();
        } catch (IOException e) {
            System.err.println("Error reading sleep log: " + e.getMessage());
            return;
        }

        List<SleepAnalysisFunction> functions = Arrays.asList(
                new TotalSessionsFunction(),
                new MinDurationFunction(),
                new MaxDurationFunction(),
                new AverageDurationFunction(),
                new BadQualitySessionsFunction(),
                new SleeplessNightsFunction(),
                new ChronotypeFunction()
        );

        System.out.println("=== Анализ трекера сна ===");
        functions.forEach(f -> {
            SleepAnalysisResult result = f.analyze(sessions);
            System.out.println(result.getDescription() + ": " + result.getValue());
        });
    }
}