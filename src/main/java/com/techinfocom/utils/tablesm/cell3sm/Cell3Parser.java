package com.techinfocom.utils.tablesm.cell3sm;

import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public interface Cell3Parser {

    void processString(String string, TextFormat textFormat);

    void processCommand(RtfCommand rtfCommand, TextFormat textFormat);

    void analyseFormat(String string, TextFormat textFormat);
}
