package com.techinfocom.utils.commandconsumer;

import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;

/**
 * Created by volkov_kv on 25.05.2016.
 */
public class Table<AI> extends StateBase<AI> {
    public Table(AI automation, EventSink eventSink) {
        super(automation, eventSink);
    }

}
