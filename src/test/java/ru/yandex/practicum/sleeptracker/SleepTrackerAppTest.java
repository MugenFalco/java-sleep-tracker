package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

public class SleepTrackerAppTest {

    // Вспомогательный метод для быстрого создания сессии
    private SleepingSession session(int y, int m, int d, int hh, int mm,
                                    int endY, int endM, int endD, int endHh, int endMm,
                                    SleepQuality quality) {
        return new SleepingSession(
                LocalDateTime.of(y, m, d, hh, mm),
                LocalDateTime.of(endY, endM, endD, endHh, endMm),
                quality);
    }

    // ================= TotalSessionsFunction =================

    @Test
    void totalSessionsMultiple() {
        TotalSessionsFunction f = new TotalSessionsFunction();
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 22, 0, 2025, 10, 2, 8, 0, SleepQuality.GOOD),
                session(2025, 10, 2, 23, 0, 2025, 10, 3, 7, 0, SleepQuality.NORMAL)
        );
        assertEquals(2L, f.analyze(sessions).getValue());
    }

    @Test
    void totalSessionsEmptyList() {
        TotalSessionsFunction f = new TotalSessionsFunction();
        assertEquals(0L, f.analyze(List.of()).getValue());
    }

    @Test
    void totalSessionsSingleSession() {
        TotalSessionsFunction f = new TotalSessionsFunction();
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 0, 2025, 10, 2, 5, 0, SleepQuality.BAD)
        );
        assertEquals(1L, f.analyze(sessions).getValue());
    }

    // ================= MinDurationFunction =================

    @Test
    void minDurationNormal() {
        MinDurationFunction f = new MinDurationFunction();
        // 600 мин и 360 мин
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 22, 0, 2025, 10, 2, 8, 0, SleepQuality.GOOD), // 600 min
                session(2025, 10, 2, 23, 0, 2025, 10, 3, 5, 0, SleepQuality.NORMAL) // 360 min
        );
        assertEquals(360L, f.analyze(sessions).getValue());
    }

    @Test
    void minDurationEmptyList() {
        MinDurationFunction f = new MinDurationFunction();
        assertEquals(0L, f.analyze(List.of()).getValue());
    }

    @Test
    void minDurationSingleSession() {
        MinDurationFunction f = new MinDurationFunction();
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 0, 2025, 10, 2, 1, 0, SleepQuality.GOOD) // 120 min
        );
        assertEquals(120L, f.analyze(sessions).getValue());
    }

    // ================= MaxDurationFunction =================

    @Test
    void maxDurationNormal() {
        MaxDurationFunction f = new MaxDurationFunction();
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 22, 0, 2025, 10, 2, 8, 0, SleepQuality.GOOD), // 600 min
                session(2025, 10, 2, 23, 0, 2025, 10, 3, 5, 0, SleepQuality.NORMAL) // 360 min
        );
        assertEquals(600L, f.analyze(sessions).getValue());
    }

    @Test
    void maxDurationEmptyList() {
        MaxDurationFunction f = new MaxDurationFunction();
        assertEquals(0L, f.analyze(List.of()).getValue());
    }

    @Test
    void maxDurationSingleSession() {
        MaxDurationFunction f = new MaxDurationFunction();
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 0, 2025, 10, 2, 7, 0, SleepQuality.GOOD) // 480 min
        );
        assertEquals(480L, f.analyze(sessions).getValue());
    }

    // ================= AverageDurationFunction =================

    @Test
    void averageDurationNormal() {
        AverageDurationFunction f = new AverageDurationFunction();
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 22, 0, 2025, 10, 2, 8, 0, SleepQuality.GOOD), // 600 min
                session(2025, 10, 2, 23, 0, 2025, 10, 3, 5, 0, SleepQuality.NORMAL) // 360 min
        );
        assertEquals(480.0, (Double) f.analyze(sessions).getValue(), 0.001);
    }

    @Test
    void averageDurationEmptyList() {
        AverageDurationFunction f = new AverageDurationFunction();
        assertEquals(0.0, (Double) f.analyze(List.of()).getValue(), 0.001);
    }

    @Test
    void averageDurationSingleSession() {
        AverageDurationFunction f = new AverageDurationFunction();
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 0, 2025, 10, 2, 5, 0, SleepQuality.GOOD) // 360 min
        );
        assertEquals(360.0, (Double) f.analyze(sessions).getValue(), 0.001);
    }

    // ================= BadQualitySessionsFunction =================

    @Test
    void badQualitySessionsMixed() {
        BadQualitySessionsFunction f = new BadQualitySessionsFunction();
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 22, 0, 2025, 10, 2, 8, 0, SleepQuality.BAD),
                session(2025, 10, 2, 23, 0, 2025, 10, 3, 7, 0, SleepQuality.GOOD),
                session(2025, 10, 3, 22, 0, 2025, 10, 4, 6, 0, SleepQuality.BAD)
        );
        assertEquals(2L, f.analyze(sessions).getValue());
    }

    @Test
    void badQualitySessionsNone() {
        BadQualitySessionsFunction f = new BadQualitySessionsFunction();
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 22, 0, 2025, 10, 2, 8, 0, SleepQuality.GOOD),
                session(2025, 10, 2, 23, 0, 2025, 10, 3, 7, 0, SleepQuality.NORMAL)
        );
        assertEquals(0L, f.analyze(sessions).getValue());
    }

    @Test
    void badQualitySessionsEmptyList() {
        BadQualitySessionsFunction f = new BadQualitySessionsFunction();
        assertEquals(0L, f.analyze(List.of()).getValue());
    }

    // ================= SleeplessNightsFunction =================

    @Test
    void sleeplessNightsSingleNightCovered() {
        SleeplessNightsFunction f = new SleeplessNightsFunction();
        // Первая сессия началась в 23:00 (после 12) -> первая ночь = 02.10.2025
        // последняя дата = 02.10.2025, всего ночей = 1; она покрыта сессией.
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 0, 2025, 10, 2, 8, 0, SleepQuality.GOOD)
        );
        assertEquals(0L, f.analyze(sessions).getValue());
    }

    @Test
    void sleeplessNightsMissingNight() {
        SleeplessNightsFunction f = new SleeplessNightsFunction();
        // Обычный пропуск ночи.
        // Сессии: 02.10.22:00-03.10.07:00 (покрывает ночь 03.10)
        //          04.10.22:00-05.10.07:00 (покрывает ночь 05.10)
        // Первая ночь = 03.10 (старт после 12), последняя = 05.10.
        // Ночи: 03.10 (покрыта), 04.10 (не покрыта), 05.10 (покрыта). => 1 бессонная.
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 2, 22, 0, 2025, 10, 3, 7, 0, SleepQuality.GOOD),
                session(2025, 10, 4, 22, 0, 2025, 10, 5, 7, 0, SleepQuality.GOOD)
        );
        assertEquals(1L, f.analyze(sessions).getValue());
    }

    @Test
    void sleeplessNightsFirstSessionAfterNoon() {
        SleeplessNightsFunction f = new SleeplessNightsFunction();
        // Первая сессия началась в 13:00 (после полудня) -> первая ночь = следующий день
        // Конец последней = 01.10.2025 14:00 (та же дата), totalNights <= 0 -> 0 бессонных.
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 13, 0, 2025, 10, 1, 14, 0, SleepQuality.NORMAL)
        );
        assertEquals(0L, f.analyze(sessions).getValue());
    }

    @Test
    void sleeplessNightsNoNightSleep() {
        SleeplessNightsFunction f = new SleeplessNightsFunction();
        // Сессия дневная, не затрагивает интервал 0:00-6:00 (07:00-11:00)
        // Начало сессии <= 12:00 → первая ночь = предыдущая (минус 1 день)
        // Дата начала: 01.10.2025 07:00 → первая ночь = 30.09.2025
        // Окончание последней: 01.10.2025 11:00 → lastDate = 01.10.2025
        // Ночи: 30.09.2025, 01.10.2025 (всего 2). Ни одна не покрыта. Бессонных = 2.
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 7, 0, 2025, 10, 1, 11, 0, SleepQuality.NORMAL)
        );
        assertEquals(2L, f.analyze(sessions).getValue());
    }

    @Test
    void sleeplessNightsBoundaryTimes() {
        SleeplessNightsFunction f = new SleeplessNightsFunction();
        // Проверка граничных моментов: сессия начинается ровно в 0:00, заканчивается в 6:00
        // Первая ночь: старт в 22:00 (после 12) -> первая ночь = 02.10.2025
        // Последняя дата = 02.10.2025
        // Ночь 02.10.2025: сессия start=02.10.2025 00:00 < 06:00, end=02.10.2025 06:00 > 00:00 => покрыта.
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 22, 0, 2025, 10, 2, 0, 0, SleepQuality.GOOD), // не перекрывает
                session(2025, 10, 2, 0, 0, 2025, 10, 2, 6, 0, SleepQuality.GOOD)  // перекрывает ровно
        );
        // totalNights=1, covered=1 => sleepless=0
        assertEquals(0L, f.analyze(sessions).getValue());

        // Дополнительно: если сессия заканчивается ровно в 0:00, она не покрывает (end > 0:00 проверка)
        List<SleepingSession> sessions2 = List.of(
                session(2025, 10, 1, 22, 0, 2025, 10, 2, 0, 0, SleepQuality.GOOD) // end = 00:00, не > 00:00 => не покрывает
        );
        // firstNight=02.10.2025, lastDate=02.10.2025, totalNights=1, covered=0 => sleepless=1
        assertEquals(1L, f.analyze(sessions2).getValue());
    }

    @Test
    void sleeplessNightsMonthTransition() {
        SleeplessNightsFunction f = new SleeplessNightsFunction();
        // Переход через месяц: сессии 31.10.2025 22:00-01.11.2025 07:00 (покрывает 01.11)
        // и 02.11.2025 22:00-03.11.2025 07:00 (покрывает 03.11)
        // firstNight = 01.11.2025, lastDate = 03.11.2025 => ночи: 01.11 (покрыта), 02.11 (нет), 03.11 (покрыта) => 1 бессонная.
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 31, 22, 0, 2025, 11, 1, 7, 0, SleepQuality.GOOD),
                session(2025, 11, 2, 22, 0, 2025, 11, 3, 7, 0, SleepQuality.GOOD)
        );
        assertEquals(1L, f.analyze(sessions).getValue());
    }

    // ================= ChronotypeFunction =================

    @Test
    void chronotypeOwl() {
        ChronotypeFunction f = new ChronotypeFunction();
        // 23:30 засыпание, 10:00 пробуждение
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 30, 2025, 10, 2, 10, 0, SleepQuality.GOOD)
        );
        assertEquals(Chronotype.OWL, f.analyze(sessions).getValue());
    }

    @Test
    void chronotypeLark() {
        ChronotypeFunction f = new ChronotypeFunction();
        // 21:30 засыпание, 6:30 пробуждение
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 21, 30, 2025, 10, 2, 6, 30, SleepQuality.GOOD)
        );
        assertEquals(Chronotype.LARK, f.analyze(sessions).getValue());
    }

    @Test
    void chronotypeDoveExactBoundaries() {
        ChronotypeFunction f = new ChronotypeFunction();
        // точно 22:00 и 7:00 -> голубь
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 22, 0, 2025, 10, 2, 7, 0, SleepQuality.GOOD)
        );
        assertEquals(Chronotype.DOVE, f.analyze(sessions).getValue());
    }

    @Test
    void chronotypeMixedWithTie() {
        ChronotypeFunction f = new ChronotypeFunction();
        // Две ночи: одна OWL, одна LARK -> равенство -> итог DOVE
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 30, 2025, 10, 2, 10, 0, SleepQuality.GOOD), // OWL
                session(2025, 10, 2, 21, 30, 2025, 10, 3, 6, 30, SleepQuality.GOOD)  // LARK
        );
        assertEquals(Chronotype.DOVE, f.analyze(sessions).getValue());
    }
}