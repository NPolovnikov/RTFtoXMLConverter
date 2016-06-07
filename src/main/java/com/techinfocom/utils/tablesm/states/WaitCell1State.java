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
public class WaitCell1State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event IN_CELL = new Event("IN_CELL");
    AgendaBuilder agendaBuilder;

    public WaitCell1State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string) {
        //ignore any stringsq
    }

    @Override
    public void processingDocEvent(DocEvent docEvent) {
        switch (docEvent) {
            case PAR:
                System.err.println("Состояние WaitCell1State, поймано событие PAR");
                eventSink.castEvent(IN_CELL);
                break;
        }
    }
}
