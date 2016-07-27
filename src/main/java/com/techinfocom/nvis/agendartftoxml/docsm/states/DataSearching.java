package com.techinfocom.nvis.agendartftoxml.docsm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParserState;
import com.techinfocom.nvis.agendartftoxml.model.*;
import com.techinfocom.nvis.agendartftoxml.report.ErrorMessage;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;

/**
 * Класс для детектирования текста или начала таблицы
 */
public class DataSearching<AI extends DocParser> extends StateBase<AI> implements DocParserState {
    public static final Event SOME_ROW_FOUND = new Event("SOME_ROW_FOUND");//какая-то строка таблицы найдена
    private final AgendaBuilder agendaBuilder;
    private StringBuilder collectedChars;

    public DataSearching(final AI automation, final EventSink eventSink, final AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        initState();
    }

    private void initState() {
        collectedChars = new StringBuilder();
    }

    @Override
    public void analyseWord(final AbstractRtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case COMMAND:
                RtfCommand rtfCommand = (RtfCommand) rtfWord;
                analyseCommand(rtfCommand);
                break;
        }
    }

    @Override
    public void processWord(final AbstractRtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case CHAR:
                FormatedChar fc = (FormatedChar) rtfWord;
                collectedChars.append(fc.getC());
                break;
            case COMMAND:
                RtfCommand rtfCommand = (RtfCommand) rtfWord;
                if (rtfCommand.getCommand() == Command.par) {
                    collectedChars.append('\n');
                }
                break;
        }
    }

    private void analyseCommand(final RtfCommand rtfCommand) {
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
