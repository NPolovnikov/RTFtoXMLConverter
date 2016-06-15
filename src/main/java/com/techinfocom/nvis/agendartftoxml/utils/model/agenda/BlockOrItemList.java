
package com.techinfocom.nvis.agendartftoxml.utils.model.agenda;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BlockOrItemList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BlockOrItemList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="item" type="{http://techinfocom.com/nvis/agenda}AgendaItem"/>
 *         &lt;element name="block" type="{http://techinfocom.com/nvis/agenda}AgendaBlockItem"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BlockOrItemList", namespace = "http://techinfocom.com/nvis/agenda", propOrder = {
    "itemOrBlock"
})
public class BlockOrItemList {

    @XmlElements({
        @XmlElement(name = "item", namespace = "http://techinfocom.com/nvis/agenda", type = AgendaItem.class),
        @XmlElement(name = "block", namespace = "http://techinfocom.com/nvis/agenda", type = AgendaBlockItem.class)
    })
    protected List<Object> itemOrBlock;

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

}
