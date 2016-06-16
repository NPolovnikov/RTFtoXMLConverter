package com.techinfocom.nvis.agendartftoxml;

import com.rtfparserkit.parser.IRtfListener;
import com.rtfparserkit.rtf.Command;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.docsm.DocParserImpl;
import com.techinfocom.nvis.agendartftoxml.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;
import org.slf4j.Logger;

import java.util.Arrays;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 26.05.2016.
 */
public class TokenDetector implements IRtfListener {
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    private final AgendaBuilder agendaBuilder;

    public TokenDetector() {
        groupState = new GroupState();
        agendaBuilder = new AgendaBuilder();//общая база данных для всех автоматов
        docParser = DocParserImpl.createAutomaton(agendaBuilder);
    }

    private final GroupState groupState; //форматирование
    private Integer dstDepthBegin; //монитор степени погружения в dst
    private final DocParser docParser; //парсер структуры документа.

    @Override
    public void processDocumentStart() {
        LOGGER.debug("message:DocBegin");
    }

    @Override
    public void processDocumentEnd() {
        LOGGER.debug("message:DocEnd");
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
            //System.err.println("Выйдем из состояния сбора DST группы");
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
            LOGGER.debug("processString={} at groupState={}", string, groupState.printCurrentLevel());
            for (char c : string.toCharArray()) {
                docParser.processWord(new FormatedChar(c, groupState.getCurrent()));
            }
        }
    }

    @Override
    public void processCommand(Command command, int parameter, boolean hasParameter, boolean optional) {
        Command[] tblCommands = {trowd, row, cell, lastrow, pard, plain, intbl, par};
        if (dstDepthBegin == null) {
            //System.err.println("processCommand. command=" + command.getCommandName() + "-" + command.getCommandType() + "; parameter=" + parameter + "; hasParameter=" + hasParameter + "; optional=" + optional);
            if (Arrays.asList(tblCommands).contains(command)) {
                LOGGER.debug("processCommand. command=" + command.getCommandName() + "-" + command.getCommandType() + "; parameter=" + parameter + "; hasParameter=" + hasParameter + "; optional=" + optional);
            }
        }
        RtfCommand rtfCommand = new RtfCommand(command, parameter, hasParameter, optional, groupState.getCurrent());

        if (dstDepthBegin != null) {
            //System.err.println("режим сбора DST. пока игнор");
            return;
        }

        String strSym = symToString(rtfCommand);
        if (strSym != null) {
            //нашли символ
            processString(strSym);
        } else {
            //нашли команду
            switch (command.getCommandType()) {
                case Destination:
                    if (command != rtf) {
                        //System.err.println("вошли в режим сбора DST");
                        dstDepthBegin = groupState.getDepth();
                    }
                    break;
                default:
                    groupState.processCommand(rtfCommand);
                    docParser.processWord(rtfCommand);
                    break;
            }
        }
    }

    public AgendaBuilder getAgendaBuilder() {
        return agendaBuilder;
    }

    /**
     * Преобразует символы, передающиеся командами в текст.
     *
     * @return
     */
    private String symToString(RtfCommand rtfCommand) {
        String res = null;
        switch (rtfCommand.getCommand()) {
            //Required line break (no paragraph break).
            case line:
                res = " ";
                break;
            case nonbreakingspace:
                res = " ";
                break;
        }
        return res;
    }
}
