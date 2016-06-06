package com.techinfocom.utils.commandconsumer;

import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;

/**
 * Created by volkov_kv on 25.05.2016.
 */
public class Group<AI> extends StateBase<AI> {
    public Group(AI automation, EventSink eventSink) {
        super(automation, eventSink);
    }

}
