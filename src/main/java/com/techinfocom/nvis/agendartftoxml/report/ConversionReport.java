package com.techinfocom.nvis.agendartftoxml.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class ConversionReport {
    private List<ReportMessage> messages;

    public ConversionReport() {
        this.messages = new ArrayList<>();
    }

    public void collectMessage(ReportMessage message){
        messages.add(message);
    }

    public List<ReportMessage> getMessages() {
        return messages;
    }
}
