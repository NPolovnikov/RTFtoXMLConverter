package com.techinfocom.nvis.agendartftoxml.utils;

import com.rtfparserkit.parser.IRtfListener;
import com.rtfparserkit.rtf.Command;
import com.techinfocom.nvis.agendartftoxml.utils.docsm.DocParser;
import com.techinfocom.nvis.agendartftoxml.utils.docsm.DocParserImpl;
import com.techinfocom.nvis.agendartftoxml.utils.model.AgendaBuilder;
import com.techinfocom.nvis.agendartftoxml.utils.model.FormatedChar;
import com.techinfocom.nvis.agendartftoxml.utils.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.utils.tablesm.TableParser;
import com.techinfocom.nvis.agendartftoxml.utils.tablesm.TableParserImpl;
import org.slf4j.Logger;

import java.util.Arrays;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 26.05.2016.
 */
public class TokenDetector implements IRtfListener {
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.utils.Logger.LOGGER;
    private final AgendaBuilder agendaBuilder;

    public TokenDetector() {
        groupState = new GroupState();
        agendaBuilder = new AgendaBuilder();
        tableParser = TableParserImpl.createAutomaton(agendaBuilder);
        docParser = DocParserImpl.createAutomaton();
    }

    private final GroupState groupState;
    private Integer dstDepthBegin;
    private final TableParser tableParser;
    private final DocParser docParser;

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
                //tableParser.processChar(new FormatedChar(c, groupState.getCurrent()));
                docParser.processChar(new FormatedChar(c, groupState.getCurrent()));
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
        RtfCommand rtfCommand = new RtfCommand(command, parameter, hasParameter, optional);

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
                    //tableParser.processCommand(rtfCommand, groupState.getCurrent());
                    docParser.processCommand(rtfCommand, groupState.getCurrent());
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
