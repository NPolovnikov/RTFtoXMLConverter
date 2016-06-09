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
import java.util.function.Function;
import java.util.function.Supplier;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell1State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event NEXT_CELL = new Event("NEXT_CELL");
    AgendaBuilder agendaBuilder;
    String collected;

    public Cell1State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        collected = "";
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        if (textFormat.getParagraphFormat().stream().anyMatch(c -> c.getCommand() == intbl)) {
            collected += string;
        }
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case par:
                processString("\r\n", textFormat);
                break;
            case cell:
                System.err.println("Состояние Cell1State, поймано событие cell"); // TODO: 08.06.2016 Убрать везде system.err
                String conformed = agendaBuilder.conformString(collected);
                agendaBuilder.getCurrentItem().setNumber(conformed);
                collected = "";//почистим для применения при следующем входе.
                eventSink.castEvent(NEXT_CELL);
                break;
        }
    }
}
