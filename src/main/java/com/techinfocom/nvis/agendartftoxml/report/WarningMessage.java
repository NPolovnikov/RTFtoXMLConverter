package com.techinfocom.nvis.agendartftoxml.report;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class WarningMessage extends ReportMessage {
    public WarningMessage(String message, String estimated) {
        super(ReportMessageType.WARNING, message);
        this.estimated = estimated;
    }
    private final String estimated; //примерное положение заданное через текст документа.
}
