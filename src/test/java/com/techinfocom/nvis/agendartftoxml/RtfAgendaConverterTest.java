package com.techinfocom.nvis.agendartftoxml;

import com.techinfocom.nvis.agendartftoxml.report.ReportMessage;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.testng.Assert.*;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class RtfAgendaConverterTest {

    @Test
    public void testSuccessConvert() throws Exception {

        File[] files = new File(getClass().getClassLoader().getResource("right/").toURI()).listFiles();
        for(File f : files){
            if(f.isDirectory()){
                File[] rtfFiles = f.listFiles((dir, name) -> name.endsWith(".rtf"));
                File[] xmlFiles = f.listFiles((dir, name) -> name.endsWith(".xml"));
                if (rtfFiles.length > 0) {
                    InputStream is = new FileInputStream(rtfFiles[0]);
                    RtfAgendaConverter converter = new RtfAgendaConverter();
                    AgendaConverterResponse agendaConverterResponse = converter.convert(is);

                    assertTrue(agendaConverterResponse.getXmlBytes().length > 10);
                    String xml = new String(agendaConverterResponse.getXmlBytes());
                    String report = agendaConverterResponse.printReport("ERROR", "WARNING");
                    assertTrue(report.isEmpty());
                    assertNotNull(xml);
                }
            }
        }

//        try(DirectoryStream<Path> rightStream = Files.newDirectoryStream(rightPath)) {
//            for(Path p: rightStream){
//
//            }
//
//        }
//
//        File file = new File(getClass().getClassLoader().getResource("right/1005/10-05.rtf").getFile());
//        InputStream is = new FileInputStream(file);
//
//        RtfAgendaConverter converter = new RtfAgendaConverter();
//        AgendaConverterResponse agendaConverterResponse = converter.convert(is);
//
//        String xml = new String(agendaConverterResponse.getXmlBytes());
//
//        assertNotNull(xml);



        // TODO: 16.06.2016 добавить упоминание имени схемы в документ

    }

    @Test
    public void testFailedConvert() throws Exception {

        //case 1
        File file = new File(getClass().getClassLoader().getResource("failed/1005wo_table/10-05_wo_table.rtf").getFile());
        InputStream is = new FileInputStream(file);

        RtfAgendaConverter converter = new RtfAgendaConverter();
        AgendaConverterResponse agendaConverterResponse = converter.convert(is);

        assertTrue(agendaConverterResponse.getXmlBytes().length == 0);
        String report = agendaConverterResponse.printReport("ERROR", "WARNING");
        assertFalse(report.isEmpty());


    }
}