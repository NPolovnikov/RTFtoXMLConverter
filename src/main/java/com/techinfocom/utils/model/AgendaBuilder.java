package com.techinfocom.utils.model;

import com.techinfocom.utils.FormatedString;
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
    private List<FormatedString> cellString;

    public AgendaBuilder() {
        objectFactory = new ObjectFactory();
        cellString = new ArrayList<>();
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

    /**
     * Обрабатывает события парсинга структуры RTF для генерации структуры XML
     * @param tableStructureEvent
     */
    public void processEvent(TableStructureEvent tableStructureEvent){
        switch (tableStructureEvent){
            case TABLE_BEGIN:
                agenda = objectFactory.createAgenda();
                break;
            case ROW_BEGIN:
                currentItem = objectFactory.createAgendaItem();
        }
    }

    public void createAgenda() {
        agenda = objectFactory.createAgenda();
    }

    // TODO: 07.06.2016 удалить currentItem, если строка таблицы оказалась битой?

}

