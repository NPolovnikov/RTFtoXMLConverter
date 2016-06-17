package com.techinfocom.nvis.agendartftoxml.report;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class ErrorMessage extends ReportMessage {
    public ErrorMessage(String message, String estimated) {
        super(ReportMessageType.ERROR, message);
        this.estimated = estimated;
    }

    public ErrorMessage(String message) {
        super(ReportMessageType.ERROR, message);
        estimated = null;
    }

    private final String estimated; //примерное положение заданное через текст документа.

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(reportMessageType.toString())
                .append(": ")
                .append(message);
        if (estimated != null){
            sb.append("; Примерное положение: ").append(estimated);
        }
        return sb.toString();
    }
}
