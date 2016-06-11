package com.techinfocom.utils;


import com.rtfparserkit.parser.*;
import com.rtfparserkit.parser.standard.StandardRtfParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "start" );

        //File file = new File(App.class.getClassLoader().getResource("super_order_from_wordpad.rtf").getFile());
        File file = new File(App.class.getClassLoader().getResource("super_order.rtf").getFile());
        System.out.println(file);
        InputStream is = new FileInputStream(file);
        IRtfSource iRtfSource = new RtfStreamSource(is);
        IRtfParser parser = new StandardRtfParser();
        //IRtfParser parser = new RawRtfParser();
        RtfListnerImpl rtfListnerImpl = new RtfListnerImpl();
        IRtfListener tokenDetector = new TokenDetector();
        IRtfListener justLogger = new IRtfListenerImplJustLogger();
        parser.parse(iRtfSource, tokenDetector);// либо тут делать agenda constructor;

        //Agenda agenda = listener.getAgenda();

        //System.out.println(agenda);

    }
}
