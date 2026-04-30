package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;

public class MinDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Минимальная продолжительность (в минутах)", 0L);
        }
        long min = sessions.stream()
                .mapToLong(s -> Duration.between(s.getStart(), s.getEnd()).toMinutes())
                .min()
                .orElse(0L);
        return new SleepAnalysisResult("Минимальная продолжительность (в минутах)", min);
    }
}
