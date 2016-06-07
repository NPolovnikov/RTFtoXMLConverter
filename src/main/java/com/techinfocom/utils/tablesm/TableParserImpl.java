package com.techinfocom.utils.tablesm;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.AutomationBase;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.tablesm.states.*;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class TableParserImpl extends AutomationBase<TableParser> implements TableParser {

    public TableParserImpl() {
        AgendaBuilder agendaBuilder = new AgendaBuilder();

        //создание объектов-состояний
        TableParser waitingForTable = new WatingForTableState<>(this, this, agendaBuilder);
        TableParser tableFound = new TableFoundState<>(this, this, agendaBuilder);
        TableParser cell1 = new Cell1State<>(this, this, agendaBuilder);
        TableParser cell2 = new Cell2State<>(this, this, agendaBuilder);
        TableParser cell3 = new Cell3State<>(this, this, agendaBuilder);
        TableParser waitForCell1 = new WaitCell1State<>(this, this, agendaBuilder);
        TableParser waitForCell2 = new WaitCell2State<>(this, this, agendaBuilder);
        TableParser waitForCell3 = new WaitCell3State<>(this, this, agendaBuilder);

        //Создание переходов
        addEdge(waitingForTable, WatingForTableState.TABLE_FOUND, tableFound);
        addEdge(tableFound, TableFoundState.ROW_FOUND, waitForCell1);
        addEdge(waitForCell1, WaitCell1State.IN_CELL, cell1);
        addEdge(cell1, Cell1State.NEXT_CELL, waitForCell2);
        addEdge(waitForCell2, WaitCell2State.IN_CELL, cell2);
        addEdge(cell2, Cell2State.NEXT_CELL, waitForCell3);
        addEdge(waitForCell3, WaitCell3State.IN_CELL, cell3);
        addEdge(cell3, Cell3State.ROW_END, tableFound);
//        addEdge(tableFound, TableFoundState.TABLE_END, parsingDone);

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
