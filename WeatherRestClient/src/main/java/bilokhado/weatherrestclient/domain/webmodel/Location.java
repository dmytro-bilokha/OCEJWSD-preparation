package bilokhado.weatherrestclient.domain.webmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Class represents the 'location' XML tag from the Weather REST API
 */
@XmlType(name = "location")
@XmlAccessorType(XmlAccessType.FIELD)
public class Location {

    private String name;
    private String region;
    private String country;
    private double lat;
    private double lon;
    @XmlElement(name = "tz_id")
    private String timeZone;
    @XmlElement(name = "localtime_epoch")
    private long localEpoch;
    @XmlElement(name = "localtime")
    private String localTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public long getLocalEpoch() {
        return localEpoch;
    }

    public void setLocalEpoch(long localEpoch) {
        this.localEpoch = localEpoch;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

}
