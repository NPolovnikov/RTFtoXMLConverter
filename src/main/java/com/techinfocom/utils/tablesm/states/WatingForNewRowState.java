package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class WatingForNewRowState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event NEXT_ROW = new Event("NEXT_ROW");
    public static final Event TABLE_END = new Event("TABLE_END");
    AgendaBuilder agendaBuilder;

    public WatingForNewRowState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
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
                System.err.println("Состояние WFTContinue.Поймали trowd");
                agendaBuilder.newAgendaItem();
                eventSink.castEvent(NEXT_ROW);
                break;
            case par:
                System.err.println("Состояние WFTContinue.Поймали par");
                eventSink.castEvent(TABLE_END);
                break;
        }
    }
}
