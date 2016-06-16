package com.techinfocom.nvis.agendartftoxml.model;

import com.rtfparserkit.rtf.Command;

/**
 * Created by volkov_kv on 27.05.2016.
 */
public class RtfCommand extends RtfWord{
    private final Command command;
    private final int parameter;
    private final boolean hasParameter;
    private final boolean optional;
    private final TextFormat textFormat;

    public RtfCommand(Command command, int parameter, boolean hasParameter, boolean optional, TextFormat textFormat) {
        super(RtfWordType.COMMAND);
        this.command = command;
        this.parameter = parameter;
        this.hasParameter = hasParameter;
        this.optional = optional;
        this.textFormat = textFormat;
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

    public TextFormat getTextFormat() {
        return textFormat;
    }
}
