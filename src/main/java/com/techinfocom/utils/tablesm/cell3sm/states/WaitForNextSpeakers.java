package com.techinfocom.utils.tablesm.cell3sm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class WaitForNextSpeakers<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {

    public static final Event UL = new Event("SPEAKERS_FOUND");
    private final AgendaBuilder agendaBuilder;

    public WaitForNextSpeakers(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {

    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()){
            case cell:
                //выходим, конец ячейки
                break;
        }
    }

    @Override
    public void analyseFormat(String string, TextFormat textFormat) {
        //Еще доклад есть
        if(textFormat.getFontFormat().stream().anyMatch(c->c.getCommand() == Command.ul)){
            agendaBuilder.newCurrentGroup();
            agendaBuilder.getCurrentGroup().setGroupName("");//инициализируем тип доклада
            eventSink.castEvent(UL);
        }
    }

}
