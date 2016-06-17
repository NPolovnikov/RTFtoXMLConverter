package com.techinfocom.nvis.agendartftoxml.tablesm.states;

import com.techinfocom.nvis.agendartftoxml.model.*;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class WaitForRowEndState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    private static final String STATE_NAME = WaitForRowEndState.class.getSimpleName().toUpperCase();
    public static final Event ROW_END = new Event("ROW_END");
    public static final Event CELL_END = new Event("CELL_END");
    private final AgendaBuilder agendaBuilder;

    public WaitForRowEndState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processWord(RtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case COMMAND:
                processCommand((RtfCommand) rtfWord);
                break;
            case CHAR:
                processChar((FormatedChar) rtfWord);
                break;
        }
    }

    @Override
    public void exit() {

    }

    public void processChar(FormatedChar fc) {
        //ignore any stringsq
    }

    public void processCommand(RtfCommand rtfCommand) {
        switch (rtfCommand.getCommand()) {
            case row:
                LOGGER.debug("В состоянии WaitForRowEnd поймали row");
                agendaBuilder.mergeAgendaItem();
                agendaBuilder.newAgendaItem();
                eventSink.castEvent(ROW_END);
                break;
            case cell:
                LOGGER.info("Неожиданное обнаружена четвертая ячейка строки таблицы. контекст = {}. Данные строки проигнорированы", "");// TODO: 13.06.2016 включить контекст
                agendaBuilder.dropAgendaItem();//Если что-то успели насобирать- забудем.
                agendaBuilder.newAgendaItem();
                eventSink.castEvent(CELL_END);
                break;
        }
    }
}
