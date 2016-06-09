package com.techinfocom.utils.tablesm.cell3sm.states;

import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class Name<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {

    public static final Event PLAIN_TEXT = new Event("PLAIN_TEXT");
    public static final Event PAR = new Event("PAR");
    public static final Event CELL_END = new Event("CELL_END");

    private final AgendaBuilder agendaBuilder;

    public Name(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        String currentName = agendaBuilder.getCurrentSpeaker().getName();
        agendaBuilder.getCurrentSpeaker().setName(currentName + string);
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()){
            case par:
                agendaBuilder.mergeCurrentSpeaker();
                agendaBuilder.mergeCurrentGroup();
                eventSink.castEvent(PAR);
                break;
            case cell:
                agendaBuilder.mergeCurrentSpeaker();
                agendaBuilder.mergeCurrentGroup();
                eventSink.castEvent(CELL_END);
                break;
        }
    }

    @Override
    public void analyseFormat(String string, TextFormat textFormat) {
        //неформатированый- новый докладчик в текущем докладе
        if(textFormat.getFontFormat().isEmpty()){
            agendaBuilder.mergeCurrentSpeaker();
            agendaBuilder.newCurrentSpeaker();
        }

    }

}
