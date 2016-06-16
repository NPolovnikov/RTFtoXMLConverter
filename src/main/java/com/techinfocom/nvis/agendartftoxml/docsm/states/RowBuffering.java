package com.techinfocom.nvis.agendartftoxml.docsm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParserState;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.RtfWord;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import org.slf4j.Logger;

import java.util.Queue;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public class RowBuffering<AI extends DocParser> extends StateBase<AI> implements DocParserState {
    public static final Event AGENDA_FOUND = new Event("AGENDA_FOUND");
    public static final Event TABLE_END_FOUND = new Event("TABLE_END_FOUND");
    private final Queue<RtfWord> dataBuffer;
    private final AgendaBuilder agendaBuilder;
    private final TableParser tableParser;

    private Integer cellCount;

    public RowBuffering(AI automation, EventSink eventSink, Queue<RtfWord> dataBuffer,
                        AgendaBuilder agendaBuilder, TableParser tableParser) {
        super(automation, eventSink);
        this.dataBuffer = dataBuffer;
        this.agendaBuilder = agendaBuilder;
        this.tableParser = tableParser;

        initState();
    }

    void initState() {
        cellCount = 0;
        dataBuffer.clear();
    }

    @Override
    public void analyseWord(RtfWord rtfWord) {
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
    public void processWord(RtfWord rtfWord) {
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

    private void analyseCommand(RtfCommand rtfCommand) {
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

    private void analyseChar(FormatedChar fc) {
        if (!fc.getTextFormat().paragraphContain(Command.intbl)) { //есть ли текст вне таблицы?
            //таблица кончилась, пошел обычный текст
            initState();
            eventSink.castEvent(TABLE_END_FOUND);
            // TODO: 16.06.2016 надо бы защититься от прихода такого символа до завершения row
        }
    }

    private void processCommand(RtfCommand rtfCommand) {
        dataBuffer.add(rtfCommand);

        switch (rtfCommand.getCommand()) {
            case cell:  //завершение ячейки
                cellCount++;
                break;
            // TODO: 16.06.2016 защититься от вложенных таблиц

            case row:  //завершение строки таблицы
                if (cellCount.equals(3)) {
                    //Нашли первую строку с тремя ячеями, принимаем ее за начало таблицы расписания.
                    agendaBuilder.newAgendaItem();
                    dataBuffer.stream().forEach(tableParser::processWord);//сначала скормим весь буффер
                    initState();
                    eventSink.castEvent(AGENDA_FOUND);

                } else {
                    // TODO: 16.06.2016  Склеить все ячейки и попробовать найти дату заседания
                    initState();
                    //остаемся тут же, мы в новой строке таблицы.
                }
                break;
        }
    }

    private void processChar(FormatedChar formatedChar) {
        dataBuffer.add(formatedChar); // TODO: 15.06.2016 проверить каковы ограничения очереди на размер. Когда вылетит исключение?
    }

}
