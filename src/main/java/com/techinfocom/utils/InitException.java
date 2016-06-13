package com.techinfocom.utils;

import javax.xml.bind.JAXBException;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class InitException extends Exception {
    public InitException(String message) {
        super(message);
    }

    public InitException(String message, Throwable cause) {
        super(message, cause);
    }
}
