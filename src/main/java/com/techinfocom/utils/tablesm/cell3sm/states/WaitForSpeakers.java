package com.techinfocom.utils.tablesm.cell3sm.states;

import com.techinfocom.utils.FormatedChar;
import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.statemachine.Event;
import com.techinfocom.utils.statemachine.EventSink;
import com.techinfocom.utils.statemachine.StateBase;
import com.techinfocom.utils.tablesm.cell3sm.Cell3Parser;
import org.slf4j.Logger;

import java.util.regex.Pattern;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class WaitForSpeakers<AI extends Cell3Parser> extends StateBase<AI> implements Cell3Parser {
    private static final String STATE_NAME = Name.class.getSimpleName().toUpperCase();
    private static final Logger LOGGER = com.techinfocom.utils.Logger.LOGGER;
    public final static Event NO_SPEAKERS = new Event("NO_SPEAKERS");
    public final static Event SPEAKERS_FOUND = new Event("SPEAKERS_FOUND");
    public static final Event EXIT = new Event("EXIT");

    private final AgendaBuilder agendaBuilder;

    public WaitForSpeakers(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
    }

    @Override
    public void processChar(FormatedChar fc) {

    }

//    @Override
//    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
//        switch (rtfCommand.getCommand()) {
//            case cell:
//                agendaBuilder.mergeGroup();
//                break;
//        }
//
//    }

    @Override
    public void analyseFormat(FormatedChar fc) {
        Pattern p = Pattern.compile("[a-zA-Zа-яА-ЯёЁ]");//только буква
        //если поймали подчеркнутый, НЕ НАКЛОННЫЙ текст, то это тип доклада, и началось описание докладчиков.
        if (fc.getTextFormat().fontContain(ul) &&
                !fc.getTextFormat().fontContain(i) &&
                p.matcher(String.valueOf(fc.getC())).matches()) {
            //создадим новую группу докладчиков и инициализируем тип доклада
            LOGGER.debug("state={}. Обнаружен подчеркнутый, ненаклонный текст ''. Это тип доклада. Созданы CurrentGroup", STATE_NAME, fc.getC());
            agendaBuilder.newGroup();
            //поищем номер документа.
            agendaBuilder.docNumberExtractAndSave();
            eventSink.castEvent(SPEAKERS_FOUND);
        } else {
            //не подчеркнутый текст, это продолжение text
            //восстановим перевод строки, принятый за сигнал
            LOGGER.debug("state={}. подчеркнутого текста не найдено. Продолжаем собирать text", STATE_NAME);
            String text = agendaBuilder.getAgendaItem().getText();
            if (text == null) {
                text = "";
            }
            agendaBuilder.getAgendaItem().setText(text + "\r\n");//восстановим пропущенный сигнальный перевод строки

            eventSink.castEvent(NO_SPEAKERS);
        }
    }

    @Override
    public void exit() {
        LOGGER.error("необрабатываемый ошибочный EXIT");
        //поищем номер документа.
        agendaBuilder.docNumberExtractAndSave();
        eventSink.castEvent(EXIT);
    }


}
