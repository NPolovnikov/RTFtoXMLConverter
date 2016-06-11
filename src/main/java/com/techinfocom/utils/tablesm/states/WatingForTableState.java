package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class WatingForTableState<AI extends TableParser> extends StateBase<AI> implements TableParser {

    public static final Event TABLE_FOUND = new Event("TABLE_FOUND");
    private static final int AGENDA_TABLE_NUMBER = 2; //номер таблицы где лежит расписание

    AgendaBuilder agendaBuilder;
    private int tableCount = 0;
    SearchState state;

    public WatingForTableState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        state = SearchState.WAITING_FOR_TABLE;// начальное состояние
    }

    @Override
    public void processChar(FormatedChar fc) {
        //ignore any strings
    }


    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (state) {
            case WAITING_FOR_TABLE:
                switch (rtfCommand.getCommand()) {
                    case trowd:
                        tableCount++;
                        state = SearchState.IN_TABLE;
                        break;
                }
                break;
            case IN_TABLE:
                switch (rtfCommand.getCommand()) {
                    case row: //row закончилась
                        state = SearchState.WAITING_FOR_TABLE_END;
                        break;
                }
                break;
            case WAITING_FOR_TABLE_END:
                switch (rtfCommand.getCommand()) {
                    case par: //таблица закончилась, если начинается новый параграф, после окончания row
                        state = SearchState.WAITING_FOR_TABLE;
                        break;
                    case trowd:
                        state = SearchState.IN_TABLE;
                        break;
                }
                break;
        }

        if (tableCount >= AGENDA_TABLE_NUMBER) {
            //нашли нужную таблицу.
            agendaBuilder.createAgenda();
            agendaBuilder.newAgendaItem();
            eventSink.castEvent(TABLE_FOUND);
        }
    }

    private enum SearchState {
        WAITING_FOR_TABLE,
        IN_TABLE,
        WAITING_FOR_TABLE_END;
    }
}
