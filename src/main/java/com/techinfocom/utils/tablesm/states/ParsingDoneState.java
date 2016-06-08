package com.techinfocom.utils.tablesm.states;

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
public class ParsingDoneState<AI extends TableParser> extends StateBase<AI> implements TableParser {


    AgendaBuilder agendaBuilder;
    private int tableCount = 0;

    public ParsingDoneState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        //ignore any strings
    }

    @Override
    public void processCommand(RtfCommand rtfCommand) {

    }
}
