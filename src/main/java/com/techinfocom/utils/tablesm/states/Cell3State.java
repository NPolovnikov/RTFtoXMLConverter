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
public class Cell3State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event NEXT_CELL = new Event("NEXT_CELL");
    public static final Event ROW_END = new Event("ROW_END");
    AgendaBuilder agendaBuilder;

    public Cell3State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
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
            case CELL_END:
                //agendaBuilder.createAgenda();
                System.err.println("Состояние Cell1State, поймано событие CELL_END");
                eventSink.castEvent(NEXT_CELL);
                break;
            case ROW_END:
                System.err.println("Состояние Cell1State, поймано событие ROW_END");
                eventSink.castEvent(ROW_END);
                break;
        }
    }
}
