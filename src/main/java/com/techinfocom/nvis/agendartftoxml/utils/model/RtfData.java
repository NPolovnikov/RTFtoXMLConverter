package com.techinfocom.nvis.agendartftoxml.utils.model;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public abstract class RtfData {
    public abstract RtfDataType getDatatype();

    public enum RtfDataType{
        command,
        string
    }
}
