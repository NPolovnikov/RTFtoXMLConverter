package com.techinfocom.nvis.agendartftoxml;

import com.techinfocom.nvis.agendartftoxml.model.FormatedChar;

/**
 * Created by volkov_kv on 11.06.2016.
 */
public class ParTrimmer {
    FormatedChar buffer;

    public FormatedChar trim(FormatedChar fc) {
        if (buffer == null) {
            if (fc.getC() == (' ') || fc.getC() == ('\n')) { //вначале всего текста следует игнорировать только пробелы и переводы строк
                return null;
            } else {
                buffer = fc; //иначе заполняем буфер
                return null;
            }
        }

        //последовательности при которых игнорируется поступивший символ
        if ((buffer.getC() == ('\n') && fc.getC() == (' ')) ||  //пробел вначале абзаца
                (buffer.getC() == ('\n') && fc.getC() == ('\n')) ||
                (buffer.getC() == (' ') && fc.getC() == (' ')))
            return null;

        //последовательности при которых игнорируется символ в буфере
        if (buffer.getC() == (' ') && fc.getC() == ('\n')) { //пробел в конце абзаца
            buffer = fc;
            return null;

        }
        FormatedChar accepted = buffer;
        buffer = fc;
        return accepted;
    }

    public FormatedChar finish() {
        FormatedChar accepted = null;
        if (buffer != null && buffer.getC() != '\n') { //последний в тексте перевод катретки не возвращаем
            accepted = buffer;
        }
        buffer = null;
        return accepted;
    }
}
