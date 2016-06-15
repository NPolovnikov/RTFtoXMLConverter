package com.techinfocom.utils.docsm;

import com.techinfocom.utils.model.FormatedChar;
import com.techinfocom.utils.model.RtfCommand;
import com.techinfocom.utils.model.TextFormat;
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
