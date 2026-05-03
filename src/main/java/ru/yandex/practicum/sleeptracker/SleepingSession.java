package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.util.Optional;

public final class SleepingSession {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        this.start = Optional.ofNullable(start)
                .orElseThrow(() -> new NullPointerException("start must not be null"));
        this.end = Optional.ofNullable(end)
                .orElseThrow(() -> new NullPointerException("end must not be null"));
        this.quality = Optional.ofNullable(quality)
                .orElseThrow(() -> new NullPointerException("quality must not be null"));

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End must be after start");
        }
    }

    @Override
    public String toString() {
        return "SleepingSession{" +
                "start=" + start +
                ", end=" + end +
                ", quality=" + quality +
                '}';
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SleepQuality getQuality() {
        return quality;
    }
}
