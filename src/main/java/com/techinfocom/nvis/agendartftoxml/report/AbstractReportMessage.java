package com.techinfocom.nvis.agendartftoxml.report;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public abstract class AbstractReportMessage {
    protected final ReportMessageType reportMessageType;
    protected String message;

    public AbstractReportMessage(final ReportMessageType reportMessageType, final String message) {
        this.reportMessageType = reportMessageType;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public ReportMessageType getReportMessageType() {
        return reportMessageType;
    }

    public enum ReportMessageType {
        INFO("INFO"),
        WARNING("WARNING"),
        ERROR("ERROR");

        private final String name;

        ReportMessageType(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
