package com.techinfocom.nvis.agendartftoxml.tablesm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.TextFormat;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class WatingForTableState<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    private static final String STATE_NAME = WatingForTableState.class.getSimpleName().toUpperCase();

    public static final Event TABLE_FOUND = new Event("TABLE_FOUND");
    private static final int AGENDA_TABLE_NUMBER = 2; //номер таблицы где лежит расписание

    private final AgendaBuilder agendaBuilder;
    private int tableCount = 0;
    SearchState state;

    public WatingForTableState(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        state = SearchState.WAITING_FOR_TABLE;// начальное состояние
    }

    @Override
    public void processChar(FormatedChar fc) {
        if (!fc.getTextFormat().paragraphContain(Command.intbl)) { //любой символ без признака того. что он находится в таблице
            state = SearchState.WAITING_FOR_TABLE;
        }
    }


    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (state) {
            case WAITING_FOR_TABLE:
                switch (rtfCommand.getCommand()) {
                    case trowd:
                        tableCount++;
                        state = SearchState.WAITING_FOR_TABLE_END;
                        break;
                }
                break;
            case WAITING_FOR_TABLE_END:
                switch (rtfCommand.getCommand()) {
                    case par:
                        processChar(new FormatedChar('\n', textFormat));
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
        WAITING_FOR_TABLE_END;
    }
}
