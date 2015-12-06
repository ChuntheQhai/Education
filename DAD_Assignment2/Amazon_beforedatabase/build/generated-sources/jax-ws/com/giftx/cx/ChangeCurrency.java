
package com.giftx.cx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for changeCurrency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="changeCurrency">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="currencyDollar" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="currencyType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "changeCurrency", propOrder = {
    "currencyDollar",
    "currencyType"
})
public class ChangeCurrency {

    protected double currencyDollar;
    protected String currencyType;

    /**
     * Gets the value of the currencyDollar property.
     * 
     */
    public double getCurrencyDollar() {
        return currencyDollar;
    }

    /**
     * Sets the value of the currencyDollar property.
     * 
     */
    public void setCurrencyDollar(double value) {
        this.currencyDollar = value;
    }

    /**
     * Gets the value of the currencyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * Sets the value of the currencyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyType(String value) {
        this.currencyType = value;
    }

}
