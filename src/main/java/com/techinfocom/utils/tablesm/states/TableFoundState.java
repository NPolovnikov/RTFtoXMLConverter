package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class TableFoundState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event ROW_FOUND = new Event("ROW_FOUND");
    AgendaBuilder agendaBuilder;

    public TableFoundState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string) {
        //ignore any strings
    }

    @Override
    public void processingDocEvent(DocEvent docEvent) {
        switch (docEvent) {
            case ROW_BEGIN:
                //agendaBuilder.createAgenda();
                System.err.println("Состояние TableFoundState, поймано событие ROW_BEGIN");
                eventSink.castEvent(ROW_FOUND);
                break;
        }
    }
}
