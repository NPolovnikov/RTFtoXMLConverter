package com.techinfocom.nvis.agendartftoxml.tablesm.states;

import com.techinfocom.nvis.agendartftoxml.Logger;
import com.techinfocom.nvis.agendartftoxml.ParTrimmer;
import com.techinfocom.nvis.agendartftoxml.model.*;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.Cell3ParserImpl;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.Cell3Parser;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell3State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final org.slf4j.Logger LOGGER = Logger.LOGGER;
    private static final String STATE_NAME = Cell3State.class.getSimpleName().toUpperCase();
    public static final Event CELL_END = new Event("CELL_END");
    public static final Event ROW_END = new Event("ROW_END");
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
    public void processWord(RtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case COMMAND:
                processCommand((RtfCommand) rtfWord);
                break;
            case CHAR:
                processChar((FormatedChar) rtfWord);
                break;
        }
    }

    @Override
    public void exit() {

    }


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

    public void processCommand(RtfCommand rtfCommand) {
        TextFormat textFormat = rtfCommand.getTextFormat();
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
                LOGGER.info("Неожиданный конец строки таблицы. контекст = {}. Данные строки проигнорированы", "");// TODO: 13.06.2016 включить контекст
                cell3Parser.exit();
                parTrimmer.finish();
                agendaBuilder.dropAgendaItem();//Если что-то успели насобирать- забудем.
                agendaBuilder.newAgendaItem();
                init();//переинициализируем для следующего применения.
                eventSink.castEvent(ROW_END);
                break;
            default:
                break;
        }
    }

}
