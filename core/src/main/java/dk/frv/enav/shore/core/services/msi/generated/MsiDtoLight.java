
package dk.frv.enav.shore.core.services.msi.generated;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for msiDtoLight complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="msiDtoLight">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="created" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ENCtext" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mainArea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subArea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="updated" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "msiDtoLight", propOrder = {
    "created",
    "enCtext",
    "latitude",
    "text",
    "longitude",
    "mainArea",
    "subArea",
    "updated"
})
public class MsiDtoLight {

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar created;
    @XmlElement(name = "ENCtext")
    protected String enCtext;
    protected String latitude;
    protected String text;
    protected String longitude;
    protected String mainArea;
    protected String subArea;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar updated;

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreated(Calendar value) {
        this.created = value;
    }

    /**
     * Gets the value of the enCtext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENCtext() {
        return enCtext;
    }

    /**
     * Sets the value of the enCtext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENCtext(String value) {
        this.enCtext = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatitude(String value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongitude(String value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the mainArea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMainArea() {
        return mainArea;
    }

    /**
     * Sets the value of the mainArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMainArea(String value) {
        this.mainArea = value;
    }

    /**
     * Gets the value of the subArea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubArea() {
        return subArea;
    }

    /**
     * Sets the value of the subArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubArea(String value) {
        this.subArea = value;
    }

    /**
     * Gets the value of the updated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getUpdated() {
        return updated;
    }

    /**
     * Sets the value of the updated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdated(Calendar value) {
        this.updated = value;
    }

}
