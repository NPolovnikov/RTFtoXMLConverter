package com.techinfocom.utils.tablesm.cell3sm;

import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.AutomationBase;
import com.techinfocom.utils.tablesm.cell3sm.states.*;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class Cell3ParserImpl extends AutomationBase<Cell3Parser> implements Cell3Parser {

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
        addEdge(name, Name.PLAIN_TEXT, post);
        addEdge(name, Name.END_OF_SPEAKER_GROUP, waitForNextSpeakers);
        addEdge(waitForNextSpeakers, WaitForNextSpeakers.NEW_SPEAKER_GROUP_FOUND, speakType);
        addEdge(name, Name.CELL_END, text);

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
    public void endOfCell() {

    }
}
