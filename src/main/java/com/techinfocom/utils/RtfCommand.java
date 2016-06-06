package com.techinfocom.utils;

import com.rtfparserkit.rtf.Command;

/**
 * Created by volkov_kv on 27.05.2016.
 */
public class RtfCommand {
    final private Command command;
    final private int parameter;
    final private boolean hasParameter;
    final private boolean optional;

    public RtfCommand(Command command, int parameter, boolean hasParameter, boolean optional) {
        this.command = command;
        this.parameter = parameter;
        this.hasParameter = hasParameter;
        this.optional = optional;
    }

    public Command getCommand() {
        return command;
    }

    public int getParameter() {
        return parameter;
    }

    public boolean isHasParameter() {
        return hasParameter;
    }

    public boolean isOptional() {
        return optional;
    }
}
