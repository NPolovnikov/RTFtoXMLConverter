package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class WaitForRowEndState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final Logger LOGGER = com.techinfocom.utils.Logger.LOGGER;
    private static final String STATE_NAME = WaitForRowEndState.class.getSimpleName().toUpperCase();
    public static final Event ROW_END = new Event("ROW_END");
    public static final Event CELL_END = new Event("CELL_END");
    private final AgendaBuilder agendaBuilder;

    public WaitForRowEndState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        //ignore any stringsq
    }


    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case row:
                System.err.println("В состоянии WaitForRowEnd поймали row");
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
