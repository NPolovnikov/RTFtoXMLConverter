
package com.techinfocom.utils.agenda;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="item" type="{http://techinfocom.com/nvis/agenda}AgendaItem"/>
 *         &lt;element name="block" type="{http://techinfocom.com/nvis/agenda}AgendaBlockItem"/>
 *       &lt;/choice>
 *       &lt;attribute name="meetingDate" type="{http://www.w3.org/2001/XMLSchema}date" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "itemOrBlock"
})
@XmlRootElement(name = "agenda", namespace = "http://techinfocom.com/nvis/agenda")
public class Agenda {

    @XmlElements({
        @XmlElement(name = "item", namespace = "http://techinfocom.com/nvis/agenda", type = AgendaItem.class),
        @XmlElement(name = "block", namespace = "http://techinfocom.com/nvis/agenda", type = AgendaBlockItem.class)
    })
    protected List<Object> itemOrBlock;
    @XmlAttribute(name = "meetingDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar meetingDate;

    /**
     * Gets the value of the itemOrBlock property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemOrBlock property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemOrBlock().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgendaItem }
     * {@link AgendaBlockItem }
     * 
     * 
     */
    public List<Object> getItemOrBlock() {
        if (itemOrBlock == null) {
            itemOrBlock = new ArrayList<Object>();
        }
        return this.itemOrBlock;
    }

    /**
     * Gets the value of the meetingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMeetingDate() {
        return meetingDate;
    }

    /**
     * Sets the value of the meetingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMeetingDate(XMLGregorianCalendar value) {
        this.meetingDate = value;
    }

}
