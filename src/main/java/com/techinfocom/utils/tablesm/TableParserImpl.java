package com.techinfocom.utils.tablesm;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.AutomationBase;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.tablesm.states.TableFoundState;
import com.techinfocom.utils.tablesm.states.WatingForTableState;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class TableParserImpl extends AutomationBase<TableParser> implements TableParser{

    public TableParserImpl() {
        AgendaBuilder agendaBuilder = new AgendaBuilder();

        //создание объектов-состояний
        TableParser waitingForTable = new WatingForTableState<>(this, this, agendaBuilder);
        TableParser tableFound = new TableFoundState<>(this, this, agendaBuilder);

        //Создание переходов
        addEdge(waitingForTable, WatingForTableState.TABLE_FOUND, tableFound);

        //Начальное состояние
        state = waitingForTable;
    }

    // Создание экземпляра автомата
    public static TableParser createAutomaton() {
        return new TableParserImpl();
    }

    // Делегирование методов интерфейса
    @Override
    public void processString(String string) {
        state.processString(string);
    }

    @Override
    public void processingDocEvent(DocEvent event) {
        state.processingDocEvent(event);
    }
}
