package com.techinfocom.nvis.agendartftoxml.report;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class InfoMessage extends ReportMessage {
    public InfoMessage(String message) {
        super(ReportMessageType.INFO, message);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(reportMessageType.toString())
                .append(": ")
                .append(message);
        return sb.toString();
    }
}
