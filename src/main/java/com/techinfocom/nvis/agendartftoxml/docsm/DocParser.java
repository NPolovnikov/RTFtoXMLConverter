package com.techinfocom.nvis.agendartftoxml.docsm;

import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.TextFormat;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public interface DocParser {
    Logger LOGGER = LoggerFactory.getLogger(DocParser.class);

    void processChar(FormatedChar fc);

    void processCommand(RtfCommand rtfCommand, TextFormat textFormat);

}
