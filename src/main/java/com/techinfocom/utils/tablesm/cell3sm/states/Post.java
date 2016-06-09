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
public class Post<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    public static final Event BOLD = new Event("BOLD");

    private final AgendaBuilder agendaBuilder;

    public Post(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        String currentPost = agendaBuilder.getCurrentSpeaker().getPost();
        agendaBuilder.getCurrentSpeaker().setPost(currentPost + string);
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {

    }

    @Override
    public void analyseFormat(String string, TextFormat textFormat) {
        //жирный текст- ФИО докладчика
        if(textFormat.getFontFormat().stream().anyMatch(c->c.getCommand() == b)){
            agendaBuilder.getCurrentSpeaker().setName("");
            eventSink.castEvent(BOLD);
        }
    }

}
