package com.techinfocom.nvis.agendartftoxml.report;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public abstract class ReportMessage {
    protected final ReportMessageType reportMessageType;
    final String message;

    public ReportMessage(ReportMessageType reportMessageType, String message) {
        this.reportMessageType = reportMessageType;
        this.message = message;
    }

    public ReportMessageType getReportMessageType() {
        return reportMessageType;
    }

    public enum ReportMessageType {
        INFO("INFO"),
        WARNING("WARNING"),
        ERROR("ERROR");

        private String name;

        ReportMessageType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
