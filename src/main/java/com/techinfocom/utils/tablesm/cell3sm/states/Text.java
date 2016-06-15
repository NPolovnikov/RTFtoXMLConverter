package com.techinfocom.utils.tablesm.cell3sm.states;

import com.techinfocom.utils.model.FormatedChar;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class Text<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = Text.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.utils.Logger.LOGGER;
    public static final Event PAR_FOUND = new Event("PAR_FOUND");

    private final AgendaBuilder agendaBuilder;

    public Text(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        if (fc.getC() == '\n') {
            LOGGER.debug("state={}. Обнаружен \\n. Ожидаем тип доклада", STATE_NAME);
            eventSink.castEvent(PAR_FOUND);
        } else {
            String text = agendaBuilder.getAgendaItem().getText();
            if (text == null) {
                text = "";
            }
            agendaBuilder.getAgendaItem().setText(text + String.valueOf(fc.getC()));
        }
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        switch (rtfCommand.getCommand()) {
//            case par:
//                eventSink.castEvent(PAR_FOUND);
//                break;
//            case cell:
//                agendaBuilder.mergeGroup();
//                break;
//        }
//
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {

    }

    @Override
    public void exit() {
        LOGGER.debug("state={}. Получен сигнал о завершении ячейки. Состояние не меняется", STATE_NAME);
        //поищем номер документа.
        agendaBuilder.docNumberExtractAndSave();
    }

}
