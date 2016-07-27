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
    public ParsingDoneState(AI automation, EventSink eventSink) {
        super(automation, eventSink);
    }


    @Override
    public void processWord(AbstractRtfWord rtfWord) {
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
