package com.techinfocom.nvis.agendartftoxml.report;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class WarningMessage extends AbstractReportMessage {
    public WarningMessage(String message, String estimated) {
        super(ReportMessageType.WARNING, message);
        this.estimated = estimated;
    }

    public WarningMessage(final String message) {
        super(ReportMessageType.WARNING, message);
        this.estimated = null;
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
