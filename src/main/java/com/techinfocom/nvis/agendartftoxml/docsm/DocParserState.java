package com.techinfocom.nvis.agendartftoxml.docsm;

import com.techinfocom.nvis.agendartftoxml.model.AbstractRtfWord;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public interface DocParserState extends DocParser {

    void analyseWord(AbstractRtfWord rtfWord);

}
