package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell2State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event NEXT_CELL = new Event("NEXT_CELL");
    AgendaBuilder agendaBuilder;

    public Cell2State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        //ignore any stringsq
    }

//    @Override
//    public void processingDocEvent(DocEvent docEvent) {
//        switch (docEvent) {
//            case CELL_END:
//                //agendaBuilder.createAgenda();
//                System.err.println("Состояние Cell1State, поймано событие CELL_END");
//                eventSink.castEvent(NEXT_CELL);
//                break;
//        }
//    }

    @Override
    public void processCommand(RtfCommand rtfCommand) {
        switch (rtfCommand.getCommand()) {
            case cell:
                System.err.println("Состояние Cell2State, поймано событие cell");
                eventSink.castEvent(NEXT_CELL);
                break;
        }
    }

}
