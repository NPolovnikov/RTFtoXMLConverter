package com.techinfocom.nvis.agendartftoxml.model;

import com.techinfocom.nvis.agendartftoxml.model.agenda.AgendaItem;
import com.techinfocom.nvis.agendartftoxml.report.ConversionReport;

import java.util.function.Consumer;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class AgendaValidator {


    public void validate(AgendaItem agendaItem, ConversionReport conversionReport, Consumer<AgendaItem> consumer) {
        consumer.accept(agendaItem);
    }
}
