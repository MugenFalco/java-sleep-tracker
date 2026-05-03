package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;

public class MaxDurationFunction implements SleepAnalysisFunction {
    private static final String DESCRIPTION = "Максимальная продолжительность (в минутах)";

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        long max = sessions.stream()
                .mapToLong(s -> Duration.between(s.getStart(), s.getEnd()).toMinutes())
                .max()
                .orElse(0L);
        return new SleepAnalysisResult(DESCRIPTION, max);
    }
}