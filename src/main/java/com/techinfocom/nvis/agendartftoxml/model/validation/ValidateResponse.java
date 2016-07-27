package com.techinfocom.nvis.agendartftoxml.model.validation;

import com.techinfocom.nvis.agendartftoxml.report.AbstractReportMessage;

/**
 * Created by volkov_kv on 19.06.2016.
 */
public class ValidateResponse<T> {
    T value;
    AbstractReportMessage message;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public AbstractReportMessage getMessage() {
        return message;
    }

    public void setMessage(AbstractReportMessage message) {
        this.message = message;
    }
}
