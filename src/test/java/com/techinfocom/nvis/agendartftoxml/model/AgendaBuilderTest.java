package com.techinfocom.nvis.agendartftoxml.model;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;

import static org.testng.Assert.*;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class AgendaBuilderTest {

    @Test
    public void testMeetingDateExtractAndSave() throws Exception {
        AgendaBuilder agendaBuilder = new AgendaBuilder();
        String header = "\n" +
                "Порядок работы Государственной Думы\n" +
                "\n" +
                "10 мая 2016 года, вторник\n" +
                "\n";
        agendaBuilder.meetingDateExtractAndSave(header);
        XMLGregorianCalendar agendaDate = agendaBuilder.getAgenda().getMeetingDate();
        Assert.assertNotNull(agendaDate);
        LocalDate asserted = LocalDate.of(2016,05,10);
        Assert.assertEquals(asserted.compareTo(agendaDate.toGregorianCalendar().toZonedDateTime().toLocalDate()), 0);

        agendaBuilder = new AgendaBuilder();
        header = "\n" +
                "Порядок работы Государственной Думы\n" +
                "\n" +
                "10.05.2016 , вторник\n" +
                "\n";
        agendaBuilder.meetingDateExtractAndSave(header);
        agendaDate = agendaBuilder.getAgenda().getMeetingDate();
        Assert.assertNotNull(agendaDate);
        asserted = LocalDate.of(2016,05,10);
        Assert.assertEquals(asserted.compareTo(agendaDate.toGregorianCalendar().toZonedDateTime().toLocalDate()), 0);
    }
}