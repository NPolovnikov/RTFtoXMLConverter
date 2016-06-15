
package com.techinfocom.nvis.agendartftoxml.utils.model.agenda;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AgendaBlockItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AgendaBlockItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="info" type="{http://techinfocom.com/nvis/agenda}NonEmptyString"/>
 *         &lt;element name="text" type="{http://techinfocom.com/nvis/agenda}NonEmptyString"/>
 *         &lt;element name="children" type="{http://techinfocom.com/nvis/agenda}BlockOrItemList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AgendaBlockItem", namespace = "http://techinfocom.com/nvis/agenda", propOrder = {
    "info",
    "text",
    "children"
})
public class AgendaBlockItem {

    @XmlElement(namespace = "http://techinfocom.com/nvis/agenda", required = true)
    protected String info;
    @XmlElement(namespace = "http://techinfocom.com/nvis/agenda", required = true)
    protected String text;
    @XmlElement(namespace = "http://techinfocom.com/nvis/agenda")
    protected BlockOrItemList children;

    /**
     * Gets the value of the info property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo() {
        return info;
    }

    /**
     * Sets the value of the info property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo(String value) {
        this.info = value;
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
     * Gets the value of the children property.
     * 
     * @return
     *     possible object is
     *     {@link BlockOrItemList }
     *     
     */
    public BlockOrItemList getChildren() {
        return children;
    }

    /**
     * Sets the value of the children property.
     * 
     * @param value
     *     allowed object is
     *     {@link BlockOrItemList }
     *     
     */
    public void setChildren(BlockOrItemList value) {
        this.children = value;
    }

}
