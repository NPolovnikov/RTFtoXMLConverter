package com.techinfocom.utils.tablesm.states;

import com.techinfocom.utils.FormattedText;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.TextFormat;
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
    public static final Event NEXT_CELL = new Event("NEXT_CELL");
    private final AgendaBuilder agendaBuilder;
    private Cell3Parser cell3Parser;
    private EventSink cell3ParserEeventSink;
    private List<FormattedText> consumed;

    public Cell3State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        init();
    }

    private void init() {
        cell3Parser = Cell3ParserImpl.createAutomation(this.agendaBuilder);
        cell3ParserEeventSink = (EventSink) cell3Parser;
        consumed = new ArrayList<>();
    }

    @Override
    public void processString(String string, TextFormat textFormat) {
        if (!textFormat.getParagraphFormat().stream().anyMatch(c -> c.getCommand() == intbl)) {
            return; //наличие обязательно
        }
        consumed.add(new FormattedText(string, textFormat));
    }

    @Override
    public void processCommand(RtfCommand rtfCommand, TextFormat textFormat) {
        switch (rtfCommand.getCommand()) {
            case par:
                processString("\n", textFormat);
                break;
            case cell:
                System.err.println("Состояние Cell3State, поймано событие cell.");
                List<FormattedText> trimmedConsumed = trimFormatedTextList(consumed);
                System.out.println(trimmedConsumed);
                parseConsumed();
                init();//переинициализируем для следующего применения.
                eventSink.castEvent(NEXT_CELL);
                break;
            default:
                break;
        }
    }

    protected List<FormattedText> trimFormatedTextList(List<FormattedText> consumed) {
        //Удалить подряд идущие пробелы
        //Удалить пробелы вначале и в конце абзацев
        //После всего этого удалить подряд идущие абзацы

        //разделим строки на массив символов
        class FormatedChars {
            List<Character> chars;
            TextFormat textFormat;
        }

        List<FormatedChars> formatedCharsList = new ArrayList<>();
        for (FormattedText ft : consumed) {
            Stream<Character> charStream = ft.getString().chars().mapToObj(i -> (char) i);
            FormatedChars formatedChars = new FormatedChars();
            formatedChars.chars = charStream.collect(Collectors.toList());
            formatedChars.textFormat = ft.getFormat();
            formatedCharsList.add(formatedChars);
        }

        Character candidate = null;
        List<FormatedChars> acceptedCharsList = new ArrayList<>();
        FormatedChars candidateAcceptedChars = null;
        for (FormatedChars fc : formatedCharsList) {
            FormatedChars acceptedChars = new FormatedChars();
            acceptedChars.textFormat = fc.textFormat;
            acceptedChars.chars = new ArrayList<>();
            for (Character c : fc.chars) {
                if (candidate == null) {
                    if (!c.equals(' ')) { //вначале всего текста следует игнорировать только пробелы
                        candidate = c;
                        candidateAcceptedChars = acceptedChars;
                    }
                } else {
                    if (candidate.equals('\n') && c.equals(' ')) { //пробел вначале абзаца
                        //candidate-остается, прибел игнорируем
                    } else
                    if ((candidate.equals('\n') && c.equals('\n')) ||
                            (candidate.equals(' ') && c.equals('\n')) ||
                            (candidate.equals(' ') && c.equals(' ')) ||
                            (candidate.equals('\n') && c.equals('\n'))) {
                        candidate = c;//игнорируем символ, он не нужен
                        candidateAcceptedChars = acceptedChars;
                    } else {
                        candidateAcceptedChars.chars.add(candidate);
                        candidate = c;
                        candidateAcceptedChars = acceptedChars;
                    }
                }
            }
            acceptedCharsList.add(acceptedChars);
        }
        if(candidate != null){
            acceptedCharsList.get(acceptedCharsList.size()-1).chars.add(candidate); //отстающий символ разместим.
        }

        //соберем обратно в строки.
        List<FormattedText> trimedList = new ArrayList<>();
        for (FormatedChars fcs : acceptedCharsList){
            if(fcs.chars.size()>0){
                StringBuilder sb = new StringBuilder(fcs.chars.size());
                fcs.chars.stream().forEach(sb::append);
                FormattedText ft = new FormattedText(sb.toString().replace("\n","\r\n"), fcs.textFormat);
                trimedList.add(ft);
            }
        }
        return trimedList;
    }

    protected void parseConsumed() {
        //поскольку формат текста отслеживается отдельным хендлером, следут сначала проанализировать
        //формат и принять решение о характере обработки текста, потом только обработать.
        // то есть смена состояния автомата должна предшествовать обработке текста.
        //cell3Parser.analyseFormat(string, textFormat);
        //cell3Parser.processString(string, textFormat);
        //cell3Parser.processCommand(rtfCommand, textFormat);
    }

    private enum TrimState {
        PAR_START,

    }


}
