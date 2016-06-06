package com.techinfocom.utils;

import com.rtfparserkit.parser.IRtfListener;
import com.rtfparserkit.rtf.Command;
import com.techinfocom.utils.agenda.Agenda;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by volkov_kv on 20.05.2016.
 */
public class RtfListnerImpl implements IRtfListener {

    Agenda agenda;
    Stack<String> context = new Stack<>();
    Stack<String> groups = new Stack<>();
    int tableNum = 0;
    boolean collectTable = false;
    String state;

    @Override
    public void processDocumentStart() {
        System.out.println("processDocumentStart");
        if (context.empty()){
            context.push("doc");
            agenda = new Agenda();
        } else {
            throw new RuntimeException("Unexpected start of document");
        }
    }

    @Override
    public void processDocumentEnd() {
        System.out.println("processDocumentEnd");
        if(!context.firstElement().equals("doc")){
            throw new RuntimeException("Unexpected end of document");
        }
    }

    @Override
    public void processGroupStart() {
        System.out.println("processGroupStart");
        String commands = null;
        if (!groups.empty()) {
            commands = groups.peek();
        } else {
            commands = "";
        }
        groups.push(commands);
        System.out.println(groups.toString());
    }

    @Override
    public void processGroupEnd() {
        System.out.println("processGroupEnd");
        groups.pop();
    }

    @Override
    public void processCharacterBytes(byte[] data) {
        System.out.println("processCharacterBytes=" + new String(data));
    }

    @Override
    public void processBinaryBytes(byte[] data) {
        System.out.println("processBinaryBytes=" + new String(data));
    }

    @Override
    public void processString(String string) {
        System.out.println("processString=" + string);
    }

    @Override
    public void processCommand(Command command, int parameter, boolean hasParameter, boolean optional) {
        //System.out.println("processCommand. command=" + command.toString() + "; parameter=" + parameter + "; hasParameter=" + hasParameter + "; optional="+ optional );
        Command[] aFilteredCommands = {Command.trowd, Command.row, Command.irow};
        List<Command> filteredCommands = Arrays.asList(aFilteredCommands);
        if (filteredCommands.contains(command)) {
            String commands = groups.pop();
            commands += " " + command;
            groups.push(commands);
            if (command.equals(Command.trowd)) {
                System.out.println("таблица начало?");
            }
        }
    }

    public Agenda getAgenda() {
        return agenda;
    }
}
