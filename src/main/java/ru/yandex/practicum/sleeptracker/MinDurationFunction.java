package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;

public class MinDurationFunction implements SleepAnalysisFunction {
    private static final String DESCRIPTION = "Минимальная продолжительность (в минутах)";

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        long min = sessions.stream()
                .mapToLong(s -> Duration.between(s.getStart(), s.getEnd()).toMinutes())
                .min()
                .orElse(0L);
        return new SleepAnalysisResult(DESCRIPTION, min);
    }
}