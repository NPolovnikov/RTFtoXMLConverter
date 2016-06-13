package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;

import static com.rtfparserkit.rtf.Command.intbl;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell2State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final String STATE_NAME = Cell2State.class.getSimpleName().toUpperCase();
    public static final Event CELL_END = new Event("CELL_END");
    AgendaBuilder agendaBuilder;
    StringBuilder collected;


    public Cell2State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        collected = new StringBuilder();
    }

    @Override
    public void processChar(FormatedChar fc) {
        if (fc.getTextFormat().paragraphContain(intbl)) {
            collected.append(fc.getC());
        }
    }


    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case par:
                processChar(new FormatedChar('\n', textFormat));
                break;
            case cell:
                System.err.println("Состояние Cell2State, поймано событие cell");
                String conformed = agendaBuilder.conformString(collected.toString());
                if(!conformed.equals("")) {
                    agendaBuilder.getCurrentItem().setInfo(conformed);
                }
                collected = new StringBuilder();
                eventSink.castEvent(CELL_END);
                break;
            case row:
                // TODO: 13.06.2016 игнорировать данные row
                break;
        }
    }

}
