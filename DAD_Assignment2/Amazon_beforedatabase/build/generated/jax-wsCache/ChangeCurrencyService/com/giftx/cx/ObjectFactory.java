
package com.giftx.cx;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.giftx.cx package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ChangeCurrencyResponse_QNAME = new QName("http://cx.giftx.com/", "changeCurrencyResponse");
    private final static QName _ChangeCurrency_QNAME = new QName("http://cx.giftx.com/", "changeCurrency");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.giftx.cx
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ChangeCurrencyResponse }
     * 
     */
    public ChangeCurrencyResponse createChangeCurrencyResponse() {
        return new ChangeCurrencyResponse();
    }

    /**
     * Create an instance of {@link ChangeCurrency }
     * 
     */
    public ChangeCurrency createChangeCurrency() {
        return new ChangeCurrency();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeCurrencyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cx.giftx.com/", name = "changeCurrencyResponse")
    public JAXBElement<ChangeCurrencyResponse> createChangeCurrencyResponse(ChangeCurrencyResponse value) {
        return new JAXBElement<ChangeCurrencyResponse>(_ChangeCurrencyResponse_QNAME, ChangeCurrencyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeCurrency }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cx.giftx.com/", name = "changeCurrency")
    public JAXBElement<ChangeCurrency> createChangeCurrency(ChangeCurrency value) {
        return new JAXBElement<ChangeCurrency>(_ChangeCurrency_QNAME, ChangeCurrency.class, null, value);
    }

}
