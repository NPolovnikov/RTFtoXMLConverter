package com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm;

import com.techinfocom.nvis.agendartftoxml.statemachine.AbstractAutomationBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.states.*;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class Cell3ParserImpl extends AbstractAutomationBase<Cell3Parser> implements Cell3Parser {
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;

    public Cell3ParserImpl(AgendaBuilder agendaBuilder) {
        //создание объектов-состояний
        Cell3Parser text = new Text<>(this, this, agendaBuilder);
        Cell3Parser waitForSpeakers = new WaitForSpeakers<>(this, this, agendaBuilder);
        Cell3Parser speakType = new SpeakType<>(this, this, agendaBuilder);
        Cell3Parser post = new Post<>(this, this, agendaBuilder);
        Cell3Parser name = new Name<>(this, this, agendaBuilder);
        Cell3Parser waitForNextSpeakers = new WaitForNextSpeakers<>(this, this, agendaBuilder);

        //Создание переходов
        addEdge(text, Text.PAR_FOUND, waitForSpeakers);
        addEdge(waitForSpeakers, WaitForSpeakers.NO_SPEAKERS, text);
        addEdge(waitForSpeakers, WaitForSpeakers.SPEAKERS_FOUND, speakType);
        addEdge(speakType, SpeakType.POST_FOUND, post);
        addEdge(post, Post.NAME_FOUND, name);
        addEdge(post, Post.PAR_FOUND, waitForNextSpeakers);
        addEdge(name, Name.POST_FOUND, post);
        addEdge(name, Name.PAR_FOUND, waitForNextSpeakers);
        addEdge(waitForNextSpeakers, WaitForNextSpeakers.NEW_SPEAKER_GROUP_FOUND, speakType);
        addEdge(waitForNextSpeakers, WaitForNextSpeakers.POST_FOUND, post);
        addEdge(waitForNextSpeakers, WaitForNextSpeakers.NAME_FOUND, name);

        addEdge(text, Text.EXIT, waitForSpeakers);
        addEdge(speakType, SpeakType.EXIT, waitForSpeakers);
        addEdge(name, Name.EXIT, waitForSpeakers);
        addEdge(waitForNextSpeakers, WaitForNextSpeakers.EXIT, waitForSpeakers);
        addEdge(post, Post.EXIT, waitForSpeakers);


        //Начальное состояние
        state = waitForSpeakers; //может начаться прямо с докладчиков.

    }

    public static Cell3Parser createAutomation(AgendaBuilder agendaBuilder) {
        return new Cell3ParserImpl(agendaBuilder);
    }

    @Override
    public void processChar(final FormatedChar fc) {
        state.processChar(fc);
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        state.processCommand(rtfCommand, textFormat);
//    }

    @Override
    public void analyseFormat(final FormatedChar fc) {
        state.analyseFormat(fc);
    }

    @Override
    public void exit() {
        state.exit();
    }
}
