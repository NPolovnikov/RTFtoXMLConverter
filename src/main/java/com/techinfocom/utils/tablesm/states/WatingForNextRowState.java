package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.model.FormatedChar;
import com.techinfocom.utils.model.RtfCommand;
import com.techinfocom.utils.model.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class WatingForNextRowState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final Logger LOGGER = com.techinfocom.utils.Logger.LOGGER;
    private static final String STATE_NAME = WatingForNextRowState.class.getSimpleName().toUpperCase();
    public static final Event NEXT_ROW = new Event("NEXT_ROW");
    public static final Event TABLE_END = new Event("TABLE_END");
    private final AgendaBuilder agendaBuilder;

    public WatingForNextRowState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        //ignore any strings
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()){
            case trowd:
                System.err.println("Состояние WFNextRow.Поймали trowd");
                agendaBuilder.newAgendaItem();
                eventSink.castEvent(NEXT_ROW);
                break;
            case par:
                System.err.println("Состояние WFNextRow.Поймали par");
                eventSink.castEvent(TABLE_END);
                break;
        }
    }
}
