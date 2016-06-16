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


        File file = new File(getClass().getClassLoader().getResource("right/1005/10-05.rtf").getFile());
        InputStream is = new FileInputStream(file);

        RtfAgendaConverter converter = new RtfAgendaConverter();
        AgendaConverterResponse agendaConverterResponse = converter.convert(is);

        String xml = new String(agendaConverterResponse.getXmlBytes());

        assertNotNull(xml);



        // TODO: 16.06.2016 добавить упоминание имени схемы в документ

    }
}