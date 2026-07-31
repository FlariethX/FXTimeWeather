package ua.fxtimeweather.model;

public enum DayPhase {
    DAWN("dawn"),
    MORNING("morning"),
    DAY("day"),
    EVENING("evening"),
    DUSK("dusk"),
    NIGHT("night");

    private final String key;

    DayPhase(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static DayPhase fromTicks(long time) {
        if (time >= 23000) {
            return DAWN;
        }
        if (time < 2000) {
            return MORNING;
        }
        if (time < 11000) {
            return DAY;
        }
        if (time < 12000) {
            return EVENING;
        }
        if (time < 13000) {
            return DUSK;
        }
        return NIGHT;
    }
}