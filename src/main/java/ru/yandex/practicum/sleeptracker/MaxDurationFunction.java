package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;

public class MaxDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Максимальная продолжительность (в минутах)", 0L);
        }
        long max = sessions.stream()
                .mapToLong(s -> Duration.between(s.getStart(), s.getEnd()).toMinutes())
                .max()
                .orElse(0L);
        return new SleepAnalysisResult("Максимальная продолжительность (в минутах)", max);
    }
}
