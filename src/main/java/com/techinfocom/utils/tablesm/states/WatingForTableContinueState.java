package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class WatingForTableContinueState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event NEXT_ROW = new Event("NEXT_ROW");
    public static final Event TABLE_END = new Event("TABLE_END");
    AgendaBuilder agendaBuilder;

    public WatingForTableContinueState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string) {
        //ignore any strings
    }
    // TODO: 07.06.2016 вот тут надо ожидать таблицы №2

//    @Override
//    public void processingDocEvent(DocEvent docEvent) {
//        switch (docEvent) {
//            case TABLE_BEGIN:
//                agendaBuilder.createAgenda();
//                System.err.println("Состояние WatingForTableState, поймано событие TABLE_BEGIN");
//                eventSink.castEvent(TABLE_FOUND);
//                break;
//        }
//
//    }


    @Override
    public void processCommand(RtfCommand rtfCommand) {
        switch (rtfCommand.getCommand()){
            case trowd:
                System.err.println("Состояние WFTContinue.Поймали trowd");
                eventSink.castEvent(NEXT_ROW);
                break;
            case par:
                System.err.println("Состояние WFTContinue.Поймали par");
                eventSink.castEvent(TABLE_END);
                break;
        }
    }
}
