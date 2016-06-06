package com.techinfocom.utils;

import com.rtfparserkit.parser.IRtfListener;
import com.rtfparserkit.rtf.Command;

/**
 * Created by volkov_kv on 30.05.2016.
 */
public class IRtfListenerImplJustLogger implements IRtfListener {
    @Override
    public void processDocumentStart() {
        System.out.println("processDocumentStart");
    }

    @Override
    public void processDocumentEnd() {
        System.out.println("processDocumentEnd");
    }

    @Override
    public void processGroupStart() {
        System.out.println("processGroupStart");
    }

    @Override
    public void processGroupEnd() {
        System.out.println("processGroupEnd");
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
        System.out.println("processCommand. command=" + command.getCommandName() + "-" + command.getCommandType() + "; parameter=" + parameter + "; hasParameter=" + hasParameter + "; optional="+ optional );
    }
}
