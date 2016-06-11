package com.techinfocom.utils.tablesm.cell3sm.states;

import com.techinfocom.utils.FormatedChar;
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
public class Post<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    public static final Event NAME_FOUND = new Event("NAME_FOUND");

    private final AgendaBuilder agendaBuilder;

    public Post(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        String currentPost = agendaBuilder.getCurrentSpeaker().getPost();
        agendaBuilder.getCurrentSpeaker().setPost(currentPost + String.valueOf(fc.getC()).replace("\n", "\r\n"));
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        //жирный текст- ФИО докладчика
        if (fc.getTextFormat().fontContain(b)) {
            eventSink.castEvent(NAME_FOUND);
        }
    }

    @Override
    public void endOfCell() {

    }
}
