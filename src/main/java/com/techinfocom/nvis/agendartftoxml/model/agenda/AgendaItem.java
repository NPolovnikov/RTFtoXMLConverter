
package com.techinfocom.nvis.agendartftoxml.model.agenda;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AgendaItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AgendaItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://techinfocom.com/nvis/agenda-internal}UUID"/>
 *         &lt;element name="number" type="{http://techinfocom.com/nvis/agenda-internal}AgendaItemNumber" minOccurs="0"/>
 *         &lt;element name="info" type="{http://techinfocom.com/nvis/agenda-internal}AgendaItemInfo" minOccurs="0"/>
 *         &lt;element name="addon" type="{http://techinfocom.com/nvis/agenda-internal}AgendaItemAddon" minOccurs="0"/>
 *         &lt;element name="rn" type="{http://techinfocom.com/nvis/agenda-internal}AgendaItemRn" minOccurs="0"/>
 *         &lt;element name="text" type="{http://techinfocom.com/nvis/agenda-internal}AgendaItemText" minOccurs="0"/>
 *         &lt;element name="notes" type="{http://techinfocom.com/nvis/agenda-internal}notesList" minOccurs="0"/>
 *         &lt;element name="speakerGroups" type="{http://techinfocom.com/nvis/agenda-internal}Speakers" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="manualdocs">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="document" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;all>
 *                               &lt;element name="displayTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;element name="searchCriteria" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;/all>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attribute name="autoupdate" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="autodocs">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="loadType" use="required">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="all"/>
 *                         &lt;enumeration value="auto"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/attribute>
 *                   &lt;attribute name="autoupdate" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AgendaItem", propOrder = {
    "id",
    "number",
    "info",
    "addon",
    "rn",
    "text",
    "notes",
    "speakerGroups",
    "manualdocs",
    "autodocs"
})
public class AgendaItem {

    @XmlElement(required = true)
    protected String id;
    protected String number;
    protected String info;
    protected String addon;
    protected String rn;
    protected String text;
    protected NotesList notes;
    protected Speakers speakerGroups;
    protected AgendaItem.Manualdocs manualdocs;
    protected AgendaItem.Autodocs autodocs;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

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
     * Gets the value of the addon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddon() {
        return addon;
    }

    /**
     * Sets the value of the addon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddon(String value) {
        this.addon = value;
    }

    /**
     * Gets the value of the rn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRn() {
        return rn;
    }

    /**
     * Sets the value of the rn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRn(String value) {
        this.rn = value;
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
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link NotesList }
     *     
     */
    public NotesList getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotesList }
     *     
     */
    public void setNotes(NotesList value) {
        this.notes = value;
    }

    /**
     * Gets the value of the speakerGroups property.
     * 
     * @return
     *     possible object is
     *     {@link Speakers }
     *     
     */
    public Speakers getSpeakerGroups() {
        return speakerGroups;
    }

    /**
     * Sets the value of the speakerGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link Speakers }
     *     
     */
    public void setSpeakerGroups(Speakers value) {
        this.speakerGroups = value;
    }

    /**
     * Gets the value of the manualdocs property.
     * 
     * @return
     *     possible object is
     *     {@link AgendaItem.Manualdocs }
     *     
     */
    public AgendaItem.Manualdocs getManualdocs() {
        return manualdocs;
    }

    /**
     * Sets the value of the manualdocs property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgendaItem.Manualdocs }
     *     
     */
    public void setManualdocs(AgendaItem.Manualdocs value) {
        this.manualdocs = value;
    }

    /**
     * Gets the value of the autodocs property.
     * 
     * @return
     *     possible object is
     *     {@link AgendaItem.Autodocs }
     *     
     */
    public AgendaItem.Autodocs getAutodocs() {
        return autodocs;
    }

    /**
     * Sets the value of the autodocs property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgendaItem.Autodocs }
     *     
     */
    public void setAutodocs(AgendaItem.Autodocs value) {
        this.autodocs = value;
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
     *       &lt;attribute name="loadType" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="all"/>
     *             &lt;enumeration value="auto"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="autoupdate" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Autodocs {

        @XmlAttribute(name = "loadType", required = true)
        protected String loadType;
        @XmlAttribute(name = "autoupdate")
        protected Boolean autoupdate;

        /**
         * Gets the value of the loadType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLoadType() {
            return loadType;
        }

        /**
         * Sets the value of the loadType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLoadType(String value) {
            this.loadType = value;
        }

        /**
         * Gets the value of the autoupdate property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isAutoupdate() {
            return autoupdate;
        }

        /**
         * Sets the value of the autoupdate property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setAutoupdate(Boolean value) {
            this.autoupdate = value;
        }

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
     *         &lt;element name="document" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;all>
     *                   &lt;element name="displayTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="searchCriteria" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/all>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="autoupdate" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "document"
    })
    public static class Manualdocs {

        @XmlElement(required = true)
        protected List<AgendaItem.Manualdocs.Document> document;
        @XmlAttribute(name = "autoupdate")
        protected Boolean autoupdate;

        /**
         * Gets the value of the document property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the document property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDocument().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AgendaItem.Manualdocs.Document }
         * 
         * 
         */
        public List<AgendaItem.Manualdocs.Document> getDocument() {
            if (document == null) {
                document = new ArrayList<AgendaItem.Manualdocs.Document>();
            }
            return this.document;
        }

        /**
         * Gets the value of the autoupdate property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isAutoupdate() {
            return autoupdate;
        }

        /**
         * Sets the value of the autoupdate property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setAutoupdate(Boolean value) {
            this.autoupdate = value;
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
         *       &lt;all>
         *         &lt;element name="displayTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="searchCriteria" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/all>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {

        })
        public static class Document {

            @XmlElement(required = true)
            protected String displayTitle;
            @XmlElement(required = true)
            protected String searchCriteria;

            /**
             * Gets the value of the displayTitle property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDisplayTitle() {
                return displayTitle;
            }

            /**
             * Sets the value of the displayTitle property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDisplayTitle(String value) {
                this.displayTitle = value;
            }

            /**
             * Gets the value of the searchCriteria property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSearchCriteria() {
                return searchCriteria;
            }

            /**
             * Sets the value of the searchCriteria property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSearchCriteria(String value) {
                this.searchCriteria = value;
            }

        }

    }

}
