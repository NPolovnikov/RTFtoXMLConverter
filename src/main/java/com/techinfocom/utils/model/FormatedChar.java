package com.techinfocom.utils.model;

import com.techinfocom.utils.model.TextFormat;

/**
 * Created by volkov_kv on 11.06.2016.
 */
public class FormatedChar {
    public FormatedChar(char c, TextFormat textFormat) {
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
