package com.techinfocom.nvis.agendartftoxml.model;

/**
 * Created by volkov_kv on 11.06.2016.
 */
public class FormatedChar extends AbstractRtfWord {
    public FormatedChar(final char c, final TextFormat textFormat) {
        super(RtfWordType.CHAR);
        this.c = c;
        this.textFormat = textFormat;
    }

    private final char c;
    private final TextFormat textFormat;

    public char getC() {
        return c;
    }

    public TextFormat getTextFormat() {
        return textFormat;
    }
}
