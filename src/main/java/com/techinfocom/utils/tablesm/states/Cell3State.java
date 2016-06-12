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
    public static final Event CELL_END = new Event("CELL_END");
    private final AgendaBuilder agendaBuilder;
    private Cell3Parser cell3Parser;
    private EventSink cell3ParserEeventSink;
    private List<FormattedText> consumed;
    private ParTrimmer parTrimmer;

    public Cell3State(AI automation, EventSink eventSink, AgendaBuilder agendaBuilder) {
        super(automation, eventSink);
        this.agendaBuilder = agendaBuilder;
        init();
    }

    private void init() {
        cell3Parser = Cell3ParserImpl.createAutomation(this.agendaBuilder);
        cell3ParserEeventSink = (EventSink) cell3Parser;
        consumed = new ArrayList<>();
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
                    cell3Parser.analyseFormat(trimmedChar);
                    cell3Parser.processChar(trimmedChar);
                }
                cell3Parser.endOfCell();

                init();//переинициализируем для следующего применения.
                eventSink.castEvent(CELL_END);
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

        Character charBuffer = null;
        List<FormatedChars> acceptedCharsList = new ArrayList<>();
        FormatedChars bufferTarget = null;
        for (FormatedChars fc : formatedCharsList) {
            FormatedChars acceptedChars = new FormatedChars();
            acceptedChars.textFormat = fc.textFormat;
            acceptedChars.chars = new ArrayList<>();
            for (Character c : fc.chars) {
                if (charBuffer == null) {
                    if (!c.equals(' ')) { //вначале всего текста следует игнорировать только пробелы
                        charBuffer = c;
                        bufferTarget = acceptedChars;
                    }
                } else {
                    //пробел вначале абзаца
                    if (charBuffer.equals('\n') && c.equals(' ')) {
                        //charBuffer-остается, текущий символ игнорируем
                    } else
                        //последовательности при которых игнорируется один символов
                        if ((charBuffer.equals('\n') && c.equals('\n')) ||
                                (charBuffer.equals(' ') && c.equals('\n')) ||
                                (charBuffer.equals(' ') && c.equals(' ')) ||
                                (charBuffer.equals('\n') && c.equals('\n'))) {
                            charBuffer = c;//игнорируем символ, он не нужен
                            bufferTarget = acceptedChars;
                        } else {
                            bufferTarget.chars.add(charBuffer);
                            charBuffer = c;
                            bufferTarget = acceptedChars;
                        }
                }
            }
            acceptedCharsList.add(acceptedChars);
        }
        if (charBuffer != null && !charBuffer.equals('\n')) {
            bufferTarget.chars.add(charBuffer); //отстающий символ разместим.
            //acceptedCharsList.get(acceptedCharsList.size()-1).chars.add(charBuffer);
        }

        //соберем обратно в строки.
        List<FormattedText> trimedList = new ArrayList<>();
        for (FormatedChars fcs : acceptedCharsList) {
            if (fcs.chars.size() > 0) {
                StringBuilder sb = new StringBuilder(fcs.chars.size());
                fcs.chars.stream().forEach(sb::append);
                FormattedText ft = new FormattedText(sb.toString(), fcs.textFormat);
                trimedList.add(ft);
            }
        }
        return trimedList;
    }

    protected void parseCell3Text(List<FormattedText> formattedTextList) {
        //поскольку формат текста отслеживается отдельным хендлером, следут сначала проанализировать
        //формат и принять решение о характере обработки текста, потом только обработать.
        // то есть смена состояния автомата должна предшествовать обработке текста.
        for (FormattedText ft : formattedTextList) {
            for (char c : ft.getString().toCharArray()) {
                //cell3Parser.analyseFormat(c, ft.getFormat());
                //cell3Parser.processChar(c, ft.getFormat());
            }
            //cell3Parser.processCommand(new RtfCommand(Command.cell, 0, false, false), new TextFormat()); //todo бред, а
        }
    }


}
