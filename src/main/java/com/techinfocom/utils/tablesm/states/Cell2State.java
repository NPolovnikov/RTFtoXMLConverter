package com.techinfocom.utils.tablesm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.rtfparserkit.rtf.Command.*;
import static com.rtfparserkit.rtf.Command.intbl;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell2State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event NEXT_CELL = new Event("NEXT_CELL");
    AgendaBuilder agendaBuilder;


    public Cell2State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        if (textFormat.getParagraphFormat().stream().anyMatch(c -> c.getCommand() == intbl)) {
            String current = agendaBuilder.getCurrentItem().getInfo();
            agendaBuilder.getCurrentItem().setInfo((current == null ? "" : current) + string);
        }
    }


    @Override
    public void processCommand(RtfCommand rtfCommand) {
        switch (rtfCommand.getCommand()) {
            case cell:
                System.err.println("Состояние Cell2State, поймано событие cell");
                eventSink.castEvent(NEXT_CELL);
                break;
        }
    }

}
