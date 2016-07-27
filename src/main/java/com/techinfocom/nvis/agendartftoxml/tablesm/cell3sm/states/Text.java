package com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.states;

import com.rtfparserkit.rtf.Command;
import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.Cell3Parser;
import org.slf4j.Logger;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class Text<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = Text.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    public static final Event PAR_FOUND = new Event("PAR_FOUND");
    public static final Event EXIT = new Event("EXIT");

    private final AgendaBuilder agendaBuilder;

    public Text(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(final FormatedChar fc) {
        //перевод строки все равно запомним. Не дело восстанавливать из другого состояния, угадывая, что он был.
        String text = agendaBuilder.getAgendaItem().getText();
        if (text == null) {
            text = "";
        }
        //отловим возникновение или исчезновение верхнего, нижнего индекса и создадим нужные html теги
        if (fc.getTextFormat().getFontFormat().stream().anyMatch(c -> c.getCommand() == Command.supercmd) &&
                !agendaBuilder.isSupActive()) {
            agendaBuilder.setSupActive(true);
            text += "<sup>";
        }
        if (fc.getTextFormat().getFontFormat().stream().noneMatch(c -> c.getCommand() == Command.supercmd) &&
                agendaBuilder.isSupActive()) {
            agendaBuilder.setSupActive(false);
            text += "</sup>";
        }

        if (fc.getC() == '\n') {
            LOGGER.debug("state={}. Обнаружен \\n. Ожидаем тип доклада", STATE_NAME);

            //не выпускаем верхний индекс за пределы абзаца. Иначе потом неверно разбивается по элементам.
            //при следующем символе тек верхнего индекса будет проставлен заново.
            if (agendaBuilder.isSupActive()) {
                text += "</sup>";
                agendaBuilder.setSupActive(false);
            }

            eventSink.castEvent(PAR_FOUND);
        }

        agendaBuilder.getAgendaItem().setText(text + fc.getC());
    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        switch (rtfCommand.getCommand()) {
//            case par:
//                eventSink.castEvent(PAR_FOUND);
//                break;
//            case cell:
//                agendaBuilder.mergeGroup();
//                break;
//        }
//
//    }

    @Override
    public void analyseFormat(final FormatedChar fc) {

    }

    @Override
    public void exit() {
        LOGGER.debug("state={}. Получен сигнал о завершении ячейки.", STATE_NAME);

        //прикроем верхний индекс, если он оказался в самом конце ячейки, а значит не будет обнаружен
        if (agendaBuilder.isSupActive()) {
            agendaBuilder.setSupActive(false);
            agendaBuilder.getAgendaItem().setText(agendaBuilder.getAgendaItem().getText() + "</sup>");
        }

        //разберем на элементы
        agendaBuilder.splitTextToItem();
        eventSink.castEvent(EXIT);

    }

}
