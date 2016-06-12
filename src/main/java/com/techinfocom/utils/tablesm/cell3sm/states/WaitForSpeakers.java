package com.techinfocom.utils.tablesm.cell3sm.states;

import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;
import org.slf4j.Logger;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class WaitForSpeakers<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = Name.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.utils.Logger.LOGGER;
    public final static Event NO_SPEAKERS = new Event("NO_SPEAKERS");
    public final static Event SPEAKERS_FOUND = new Event("SPEAKERS_FOUND");

    private final AgendaBuilder agendaBuilder;

    public WaitForSpeakers(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {

    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        switch (rtfCommand.getCommand()) {
//            case cell:
//                agendaBuilder.mergeCurrentGroup();
//                break;
//        }
//
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        //если поймали подчеркнутый, НЕ НАКЛОННЫЙ текст, то это тип доклада, и началось описание докладчиков.
        if (fc.getTextFormat().fontContain(ul) &&
                !fc.getTextFormat().fontContain(i)) {
            //создадим новую группу докладчиков и инициализируем тип доклада
            LOGGER.debug("state={}. Обнаружен подчеркнутый, ненаклонный текст ''. Созданы CurrentGroup", STATE_NAME, fc.getC());
            agendaBuilder.newCurrentGroup();
            eventSink.castEvent(SPEAKERS_FOUND);
        } else {
            //не подчеркнутый текст, это продолжение text
            //восстановим перевод строки, принятый за сигнал
            LOGGER.debug("state={}. подчеркнутого текста не найдено.", STATE_NAME);
            String text = agendaBuilder.getCurrentItem().getText();
            if (text == null) {
                text = "";
            }
            agendaBuilder.getCurrentItem().setText(text + "\r\n");

            eventSink.castEvent(NO_SPEAKERS);
        }
    }

    @Override
    public void exit() {

    }
}
