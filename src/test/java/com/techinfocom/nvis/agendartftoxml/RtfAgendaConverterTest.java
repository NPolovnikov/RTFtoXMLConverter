package com.techinfocom.nvis.agendartftoxml;

import com.techinfocom.nvis.agendartftoxml.report.ReportMessage;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by volkov_kv on 13.06.2016.
 */
@Test(testName = "Тестирование компонента RTFtoXMLConverter")
public class RtfAgendaConverterTest {

    @Test(description = "Проверка соответствия результата импорта 17 документов-образцов предопределенным соответствующим XML")
    // TODO: 30.06.2016 натравить на XML
    public void testSuccessConvert() throws Exception {

        File[] files = new File(getClass().getClassLoader().getResource("right/").toURI()).listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                File[] rtfFiles = f.listFiles((dir, name) -> name.endsWith(".rtf"));
                File[] xmlFiles = f.listFiles((dir, name) -> name.endsWith(".xml"));
                if (rtfFiles.length > 0) {
                    InputStream is = new FileInputStream(rtfFiles[0]);
                    RtfAgendaConverter converter = new RtfAgendaConverter();
                    AgendaConverterResponse agendaConverterResponse = converter.convert(is);

                    assertTrue(agendaConverterResponse.getXmlBytes().length > 10);
                    String xml = new String(agendaConverterResponse.getXmlBytes());
                    String report = agendaConverterResponse.printReport("ERROR", "WARNING");
                    assertTrue(report.isEmpty(), "Возник ERROR или WARNING при импорте корректного документа");
                    assertNotNull(xml, "результат импорта - null");
                    assertTrue(xml.length() > 0, "результат импорта - пустая строка");
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

    @Test(description = "Проверка обработки фатальных ошибок при импорте")
    // TODO: 30.06.2016 проверить на соответствие заявленным целям
    public void testFailedConvert() throws Exception {

        //case 1
        File file = new File(getClass().getClassLoader().getResource("failed/1005wo_table/10-05_wo_table.rtf").getFile());
        InputStream is = new FileInputStream(file);

        RtfAgendaConverter converter = new RtfAgendaConverter();
        AgendaConverterResponse agendaConverterResponse = converter.convert(is);

        assertTrue(agendaConverterResponse.getXmlBytes().length == 0);
        String report = agendaConverterResponse.printReport("ERROR", "WARNING");
        assertFalse(report.isEmpty());

        //case 2
        file = null;
        agendaConverterResponse = null;
        report = null;
        file = new File(getClass().getClassLoader().getResource("failed/1005wo_date/10-05_wo_date.rtf").getFile());
        is = new FileInputStream(file);

        converter = new RtfAgendaConverter();
        agendaConverterResponse = converter.convert(is);

        assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
        String xml = new String(agendaConverterResponse.getXmlBytes());
        report = agendaConverterResponse.printReport("ERROR", "WARNING");
        assertFalse(report.isEmpty());


    }

    @Test(description = "Проверка функционирования валидаторов")
    public void testOfValidators() throws Exception {

        //Тесты на длину строк
        //длина номера пункта работы
        {
            File file = new File(getClass().getClassLoader().getResource("validators/number/number_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            String report = agendaConverterResponse.printReport("WARNING");
            //WARNING: В пункте 700.10.2 длина строки превышает максимальную - 8; Примерное положение: 700.10.23"
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("700.10.23"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
        }

        // длина info
        {
            File file = new File(getClass().getClassLoader().getResource("validators/info/info_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            String report = agendaConverterResponse.printReport("WARNING");
            //WARNING: В пункте 3., в доп информации длина строки превышает максимальную - 255; Примерное положение: Комитет рекомендует принять и еще раз принять, и еще попринимать, и напринимать, и с рассмотрением и без рассмотрения, Можно просто кнопки понажимать, можно в бД записать и сказать, что приняли Это строка из 256 символов. на 1 больше чем разрешено.. Лишнее
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("Комитет рекомендует"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина addon
        {
            File file = new File(getClass().getClassLoader().getResource("validators/addon/addon_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            String report = agendaConverterResponse.printReport("WARNING");
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("О проекте постановления"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина rn
        {
            File file = new File(getClass().getClassLoader().getResource("validators/rn/rn_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            String report = agendaConverterResponse.printReport("WARNING");
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("8888888888-66"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("8888888888-66"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("8888888888-6"), "Проигнорировано больше чем ожидалось");
        }

        // длина text
        {
            File file = new File(getClass().getClassLoader().getResource("validators/text/text_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            String report = agendaConverterResponse.printReport("WARNING");
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("Об Основных "), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина note
        {
            File file = new File(getClass().getClassLoader().getResource("validators/note/note_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            String report = agendaConverterResponse.printReport("WARNING");
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("по решению Совета Г"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }
        // TODO: 30.06.2016 тест на кол-во нотесов
        // длина GroupName
        {
            File file = new File(getClass().getClassLoader().getResource("validators/group_name/group_name_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            String report = agendaConverterResponse.printReport("WARNING");
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("Содокладыыыыыыыыыыыы"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина Post
        {
            File file = new File(getClass().getClassLoader().getResource("validators/post/post_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            String report = agendaConverterResponse.printReport("WARNING");
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("председателя Комитета по экономической политике"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина Name
        {
            File file = new File(getClass().getClassLoader().getResource("validators/name/name_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            String report = agendaConverterResponse.printReport("WARNING");
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("Андрея Михайловича Макарова"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        //тесты на кол-во элементов

    }


}