package com.techinfocom.utils.tablesm;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public interface TableParser {

    void processString(String string, TextFormat textFormat);

    void processCommand(RtfCommand rtfCommand, TextFormat textFormat);

    //void processingDocEvent(DocEvent docEvent);

}
