package com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.states;

import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.Cell3Parser;
import org.slf4j.Logger;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class Post<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = Post.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    public static final Event NAME_FOUND = new Event("NAME_FOUND");
    public static final Event EXIT = new Event("EXIT");
    public static final Event END_OF_SPEAKER_GROUP = new Event("END_OF_SPEAKER_GROUP");

    private final AgendaBuilder agendaBuilder;

    public Post(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
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
            String currentPost = agendaBuilder.getSpeaker().getPost();
            if (currentPost == null) {
                currentPost = "";
            }
            agendaBuilder.getSpeaker().setPost(currentPost + String.valueOf(fc.getC()).replace("\n", "\r\n"));
        }
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
