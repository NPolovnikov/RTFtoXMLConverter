package com.techinfocom.nvis.agendartftoxml.utils.docsm.states;

import com.techinfocom.nvis.agendartftoxml.utils.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.utils.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.utils.model.TextFormat;
import com.techinfocom.nvis.agendartftoxml.utils.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.utils.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.utils.statemachine.StateBase;
import org.slf4j.Logger;

import java.util.Queue;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public class DataSearching<AI extends DocParser> extends StateBase<AI> implements DocParser {
    private static final Logger LOGGER = DocParser.LOGGER;
    private final Queue<FormatedChar> dataBuffer;

    public DataSearching(AI automation, EventSink eventSink, Queue dataBuffer) {
        super(automation, eventSink);
        this.dataBuffer = dataBuffer;
    }

    @Override
    public void processChar(FormatedChar fc) {

    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case trowd:
                //dataBuffer.add()
                break;
        }

    }
}
