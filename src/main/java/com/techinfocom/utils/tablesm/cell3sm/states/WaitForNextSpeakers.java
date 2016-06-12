package com.techinfocom.utils.tablesm.cell3sm.states;

import com.rtfparserkit.rtf.Command;
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
public class WaitForNextSpeakers<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = WaitForNextSpeakers.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.utils.Logger.LOGGER;
    public static final Event NEW_SPEAKER_GROUP_FOUND = new Event("NEW_SPEAKER_GROUP_FOUND");
    public static final Event EXIT = new Event("EXIT");
    private final AgendaBuilder agendaBuilder;

    public WaitForNextSpeakers(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        //eventSink.castEvent(ERROR); // TODO: 11.06.2016 не может быть тут текста
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        switch (rtfCommand.getCommand()) {
//            case cell:
//                //выходим, конец ячейки
//                break;
//        }
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        //подчеркнутый, не наклонный. Еще группа докладчиков
        if (fc.getTextFormat().fontContain(ul) &&
                !fc.getTextFormat().fontContain(i)) {
            LOGGER.debug("state={}. Обнаружен подчеркнутый, ненаклонный текст ''. Созданы CurrentGroup", STATE_NAME, fc.getC());
            agendaBuilder.newCurrentGroup();
            eventSink.castEvent(NEW_SPEAKER_GROUP_FOUND);
        }
    }

    @Override
    public void exit() {
        LOGGER.debug("state={}. Получен сигнал о завершении ячейки. Объединены CurrentSpeaker, CurrentGroup", STATE_NAME);
        agendaBuilder.mergeCurrentSpeaker();
        agendaBuilder.mergeCurrentGroup();
        eventSink.castEvent(EXIT);
    }
}
