package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.model.agenda.Group;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;
import com.techinfocom.utils.tablesm.cell3sm.Cell3ParserImpl;

import java.util.List;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell3State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event NEXT_CELL = new Event("NEXT_CELL");
    private final AgendaBuilder agendaBuilder;
    private Cell3Parser cell3Parser;
    private EventSink cell3ParserEeventSink;

    public Cell3State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        init();
    }

    private void init() {
        cell3Parser = Cell3ParserImpl.createAutomation(this.agendaBuilder);
        cell3ParserEeventSink = (EventSink) cell3Parser;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {

        System.err.println(textFormat.toString());
        if (!textFormat.getParagraphFormat().stream().anyMatch(c -> c.getCommand() == intbl)) {
            return; //наличие обязательно
        }

        //поскольку формат текста отслеживается отдельным хендлером, следут сначала проанализировать
        //формат и принять решение о характере обработки текста, потом только обработать.
        // то есть смена состояния автомата должна предшествовать обработке текста.
        cell3Parser.analyseFormat(textFormat);
        cell3Parser.processString(string, textFormat);
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case cell:
                System.err.println("Состояние Cell3State, поймано событие cell.");
                cell3Parser.processCommand(rtfCommand, textFormat);//заканчивай там, ячейка кончилась.
                init();//переинициализируем для следующего применения.
                eventSink.castEvent(NEXT_CELL);
                break;
            default:
                cell3Parser.processCommand(rtfCommand, textFormat);
                break;
        }
    }

}
