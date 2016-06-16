package com.techinfocom.nvis.agendartftoxml.docsm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParserState;
import com.techinfocom.nvis.agendartftoxml.model.*;
import com.techinfocom.nvis.agendartftoxml.report.ErrorMessage;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import org.slf4j.Logger;

import java.util.Queue;

/**
 * Класс для детектирования текста или начала таблицы
 */
public class DataSearching<AI extends DocParser> extends StateBase<AI> implements DocParserState {
    public static final Event SOME_ROW_FOUND = new Event("SOME_ROW_FOUND");//какая-то строка таблицы найдена
    private final Queue<RtfWord> dataBuffer;
    private final AgendaBuilder agendaBuilder;
    private final TableParser tableParser;
    private StringBuilder collectedChars;

    public DataSearching(AI automation, EventSink eventSink, Queue<RtfWord> dataBuffer,
                         AgendaBuilder agendaBuilder, TableParser tableParser) {
        super(automation, eventSink);
        this.dataBuffer = dataBuffer;
        this.agendaBuilder = agendaBuilder;
        this.tableParser = tableParser;
        initState();
    }

    private void initState() {
        collectedChars = new StringBuilder();
    }

    @Override
    public void analyseWord(RtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case COMMAND:
                RtfCommand rtfCommand = (RtfCommand) rtfWord;
                analyseCommand(rtfCommand);
                break;
        }
    }

    @Override
    public void processWord(RtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case CHAR:
                FormatedChar fc = (FormatedChar) rtfWord;
                collectedChars.append(fc.getC());
                break;
            case COMMAND:
                RtfCommand rtfCommand = (RtfCommand) rtfWord;
                if (rtfCommand.getCommand() == Command.par) {
                    collectedChars.append("\n");
                }
                break;
        }
    }

    private void analyseCommand(RtfCommand rtfCommand) {
        switch (rtfCommand.getCommand()) {
            case trowd: //начало таблицы
                if(collectedChars.length() > 0) {
                    agendaBuilder.meetingDateExtractAndSave(collectedChars.toString());  //попробуем отыскать дату заседания
                }
                 initState();
                eventSink.castEvent(SOME_ROW_FOUND);
                break;
        }
    }

    @Override
    public void processDocumentEnd() {
        agendaBuilder.getConversionReport().collectMessage(new ErrorMessage("таблица с расписанием не обнаружена"));
    }

}
