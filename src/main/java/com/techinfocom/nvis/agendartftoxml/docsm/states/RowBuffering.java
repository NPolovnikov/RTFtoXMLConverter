package com.techinfocom.nvis.agendartftoxml.docsm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParserState;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.AbstractRtfWord;
import com.techinfocom.nvis.agendartftoxml.report.WarningMessage;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;

import java.util.Queue;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public class RowBuffering<AI extends DocParser> extends StateBase<AI> implements DocParserState {
    public static final Event AGENDA_FOUND = new Event("AGENDA_FOUND");
    public static final Event TABLE_END_FOUND = new Event("TABLE_END_FOUND");
    private final Queue<AbstractRtfWord> wordBuffer;
    private final AgendaBuilder agendaBuilder;
    private final TableParser tableParser;

    private Integer cellCount;

    public RowBuffering(final AI automation, final EventSink eventSink, final Queue<AbstractRtfWord> wordBuffer, final AgendaBuilder agendaBuilder, final TableParser tableParser) {
        super(automation, eventSink);
        this.wordBuffer = wordBuffer;
        this.agendaBuilder = agendaBuilder;
        this.tableParser = tableParser;
        initState();
    }

    void initState() {
        cellCount = 0;
        wordBuffer.clear();
    }

    @Override
    public void analyseWord(final AbstractRtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case COMMAND:
                RtfCommand rtfCommand = (RtfCommand) rtfWord;
                analyseCommand(rtfCommand);
                break;
            case CHAR:
                FormatedChar fc = (FormatedChar) rtfWord;
                analyseChar(fc);
                break;
        }
    }

    @Override
    public void processWord(final AbstractRtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case COMMAND:
                RtfCommand rtfCommand = (RtfCommand) rtfWord;
                processCommand(rtfCommand);
                break;
            case CHAR:
                FormatedChar fc = (FormatedChar) rtfWord;
                processChar(fc);
                break;
        }
    }

    private void analyseCommand(final RtfCommand rtfCommand) {
        switch (rtfCommand.getCommand()) {
            case par:
                if (!rtfCommand.getTextFormat().paragraphContain(Command.intbl)) { //текст снова просится в таблицу- значит таблица продолжается.
                    initState();
                    eventSink.castEvent(TABLE_END_FOUND); //таблица кончилась, пошел обычный текст
                    // TODO: 16.06.2016 надо бы защититься от прихода такого символа до завершения row
                }
                break;
        }
    }

    private void analyseChar(final FormatedChar fc) {
        if (!fc.getTextFormat().paragraphContain(Command.intbl)) { //есть ли текст вне таблицы?
            //таблица кончилась, пошел обычный текст
            initState();
            eventSink.castEvent(TABLE_END_FOUND);
            // TODO: 16.06.2016 надо бы защититься от прихода такого символа до завершения row
        }
    }

    private void processCommand(final RtfCommand rtfCommand) {
        wordBuffer.add(rtfCommand);

        switch (rtfCommand.getCommand()) {
            case cell:  //завершение ячейки
                cellCount++;
                break;
            // TODO: 16.06.2016 защититься от вложенных таблиц

            case row:  //завершение строки таблицы
                if (cellCount.equals(3)) {
                    //Нашли первую строку с тремя ячеями, принимаем ее за начало таблицы расписания.
                    if (agendaBuilder.getAgenda().getMeetingDate() == null) {
                        agendaBuilder.getConversionReport().collectMessage(new WarningMessage("Не обнаружена дата заседания"));
                    }
                    agendaBuilder.newAgendaItem();
                    wordBuffer.stream().forEach(tableParser::processWord);//сначала скормим весь буффер
                    initState();
                    eventSink.castEvent(AGENDA_FOUND);

                } else {
                    // Склеим все ячейки и попробуем найти дату заседания
                    final StringBuilder sb = new StringBuilder();
                    Predicate<AbstractRtfWord> isString = (w) -> {
                        switch (w.getRtfWordType()) {
                            case CHAR:
                                return true;

                            case COMMAND:
                                switch (((RtfCommand) w).getCommand()) {
                                    case par:
                                    case cell:
                                        return true;
                                    default:
                                        return false;
                                }
                            default:
                                return false;
                        }
                    };
                    final Function<AbstractRtfWord, Character> toChar = (w) -> {
                        switch (w.getRtfWordType()) {
                            case CHAR:
                                return ((FormatedChar) w).getC();
                            case COMMAND:
                            default:
                                return '\n'; //ранее отфильтровано.

                        }

                    };
                    wordBuffer.stream().filter(isString).map(toChar).forEach(sb::append);
                    agendaBuilder.meetingDateExtractAndSave(sb.toString());
                    initState();
                    //остаемся тут же, мы в новой строке таблицы.
                }
                break;
        }
    }

    private void processChar(final FormatedChar formatedChar) {
        wordBuffer.add(formatedChar); // TODO: 15.06.2016 проверить каковы ограничения очереди на размер. Когда вылетит исключение?
    }

    @Override
    public void processDocumentEnd() {

    }


}
