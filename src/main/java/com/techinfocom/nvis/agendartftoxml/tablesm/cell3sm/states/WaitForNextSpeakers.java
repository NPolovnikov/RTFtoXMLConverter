package com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.states;

import com.techinfocom.nvis.agendartftoxml.statemachine.EventSink;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.statemachine.Event;
import com.techinfocom.nvis.agendartftoxml.statemachine.StateBase;
import com.techinfocom.nvis.agendartftoxml.tablesm.cell3sm.Cell3Parser;
import org.slf4j.Logger;

import java.util.regex.Pattern;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class WaitForNextSpeakers<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = WaitForNextSpeakers.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    public static final Event NEW_SPEAKER_GROUP_FOUND = new Event("NEW_SPEAKER_GROUP_FOUND");
    public static final Event EXIT = new Event("EXIT");
    public static final Event POST_FOUND = new Event("POST_FOUND");
    public static final Event NAME_FOUND = new Event("NAME_FOUND");
    private final AgendaBuilder agendaBuilder;

    public WaitForNextSpeakers(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(final FormatedChar fc) {
        //текста тут быть не может. А если появился- значит нарушена структура.
        agendaBuilder.appendToIgnored(String.valueOf(fc.getC()));
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
    public void analyseFormat(final FormatedChar fc) {
        Pattern p = Pattern.compile("[a-zA-Zа-яА-ЯёЁ]");//только буква
        //подчеркнутый, не наклонный. Еще группа докладчиков
        if (fc.getTextFormat().fontContain(ul) &&
                !fc.getTextFormat().fontContain(i) &&
                p.matcher(String.valueOf(fc.getC())).matches()) {
            LOGGER.debug("state={}. Обнаружен подчеркнутый, ненаклонный текст ''. Это тип очередного доклада. Объединены CurrentSpeaker, CurrentGroup. Созданы CurrentGroup", STATE_NAME, fc.getC());
            agendaBuilder.mergeSpeaker();
            agendaBuilder.mergeGroup();
            agendaBuilder.newGroup();
            eventSink.castEvent(NEW_SPEAKER_GROUP_FOUND);
        }

        //неформатированый- новый докладчик в текущем докладе
        if (fc.getTextFormat().getFontFormat().isEmpty() &&
                fc.getC() != '\n' && fc.getC() != ' ') {    //но не перевод и не пробелы, иначе можно ошибочно переключиться в ожидание следующей должности
            LOGGER.debug("state={}. Обнаружен неформатированный текст '{}'. Это должность очередного докладчика. Объединены CurrentSpeaker, создан новый CurrentSpeaker", STATE_NAME, fc.getC());
            agendaBuilder.mergeSpeaker();
            agendaBuilder.newSpeaker();
            eventSink.castEvent(POST_FOUND);
        }

        //жирный текст- ФИО докладчика
        if (fc.getTextFormat().fontContain(b) &&
                fc.getC() != '\n' && fc.getC() != ' ') {
            LOGGER.debug("state={}. Обнаружен жирный текст '{}'.", STATE_NAME, fc.getC());
            eventSink.castEvent(NAME_FOUND);
        }


    }

    @Override
    public void exit() {
        LOGGER.debug("state={}. Получен сигнал о завершении ячейки.", STATE_NAME);
        if (agendaBuilder.getSpeaker() != null){
            agendaBuilder.mergeSpeaker();
        }
        if(agendaBuilder.getGroup() != null){
            agendaBuilder.mergeGroup();
        }
        eventSink.castEvent(EXIT);
    }
}
