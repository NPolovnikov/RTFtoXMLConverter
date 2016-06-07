package com.techinfocom.utils.tablesm;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.statemachine.AutomationBase;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class TableParserImpl extends AutomationBase<TableParser> implements TableParser{

    @Override
    public void processString(String string) {
        System.err.println("PROCESSINGsTRING");
    }

    @Override
    public void processingDocEvent(DocEvent event) {
        System.err.println("PROCESSINGdocEVENT");
    }
}
