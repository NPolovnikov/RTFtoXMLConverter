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

    private AgendaItem currentItem;

    public AgendaBuilder() {
        objectFactory = new ObjectFactory();
    }

    private ObjectFactory objectFactory;
    private Agenda agenda;


    public void processString(String string) {
        System.err.println("Билдеру дали строку=" + string);
    }


    public void createAgenda() {
        if (agenda == null) {
            agenda = objectFactory.createAgenda();
        } else {
            throw new RuntimeException("agenda already exists"); // TODO: 08.06.2016 заменить исключение
        }
    }

    public void newAgendaItem() {
        currentItem = objectFactory.createAgendaItem();
    }

    public AgendaItem getCurrentItem() {
        return currentItem;
    }

    public void mergeItem(){
        agenda.getItemOrBlock().add(currentItem);
    }

// TODO: 07.06.2016 удалить currentItem, если строка таблицы оказалась битой?

}

