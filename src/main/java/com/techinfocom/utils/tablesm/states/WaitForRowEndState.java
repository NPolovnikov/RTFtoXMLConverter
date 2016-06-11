package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.FormatedChar;
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
public class WaitForRowEndState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event ROW_END = new Event("ROW_END");
    AgendaBuilder agendaBuilder;

    public WaitForRowEndState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        //ignore any stringsq
    }



    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()){
            case row:
                System.err.println("В состоянии WaitForRowEnd поймали row");
                agendaBuilder.mergeItem();
                eventSink.castEvent(ROW_END);
                break;
        }
    }
}
