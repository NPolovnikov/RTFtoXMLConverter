package com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.states;

import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.Cell3Parser;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class Name<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = Name.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    public static final Event POST_FOUND = new Event("POST_FOUND");
    public static final Event END_OF_SPEAKER_GROUP = new Event("END_OF_SPEAKER_GROUP");
    public static final Event EXIT = new Event("EXIT");

    private final AgendaBuilder agendaBuilder;

    public Name(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        if (fc.getC() == '\n') {
            agendaBuilder.mergeSpeaker();
            agendaBuilder.mergeGroup();
            eventSink.castEvent(END_OF_SPEAKER_GROUP);
            LOGGER.debug("state={}. Обнаружен \\n. Объединены CurrentSpeaker, CurrentGroup", STATE_NAME);
        } else {
            String currentName = agendaBuilder.getSpeaker().getName();
            if (currentName == null) {
                currentName = "";
            }
            agendaBuilder.getSpeaker().setName(currentName + String.valueOf(fc.getC()));
        }
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        switch (rtfCommand.getCommand()){
//            case par:
//                agendaBuilder.mergeSpeaker();
//                agendaBuilder.mergeGroup();
//                eventSink.castEvent(END_OF_SPEAKER_GROUP);
//                break;
//            case cell:
//                agendaBuilder.mergeSpeaker();
//                agendaBuilder.mergeGroup();
//                eventSink.castEvent(CELL_END);
//                break;
//        }
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        //неформатированый- новый докладчик в текущем докладе
        if (fc.getTextFormat().getFontFormat().isEmpty() &&
                fc.getC() != '\n' && fc.getC() != ' ') {    //но не перевод и не пробелы, иначе можно ошибочно переключиться в ожидание следующей должности
            LOGGER.debug("state={}. Обнаружен неформатированный текст '{}'. Это должность очередного докладчика. Объединены CurrentSpeaker, создан новый CurrentSpeaker", STATE_NAME, fc.getC());
            agendaBuilder.mergeSpeaker();
            agendaBuilder.newSpeaker();
            eventSink.castEvent(POST_FOUND);
        }
    }

    @Override
    public void exit() {
        LOGGER.debug("state={}. Получен сигнал о завершении ячейки. Объединены CurrentSpeaker, CurrentGroup", STATE_NAME);
        agendaBuilder.mergeSpeaker();
        agendaBuilder.mergeGroup();
        eventSink.castEvent(EXIT);
    }
}
