package com.techinfocom.nvis.agendartftoxml.model;

/**
 * Базовый класс для объединения всех типов данных, которые возникают при парсинге RTF
 */
public abstract class RtfWord {
    private final RtfWordType rtfWordType;

    public RtfWord(RtfWordType rtfWordType) {
        this.rtfWordType = rtfWordType;
    }


    public RtfWordType getRtfWordType() {
        return rtfWordType;
    }

    public enum RtfWordType {
        COMMAND,
        CHAR
    }
}
