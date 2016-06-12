package com.techinfocom.utils.tablesm;

import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.AutomationBase;
import com.techinfocom.utils.tablesm.states.*;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class TableParserImpl extends AutomationBase<TableParser> implements TableParser {

    public TableParserImpl() {
        AgendaBuilder agendaBuilder = new AgendaBuilder();

        //создание объектов-состояний
        TableParser waitingForTable = new WatingForTableState<>(this, this, agendaBuilder);
        TableParser cell1 = new Cell1State<>(this, this, agendaBuilder);
        TableParser cell2 = new Cell2State<>(this, this, agendaBuilder);
        TableParser cell3 = new Cell3State<>(this, this, agendaBuilder);
        TableParser waitForRowEnd = new WaitForRowEndState<>(this, this, agendaBuilder);
        TableParser watingForNextRow = new WatingForNextRowState<>(this, this, agendaBuilder);
        TableParser parsingDone = new ParsingDoneState<>(this, this, agendaBuilder);



        //Создание переходов
        addEdge(waitingForTable, WatingForTableState.TABLE_FOUND, cell1);
        addEdge(cell1, Cell1State.CELL_END, cell2);
        addEdge(cell2, Cell2State.CELL_END, cell3);
        addEdge(cell3, Cell3State.CELL_END, waitForRowEnd);
        addEdge(waitForRowEnd, WaitForRowEndState.ROW_END, cell1);
        addEdge(cell1, Cell1State.TABLE_END, parsingDone);

        //addEdge(watingForNextRow, WatingForNextRowState.NEXT_ROW, cell1);
        //addEdge(watingForNextRow, WatingForNextRowState.TABLE_END, parsingDone);

        //Начальное состояние
        state = waitingForTable;
    }

    // Создание экземпляра автомата
    public static TableParser createAutomaton() {
        return new TableParserImpl();
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

