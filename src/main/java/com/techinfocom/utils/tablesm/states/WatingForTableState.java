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
public class WatingForTableState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event TABLE_FOUND = new Event("TABLE_FOUND");
    AgendaBuilder agendaBuilder;

    public WatingForTableState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string) {
        //ignore any strings
    }

    @Override
    public void processingDocEvent(DocEvent docEvent) {
        agendaBuilder.createAgenda();
        if (docEvent == DocEvent.TABLE_BEGIN){
            eventSink.castEvent(TABLE_FOUND);
        }
    }
}
