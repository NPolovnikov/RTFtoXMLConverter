package com.techinfocom.utils.docsm.states;

import com.techinfocom.utils.model.FormatedChar;
import com.techinfocom.utils.model.RtfCommand;
import com.techinfocom.utils.model.TextFormat;
import com.techinfocom.utils.docsm.DocParser;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public class AgendaSearching<AI extends DocParser> extends StateBase<AI> implements DocParser {
    private static final Logger LOGGER = DocParser.LOGGER;

    public AgendaSearching(AI automation, EventSink eventSink) {
        super(automation, eventSink);
    }

    @Override
    public void processChar(FormatedChar fc) {

    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {

    }
}
