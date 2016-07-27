package com.techinfocom.nvis.agendartftoxml.docsm;

import com.techinfocom.nvis.agendartftoxml.docsm.states.*;
import com.techinfocom.nvis.agendartftoxml.model.*;
import com.techinfocom.nvis.agendartftoxml.statemachine.AbstractAutomationBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParserImpl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class DocParserImpl extends AbstractAutomationBase<DocParserState> implements DocParser {

    public DocParserImpl(final AgendaBuilder agendaBuilder) {
        //создание автомата парсинга таблицы
        final TableParser tableParser = TableParserImpl.createAutomaton(agendaBuilder);
        final Queue<AbstractRtfWord> dataBuffer = new ConcurrentLinkedQueue<>();

        //создание объектов-состояний
        final DocParserState dataSearching = new DataSearching<>(this, this, agendaBuilder);
        final RowBuffering rowBuffering = new RowBuffering<>(this, this, dataBuffer, agendaBuilder, tableParser);
        final AgendaPassthrough agendaPassthrough = new AgendaPassthrough<>(this, this, tableParser);
        PassOver passOver = new PassOver<>(this, this);

        //Создание переходов
        addEdge(dataSearching, DataSearching.SOME_ROW_FOUND, rowBuffering);//нашлась какя-то строка, какой-то таблицы. Надо начать собирать ее в буфер.
        addEdge(rowBuffering, RowBuffering.AGENDA_FOUND, agendaPassthrough);
        addEdge(rowBuffering, RowBuffering.TABLE_END_FOUND, dataSearching);
        addEdge(agendaPassthrough, AgendaPassthrough.AGENDA_END_FOUND, passOver);

        //Начальное состояние
        state = dataSearching;
    }

    // Создание экземпляра автомата
    public static DocParser createAutomaton(final AgendaBuilder agendaBuilder) {
        return new DocParserImpl(agendaBuilder);
    }

    // Делегирование методов интерфейса

    @Override
    public void processWord(final AbstractRtfWord rtfWord) {
        //сначала анализ и смена состояния, потом обработка.
        state.analyseWord(rtfWord);
        state.processWord(rtfWord);
    }

    @Override
    public void processDocumentEnd() {
        state.processDocumentEnd();
    }


}

