package com.techinfocom.nvis.agendartftoxml;

import com.techinfocom.nvis.agendartftoxml.report.AbstractReportMessage;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static org.testng.Assert.*;

/**
 * Created by volkov_kv on 13.06.2016.
 */
// TODO: 05.07.2016 сделать предупреждение о неполном использовании текста (уходим в ожидание конца ячейки, и начинаем собирать текст)
// TODO: 16.06.2016 добавить упоминание имени схемы в документ
@Test(testName = "Тестирование компонента RTFtoXMLConverter")
public class RtfAgendaConverterTest {
    private static final String ERROR = "ERROR";
    private static final String WARNING = "WARNING";

    @Test(description = "Проверка соответствия результата импорта 17 документов-образцов предопределенным соответствующим XML")
    // TODO: 30.06.2016 натравить на XML
    public void testSuccessConvert() throws Exception {

        final File[] files = new File(getClass().getClassLoader().getResource("right/").toURI()).listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                final File[] rtfFiles = f.listFiles((dir, name) -> name.endsWith(".rtf"));
                final File[] xmlFiles = f.listFiles((dir, name) -> name.endsWith(".xml"));
                if (rtfFiles.length > 0) {
                    final InputStream is = new FileInputStream(rtfFiles[0]);//планируется по однму файлу, потому и 0
                    final RtfAgendaConverter converter = new RtfAgendaConverter();
                    final AgendaConverterResponse agendaConverterResponse = converter.convert(is);

                    assertTrue(agendaConverterResponse.getXmlBytes().length > 10);
                    final String xml = new String(agendaConverterResponse.getXmlBytes());
                    final String report = agendaConverterResponse.printReport(ERROR, WARNING);
                    assertTrue(report.isEmpty(), "Возник ERROR или WARNING при импорте корректного документа " + rtfFiles[0].getName() + ". Сообщение: " + report);
                    assertNotNull(xml, "результат импорта - null");
                    assertTrue(xml.length() > 0, "результат импорта - пустая строка");
                }
            }
        }
    }

    @Test(description = "Проверка обработки строк в верхнем индексе")
    public void testSuperscript() throws Exception {
        {
            final File rtfFile = new File(getClass().getClassLoader().getResource("superscript/upper_index1.rtf").getFile());
            final File xmlFile = new File(getClass().getClassLoader().getResource("superscript/upper_index1.xml").getFile());
            final InputStream rtfIs = new FileInputStream(rtfFile);
            final InputStream xmlIs = new FileInputStream(xmlFile);
            String assertXml;
            try (Scanner scanner = new Scanner(xmlFile, "UTF-8")) {
                assertXml = scanner.useDelimiter("\\A").next();
            }

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(rtfIs);
            rtfIs.close();

            assertTrue(agendaConverterResponse.getXmlBytes().length > 10);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            final String report = agendaConverterResponse.printReport(ERROR, WARNING);
            assertTrue(report.isEmpty(), "Возник ERROR или WARNING при импорте корректного документа");
            assertNotNull(xml, "результат импорта - null");
            assertTrue(xml.length() > 0, "результат импорта - пустая строка");

            assertTrue(xml.contains("<text>\"О внесении изменения в статью 55&lt;sup&gt;24&lt;/sup&gt; Градостроительного кодекса Российской "), xml);
            assertTrue(xml.contains("Воткнем даже перенос строки в верхнем индексе) 55&lt;sup&gt;24&lt;/sup&gt;</text>"));


            //assertTrue(assertXml.equals(xml)); не прокатит. будут разные UUID. Надо делать выброчное сравнение узлов xml
        }

    }


    @Test(description = "Проверка обработки фатальных ошибок при импорте")
    public void testFailedConvert() throws Exception {

        //case 1
        {
            final File file = new File(getClass().getClassLoader().getResource("failed/1005wo_table/10-05_wo_table.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);

            assertTrue(agendaConverterResponse.getXmlBytes().length == 0);
            //ERROR: таблица с расписанием не обнаружена
            assertTrue(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте НЕ содержит ERROR");
            assertFalse(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(ERROR);
            assertTrue(report.contains("таблица с расписанием не обнаружена"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
        }
        //case 2
        {
            final File file = new File(getClass().getClassLoader().getResource("failed/1005wo_date/10-05_wo_date.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            final String xml = new String(agendaConverterResponse.getXmlBytes());
            final String report = agendaConverterResponse.printReport(ERROR, WARNING);

            assertFalse(report.isEmpty());
        }
    }

    @Test(description = "Проверка функционирования валидаторов")
    public void testOfValidators() throws Exception {

        //Тесты на длину строк
        //длина номера пункта работы
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/number/number_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            //WARNING: В пункте 700.10.2, в номере пункта порядка работы длина строки превышает максимальную - 8; Примерное положение: 700.10.23
            assertTrue(report.contains("в номере пункта порядка работы длина строки превышает максимальную") &&
                    report.contains("700.10.23"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
        }

        // длина info
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/info/info_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            //WARNING: В пункте 3., в доп информации длина строки превышает максимальную - 255; Примерное положение: Комитет рекомендует принять и еще раз принять, и еще попринимать, и напринимать, и с рассмотрением и без рассмотрения, Можно просто кнопки понажимать, можно в бД записать и сказать, что приняли Это строка из 256 символов. на 1 больше чем разрешено.. Лишнее
            assertTrue(report.contains("в доп информации длина строки превышает максимальную") &&
                    report.contains("Комитет рекомендует"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина addon
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/addon/addon_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            //WARNING: В пункте 3., в дополнении длина строки превышает максимальную - 255; Примерное положение: О проекте постановления Государственной Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Думы Лишнее
            assertTrue(report.contains("в дополнении длина строки превышает максимальную") &&
                    report.contains("О проекте постановления"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина rn
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/rn/rn_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            //WARNING: В пункте 3., в номере документа длина строки превышает максимальную - 12; Примерное положение: 8888888888-66
            assertTrue(report.contains("в номере документа длина строки превышает максимальную") &&
                    report.contains("8888888888-66"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("8888888888-66"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("8888888888-6"), "Проигнорировано больше чем ожидалось");
        }

        // длина text
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/text/text_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            //WARNING: В пункте 3., в тексте пункта порядка работы длина строки превышает максимальную - 4096; Примерное положение: "Об Основных направлениях единой государственной денежно-кредитной политики на 2016 год и период 2017 и 2018 годов" много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много слов много много много много много многоо Лишнее
            assertTrue(report.contains("в тексте пункта порядка работы длина строки превышает максимальную") &&
                    report.contains("Об Основных "), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина note
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/note/note_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            //WARNING: В пункте 3., в примечании длина строки превышает максимальную - 250; Примерное положение: (по решению Совета Государственной Думы) и по решению синода и схода и парламента и попов всея руси, Решали решали решали решали решали решали решали решали решали решали решали решали решали решали решали решали решали решали л реша перерешали Лишнее
            assertTrue(report.contains("в примечании длина строки превышает максимальную") &&
                    report.contains("по решению Совета Г"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина GroupName
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/group_name/group_name_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("Содокладыыыыыыыыыыыы"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина Post
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/post/post_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("председателя Комитета по экономической политике"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        // длина Name
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/name/name_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("Андрея Михайловича Макарова"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("Лишне"), "Проигнорировано больше чем ожидалось");
        }

        //тесты на кол-во элементов

        //maxTotalSpeakerCount
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/speakerCount/speaker_count_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            assertTrue(report.contains("кол-во докладчиков превосходит максимально") &&
                    report.contains("Министра юстиции Российской Федерации"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("ВЛЕЗ"), "Проигнорировано больше чем ожидалось");
        }

        //maxItemCount разрешенное кол-во строк.
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/itemCount/item_count_right.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertFalse(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 0, "Кол-во сообщений в отчете не равно 0");
            assertTrue(xml.contains("как раз"), "проигнорировано больше чем ожидалось");
        }
        //maxItemCount кол-во строк превосходит разрешенное
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/itemCount/item_count_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            assertTrue(report.contains("Кол-во пунктов превышает максимально допустимое") &&
                    report.contains("Многа"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("как раз"), "Проигнорировано больше чем ожидалось");
        }

        //maxNoteCount
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/noteCount/note_count_wrong.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            assertTrue(report.contains("кол-во примечаний превосходит максимально разрешенное") &&
                    report.contains("по решению Совета Государственной"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
            assertFalse(xml.contains("Лишнее"), "часть строки превосходящая разрешенную длину не была проигнорирована");
            assertTrue(xml.contains("как раз1"), "Проигнорировано больше чем ожидалось");
            assertTrue(xml.contains("как раз2"), "Проигнорировано больше чем ожидалось");
        }

        //сообщение об игнорировании текста
        {
            final File file = new File(getClass().getClassLoader().getResource("validators/ignoredtext/ignored_text.rtf").getFile());
            final InputStream is = new FileInputStream(file);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(is);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage(ERROR), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage(WARNING), "отчет об импорте НЕ содержит WARNING");
            final List<AbstractReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете не равно 1");
            final String report = agendaConverterResponse.printReport(WARNING);
            assertTrue(report.contains("WARNING: Вероятно, нарушена структура документа. В пункте 2. проигнорирован текст: Фигня какая-то"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
        }

    }

    @Test(enabled = false)
    public void learnSomeDoc() throws Exception {
        {
            final File rtfFile = new File(getClass().getClassLoader().getResource("learn/18-12.rtf").getFile());
            final InputStream rtfIs = new FileInputStream(rtfFile);

            final RtfAgendaConverter converter = new RtfAgendaConverter();
            final AgendaConverterResponse agendaConverterResponse = converter.convert(rtfIs);
            rtfIs.close();

            assertTrue(agendaConverterResponse.getXmlBytes().length > 10);
            final String xml = new String(agendaConverterResponse.getXmlBytes());

            final String report = agendaConverterResponse.printReport(ERROR, WARNING);
            assertTrue(report.isEmpty(), "Возник ERROR или WARNING при импорте корректного документа");
            assertNotNull(xml, "результат импорта - null");
            assertTrue(xml.length() > 0, "результат импорта - пустая строка");


            //assertTrue(assertXml.equals(xml)); не прокатит. будут разные UUID. Надо делать выброчное сравнение узлов xml
        }

    }


}