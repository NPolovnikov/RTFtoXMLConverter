package com.techinfocom.nvis.agendartftoxml;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class MarshalingException extends Exception {
    private static final long serialVersionUID = -6965279645377836119L;
    
    public MarshalingException() {
    }

    public MarshalingException(final String message) {
        super(message);
    }

    public MarshalingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MarshalingException(final Throwable cause) {
        super(cause);
    }

    public MarshalingException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
