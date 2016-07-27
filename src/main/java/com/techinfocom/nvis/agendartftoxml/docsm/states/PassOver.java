package com.techinfocom.nvis.agendartftoxml.docsm.states;

import com.techinfocom.nvis.agendartftoxml.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParserState;
import com.techinfocom.nvis.agendartftoxml.model.AbstractRtfWord;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public class PassOver<AI extends DocParser> extends StateBase<AI> implements DocParserState {

    public PassOver(AI automation, EventSink eventSink) {
        super(automation, eventSink);
    }

    @Override
    public void analyseWord(AbstractRtfWord rtfWord) {

    }

    @Override
    public void processWord(AbstractRtfWord rtfWord) {

    }

    @Override
    public void processDocumentEnd() {
        //Идеальный случай
    }

}
