package com.techinfocom.utils.model;

import com.techinfocom.utils.GroupState;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.model.agenda.Agenda;
import com.techinfocom.utils.model.agenda.AgendaItem;
import com.techinfocom.utils.model.agenda.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class AgendaBuilder {

    private AgendaItem preparedAgendaItem;
    private AgendaItem currentItem;

    public AgendaBuilder() {
        objectFactory = new ObjectFactory();
    }

    private ObjectFactory objectFactory;
    private Agenda agenda;
    private GroupState groupState;


    public void processString(String string){
        System.err.println("Билдеру дали строку=" + string + ". Контекст = " + groupState.printCurrentLevel());
        //копим, пока не кончится row.
        //cellString.add(new FormatedString(groupState.getCurrentLevel()))
    }

    public void processCommand(RtfCommand rtfCommand){

    }


    public void createAgenda() {
        agenda = objectFactory.createAgenda();
    }

    // TODO: 07.06.2016 удалить currentItem, если строка таблицы оказалась битой?

}

