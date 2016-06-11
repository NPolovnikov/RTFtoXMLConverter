package com.techinfocom.utils.tablesm.cell3sm.states;

import com.rtfparserkit.rtf.Command;
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
public class WaitForNextSpeakers<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {

    public static final Event NEW_SPEAKER_GROUP_FOUND = new Event("NEW_SPEAKER_GROUP_FOUND");
    private final AgendaBuilder agendaBuilder;

    public WaitForNextSpeakers(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {
        //eventSink.castEvent(ERROR); // TODO: 11.06.2016 не может быть тут текста
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        switch (rtfCommand.getCommand()) {
//            case cell:
//                //выходим, конец ячейки
//                break;
//        }
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        //подчеркнутый, не наклонный. Еще группа докладчиков
        if (fc.getTextFormat().paragraphContain(ul) &&
                !fc.getTextFormat().paragraphContain(i)) {
            agendaBuilder.newCurrentGroup();
            agendaBuilder.getCurrentGroup().setGroupName("");//инициализируем тип доклада
            eventSink.castEvent(NEW_SPEAKER_GROUP_FOUND);
        }
    }

    @Override
    public void endOfCell() {

    }
}
