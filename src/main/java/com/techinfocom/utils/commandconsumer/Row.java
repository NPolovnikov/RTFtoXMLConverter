package com.techinfocom.utils.commandconsumer;

import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;

/**
 * Created by volkov_kv on 25.05.2016.
 */
public class Row<AI> extends StateBase<AI> {
    public Row(AI automation, EventSink eventSink) {
        super(automation, eventSink);
    }

}
