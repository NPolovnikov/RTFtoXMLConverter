package com.techinfocom.nvis.agendartftoxml;

import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.testng.Assert.*;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class RtfAgendaConverterTest {

    @Test
    public void testConvert() throws Exception {
//        File file = new File(RtfAgendaConverterTest.class.getClassLoader().getResource("10-05.rtf").getFile());
//        File file = new File(RtfAgendaConverterTest.class.getClassLoader().getResource("10-05_wo_date.rtf").getFile());
        File file = new File(RtfAgendaConverterTest.class.getClassLoader().getResource("10-05_wo_table.rtf").getFile());
//        File file = new File(RtfAgendaConverterTest.class.getClassLoader().getResource("super_order.rtf").getFile());
//        File file = new File(RtfAgendaConverterTest.class.getClassLoader().getResource("agenda-internal-new.xsd").getFile());
        System.out.println(file);
        InputStream is = new FileInputStream(file);

        RtfAgendaConverter converter = new RtfAgendaConverter();
        AgendaConverterResponse agendaConverterResponse = converter.convert(is);

        String xml = new String(agendaConverterResponse.getXmlBytes());

        assertNotNull(xml);

        // TODO: 16.06.2016 добавить упоминание имени схемы в документ

    }
}