package com.techinfocom.utils.commandconsumer;

import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;

/**
 * Created by volkov_kv on 25.05.2016.
 */
public class WaitingForStructure<AI> extends StateBase<AI> {
    public WaitingForStructure(AI automation, EventSink eventSink) {
        super(automation, eventSink);
    }

}
