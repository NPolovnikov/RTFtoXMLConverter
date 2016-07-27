package com.techinfocom.nvis.agendartftoxml.statemachine;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by volkov_kv on 24.05.2016.
 * базовый класс для всех автоматов. Он предоставляет возможность
 * наследнику регистрировать переходы, используя метод addEdge. Дополнительно класс
 * AutomatonBase реализует интерфейс уведомления о событии
 */
public abstract class AbstractAutomationBase<AI> implements EventSink {
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    protected AI state;
    private final Map<AI, Map<Event, AI>> edges = new HashMap<>();

    protected void addEdge(AI source, Event event, AI target) {
        Map<Event, AI> row = edges.get(source);
        if (row == null) {
            row = new IdentityHashMap<>();
            edges.put(source, row);
        }
        row.put(event, target);
    }

    public void castEvent(final Event event) {
        final String srcState = state.getClass().getSimpleName().toUpperCase();
        String report = srcState + "-->";
        try {
            state = edges.get(state).get(event);
            report += state.getClass().getSimpleName().toUpperCase();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Edge is not defined: from state " + srcState + " with event " + event.getName());
        }
        LOGGER.debug(report);
    }

}
