package ir.payeshgaran.person.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@Data
@XmlRootElement(name = "query")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Query {
    private String key;
    private String value;
}

