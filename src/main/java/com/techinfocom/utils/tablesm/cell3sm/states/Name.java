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
public class Name<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {

    public static final Event PLAIN_TEXT = new Event("POST_FOUND");
    public static final Event END_OF_SPEAKER_GROUP = new Event("END_OF_SPEAKER_GROUP");
    public static final Event CELL_END = new Event("CELL_END");

    private final AgendaBuilder agendaBuilder;

    public Name(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        if (fc.getC() == '\n') {
            agendaBuilder.mergeCurrentSpeaker();
            agendaBuilder.mergeCurrentGroup();
            eventSink.castEvent(END_OF_SPEAKER_GROUP);
        } else {
            String currentName = agendaBuilder.getCurrentSpeaker().getName();
            agendaBuilder.getCurrentSpeaker().setName(currentName + String.valueOf(fc.getC()));
        }
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        switch (rtfCommand.getCommand()){
//            case par:
//                agendaBuilder.mergeCurrentSpeaker();
//                agendaBuilder.mergeCurrentGroup();
//                eventSink.castEvent(END_OF_SPEAKER_GROUP);
//                break;
//            case cell:
//                agendaBuilder.mergeCurrentSpeaker();
//                agendaBuilder.mergeCurrentGroup();
//                eventSink.castEvent(CELL_END);
//                break;
//        }
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        //неформатированый- новый докладчик в текущем докладе
        if (fc.getTextFormat().getFontFormat().isEmpty()) {
            agendaBuilder.mergeCurrentSpeaker();
            agendaBuilder.newCurrentSpeaker();
        }
    }

    @Override
    public void endOfCell() {

    }
}
