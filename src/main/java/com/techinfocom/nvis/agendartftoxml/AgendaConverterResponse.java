package com.techinfocom.nvis.agendartftoxml;

import com.techinfocom.nvis.agendartftoxml.report.ConversionReport;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class AgendaConverterResponse {
    private final byte[] xmlBytes;
    private final ConversionReport conversionReport;

    public AgendaConverterResponse(byte[] xmlBytes, ConversionReport conversionReport) {
        this.xmlBytes = xmlBytes;
        this.conversionReport = conversionReport;
    }

    public byte[] getXmlBytes() {
        return xmlBytes;
    }

    public ConversionReport getConversionReport() {
        return conversionReport;
    }

}
