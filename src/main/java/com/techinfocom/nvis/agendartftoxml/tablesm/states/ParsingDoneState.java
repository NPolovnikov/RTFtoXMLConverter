package com.techinfocom.nvis.agendartftoxml.tablesm.states;

import com.techinfocom.nvis.agendartftoxml.model.*;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class ParsingDoneState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    private static final String STATE_NAME = ParsingDoneState.class.getSimpleName().toUpperCase();

    private final AgendaBuilder agendaBuilder;
    private int tableCount = 0;

    public ParsingDoneState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }


    @Override
    public void processWord(RtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case COMMAND:
                break;
            case CHAR:
                break;
        }
    }

    @Override
    public void exit() {

    }
}
