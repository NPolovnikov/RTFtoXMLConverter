package com.techinfocom.nvis.agendartftoxml.utils.tablesm.cell3sm.states;

import com.techinfocom.nvis.agendartftoxml.utils.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.utils.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.utils.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.utils.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.utils.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.utils.tablesm.cell3sm.Cell3Parser;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class SpeakType<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = SpeakType.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.utils.Logger.LOGGER;
    public static final Event POST_FOUND = new Event("POST_FOUND");
    public static final Event EXIT = new Event("EXIT");
    private final AgendaBuilder agendaBuilder;

    public SpeakType(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        if (fc.getC() == '\n') {
            LOGGER.error("state={}. НЕОЖИДАННО Обнаружен \\n.", STATE_NAME);
            //castEvent(ERROR); //todo не можем встретить перевод строки в этом состоянии. Тип доклада- однострочный.
        } else {
            //допишем тип доклада
            String currentGroupName = agendaBuilder.getGroup().getGroupName();
            if (currentGroupName == null) {
                currentGroupName = "";
            }
            agendaBuilder.getGroup().setGroupName(currentGroupName + String.valueOf(fc.getC()));
        }
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        //текст без форматирования- началась должность.
        if (fc.getTextFormat().getFontFormat().isEmpty() &&
                fc.getC() != '\n' && fc.getC() != ' ') {
            LOGGER.debug("state={}. Обнаружен текст '{}' без форматирования. Созданы CurrentSpeaker", STATE_NAME, fc.getC());
            agendaBuilder.newSpeaker();
            castEvent(POST_FOUND);
        }
    }

    @Override
    public void exit() {
        LOGGER.debug("state={}. Получен сигнал о завершении ячейки. Объединены CurrentGroup", STATE_NAME);
        agendaBuilder.mergeGroup();
        eventSink.castEvent(EXIT);
    }
}
