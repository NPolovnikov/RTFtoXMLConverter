package com.techinfocom.utils.tablesm.cell3sm.states;

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
public class WaitForSpeakers<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    public final static Event NO_SPEAKERS = new Event("NO_SPEAKERS");
    public final static Event SPEAKERS_FOUND = new Event("SPEAKERS_FOUND");

    private final AgendaBuilder agendaBuilder;

    public WaitForSpeakers(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
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
                agendaBuilder.mergeCurrentGroup();
                break;
        }

    }

    @Override
    public void analyseFormat(String string, TextFormat textFormat) {
        //если поймали подчеркнутый, НЕ НАКЛОННЫЙ текст, и первый символ не пробел, то это тип доклада, и началось описание докладчиков.
        if (textFormat.getFontFormat().stream().anyMatch(c -> c.getCommand() == ul) &&
                textFormat.getFontFormat().stream().noneMatch(c->c.getCommand() == i) &&
                !string.startsWith(" ")) {
            //создадим новую группу докладчиков и инициализируем тип доклада
            agendaBuilder.newCurrentGroup();
            agendaBuilder.getCurrentGroup().setGroupName("");

            eventSink.castEvent(SPEAKERS_FOUND);
        } else {
            //не подчеркнутый текст, это продолжение text
            eventSink.castEvent(NO_SPEAKERS);
        }
    }

}
