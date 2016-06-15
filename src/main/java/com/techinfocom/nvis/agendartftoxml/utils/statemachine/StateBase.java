package com.techinfocom.nvis.agendartftoxml.utils.statemachine;

/**
 * Created by volkov_kv on 24.05.2016.
 * базовый класс для состояний
 */
public abstract class StateBase<AI> {
    protected final AI automation;
    protected final EventSink eventSink;

    public StateBase(AI automation, EventSink eventSink) {
        if(automation == null || eventSink == null){
            throw new IllegalArgumentException();
        }
        this.automation = automation;
        this.eventSink = eventSink;
    }

    protected void castEvent(Event event){
        eventSink.castEvent(event);
    }

}
