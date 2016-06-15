package com.techinfocom.utils.tablesm.cell3sm.states;

import com.techinfocom.utils.model.FormatedChar;
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
public class Post<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = Post.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.utils.Logger.LOGGER;
    public static final Event NAME_FOUND = new Event("NAME_FOUND");
    public static final Event EXIT = new Event("EXIT");

    private final AgendaBuilder agendaBuilder;

    public Post(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        String currentPost = agendaBuilder.getSpeaker().getPost();
        if (currentPost == null) {
            currentPost = "";
        }
        agendaBuilder.getSpeaker().setPost(currentPost + String.valueOf(fc.getC()).replace("\n", "\r\n"));
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        //жирный текст- ФИО докладчика
        if (fc.getTextFormat().fontContain(b) &&
                fc.getC() != '\n' && fc.getC() != ' ') {
            LOGGER.debug("state={}. Обнаружен жирный текст '{}'.", STATE_NAME, fc.getC());
            eventSink.castEvent(NAME_FOUND);
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
