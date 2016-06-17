package com.techinfocom.nvis.agendartftoxml.docsm.states;

import com.techinfocom.nvis.agendartftoxml.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParserState;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.RtfWord;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import org.slf4j.Logger;

import java.util.Queue;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public class PassOver<AI extends DocParser> extends StateBase<AI> implements DocParserState {
    private final Queue<RtfWord> dataBuffer;
    private final AgendaBuilder agendaBuilder;
    private final TableParser tableParser;

    public PassOver(AI automation, EventSink eventSink, Queue<RtfWord> dataBuffer,
                    AgendaBuilder agendaBuilder, TableParser tableParser) {
        super(automation, eventSink);
        this.dataBuffer = dataBuffer;
        this.agendaBuilder = agendaBuilder;
        this.tableParser = tableParser;
    }

    @Override
    public void analyseWord(RtfWord rtfWord) {

    }

    @Override
    public void processWord(RtfWord rtfWord) {

    }

    @Override
    public void processDocumentEnd() {
        //Идеальный случай
    }

}
