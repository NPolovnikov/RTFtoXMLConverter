package com.techinfocom.nvis.agendartftoxml.docsm;

import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.TextFormat;
import com.techinfocom.nvis.agendartftoxml.statemachine.AutomationBase;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.docsm.states.DataSearching;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class DocParserImpl extends AutomationBase<DocParser> implements DocParser {

    public DocParserImpl() {
        Queue<FormatedChar> dataBuffer = new ConcurrentLinkedQueue<>();

        //создание объектов-состояний
        DocParser dataSearching = new DataSearching<>(this, this, dataBuffer);


        //Создание переходов
        //addEdge(dataSearching, DataSearching.AGENDA_FOUND, dataSearching);

        //Начальное состояние
        state = dataSearching;
    }

    // Создание экземпляра автомата
    public static DocParser createAutomaton() {
        return new DocParserImpl();
    }

    // Делегирование методов интерфейса
    @Override
    public void processChar(FormatedChar fc) {
        state.processChar(fc);
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        state.processCommand(rtfCommand, textFormat);
    }
}

