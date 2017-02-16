package bilokhado.tomcatjerseyrest.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Wrapper class to return list of country objects as XML or JSON
 */
@XmlRootElement(name = "CountryList")
@XmlAccessorType(XmlAccessType.FIELD)
public class CountryList implements Serializable {

    @XmlElement(name = "Country")
    private List<Country> countryList;

    public CountryList() {}

    public CountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

}
