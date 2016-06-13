package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;
import com.techinfocom.utils.tablesm.cell3sm.states.Name;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell1State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final String STATE_NAME = Cell1State.class.getSimpleName().toUpperCase();
    public static final Event CELL_END = new Event("CELL_END");
    public static final Event TABLE_END = new Event("TABLE_END");
    AgendaBuilder agendaBuilder;
    StringBuilder collected;

    public Cell1State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        collected = new StringBuilder();
    }

    @Override
    public void processChar(FormatedChar fc) {
        if (fc.getTextFormat().paragraphContain(intbl)) {
            collected.append(fc.getC());
        } else {
            eventSink.castEvent(TABLE_END);
        }
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case par:
                processChar(new FormatedChar('\n', textFormat));
                break;
            case cell:
                System.err.println("Состояние Cell1State, поймано событие cell"); // TODO: 08.06.2016 Убрать везде system.err
                String conformed = agendaBuilder.conformString(collected.toString());
                if (!conformed.equals("")) { //пустые строки не пускаем. иначе возникает несоответствие схеме.
                    agendaBuilder.getCurrentItem().setNumber(conformed);
                }
                collected = new StringBuilder();//почистим для применения при следующем входе.
                eventSink.castEvent(CELL_END);
                break;
            case row:
                //todo игнорировать данные
                break;
        }
    }
}
