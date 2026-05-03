package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;

public class AverageDurationFunction implements SleepAnalysisFunction {
    private static final String DESCRIPTION = "Средняя продолжительность (в минутах)";

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        double avg = sessions.stream()
                .mapToLong(s -> Duration.between(s.getStart(), s.getEnd()).toMinutes())
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult(DESCRIPTION, avg);
    }
}