
package dk.frv.enav.shore.core.services.msi.generated;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for msiDto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="msiDto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="areaEnglish" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="created" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="deleted" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="encText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="locationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="messageId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="navWarning" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="navtex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="navtexNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organisation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="points" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="point" type="{http://message.webservice.core.msiedit.frv.dk/}pointDto" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subarea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="updated" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="validTo" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "msiDto", propOrder = {
    "areaEnglish",
    "created",
    "deleted",
    "encText",
    "id",
    "locationType",
    "messageId",
    "navWarning",
    "navtex",
    "navtexNo",
    "organisation",
    "points",
    "priority",
    "subarea",
    "updated",
    "username",
    "validFrom",
    "validTo",
    "version"
})
public class MsiDto {

    protected String areaEnglish;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar created;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar deleted;
    protected String encText;
    protected Integer id;
    protected String locationType;
    protected Integer messageId;
    protected String navWarning;
    protected String navtex;
    protected String navtexNo;
    protected String organisation;
    protected MsiDto.Points points;
    protected String priority;
    protected String subarea;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar updated;
    protected String username;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar validFrom;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar validTo;
    protected Integer version;

    /**
     * Gets the value of the areaEnglish property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAreaEnglish() {
        return areaEnglish;
    }

    /**
     * Sets the value of the areaEnglish property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAreaEnglish(String value) {
        this.areaEnglish = value;
    }

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
     * Gets the value of the deleted property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getDeleted() {
        return deleted;
    }

    /**
     * Sets the value of the deleted property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeleted(Calendar value) {
        this.deleted = value;
    }

    /**
     * Gets the value of the encText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncText() {
        return encText;
    }

    /**
     * Sets the value of the encText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncText(String value) {
        this.encText = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the locationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * Sets the value of the locationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationType(String value) {
        this.locationType = value;
    }

    /**
     * Gets the value of the messageId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMessageId(Integer value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the navWarning property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNavWarning() {
        return navWarning;
    }

    /**
     * Sets the value of the navWarning property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNavWarning(String value) {
        this.navWarning = value;
    }

    /**
     * Gets the value of the navtex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNavtex() {
        return navtex;
    }

    /**
     * Sets the value of the navtex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNavtex(String value) {
        this.navtex = value;
    }

    /**
     * Gets the value of the navtexNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNavtexNo() {
        return navtexNo;
    }

    /**
     * Sets the value of the navtexNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNavtexNo(String value) {
        this.navtexNo = value;
    }

    /**
     * Gets the value of the organisation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * Sets the value of the organisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganisation(String value) {
        this.organisation = value;
    }

    /**
     * Gets the value of the points property.
     * 
     * @return
     *     possible object is
     *     {@link MsiDto.Points }
     *     
     */
    public MsiDto.Points getPoints() {
        return points;
    }

    /**
     * Sets the value of the points property.
     * 
     * @param value
     *     allowed object is
     *     {@link MsiDto.Points }
     *     
     */
    public void setPoints(MsiDto.Points value) {
        this.points = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the subarea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubarea() {
        return subarea;
    }

    /**
     * Sets the value of the subarea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubarea(String value) {
        this.subarea = value;
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

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the validFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the value of the validFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidFrom(Calendar value) {
        this.validFrom = value;
    }

    /**
     * Gets the value of the validTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getValidTo() {
        return validTo;
    }

    /**
     * Sets the value of the validTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidTo(Calendar value) {
        this.validTo = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVersion(Integer value) {
        this.version = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="point" type="{http://message.webservice.core.msiedit.frv.dk/}pointDto" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "point"
    })
    public static class Points {

        protected List<PointDto> point;

        /**
         * Gets the value of the point property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the point property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPoint().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PointDto }
         * 
         * 
         */
        public List<PointDto> getPoint() {
            if (point == null) {
                point = new ArrayList<PointDto>();
            }
            return this.point;
        }

    }

}
