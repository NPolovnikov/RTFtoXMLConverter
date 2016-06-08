package com.techinfocom.utils.tablesm;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
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
        TableParser cell1 = new Cell1State<>(this, this, agendaBuilder);
        TableParser cell2 = new Cell2State<>(this, this, agendaBuilder);
        TableParser cell3 = new Cell3State<>(this, this, agendaBuilder);
        TableParser waitForRowEnd = new WaitForRowEndState<>(this, this, agendaBuilder);
        TableParser waitForTableContinue = new WatingForTableContinueState<>(this, this, agendaBuilder);
        TableParser parsingDone = new ParsingDoneState<>(this, this, agendaBuilder);



        //Создание переходов
        addEdge(waitingForTable, WatingForTableState.TABLE_FOUND, cell1);
        addEdge(cell1, Cell1State.NEXT_CELL, cell2);
        addEdge(cell2, Cell2State.NEXT_CELL, cell3);
        addEdge(cell3, Cell3State.NEXT_CELL, waitForRowEnd);
        addEdge(waitForRowEnd, WaitForRowEndState.ROW_END, waitForTableContinue);
        addEdge(waitForTableContinue, WatingForTableContinueState.NEXT_ROW, cell1);
        addEdge(waitForTableContinue, WatingForTableContinueState.TABLE_END, parsingDone);

        //Начальное состояние
        state = waitingForTable;
    }

    // Создание экземпляра автомата
    public static TableParser createAutomaton() {
        return new TableParserImpl();
    }

    // Делегирование методов интерфейса
    @Override
    public void processString(String string, TextFormat textFormat) {
        state.processString(string, textFormat);
    }

//    @Override
//    public void processingDocEvent(DocEvent event) {
//        state.processingDocEvent(event);
//    }

    @Override
    public void processCommand(RtfCommand rtfCommand) {
        state.processCommand(rtfCommand);
    }
}

