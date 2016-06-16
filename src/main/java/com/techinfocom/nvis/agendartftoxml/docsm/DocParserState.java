package com.techinfocom.nvis.agendartftoxml.docsm;

import com.techinfocom.nvis.agendartftoxml.model.RtfWord;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public interface DocParserState extends DocParser {

    void analyseWord(RtfWord rtfWord);

}
