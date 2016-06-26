package com.techinfocom.nvis.agendartftoxml;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class InitException extends RuntimeException{
    public InitException(String message) {
        super(message);
    }

    public InitException(String message, Throwable cause) {
        super(message, cause);
    }
}
