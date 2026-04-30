package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadQualitySessionsFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        long count = sessions.stream()
                .filter(s -> s.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult("Сессии сна плохого качества", count);
    }
}
