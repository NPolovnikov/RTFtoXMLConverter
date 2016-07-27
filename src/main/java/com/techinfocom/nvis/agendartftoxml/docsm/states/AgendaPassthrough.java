package com.techinfocom.nvis.agendartftoxml.docsm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParserState;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.AbstractRtfWord;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.TableParser;

import java.util.Queue;

/**
 * Created by volkov_kv on 15.06.2016.
 */
public class AgendaPassthrough<AI extends DocParser> extends StateBase<AI> implements DocParserState {
    public static final Event AGENDA_END_FOUND = new Event("AGENDA_END_FOUND");
    private final TableParser tableParser;

    State state; //легкое слежение за структурой таблицы, чтобы определить момент её завершения.

    public AgendaPassthrough(final AI automation, final EventSink eventSink, final TableParser tableParser) {
        super(automation, eventSink);
        this.tableParser = tableParser;

        initState();
    }

    void initState() {
        state = State.OUT_OF_ROW; //входим именно в этом состоянии
    }

    @Override
    public void analyseWord(AbstractRtfWord rtfWord) {
        switch (rtfWord.getRtfWordType()) {
            case COMMAND:
                RtfCommand rtfCommand = (RtfCommand) rtfWord;
                analyseRtfCommand(rtfCommand);
                break;
            case CHAR:
                FormatedChar fc = (FormatedChar) rtfWord;
                analyseChar(fc);
                break;
        }
    }

    @Override
    public void processWord(AbstractRtfWord rtfWord) {
        tableParser.processWord(rtfWord);
    }

    private void analyseChar(FormatedChar formatedChar) {
        switch (state) {
            case IN_ROW:
                if (!formatedChar.getTextFormat().paragraphContain(Command.intbl)) {
                    // TODO: 16.06.2016 это ошибка, такого быть не может.
                }
                break;
            case OUT_OF_ROW:
                if (!formatedChar.getTextFormat().paragraphContain(Command.intbl)) {
                    //нашли текст вне таблицы- таблица кончилась.
                    tableParser.exit();
                    eventSink.castEvent(AGENDA_END_FOUND);
                }
                break;
        }
    }

    private void analyseRtfCommand(RtfCommand rtfCommand) {
        switch (state) {
            case IN_ROW:
                switch (rtfCommand.getCommand()) {
                    case par:
                        if (!rtfCommand.getTextFormat().paragraphContain(Command.intbl)) {
                            // TODO: 16.06.2016 это ошибка, такого быть не может.
                        }
                        break;
                    case row:
                        state = State.OUT_OF_ROW;
                        break;
                }
                break;

            case OUT_OF_ROW:
                switch (rtfCommand.getCommand()) {
                    case par:
                        if (!rtfCommand.getTextFormat().paragraphContain(Command.intbl)) {
                            //нашли текст вне таблицы- таблица кончилась.
                            tableParser.exit();
                            eventSink.castEvent(AGENDA_END_FOUND);
                        } else {
                            //есть параграмф с индикатором принадлежности к таблице. Значит идет следующая строка.
                            state = State.IN_ROW;
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void processDocumentEnd() {
        //возможно, если после таблицы не будет ни одного символа
        tableParser.exit();
        eventSink.castEvent(AGENDA_END_FOUND);

    }

    private enum State {
        IN_ROW,
        OUT_OF_ROW
    }
}
