package com.techinfocom.utils.statemachine;

/**
 * Created by volkov_kv on 24.05.2016.
 */
public interface EventSink {
    void castEvent(Event event);
}
