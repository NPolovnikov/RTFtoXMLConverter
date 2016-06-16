package com.techinfocom.nvis.agendartftoxml.docsm;

import com.techinfocom.nvis.agendartftoxml.docsm.states.*;
import com.techinfocom.nvis.agendartftoxml.model.*;
import com.techinfocom.nvis.agendartftoxml.statemachine.AutomationBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParserImpl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class DocParserImpl extends AutomationBase<DocParserState> implements DocParser {

    public DocParserImpl(AgendaBuilder agendaBuilder) {
        //создание автомата парсинга таблицы
        TableParser tableParser = TableParserImpl.createAutomaton(agendaBuilder);

        Queue<RtfWord> dataBuffer = new ConcurrentLinkedQueue<>();

        //создание объектов-состояний
        DocParserState dataSearching = new DataSearching<>(this, this, dataBuffer, agendaBuilder, tableParser);
        RowBuffering rowBuffering = new RowBuffering<>(this, this, dataBuffer, agendaBuilder, tableParser);
        AgendaPassthrough agendaPassthrough = new AgendaPassthrough<>(this, this, dataBuffer, agendaBuilder, tableParser);
        PassOver passOver = new PassOver<>(this, this, dataBuffer, agendaBuilder, tableParser);

        //Создание переходов
        addEdge(dataSearching, DataSearching.SOME_ROW_FOUND, rowBuffering);//нашлась какя-то строка, какой-то таблицы. Надо начать собирать ее в буфер.
        addEdge(rowBuffering, RowBuffering.AGENDA_FOUND, agendaPassthrough);
        addEdge(rowBuffering, RowBuffering.TABLE_END_FOUND, dataSearching);
        addEdge(agendaPassthrough, AgendaPassthrough.AGENDA_END_FOUND, passOver);

        //Начальное состояние
        state = dataSearching;
    }

    // Создание экземпляра автомата
    public static DocParser createAutomaton(AgendaBuilder agendaBuilder) {
        return new DocParserImpl(agendaBuilder);
    }

    // Делегирование методов интерфейса

    @Override
    public void processWord(RtfWord rtfWord) {
        //сначала анализ и смена состояния, потом обработка.
        state.analyseWord(rtfWord);
        state.processWord(rtfWord);
    }
}

