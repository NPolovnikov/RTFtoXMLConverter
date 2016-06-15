package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.model.FormatedChar;
import com.techinfocom.utils.model.RtfCommand;
import com.techinfocom.utils.model.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;
import org.slf4j.Logger;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell1State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    private static final Logger LOGGER = com.techinfocom.utils.Logger.LOGGER;
    private static final String STATE_NAME = Cell1State.class.getSimpleName().toUpperCase();
    public static final Event CELL_END = new Event("CELL_END");
    public static final Event TABLE_END = new Event("TABLE_END");
    public static final Event ROW_END = new Event("ROW_END");
    private final AgendaBuilder agendaBuilder;
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
                    agendaBuilder.getAgendaItem().setNumber(conformed);
                }
                collected = new StringBuilder();//почистим для применения при следующем входе.
                eventSink.castEvent(CELL_END);
                break;
            case row:
                LOGGER.info("Неожиданный конец строки таблицы. контекст = {}. Данные строки проигнорированы", "");// TODO: 13.06.2016 включить контекст
                agendaBuilder.dropAgendaItem();//Если что-то успели насобирать- забудем.
                agendaBuilder.newAgendaItem();
                collected = new StringBuilder();//почистим
                eventSink.castEvent(ROW_END);//смены статуса не произойдет. Так, для порядка.
                break;
        }
    }
}
