package com.techinfocom.nvis.agendartftoxml.model.validation;

import com.techinfocom.nvis.agendartftoxml.report.ReportMessage;

import java.util.List;

/**
 * Created by volkov_kv on 27.06.2016.
 */
public class ValidationCountResponse<T> {
    List<T> anyList;
    ReportMessage message;

    public List<T> getAnyList() {
        return anyList;
    }

    public void setAnyList(List<T> anyList) {
        this.anyList = anyList;
    }

    public ReportMessage getMessage() {
        return message;
    }

    public void setMessage(ReportMessage message) {
        this.message = message;
    }
}
