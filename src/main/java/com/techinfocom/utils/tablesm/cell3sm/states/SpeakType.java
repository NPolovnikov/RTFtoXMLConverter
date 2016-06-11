package com.techinfocom.utils.tablesm.cell3sm.states;

import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class SpeakType<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {

    public static final Event POST_FOUND = new Event("POST_FOUND");
    private final AgendaBuilder agendaBuilder;

    public SpeakType(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        if (fc.getC() == '\n') {
            //castEvent(ERROR); //todo не можем встретить перевод строки в этом состоянии. Тип доклада- однострочный.
        } else {
            //допишем тип доклада
            String currentGroupName = agendaBuilder.getCurrentGroup().getGroupName();
            agendaBuilder.getCurrentGroup().setGroupName(currentGroupName + String.valueOf(fc.getC()));
        }
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        //текст без форматирования- началась должность.
        if (fc.getTextFormat().getFontFormat().isEmpty()) {
            agendaBuilder.newCurrentSpeaker();
            agendaBuilder.getCurrentSpeaker().setPost("");
            agendaBuilder.getCurrentSpeaker().setName("");
            castEvent(POST_FOUND);
        }
    }

    @Override
    public void endOfCell() {

    }
}
