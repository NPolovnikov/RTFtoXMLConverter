package com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm;

import com.techinfocom.nvis.agendartftoxml.statemachine.AutomationBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.states.*;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class Cell3ParserImpl extends AutomationBase<Cell3Parser> implements Cell3Parser {
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
        addEdge(name, Name.POST_FOUND, post);
        addEdge(name, Name.END_OF_SPEAKER_GROUP, waitForNextSpeakers);
        addEdge(waitForNextSpeakers, WaitForNextSpeakers.NEW_SPEAKER_GROUP_FOUND, speakType);

        addEdge(waitForSpeakers, WaitForSpeakers.EXIT, text);
        addEdge(speakType, SpeakType.EXIT, text);
        addEdge(name, Name.EXIT, text);
        addEdge(waitForNextSpeakers, WaitForNextSpeakers.EXIT, text);
        addEdge(post, Post.EXIT, text);


        //Начальное состояние
        state = text;

    }

    public static Cell3Parser createAutomation(AgendaBuilder agendaBuilder) {
        return new Cell3ParserImpl(agendaBuilder);
    }

    @Override
    public void processChar(FormatedChar fc) {
        state.processChar(fc);
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        state.processCommand(rtfCommand, textFormat);
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        state.analyseFormat(fc);
    }

    @Override
    public void exit() {
        state.exit();
    }
}
