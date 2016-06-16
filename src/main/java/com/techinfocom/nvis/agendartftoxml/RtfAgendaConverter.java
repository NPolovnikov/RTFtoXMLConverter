package com.techinfocom.nvis.agendartftoxml;

import com.rtfparserkit.parser.IRtfParser;
import com.rtfparserkit.parser.IRtfSource;
import com.rtfparserkit.parser.RtfStreamSource;
import com.rtfparserkit.parser.standard.StandardRtfParser;
import com.techinfocom.nvis.agendartftoxml.report.ConversionReport;
import com.techinfocom.nvis.agendartftoxml.report.ErrorMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class RtfAgendaConverter {
    private static final org.slf4j.Logger LOGGER = Logger.LOGGER;

    public AgendaConverterResponse convert(InputStream is) throws Exception { // TODO: 13.06.2016 убрать исключение
        LOGGER.info("start RTF Agenda conversion");
        IRtfSource iRtfSource = new RtfStreamSource(is);
        IRtfParser parser = new StandardRtfParser();
        TokenDetector tokenDetector = new TokenDetector();
        ConversionReport conversionReport = tokenDetector.getAgendaBuilder().getConversionReport();
        Exception exception = null;
        try {
            parser.parse(iRtfSource, tokenDetector);
        } catch (Exception e) {
            exception = e;
        }
        byte[] xmlBytes = new byte[0];
        if (exception == null) {
            JaxbXmlCodec xmlCodec = JaxbXmlCodec.getInstance();
            xmlBytes = xmlCodec.marshalData(tokenDetector.getAgendaBuilder().getAgenda());
        } else {
            conversionReport.collectMessage(new ErrorMessage("Фатальная ошибка. Не удается обработать документ", null));
        }

        return new AgendaConverterResponse(xmlBytes, conversionReport);
    }
}
