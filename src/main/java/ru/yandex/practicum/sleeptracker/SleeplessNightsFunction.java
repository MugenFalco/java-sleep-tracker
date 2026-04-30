package ru.yandex.practicum.sleeptracker;

import java.util.stream.Stream;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SleeplessNightsFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Бессонные ночи", 0L);
        }

        // Первая и последняя сессии
        SleepingSession first = sessions.get(0);
        SleepingSession last = sessions.get(sessions.size() - 1);

        LocalDateTime firstStart = first.getStart();
        LocalDate lastDate = last.getEnd().toLocalDate();

        // Определяем первую потенциальную ночь по правилу:
        // если время начала первой сессии > 12:00 -> следующая ночь (дата старта + 1 день)
        // если ≤ 12:00 -> предыдущая ночь (дата старта - 1 день)
        LocalDate firstNightDate;
        if (firstStart.toLocalTime().isAfter(java.time.LocalTime.NOON)) {
            // следующая ночь
            firstNightDate = firstStart.toLocalDate().plusDays(1);
        } else {
            // предыдущая ночь
            firstNightDate = firstStart.toLocalDate().minusDays(1);
        }

        long totalNights = ChronoUnit.DAYS.between(firstNightDate, lastDate) + 1;
        if (totalNights <= 0) {
            return new SleepAnalysisResult("Бессонные ночи", 0L);
        }

        long coveredNights = Stream.iterate(firstNightDate, d -> d.plusDays(1))
                .limit(totalNights)
                .filter(nightDate ->
                        sessions.stream().anyMatch(s ->
                                // сессия пересекает интервал 0:00-6:00 даты nightDate
                                s.getStart().isBefore(nightDate.atTime(6, 0)) &&
                                        s.getEnd().isAfter(nightDate.atStartOfDay())
                        )
                )
                .count();

        long sleepless = totalNights - coveredNights;
        return new SleepAnalysisResult("Sleepless nights", sleepless);
    }
}
