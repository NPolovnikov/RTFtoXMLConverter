package com.techinfocom.nvis.agendartftoxml.report;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class ErrorMessage extends ReportMessage {
    public ErrorMessage(String message) {
        super(ReportMessageType.ERROR, message);
    }
}
