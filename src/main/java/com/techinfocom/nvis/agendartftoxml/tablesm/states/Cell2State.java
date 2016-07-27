package com.techinfocom.nvis.agendartftoxml.tablesm.states;

import com.techinfocom.nvis.agendartftoxml.model.*;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;
import org.slf4j.Logger;

import static com.rtfparserkit.rtf.Command.intbl;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell2State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    public static final Event CELL_END = new Event("CELL_END");
    public static final Event ROW_END = new Event("ROW_END");
    private final AgendaBuilder agendaBuilder;
    StringBuilder collected;


    public Cell2State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        collected = new StringBuilder();
    }

    @Override
    public void processWord(AbstractRtfWord rtfWord) {
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


    public void processChar(final FormatedChar fc) {
        if (fc.getTextFormat().paragraphContain(intbl)) {
            collected.append(fc.getC());
        }
    }


    public void processCommand(final RtfCommand rtfCommand) {
        final TextFormat textFormat = rtfCommand.getTextFormat();

        switch (rtfCommand.getCommand()) {
            case par:
                processChar(new FormatedChar('\n', textFormat));
                break;
            case cell:
                LOGGER.debug("Состояние Cell2State, поймано событие cell");
                String conformed = agendaBuilder.conformString(collected.toString());
                if (!conformed.equals("")) {
                    agendaBuilder.getAgendaItem().setInfo(conformed);
                }
                collected = new StringBuilder();
                eventSink.castEvent(CELL_END);
                break;
            case row:
                LOGGER.info("Неожиданный конец строки таблицы. контекст = {}. Данные строки проигнорированы", "");// TODO: 13.06.2016 включить контекст
                agendaBuilder.dropAgendaItem();//Если что-то успели насобирать- забудем.
                agendaBuilder.newAgendaItem();
                collected = new StringBuilder();//почистим
                eventSink.castEvent(ROW_END);
                break;
        }
    }

}
