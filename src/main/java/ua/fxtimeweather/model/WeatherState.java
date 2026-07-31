package ua.fxtimeweather.model;

public enum WeatherState {
    CLEAR("clear"),
    RAIN("rain"),
    STORM("storm");

    private final String key;

    WeatherState(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}