package bilokhado.weatherrestclient.domain;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Class represents current weather
 */
public class CurrentWeather {

    private static final String NEW_LINE = System.lineSeparator();

    private String locationName;
    private String countryName;
    private double temperature;
    private double windSpeed;
    private int windDegree;
    private int humidity;
    private double feelsLike;
    private double visibilityDistance;
    private int pressure;
    private double precipitation;
    private String condition;
    private LocalDateTime lastUpdated;

    private CurrentWeather(Builder builder) {
        this.locationName = builder.locationName;
        this.countryName = builder.countryName;
        this.temperature = builder.temperature;
        this.windSpeed = builder.windSpeed;
        this.windDegree = builder.windDegree;
        this.humidity = builder.humidity;
        this.feelsLike = builder.feelsLike;
        this.visibilityDistance = builder.visibilityDistance;
        this.pressure = builder.pressure;
        this.precipitation = builder.precipitation;
        this.condition = builder.condition;
        this.lastUpdated = builder.lastUpdated;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public String getLocationName() {
        return locationName;
    }

    public String getCountryName() {
        return countryName;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getWindDegree() {
        return windDegree;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public double getVisibilityDistance() {
        return visibilityDistance;
    }

    public int getPressure() {
        return pressure;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public String getCondition() {
        return condition;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("The weather in ");
        output.append(locationName);
        output.append(", ");
        output.append(countryName);
        output.append(" last updated on ");
        output.append(lastUpdated.toString());
        output.append(NEW_LINE);
        output.append("Temperature: ");
        output.append(temperature);
        output.append(" C");
        output.append(NEW_LINE);
        output.append("Pressure: ");
        output.append(pressure);
        output.append(" mBar");
        output.append(NEW_LINE);
        output.append("Wind speed: ");
        output.append(windSpeed);
        output.append(" km/h");
        output.append(NEW_LINE);
        output.append("Wind degree: ");
        output.append(windDegree);
        output.append(NEW_LINE);
        output.append("Humidity: ");
        output.append(humidity);
        output.append(" %");
        output.append(NEW_LINE);
        output.append("Precipitation: ");
        output.append(precipitation);
        output.append(" mm");
        output.append(NEW_LINE);
        output.append("Visibility distance: ");
        output.append(visibilityDistance);
        output.append(" km");
        output.append(NEW_LINE);
        output.append("Condition: ");
        output.append(condition);
        output.append(NEW_LINE);
        output.append("Feels like: ");
        output.append(feelsLike);
        output.append(" C");
        output.append(NEW_LINE);
        return output.toString();
    }

    public static class Builder {

        private String locationName;
        private String countryName;
        private double temperature;
        private double windSpeed;
        private int windDegree;
        private int humidity;
        private double feelsLike;
        private double visibilityDistance;
        private int pressure;
        private double precipitation;
        private String condition;
        private LocalDateTime lastUpdated;
        private long lastUpdatedEpoch;

        private Builder() {
            //Avoid instantiation offside of our class
        }

        public Builder locationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public Builder countryName(String countryName) {
            this.countryName = countryName;
            return this;
        }

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder windSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder windDegree(int windDegree) {
            this.windDegree = windDegree;
            return this;
        }

        public Builder humidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder feelsLike(double feelsLike) {
            this.feelsLike = feelsLike;
            return this;
        }

        public Builder visibilityDistance(double visibilityDistance) {
            this.visibilityDistance = visibilityDistance;
            return this;
        }

        public Builder pressure(int pressure) {
            this.pressure = pressure;
            return this;
        }

        public Builder precipitation(double precipitation) {
            this.precipitation = precipitation;
            return this;
        }

        public Builder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder lastUpdatedEpoch(long epoch) {
            this.lastUpdatedEpoch = epoch;
            return this;
        }

        public CurrentWeather build() {
            try {
                this.lastUpdated = LocalDateTime.ofEpochSecond(lastUpdatedEpoch, 0, ZoneOffset.UTC);
            } catch (DateTimeException ex) {
                throw new IllegalStateException("Provided last updated epoch value is invalid", ex);
            }
            return new CurrentWeather(this);
        }

    }

}
