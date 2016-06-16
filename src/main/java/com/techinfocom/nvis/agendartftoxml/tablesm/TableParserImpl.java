package com.techinfocom.nvis.agendartftoxml.tablesm;

import com.techinfocom.nvis.agendartftoxml.model.*;
import com.techinfocom.nvis.agendartftoxml.statemachine.AutomationBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.states.*;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class TableParserImpl extends AutomationBase<TableParser> implements TableParser {

    public TableParserImpl(AgendaBuilder agendaBuilder) {

        //создание объектов-состояний
        TableParser waitingForTable = new WatingForTableState<>(this, this, agendaBuilder);
        TableParser cell1 = new Cell1State<>(this, this, agendaBuilder);
        TableParser cell2 = new Cell2State<>(this, this, agendaBuilder);
        TableParser cell3 = new Cell3State<>(this, this, agendaBuilder);
        TableParser waitForRowEnd = new WaitForRowEndState<>(this, this, agendaBuilder);
        TableParser parsingDone = new ParsingDoneState<>(this, this, agendaBuilder);



        //Создание переходов
        addEdge(cell1, Cell1State.CELL_END, cell2);
        addEdge(cell2, Cell2State.CELL_END, cell3);
        addEdge(cell3, Cell3State.CELL_END, waitForRowEnd);
        addEdge(waitForRowEnd, WaitForRowEndState.ROW_END, cell1);
        addEdge(cell1, Cell1State.TABLE_END, parsingDone);
        addEdge(cell1, Cell1State.ROW_END, cell1);
        addEdge(cell2, Cell2State.ROW_END, cell1);
        addEdge(cell3, Cell3State.ROW_END, cell1);
        addEdge(waitForRowEnd, WaitForRowEndState.CELL_END, cell1);

        //Начальное состояние
        state = cell1;
    }

    // Создание экземпляра автомата
    public static TableParser createAutomaton(AgendaBuilder agendaBuilder) {
        return new TableParserImpl(agendaBuilder);
    }

    // Делегирование методов интерфейса

    @Override
    public void processWord(RtfWord rtfWord) {
        state.processWord(rtfWord);
    }

    @Override
    public void exit() {
        state.exit();
    }
}

