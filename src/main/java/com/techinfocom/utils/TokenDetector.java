package com.techinfocom.utils;

import com.rtfparserkit.parser.IRtfListener;
import com.rtfparserkit.rtf.Command;
import com.techinfocom.utils.tablesm.TableParser;
import com.techinfocom.utils.tablesm.TableParserImpl;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 26.05.2016.
 * На выходе должно получиться: docBegin, TableBegin
 * Надо сделать некую низкоуровневую фигню, чтобы объединять разрозненные команды в одну, и детекрировать нужные нам события.
 * Но куда эти события пихать? - текущее состояние парсера. докПарсинг, таблеПарсинг, РоуПарсинг
 */
public class TokenDetector implements IRtfListener {

    public TokenDetector() {
        groupState = new GroupState();
        tableParser = TableParserImpl.createAutomaton();
    }

    private final GroupState groupState;
    private Integer dstDepthBegin;
    private final TableParser tableParser;

    @Override
    public void processDocumentStart() {
        System.err.println("message:DocBegin");
    }

    @Override
    public void processDocumentEnd() {
        System.err.println("message:DocEnd");
    }

    @Override
    public void processGroupStart() {
        groupState.addLevel();
    }

    @Override
    public void processGroupEnd() {
        groupState.removeLevel();
        //"Эмуляция состояния
        //Если мы в режиме сбора DST,то проверим не пора ли выйти из него
        if (dstDepthBegin != null && groupState.getDepth() < dstDepthBegin) {
            System.err.println("Выйдем из состояния сбора DST группы");
            dstDepthBegin = null;
        }
    }

    @Override
    public void processCharacterBytes(byte[] data) {
        System.err.println("processCharacterBytes=" + new String(data));
    }

    @Override
    public void processBinaryBytes(byte[] data) {
        System.err.println("processBinaryBytes=" + new String(data));
    }

    @Override
    public void processString(String string) {
        if (dstDepthBegin == null) {
            System.err.println("processString=" + string);
            System.err.println("at " + groupState.printCurrentLevel());
            tableParser.processString(string, groupState.getCurrent());
        }
    }

    @Override
    public void processCommand(Command command, int parameter, boolean hasParameter, boolean optional) {
        System.err.println("processCommand. command=" + command.getCommandName() + "-" + command.getCommandType() + "; parameter=" + parameter + "; hasParameter=" + hasParameter + "; optional=" + optional);
        RtfCommand rtfCommand = new RtfCommand(command, parameter, hasParameter, optional);

        if (dstDepthBegin != null) {
            System.err.println("режим сбора DST. пока игнор");
            return;
        }
        switch (command.getCommandType()) {
            case Destination:
                if (command != rtf) {
                    System.err.println("вошли в режим сбора DST");
                    dstDepthBegin = groupState.getDepth();
                }
                break;
            default:
                groupState.processCommand(rtfCommand);
                tableParser.processCommand(rtfCommand);
                break;
        }
    }
}
