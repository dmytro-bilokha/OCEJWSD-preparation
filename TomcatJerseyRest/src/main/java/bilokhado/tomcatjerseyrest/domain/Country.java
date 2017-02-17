package bilokhado.tomcatjerseyrest.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class represents the country object
 */
@XmlRootElement(name = "Country")
@XmlAccessorType(XmlAccessType.FIELD)
public class Country implements Serializable, Comparable<Country> {
    @XmlElement(name = "Code")
    private String code;
    @XmlElement(name = "Name")
    private String name;

    public Country() {
        //No-args constructor to make marshaller/unmarsharller happy
    }

    @Override
    public int compareTo(Country o) {
        if (code != null)
            return code.compareTo(o.code);
        if (o.code != null)
            return -1;
        else
            return 0;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Country country = (Country) o;
        return Objects.equals(code, country.code) &&
                Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Country{");
        sb.append("code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
