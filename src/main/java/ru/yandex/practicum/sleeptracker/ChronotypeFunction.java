package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип", Chronotype.DOVE);
        }

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(s -> {
                    LocalDate startDate = s.getStart().toLocalDate();
                    LocalDate endDate = s.getEnd().toLocalDate();
                    if (!startDate.equals(endDate)) {
                        return true;
                    }
                    return s.getStart().toLocalTime().isBefore(LocalTime.of(6, 0))
                            && s.getEnd().toLocalTime().isAfter(LocalTime.MIDNIGHT);
                })
                .toList();

        Map<LocalDate, SleepingSession> mainSessionByNight = nightSessions.stream()
                .collect(Collectors.toMap(s -> {
                    if (s.getStart().toLocalDate().isBefore(s.getEnd().toLocalDate())) {
                        return s.getEnd().toLocalDate();
                    } else {
                        return s.getStart().toLocalDate();
                    }
                }, Function.identity(), BinaryOperator.minBy(Comparator.comparing(SleepingSession::getStart))));

        Map<Chronotype, Long> typeCounts = mainSessionByNight.values().stream()
                .map(s -> {
                    LocalTime sleepTime = s.getStart().toLocalTime();
                    LocalTime wakeTime = s.getEnd().toLocalTime();
                    if (sleepTime.isAfter(LocalTime.of(23, 0)) && wakeTime.isAfter(LocalTime.of(9, 0))) {
                        return Chronotype.OWL;
                    } else if (sleepTime.isBefore(LocalTime.of(22, 0)) && wakeTime.isBefore(LocalTime.of(7, 0))) {
                        return Chronotype.LARK;
                    } else {
                        return Chronotype.DOVE;
                    }
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Chronotype resultType = typeCounts.entrySet().stream()
                .max(Map.Entry.<Chronotype, Long>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder()))) // не важно, при равенстве выберем DOVE ниже
                .map(Map.Entry::getKey)
                .orElse(Chronotype.DOVE);

        long maxCount = typeCounts.values().stream().mapToLong(Long::longValue).max().orElse(0);
        long maxTypesCount = typeCounts.values().stream().filter(v -> v == maxCount).count();
        if (maxTypesCount > 1) {
            resultType = Chronotype.DOVE;
        }

        return new SleepAnalysisResult("Хронотип", resultType);
    }
}

