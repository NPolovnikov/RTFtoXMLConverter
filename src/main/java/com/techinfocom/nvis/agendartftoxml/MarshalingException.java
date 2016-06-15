package com.techinfocom.nvis.agendartftoxml;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class MarshalingException extends Exception {
    public MarshalingException() {
    }

    public MarshalingException(String message) {
        super(message);
    }

    public MarshalingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarshalingException(Throwable cause) {
        super(cause);
    }

    public MarshalingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
