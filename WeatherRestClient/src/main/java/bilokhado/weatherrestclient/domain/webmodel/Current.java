package bilokhado.weatherrestclient.domain.webmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Class represents the 'current' XML tag from the Weather REST API
 */
@XmlType(name = "current")
@XmlAccessorType(XmlAccessType.FIELD)
public class Current {

    @XmlElement(name = "last_updated_epoch")
    private long lastUpdatedEpoch;
    @XmlElement(name = "last_updated")
    private String lastUpdated;
    @XmlElement(name = "temp_c")
    private double tempC;
    @XmlElement(name = "temp_f")
    private double tempF;
    @XmlElement(name = "is_day")
    private int dayMarker;
    private Condition condition;
    @XmlElement(name = "wind_mph")
    private double windMph;
    @XmlElement(name = "wind_kph")
    private double windKph;
    @XmlElement(name = "wind_degree")
    private int windDegree;
    @XmlElement(name = "wind_dir")
    private String windDir;
    @XmlElement(name = "pressure_mb")
    private int pressureMillibars;
    @XmlElement(name = "pressure_in")
    private double pressureInches;
    @XmlElement(name = "precip_mm")
    private double precipitationMm;
    @XmlElement(name = "precip_in")
    private double precipitationInches;
    private int humidity;
    private int cloud;
    @XmlElement(name = "feelslike_c")
    private double feelsLikeC;
    @XmlElement(name = "feelslike_f")
    private double feelsLikeF;
    @XmlElement(name = "vis_km")
    private double visibilityKm;
    @XmlElement(name = "vis_miles")
    private double visibilityMiles;

    public long getLastUpdatedEpoch() {
        return lastUpdatedEpoch;
    }

    public void setLastUpdatedEpoch(long lastUpdatedEpoch) {
        this.lastUpdatedEpoch = lastUpdatedEpoch;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getTempC() {
        return tempC;
    }

    public void setTempC(double tempC) {
        this.tempC = tempC;
    }

    public double getTempF() {
        return tempF;
    }

    public void setTempF(double tempF) {
        this.tempF = tempF;
    }

    public int getDayMarker() {
        return dayMarker;
    }

    public void setDayMarker(int dayMarker) {
        this.dayMarker = dayMarker;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public double getWindMph() {
        return windMph;
    }

    public void setWindMph(double windMph) {
        this.windMph = windMph;
    }

    public double getWindKph() {
        return windKph;
    }

    public void setWindKph(double windKph) {
        this.windKph = windKph;
    }

    public int getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(int windDegree) {
        this.windDegree = windDegree;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public int getPressureMillibars() {
        return pressureMillibars;
    }

    public void setPressureMillibars(int pressureMillibars) {
        this.pressureMillibars = pressureMillibars;
    }

    public double getPressureInches() {
        return pressureInches;
    }

    public void setPressureInches(double pressureInches) {
        this.pressureInches = pressureInches;
    }

    public double getPrecipitationMm() {
        return precipitationMm;
    }

    public void setPrecipitationMm(double precipitationMm) {
        this.precipitationMm = precipitationMm;
    }

    public double getPrecipitationInches() {
        return precipitationInches;
    }

    public void setPrecipitationInches(double precipitationInches) {
        this.precipitationInches = precipitationInches;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getCloud() {
        return cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    public double getFeelsLikeC() {
        return feelsLikeC;
    }

    public void setFeelsLikeC(double feelsLikeC) {
        this.feelsLikeC = feelsLikeC;
    }

    public double getFeelsLikeF() {
        return feelsLikeF;
    }

    public void setFeelsLikeF(double feelsLikeF) {
        this.feelsLikeF = feelsLikeF;
    }

    public double getVisibilityKm() {
        return visibilityKm;
    }

    public void setVisibilityKm(double visibilityKm) {
        this.visibilityKm = visibilityKm;
    }

    public double getVisibilityMiles() {
        return visibilityMiles;
    }

    public void setVisibilityMiles(double visibilityMiles) {
        this.visibilityMiles = visibilityMiles;
    }
}
