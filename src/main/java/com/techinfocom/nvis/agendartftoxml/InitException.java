package com.techinfocom.nvis.agendartftoxml;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class InitException extends RuntimeException{
    private static final long serialVersionUID = -4515291958730729123L;

    public InitException(final String message) {
        super(message);
    }

    public InitException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
