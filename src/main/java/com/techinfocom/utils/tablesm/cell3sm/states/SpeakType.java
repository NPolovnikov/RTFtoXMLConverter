package com.techinfocom.utils.tablesm.cell3sm.states;

import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.model.agenda.Group;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;

import java.util.List;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class SpeakType<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {

    public static final Event PLAIN_TEXT = new Event("PLAIN_TEXT");
    private final AgendaBuilder agendaBuilder;

    public SpeakType(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        //допишем тип доклада
        String currentGroupName = agendaBuilder.getCurrentGroup().getGroupName();
        agendaBuilder.getCurrentGroup().setGroupName(currentGroupName + string);
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {

    }

    @Override
    public void analyseFormat(String string, TextFormat textFormat) {
        //текст без форматирования- началась должность.
        if(textFormat.getFontFormat().isEmpty()){
            agendaBuilder.newCurrentSpeaker();
            agendaBuilder.getCurrentSpeaker().setPost("");
            castEvent(PLAIN_TEXT);
        }
    }

}
