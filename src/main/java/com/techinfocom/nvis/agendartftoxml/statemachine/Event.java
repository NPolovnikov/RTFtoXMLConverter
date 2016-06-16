package com.techinfocom.nvis.agendartftoxml.statemachine;

/**
 * Created by volkov_kv on 24.05.2016.
 */
public class Event {
    private final String name;

    public Event(String name){
        if (name == null) throw new IllegalArgumentException();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
