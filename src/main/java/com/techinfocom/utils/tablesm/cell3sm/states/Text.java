package com.techinfocom.utils.tablesm.cell3sm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class Text<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    public static final Event PAR_FOUND = new Event("PAR_FOUND");

    private final AgendaBuilder agendaBuilder;

    public Text(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        String text = agendaBuilder.getCurrentItem().getText();
        if (text == null) {
            text = "";
        }
        agendaBuilder.getCurrentItem().setText(text + string);
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case par:
                eventSink.castEvent(PAR_FOUND);
                break;
            case cell:
                agendaBuilder.mergeCurrentGroup();
                break;
        }

    }

    @Override
    public void analyseFormat(String string, TextFormat textFormat) {

    }
}
