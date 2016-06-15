package com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm;

import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public interface Cell3Parser {

    void processChar(FormatedChar fc);

    //void processCommand(RtfCommand rtfCommand, TextFormat textFormat);

    void analyseFormat(FormatedChar fc);

    void exit();
}
