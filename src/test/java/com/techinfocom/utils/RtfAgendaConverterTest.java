package com.techinfocom.utils;

import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class RtfAgendaConverterTest {

    @Test
    public void testConvert() throws Exception {
        File file = new File(RtfAgendaConverterTest.class.getClassLoader().getResource("super_order.rtf").getFile());
        System.out.println(file);
        InputStream is = new FileInputStream(file);

        RtfAgendaConverter converter = new RtfAgendaConverter();
        converter.convert(is);
    }
}