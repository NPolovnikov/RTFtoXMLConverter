
package com.techinfocom.nvis.agendartftoxml.model.agenda;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.techinfocom.nvis.agendartftoxml.model.agenda package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.techinfocom.nvis.agendartftoxml.model.agenda
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Group }
     * 
     */
    public Group createGroup() {
        return new Group();
    }

    /**
     * Create an instance of {@link Group.Speakers }
     * 
     */
    public Group.Speakers createGroupSpeakers() {
        return new Group.Speakers();
    }

    /**
     * Create an instance of {@link AgendaItem }
     * 
     */
    public AgendaItem createAgendaItem() {
        return new AgendaItem();
    }

    /**
     * Create an instance of {@link AgendaItem.Manualdocs }
     * 
     */
    public AgendaItem.Manualdocs createAgendaItemManualdocs() {
        return new AgendaItem.Manualdocs();
    }

    /**
     * Create an instance of {@link Agenda }
     * 
     */
    public Agenda createAgenda() {
        return new Agenda();
    }

    /**
     * Create an instance of {@link AgendaBlockItem }
     * 
     */
    public AgendaBlockItem createAgendaBlockItem() {
        return new AgendaBlockItem();
    }

    /**
     * Create an instance of {@link BlockOrItemList }
     * 
     */
    public BlockOrItemList createBlockOrItemList() {
        return new BlockOrItemList();
    }

    /**
     * Create an instance of {@link com.techinfocom.nvis.agendartftoxml.model.agenda.Speakers }
     * 
     */
    public com.techinfocom.nvis.agendartftoxml.model.agenda.Speakers createSpeakers() {
        return new com.techinfocom.nvis.agendartftoxml.model.agenda.Speakers();
    }

    /**
     * Create an instance of {@link NotesList }
     * 
     */
    public NotesList createNotesList() {
        return new NotesList();
    }

    /**
     * Create an instance of {@link Group.Speakers.Speaker }
     * 
     */
    public Group.Speakers.Speaker createGroupSpeakersSpeaker() {
        return new Group.Speakers.Speaker();
    }

    /**
     * Create an instance of {@link AgendaItem.Autodocs }
     * 
     */
    public AgendaItem.Autodocs createAgendaItemAutodocs() {
        return new AgendaItem.Autodocs();
    }

    /**
     * Create an instance of {@link AgendaItem.Manualdocs.Document }
     * 
     */
    public AgendaItem.Manualdocs.Document createAgendaItemManualdocsDocument() {
        return new AgendaItem.Manualdocs.Document();
    }

}
