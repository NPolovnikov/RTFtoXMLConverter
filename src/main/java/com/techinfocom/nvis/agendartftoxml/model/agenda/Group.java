
package com.techinfocom.nvis.agendartftoxml.model.agenda;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Group complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Group">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="groupName" type="{http://techinfocom.com/nvis/agenda}NonEmptyString"/>
 *         &lt;element name="speakers">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="speaker" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;all>
 *                             &lt;element name="post" type="{http://techinfocom.com/nvis/agenda}NonEmptyString"/>
 *                             &lt;element name="name" type="{http://techinfocom.com/nvis/agenda}NonEmptyString" minOccurs="0"/>
 *                           &lt;/all>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Group", propOrder = {

})
public class Group {

    @XmlElement(required = true)
    protected String groupName;
    @XmlElement(required = true)
    protected Group.Speakers speakers;

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the speakers property.
     * 
     * @return
     *     possible object is
     *     {@link Group.Speakers }
     *     
     */
    public Group.Speakers getSpeakers() {
        return speakers;
    }

    /**
     * Sets the value of the speakers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Group.Speakers }
     *     
     */
    public void setSpeakers(Group.Speakers value) {
        this.speakers = value;
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
     *         &lt;element name="speaker" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;all>
     *                   &lt;element name="post" type="{http://techinfocom.com/nvis/agenda}NonEmptyString"/>
     *                   &lt;element name="name" type="{http://techinfocom.com/nvis/agenda}NonEmptyString" minOccurs="0"/>
     *                 &lt;/all>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "speaker"
    })
    public static class Speakers {

        @XmlElement(required = true)
        protected List<Group.Speakers.Speaker> speaker;

        /**
         * Gets the value of the speaker property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the speaker property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSpeaker().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Group.Speakers.Speaker }
         * 
         * 
         */
        public List<Group.Speakers.Speaker> getSpeaker() {
            if (speaker == null) {
                speaker = new ArrayList<Group.Speakers.Speaker>();
            }
            return this.speaker;
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
         *         &lt;element name="post" type="{http://techinfocom.com/nvis/agenda}NonEmptyString"/>
         *         &lt;element name="name" type="{http://techinfocom.com/nvis/agenda}NonEmptyString" minOccurs="0"/>
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
        public static class Speaker {

            @XmlElement(required = true)
            protected String post;
            protected String name;

            /**
             * Gets the value of the post property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPost() {
                return post;
            }

            /**
             * Sets the value of the post property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPost(String value) {
                this.post = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

        }

    }

}
