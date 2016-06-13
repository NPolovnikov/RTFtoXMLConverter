package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.*;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;
import com.techinfocom.utils.tablesm.cell3sm.Cell3ParserImpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell3State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final String STATE_NAME = Cell3State.class.getSimpleName().toUpperCase();
    public static final Event CELL_END = new Event("CELL_END");
    private final AgendaBuilder agendaBuilder;
    private Cell3Parser cell3Parser;
    private ParTrimmer parTrimmer;

    public Cell3State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        init();
    }

    private void init() {
        cell3Parser = Cell3ParserImpl.createAutomation(this.agendaBuilder);
        parTrimmer = new ParTrimmer();
    }

    @Override
    public void processChar(FormatedChar fc) {
        if (!fc.getTextFormat().paragraphContain(intbl)) {
            return; //наличие обязательно
        }
        FormatedChar trimmedChar = parTrimmer.trim(fc);
        if (trimmedChar != null) {
            cell3Parser.analyseFormat(trimmedChar);
            cell3Parser.processChar(trimmedChar);

        }
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case par:
                processChar(new FormatedChar('\n', textFormat));
                break;
            case cell:
                System.err.println("Состояние Cell3State, поймано событие cell.");

                FormatedChar trimmedChar = parTrimmer.finish();
                if (trimmedChar != null) {
                    //поскольку формат текста отслеживается отдельным хендлером, следут сначала проанализировать
                    //формат и принять решение о характере обработки текста, потом только обработать.
                    // то есть смена состояния автомата должна предшествовать обработке текста.
                    cell3Parser.analyseFormat(trimmedChar);
                    cell3Parser.processChar(trimmedChar);
                }
                cell3Parser.exit();

                init();//переинициализируем для следующего применения.
                eventSink.castEvent(CELL_END);
                break;
            case row:
                // TODO: 13.06.2016 игнорировать? два же столбца оказывается
                break;
            default:
                break;
        }
    }

}
