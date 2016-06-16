package com.techinfocom.nvis.agendartftoxml.tablesm;

import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.RtfWord;
import com.techinfocom.nvis.agendartftoxml.model.TextFormat;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public interface TableParser {

    void processWord(RtfWord rtfWord);

    void exit();

}
