package com.techinfocom.nvis.agendartftoxml.docsm.states;

import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.TextFormat;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public class RowBuffering<AI extends DocParser> extends StateBase<AI> implements DocParser {
    private static final Logger LOGGER = DocParser.LOGGER;

    public RowBuffering(AI automation, EventSink eventSink) {
        super(automation, eventSink);
    }

    @Override
    public void processChar(FormatedChar fc) {

    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case trowd:

                break;
        }

    }
}
