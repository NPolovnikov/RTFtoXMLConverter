package com.techinfocom.utils;

/**
 * Created by volkov_kv on 09.06.2016.
 */
public class FormattedText {
    public FormattedText(String string, TextFormat format) {
        this.string = string;
        this.format = format;
    }
    private final String string;
    private final TextFormat format;

    public String getString() {
        return string;
    }

    public TextFormat getFormat() {
        return format;
    }
}
