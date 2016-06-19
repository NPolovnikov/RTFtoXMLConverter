package com.techinfocom.nvis.agendartftoxml.model.validation;

import com.techinfocom.nvis.agendartftoxml.report.ReportMessage;

/**
 * Created by volkov_kv on 19.06.2016.
 */
public class ValidateResponse<T> {
    T value;
    ReportMessage message;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public ReportMessage getMessage() {
        return message;
    }

    public void setMessage(ReportMessage message) {
        this.message = message;
    }
}
