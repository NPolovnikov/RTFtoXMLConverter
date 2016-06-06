package com.techinfocom.utils;

import com.rtfparserkit.rtf.Command;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 01.06.2016.
 */
public class StructureTracer {

    public StructureTracer() {
        this.tableCount = 0;
        this.state = StructureElement.EMPTY;
    }

    private int tableCount;
    private StructureElement state;

    public void processCommand(RtfCommand rtfCommand) {
        switch (rtfCommand.getCommand()) {
            case trowd:
                switch (state) {
                    case EMPTY:
                        tableCount++;
                        state = StructureElement.ROW;
                        System.err.println("обнаружили начало таблицы, первый ROW");
                        if (tableCount == 2) {
                            System.err.println("Нашли таблицу №2, можно писать xml");
                        }
                        break;
                    case ROW:
                        System.err.println("ОШИБКА, НАФИГ. ИМЕЕМ TROWD В СОСТОЯНИИ ROW. ОЖИДАЛОСЬ, ЧТО ROW БУДЕТ ЗАВЕРШЕН");
                        break;
                    case ROWENDED:
                        System.err.println("нашлась следующая row");
                        state = StructureElement.ROW;
                        break;
                }
                break;
            case row:
                System.err.println("Обнаружили конец row");
                state = StructureElement.ROWENDED;
                break;
            case cell:
                System.err.println("Обнаружили конец ячейки");
                break;
            default:
            //case pard:
                switch (state) {
                    case ROWENDED:
                        state = StructureElement.EMPTY;
                        System.err.println("Похоже, таблица закончилась");
                }
        }

    }

    private enum StructureElement {
        EMPTY,
        ROW,
        ROWENDED
    }

}
