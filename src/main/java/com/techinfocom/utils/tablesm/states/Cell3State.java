package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.DocEvent;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.model.agenda.Group;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.TableParser;

import java.util.List;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class Cell3State<AI extends TableParser> extends StateBase<AI> implements TableParser {
    public static final Event NEXT_CELL = new Event("NEXT_CELL");
    AgendaBuilder agendaBuilder;
    String rn; //регистрационный номер
    String text; //текст пункта порядка работы
    Group groupElement; //элемент списка выступающих
    List<Group> groupList; // список выступающих

    public Cell3State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        System.err.println("Состояние Cell3State, видим текст" + string);
        System.err.println(textFormat.toString());

    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case cell:
                System.err.println("Состояние Cell3State, поймано событие cell.");
                eventSink.castEvent(NEXT_CELL);
                break;
        }
    }

    //Для разбора текста третьей ячейки определим автомат состояний
    private enum Cell3States {
        WAIT_FOR_SPEAKERS,
        SPEAKERS
    }

}
