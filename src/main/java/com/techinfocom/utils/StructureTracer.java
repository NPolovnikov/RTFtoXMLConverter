package com.techinfocom.utils;

import com.techinfocom.utils.model.AgendaBuilder;
import com.techinfocom.utils.model.TableStructureEvent;
import com.techinfocom.utils.model.agenda.AgendaItem;

/**
 * Created by volkov_kv on 01.06.2016.
 */
public class StructureTracer {

    public StructureTracer(AgendaBuilder agendaBuilder) {
        this.tableCount = 0;
        this.cellCount = 0;
        this.state = StructureElement.NOTHING;
        this.agendaBuilder = agendaBuilder;
        searchState = SearchState.AGENDA_TABLE_NOT_FOUND;
    }

    private int tableCount;
    private int cellCount;
    private StructureElement state;
    private AgendaBuilder agendaBuilder;
    private AgendaItem agendaItem;
    private SearchState searchState;

    public void processCommand(RtfCommand rtfCommand) {
        switch (rtfCommand.getCommand()) {
            case trowd:
                switch (state) {
                    case NOTHING:
                        if (tableCount == 1) {
                            System.err.println("Нашли таблицу №2, можно писать xml");
                            agendaBuilder.processEvent(TableStructureEvent.TABLE_BEGIN);
                            searchState = SearchState.AGENDA_TABLE_FOUND;

                        }
                        tableCount++;
                        cellCount = 1;
                        agendaBuilder.processEvent(TableStructureEvent.ROW_BEGIN);
                        state = StructureElement.ROW;
                        System.err.println("обнаружили начало таблицы, первый ROW");
                        break;
                    case ROW:
                        System.err.println("ОШИБКА, НАФИГ. ИМЕЕМ TROWD В СОСТОЯНИИ ROW. ОЖИДАЛОСЬ, ЧТО ROW БУДЕТ ЗАВЕРШЕН");
                        break;
                    case ROWENDED:
                        System.err.println("нашлась следующая row");
                        agendaBuilder.processEvent(TableStructureEvent.ROW_BEGIN);
                        state = StructureElement.ROW;
                        cellCount = 1;
                        break;
                }
                break;
            case row:
                System.err.println("Обнаружили конец row");
                agendaBuilder.processEvent(TableStructureEvent.ROW_END);
                state = StructureElement.ROWENDED;
                cellCount = 0;
                break;
            case cell:
                System.err.println("Обнаружили конец ячейки");
                cellCount++;
                agendaBuilder.processEvent(TableStructureEvent.CELL_END);
                break;
            default:
                //case pard:
                switch (state) {
                    case ROWENDED:
                        System.err.println("Похоже, таблица закончилась");
                        state = StructureElement.NOTHING;
                        searchState = SearchState.AGENDA_TABLE_NOT_FOUND;
                        agendaBuilder.processEvent(TableStructureEvent.TABLE_END);
                }
        }

    }

    public int getTableCount() {
        return tableCount;
    }

    public int getCellCount() {
        return cellCount;
    }

    public SearchState getSearchState() {
        return searchState;
    }

    public void setSearchState(SearchState searchState) {
        this.searchState = searchState;
    }

    private enum StructureElement {
        NOTHING,
        ROW,
        ROWENDED
    }

    private enum SearchState {
        AGENDA_TABLE_FOUND,
        AGENDA_TABLE_NOT_FOUND;
    }

}
