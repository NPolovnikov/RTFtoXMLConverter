package com.techinfocom.utils;

import com.rtfparserkit.parser.IRtfListener;
import com.rtfparserkit.parser.IRtfParser;
import com.rtfparserkit.parser.IRtfSource;
import com.rtfparserkit.parser.RtfStreamSource;
import com.rtfparserkit.parser.standard.StandardRtfParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by volkov_kv on 13.06.2016.
 */
public class RtfAgendaConverter {
    private static final org.slf4j.Logger LOGGER = Logger.LOGGER;

    public OutputStream convert(InputStream is) throws IOException { // TODO: 13.06.2016 убрать исключение
        LOGGER.info("start RTF Agenda conversion");
        IRtfSource iRtfSource = new RtfStreamSource(is);
        IRtfParser parser = new StandardRtfParser();
        //IRtfParser parser = new RawRtfParser();
        IRtfListener tokenDetector = new TokenDetector();
        parser.parse(iRtfSource, tokenDetector);// либо тут делать agenda constructor;

        return null;
    }
}
